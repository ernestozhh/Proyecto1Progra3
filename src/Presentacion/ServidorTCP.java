package Presentacion;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServidorTCP {

    // El servidor TCP usa un pool fijo para atender multiples clientes sin crear hilos ilimitados.
    private int puerto;
    private final ExecutorService executor;
    private volatile boolean activo;
    private ServerSocket serverSocket;

    public ServidorTCP(int puerto) {
        this.puerto = puerto;
        this.executor = Executors.newFixedThreadPool(10);
        this.activo = false;
    }

    public void iniciar() {
        try (ServerSocket socketServidor = new ServerSocket(puerto)) {
            this.serverSocket = socketServidor;
            socketServidor.setReuseAddress(true);
            activo = true;

            System.out.println("Servidor TCP escuchando en el puerto " + puerto);
            System.out.println("Protocolo: GET|cedula|JSON o GET|cedula|XML");
            System.out.println("Comando de cierre: BYE");

            while (activo) {
                Socket cliente = socketServidor.accept();
                // Cada cliente recibe un timeout para evitar conexiones colgadas.
                cliente.setSoTimeout(10000);
                System.out.println("Cliente conectado.");
                executor.execute(new ManejadorCliente(cliente));
            }

        } catch (SocketException e) {
            if (activo) {
                System.out.println("Error en el servidor TCP: " + e.getMessage());
            }
        } catch (Exception e) {
            if (activo) {
                System.out.println("Error en el servidor TCP: " + e.getMessage());
            }
        } finally {
            activo = false;
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    public void detener() {
        activo = false;

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (Exception e) {
            System.out.println("Error cerrando servidor TCP: " + e.getMessage());
        }
    }
}
