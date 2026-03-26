package Presentacion;

import java.net.Socket;
import java.util.Scanner;

public class MenuConsola {

    // Este menu permite levantar ambos servicios y probarlos desde consola.
    private Scanner sc;
    private boolean servidorActivo;
    private ServidorTCP servidorTcp;
    private ServidorHTTP servidorHttp;

    public MenuConsola() {
        this.sc = new Scanner(System.in);
        this.servidorActivo = false;
        this.servidorTcp = null;
        this.servidorHttp = null;
    }

    public void iniciar() {

        int opcion;

        do {

            mostrarMenuPrincipal();
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    levantarServidores();
                    break;
                case 2:
                    consultarPersonaTcp();
                    break;
                case 3:
                    consultarPersonaHttp();
                    break;
                case 4:
                    apagarServidores();
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opcion invalida.");
                    break;
            }

        }

        while (opcion != 4);

    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n===== Menu =====");
        System.out.println("1. Levantar servidores");
        System.out.println("2. Consultar persona por TCP");
        System.out.println("3. Consultar persona por HTTP");
        System.out.println("4. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    private int leerOpcion() {

        try {
            // Si el usuario escribe algo no numerico, se fuerza la opcion invalida.
            return Integer.parseInt(sc.nextLine());

        } catch (Exception e) {

            return 0;

        }

    }

    private void levantarServidores() {

        if (!servidorActivo) {
            // Cada servidor corre en su propio hilo para no bloquear el menu.
            servidorTcp = new ServidorTCP(5000);
            servidorHttp = new ServidorHTTP(8080);

            Thread servidorTcpThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    servidorTcp.iniciar();
                }
            });

            Thread servidorHttpThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    servidorHttp.iniciar();
                }
            });

            servidorTcpThread.start();
            servidorHttpThread.start();
            esperarInicioServidor("localhost", 5000);
            esperarInicioServidor("localhost", 8080);
            servidorActivo = true;
            System.out.println("Servidor TCP levantado en puerto 5000.");
            System.out.println("Servidor HTTP levantado en puerto 8080.");

        } else {

            System.out.println("El servidor ya esta activo.");

        }

    }

    private void apagarServidores() {
        if (!servidorActivo) {
            return;
        }

        if (servidorTcp != null) {
            servidorTcp.detener();
        }

        if (servidorHttp != null) {
            servidorHttp.detener();
        }

        servidorActivo = false;
    }

    private void esperarInicioServidor(String host, int puerto) {
        // Espera activa corta para evitar consultas antes de que el puerto este listo.
        for (int i = 0; i < 20; i++) {
            try (Socket socket = new Socket(host, puerto)) {
                return;
            } catch (Exception e) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private void consultarPersonaTcp() {

        if (!servidorActivo) {
            System.out.println("Primero debe levantar el servidor.");
            return;
        }

        String formato = leerFormato();

        if (formato.equals("")) {
            System.out.println("Formato invalido.");
            return;
        }

        System.out.print("Ingrese la cedula: ");
        String cedula = sc.nextLine().trim();

        if (cedula.equals("")) {
            System.out.println("La cedula no puede ir vacia.");
            return;
        }

        // El menu construye el mensaje con el protocolo TCP definido por el proyecto.
        String comando = "GET|" + cedula + "|" + formato;

        ClienteTCP cliente = new ClienteTCP("localhost", 5000);
        String respuesta = cliente.enviar(comando);

        System.out.println("\n===== RESPUESTA TCP =====");
        System.out.println(respuesta);

    }

    private void consultarPersonaHttp() {

        if (!servidorActivo) {
            System.out.println("Primero debe levantar el servidor.");
            return;
        }

        String formato = leerFormato();

        if (formato.equals("")) {
            System.out.println("Formato invalido.");
            return;
        }

        System.out.print("Ingrese la cedula: ");
        String cedula = sc.nextLine().trim();

        if (cedula.equals("")) {
            System.out.println("La cedula no puede ir vacia.");
            return;
        }

        // La interfaz HTTP consulta el mismo servicio de negocio por medio de la URL local.
        ClienteHTTP cliente = new ClienteHTTP("localhost", 8080);
        String respuesta = cliente.consultar(cedula, formato);

        System.out.println("\n===== RESPUESTA HTTP =====");
        System.out.println(respuesta);

    }

    private String leerFormato() {

        int opcionFormato;

        System.out.println("\n===== CONSULTA =====");
        System.out.println("1. JSON");
        System.out.println("2. XML");
        System.out.print("Seleccione el formato de respuesta: ");

        try {
            // El formato se selecciona por numero para simplificar la prueba manual.
            opcionFormato = Integer.parseInt(sc.nextLine());

        } catch (Exception e) {

            opcionFormato = 0;

        }

        switch (opcionFormato) {
            case 1:
                return "JSON";
            case 2:
                return "XML";
            default:
                return "";

        }

    }
}
