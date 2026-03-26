package AccesoDatos;

import Entidades.Persona;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class RepositorioDistritos {

    // distelec.txt se carga una sola vez en memoria para acelerar las consultas.
    private static final Map<String, String[]> DISTRITOS_POR_CODIGO = new HashMap<>();
    private static boolean cargado = false;

    public RepositorioDistritos() {
        cargarDistritosSiEsNecesario();
    }

    public void completarUbicacion(Persona persona) {
        try {
            // Si la persona no trae codigo electoral no es posible enriquecer la direccion.
            if (persona == null) {
                return;
            }

            if (persona.getCodElectoral() == null || persona.getCodElectoral().trim().equals("")) {
                return;
            }

            String codigoBuscado = persona.getCodElectoral().trim();
            String[] ubicacion = DISTRITOS_POR_CODIGO.get(codigoBuscado);

            if (ubicacion == null) {
                return;
            }

            persona.setProvincia(ubicacion[0]);
            persona.setCanton(ubicacion[1]);
            persona.setDistrito(ubicacion[2]);
        } catch (Exception e) {
            System.out.println("Error completando ubicacion: " + e.getMessage());
        }
    }

    private File obtenerArchivoDistritos() {
        // Se revisan varias rutas para facilitar la ejecucion desde distintos entornos.
        String[] rutas = {
            "data/distelec.txt",
            "distelec.txt"
        };

        for (int i = 0; i < rutas.length; i++) {
            File archivo = new File(rutas[i]);

            if (archivo.exists()) {
                return archivo;
            }
        }

        return null;
    }

    private synchronized void cargarDistritosSiEsNecesario() {
        if (cargado) {
            return;
        }

        BufferedReader br = null;

        try {
            File archivo = obtenerArchivoDistritos();

            if (archivo == null) {
                System.out.println("No se encontro el archivo distelec.txt");
                cargado = true;
                return;
            }

            br = new BufferedReader(new FileReader(archivo));
            String linea;

            while ((linea = br.readLine()) != null) {
                registrarDistrito(linea);
            }

            cargado = true;
        } catch (Exception e) {
            System.out.println("Error leyendo distelec.txt: " + e.getMessage());
            cargado = true;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                System.out.println("Error cerrando distelec.txt: " + e.getMessage());
            }
        }
    }

    private void registrarDistrito(String linea) {
        // Cada entrada valida se guarda como codigo -> provincia, canton, distrito.
        String[] partes = linea.split(",");

        if (partes.length < 4) {
            return;
        }

        String codigo = partes[0].trim();
        if (codigo.equals("")) {
            return;
        }

        String[] ubicacion = {
            partes[1].trim(),
            partes[2].trim(),
            partes[3].trim()
        };

        DISTRITOS_POR_CODIGO.put(codigo, ubicacion);
    }
}
