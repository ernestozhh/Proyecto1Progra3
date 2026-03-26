
package util;

import DTO.RespuestaPadron;
import Entidades.Persona;

public class Serializador {

    public static String toJson(RespuestaPadron respuesta) {
        // Se construye manualmente el JSON para evitar dependencias externas.
        if (respuesta == null) {
            return "{\"recibido\":false,\"respuesta\":\"Respuesta nula\"}";
        }

        if (!respuesta.isRecibido() || respuesta.getPersona() == null) {
            return "{"
                    + "\"recibido\":false,"
                    + "\"respuesta\":\"" + escaparJson(respuesta.getRespuesta()) + "\""
                    + "}";
        }

        Persona p = respuesta.getPersona();

        return "{"
                + "\"recibido\":true,"
                + "\"respuesta\":\"" + escaparJson(respuesta.getRespuesta()) + "\","
                + "\"persona\":{"
                + "\"cedula\":\"" + escaparJson(p.getCedula()) + "\","
                + "\"nombre\":\"" + escaparJson(p.getNombre()) + "\","
                + "\"apellido1\":\"" + escaparJson(p.getApellido1()) + "\","
                + "\"apellido2\":\"" + escaparJson(p.getApellido2()) + "\","
                + "\"codElectoral\":\"" + escaparJson(p.getCodElectoral()) + "\","
                + "\"provincia\":\"" + escaparJson(p.getProvincia()) + "\","
                + "\"canton\":\"" + escaparJson(p.getCanton()) + "\","
                + "\"distrito\":\"" + escaparJson(p.getDistrito()) + "\""
                + "}"
                + "}";
        
    }

    public static String toXml(RespuestaPadron respuesta) {
        // La estructura XML mantiene el mismo contenido semantico que la salida JSON.
        if (respuesta == null) {
            return "<respuesta><recibido>false</recibido><mensaje>Respuesta nula</mensaje></respuesta>";
        }

        if (!respuesta.isRecibido() || respuesta.getPersona() == null) {
            return "<respuesta>"
                    + "<recibido>false</recibido>"
                    + "<mensaje>" + escaparXml(respuesta.getRespuesta()) + "</mensaje>"
                    + "</respuesta>";
        }

        Persona p = respuesta.getPersona();

        return "<respuesta>"
                + "<recibido>true</recibido>"
                + "<mensaje>" + escaparXml(respuesta.getRespuesta()) + "</mensaje>"
                + "<persona>"
                + "<cedula>" + escaparXml(p.getCedula()) + "</cedula>"
                + "<nombre>" + escaparXml(p.getNombre()) + "</nombre>"
                + "<apellido1>" + escaparXml(p.getApellido1()) + "</apellido1>"
                + "<apellido2>" + escaparXml(p.getApellido2()) + "</apellido2>"
                + "<codElectoral>" + escaparXml(p.getCodElectoral()) + "</codElectoral>"
                + "<provincia>" + escaparXml(p.getProvincia()) + "</provincia>"
                + "<canton>" + escaparXml(p.getCanton()) + "</canton>"
                + "<distrito>" + escaparXml(p.getDistrito()) + "</distrito>"
                + "</persona>"
                + "</respuesta>";
        
    }

    private static String escaparJson(String texto) {
        // Se escapan caracteres basicos para no romper la sintaxis del JSON.
        if (texto == null) {
            return "";
        }

        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
        
    }

    private static String escaparXml(String texto) {
        // Se convierten caracteres reservados para mantener XML valido.
        if (texto == null) {
            return "";
        }

        return texto.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
        
    }
    
}

