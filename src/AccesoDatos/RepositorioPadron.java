package AccesoDatos;

import Entidades.Persona;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class RepositorioPadron {

    private final RepositorioDistritos repositorioDistritos;

    public RepositorioPadron() {
        this.repositorioDistritos = new RepositorioDistritos();
    }

    public Persona buscarPorCedula(String cedulaBuscada) {
        BufferedReader br = null;

        try {
            // PADRON.txt se recorre hasta encontrar la cedula solicitada.
            File archivo = obtenerArchivoPadron();

            if (archivo == null) {
                System.out.println("No se encontro el archivo PADRON.");
                return null;
            }

            br = new BufferedReader(new FileReader(archivo));
            String linea;

            while ((linea = br.readLine()) != null) {
                Persona persona = convertirLineaAPersona(linea);

                if (persona != null && persona.getCedula().equals(cedulaBuscada)) {
                    // La direccion se completa desde el mapa cargado de distritos.
                    repositorioDistritos.completarUbicacion(persona);
                    return persona;
                }
            }

        } catch (Exception e) {
            System.out.println("Error leyendo PADRON: " + e.getMessage());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        return null;
    }

    private File obtenerArchivoPadron() {
        // Se contemplan nombres comunes del archivo para no depender de una sola ruta.
        String[] rutas = {
            "PADRON.txt",
            "data/PADRON_COMPLETO.txt",
            "PADRON_COMPLETO.txt",
            "data/PADRON.TXT",
            "PADRON.TXT",
            "data/PADRON",
            "PADRON"
        };

        for (int i = 0; i < rutas.length; i++) {
            File archivo = new File(rutas[i]);

            if (archivo.exists()) {
                return archivo;
            }
        }

        return null;
    }

    private Persona convertirLineaAPersona(String linea) {
        // Se extraen solo las columnas necesarias para la respuesta del sistema.
        String[] partes = linea.split(",");

        if (partes.length < 8) {
            return null;
        }

        Persona p = new Persona();
        // El archivo original trae mas columnas, pero solo se copian las necesarias.
        p.setCedula(partes[0].trim());
        p.setCodElectoral(partes[1].trim());
        p.setNombre(partes[5].trim());
        p.setApellido1(partes[6].trim());
        p.setApellido2(partes[7].trim());
        p.setProvincia("");
        p.setCanton("");
        p.setDistrito("");

        return p;
    }
}
