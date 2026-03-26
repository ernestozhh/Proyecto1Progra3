package logica;

import DTO.FormatoSalida;
import DTO.RespuestaPadron;
import DTO.SolicitudPadron;
import util.Serializador;

public class ParserProtocolo {

    private static final String ERROR_MENSAJE_VACIO = "Mensaje vacio";
    private static final String ERROR_FORMATO_INVALIDO = "Formato invalido. Use GET|cedula|JSON o GET|cedula|XML";
    private static final String ERROR_COMANDO_INVALIDO = "Comando invalido";
    private static final String ERROR_FORMATO_NO_SOPORTADO = "Formato no soportado";
    private static final String MENSAJE_CONEXION_CERRADA = "Conexion cerrada";

    private ServicioPadron servicio;

    public ParserProtocolo() {
        this.servicio = new ServicioPadron();
    }

    public String procesar(String mensaje) {
        if (mensaje == null || mensaje.trim().equals("")) {
            return construirError(ERROR_MENSAJE_VACIO, FormatoSalida.JSON);
        }

        if (mensaje.equalsIgnoreCase("BYE")) {
            return construirRespuestaSimple(true, MENSAJE_CONEXION_CERRADA, FormatoSalida.JSON);
        }

        // El protocolo TCP esperado es GET|cedula|JSON o GET|cedula|XML.
        String[] partes = mensaje.split("\\|");
        FormatoSalida formatoDetectado = detectarFormato(partes);

        if (partes.length != 3) {
            return construirError(ERROR_FORMATO_INVALIDO, formatoDetectado);
        }

        String comando = partes[0].trim();
        String cedula = partes[1].trim();
        String formatoTexto = partes[2].trim();

        if (!comando.equalsIgnoreCase("GET")) {
            return construirError(ERROR_COMANDO_INVALIDO, formatoDetectado);
        }

        FormatoSalida formato = obtenerFormato(formatoTexto);

        if (formato == null) {
            return construirError(ERROR_FORMATO_NO_SOPORTADO, FormatoSalida.JSON);
        }

        SolicitudPadron solicitud = new SolicitudPadron();
        solicitud.setCedula(cedula);
        solicitud.setFormato(formato);

        RespuestaPadron respuesta = servicio.buscar(solicitud);

        return serializarRespuesta(respuesta, formato);
    }

    private FormatoSalida obtenerFormato(String formatoTexto) {
        if (formatoTexto == null) {
            return null;
        }

        if (formatoTexto.equalsIgnoreCase("JSON")) {
            return FormatoSalida.JSON;
        }

        if (formatoTexto.equalsIgnoreCase("XML")) {
            return FormatoSalida.XML;
        }

        return null;
    }

    private FormatoSalida detectarFormato(String[] partes) {
        if (partes == null || partes.length < 3) {
            return FormatoSalida.JSON;
        }

        FormatoSalida formato = obtenerFormato(partes[2].trim());
        if (formato != null) {
            return formato;
        }

        return FormatoSalida.JSON;
    }

    private String construirError(String mensaje, FormatoSalida formato) {
        RespuestaPadron respuesta = new RespuestaPadron();
        respuesta.setRecibido(false);
        respuesta.setRespuesta(mensaje);
        respuesta.setPersona(null);
        return serializarRespuesta(respuesta, formato);
    }

    private String construirRespuestaSimple(boolean recibido, String mensaje, FormatoSalida formato) {
        RespuestaPadron respuesta = new RespuestaPadron();
        respuesta.setRecibido(recibido);
        respuesta.setRespuesta(mensaje);
        respuesta.setPersona(null);
        return serializarRespuesta(respuesta, formato);
    }

    private String serializarRespuesta(RespuestaPadron respuesta, FormatoSalida formato) {
        // La misma respuesta de negocio puede serializarse segun el canal lo pida.
        if (formato == FormatoSalida.XML) {
            return Serializador.toXml(respuesta);
        }
        return Serializador.toJson(respuesta);
    }
}
