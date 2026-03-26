package Presentacion;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import DTO.FormatoSalida;
import DTO.RespuestaPadron;
import DTO.SolicitudPadron;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import logica.ServicioPadron;
import util.Serializador;

public class ManejadorHTTP implements HttpHandler {

    private static final String ERROR_METODO_NO_PERMITIDO = "Metodo no permitido";
    private static final String ERROR_CEDULA_VACIA = "Cedula vacia";
    private static final String ERROR_CEDULA_INVALIDA = "Cedula invalida";
    private static final String ERROR_CEDULA_NO_ENCONTRADA = "Cedula no encontrada";
    private static final String ERROR_FORMATO_INVALIDO = "Formato invalido";
    private static final String ERROR_INTERNO_CONTROLADO = "Error interno controlado";

    private ServicioPadron servicio = new ServicioPadron();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            // Se intenta respetar el formato pedido incluso en respuestas de error.
            FormatoSalida formatoPreferido = obtenerFormatoSolicitado(exchange.getRequestURI().getQuery());

            if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                exchange.getResponseHeaders().set("Allow", "GET");
                enviar(exchange, 405, ERROR_METODO_NO_PERMITIDO, formatoPreferido);
                return;
            }

            URI uri = exchange.getRequestURI();
            String path = uri.getPath();
            String query = uri.getQuery();

            Map<String, String> params = parseQuery(query);

            String cedula = params.get("cedula");
            String formatStr = params.get("format");

            // Soporta ambos estilos: /padron?cedula=... y /padron/{cedula}?format=...
            String[] partes = path.split("/");
            if (partes.length == 3 && !partes[2].trim().equals("")) {
                cedula = partes[2].trim();
            }

            if (cedula == null || cedula.trim().equals("")) {
                enviar(exchange, 400, ERROR_CEDULA_VACIA, formatoPreferido);
                return;
            }

            FormatoSalida formato = obtenerFormatoDesdeTexto(formatStr);
            if (formato == null) {
                enviar(exchange, 400, ERROR_FORMATO_INVALIDO, FormatoSalida.JSON);
                return;
            }

            SolicitudPadron solicitud = new SolicitudPadron(cedula.trim(), formato);
            RespuestaPadron respuesta = servicio.buscar(solicitud);

            if (!respuesta.isRecibido()) {
                String msg = respuesta.getRespuesta();

                if (msg.equalsIgnoreCase(ERROR_CEDULA_VACIA) || msg.equalsIgnoreCase(ERROR_CEDULA_INVALIDA)) {
                    enviar(exchange, 400, msg, formato);
                } else if (msg.equalsIgnoreCase(ERROR_CEDULA_NO_ENCONTRADA)) {
                    enviar(exchange, 404, msg, formato);
                } else {
                    enviar(exchange, 500, ERROR_INTERNO_CONTROLADO, formato);
                }
                return;
            }

            String body = (formato == FormatoSalida.JSON)
                    ? Serializador.toJson(respuesta)
                    : Serializador.toXml(respuesta);

            exchange.getResponseHeaders().set("Content-Type",
                    formato == FormatoSalida.JSON ? "application/json" : "application/xml");

            exchange.sendResponseHeaders(200, body.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(body.getBytes());
            os.close();

        } catch (Exception e) {
            enviar(exchange, 500, ERROR_INTERNO_CONTROLADO, FormatoSalida.JSON);
        }
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();

        if (query == null) {
            return map;
        }

        String[] pares = query.split("&");

        for (String p : pares) {
            String[] kv = p.split("=", 2);

            if (kv.length == 2) {
                map.put(decodificar(kv[0]), decodificar(kv[1]));
            }
        }

        return map;
    }

    private void enviar(HttpExchange exchange, int status, String mensaje, FormatoSalida formato) throws IOException {

        RespuestaPadron resp = new RespuestaPadron(false, mensaje, null);

        String body = (formato == FormatoSalida.XML)
                ? Serializador.toXml(resp)
                : Serializador.toJson(resp);

        exchange.getResponseHeaders().set("Content-Type",
                formato == FormatoSalida.XML ? "application/xml; charset=UTF-8" : "application/json; charset=UTF-8");

        exchange.sendResponseHeaders(status, body.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(body.getBytes());
        os.close();
    }

    private FormatoSalida obtenerFormatoSolicitado(String query) {
        Map<String, String> params = parseQuery(query);
        String formatStr = params.get("format");
        FormatoSalida formato = obtenerFormatoDesdeTexto(formatStr);

        if (formato == null) {
            return FormatoSalida.JSON;
        }

        return formato;
    }

    private FormatoSalida obtenerFormatoDesdeTexto(String formatStr) {
        if (formatStr == null) {
            return null;
        }

        try {
            return FormatoSalida.valueOf(formatStr.trim().toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private String decodificar(String valor) {
        return URLDecoder.decode(valor, StandardCharsets.UTF_8);
    }
}
