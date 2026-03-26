package Presentacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import logica.ParserProtocolo;

public class ManejadorCliente implements Runnable {

    // Cada instancia atiende una unica conexion TCP.
    private Socket socket;
    private ParserProtocolo parser;

    public ManejadorCliente(Socket socket) {
        this.socket = socket;
        this.parser = new ParserProtocolo();
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String mensaje = in.readLine();
            // La interpretacion del mensaje se delega a la capa logica del protocolo.
            String respuesta = parser.procesar(mensaje);
            out.println(respuesta);

        } catch (SocketTimeoutException e) {
            enviarErrorControlado("Tiempo de espera agotado");
        } catch (IOException e) {
            enviarErrorControlado("Error de comunicacion");
        } catch (Exception e) {
            System.out.println("Error atendiendo cliente: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void enviarErrorControlado(String mensaje) {
        try {
            // Se responde algo simple para no dejar al cliente sin respuesta ante fallos de red.
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("{\"recibido\":false,\"respuesta\":\"" + mensaje + "\"}");
        } catch (Exception e) {
            System.out.println("No se pudo enviar error controlado: " + e.getMessage());
        }
    }
}
