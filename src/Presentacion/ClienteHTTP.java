package Presentacion;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ClienteHTTP {

    private String host;
    private int puerto;

    public ClienteHTTP(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }

    public String consultar(String cedula, String formato) {
        // Se reintenta unas pocas veces para absorber el arranque inicial del servidor HTTP.
        String ultimoError = "Error interno controlado";

        for (int intento = 0; intento < 3; intento++) {
            HttpURLConnection conexion = null;

            try {
                String formatoHttp = formato.toLowerCase();
                // La cedula se codifica para poder enviarla de forma segura en la URL.
                String cedulaUrl = URLEncoder.encode(cedula, StandardCharsets.UTF_8);
                String urlTexto = "http://" + host + ":" + puerto
                        + "/padron?cedula=" + cedulaUrl + "&format=" + formatoHttp;

                URL url = new URL(urlTexto);
                conexion = (HttpURLConnection) url.openConnection();
                conexion.setRequestMethod("GET");
                conexion.setConnectTimeout(3000);
                conexion.setReadTimeout(3000);

                int codigo = conexion.getResponseCode();
                BufferedReader reader;

                // Si el servidor devuelve error HTTP, se intenta leer el cuerpo igualmente.
                if (codigo >= 400 && conexion.getErrorStream() != null) {
                    reader = new BufferedReader(new InputStreamReader(conexion.getErrorStream()));
                } else {
                    reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                }

                StringBuilder respuesta = new StringBuilder();
                String linea;

                while ((linea = reader.readLine()) != null) {
                    respuesta.append(linea);
                }

                reader.close();
                return respuesta.toString();

            } catch (Exception e) {
                ultimoError = e.getMessage();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return "Error: " + ultimoError;
                }
            } finally {
                if (conexion != null) {
                    conexion.disconnect();
                }
            }
        }

        return "Error: " + ultimoError;
    }
}
