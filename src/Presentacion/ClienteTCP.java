
package Presentacion;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteTCP {

    private String host;
    private int puerto;

    public ClienteTCP(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }

    public String enviar(String mensaje) {
        // Este cliente solo abre la conexion, envia una peticion y espera una respuesta.
        String respuesta = "";

        try (Socket socket = new Socket(host, puerto);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            socket.setSoTimeout(5000);
            
            out.println(mensaje);

            respuesta = in.readLine();

            if (respuesta == null || respuesta.trim().equals("")) {
                // Se normaliza el caso donde el servidor cierra sin devolver contenido.
                respuesta = "No se recibio respuesta del servidor.";
            }

        } catch (Exception e) {
            
            respuesta = "Error: " + e.getMessage();
            
        }

        return respuesta;
        
    }
    
}
