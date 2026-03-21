/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import DTO.RespuestaPadron;
import Entidades.Persona;
/**
 *
 * @author germa
 */
public class Serializador {
    
     public static String toJson(RespuestaPadron respuesta) {
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
        if (texto == null) {
            return "";
        }
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String escaparXml(String texto) {
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
