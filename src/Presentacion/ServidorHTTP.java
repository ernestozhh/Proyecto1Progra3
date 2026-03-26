
package Presentacion;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorHTTP {

    private int puerto;
    private HttpServer server;
    private ExecutorService executor;

    public ServidorHTTP(int puerto) {
        this.puerto = puerto;
    }

    public void iniciar() {
        try {
            server = HttpServer.create(new InetSocketAddress(puerto), 0);

            server.createContext("/padron", new ManejadorHTTP());
            executor = Executors.newFixedThreadPool(10);
            server.setExecutor(executor);

            server.start();

        } catch (Exception e) {
            System.out.println("Error al iniciar HTTP: " + e.getMessage());
        }
    }

    public void detener() {
        if (server != null) {
            server.stop(0);
        }

        if (executor != null) {
            executor.shutdownNow();
        }
    }
}

