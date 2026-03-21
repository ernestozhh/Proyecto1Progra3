/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;
import AccesoDatos.RepositorioPadron;
import DTO.SolicitudPadron;
import DTO.RespuestaPadron;
import Entidades.Persona;
/**
 *
 * @author germa
 */
public class ServicioPadron {
    
    private RepositorioPadron repoPadron;

    public ServicioPadron() {
        this.repoPadron = new RepositorioPadron();
    }

    public RespuestaPadron atender(SolicitudPadron solicitud) {

        try {
            if (solicitud == null) {
                return new RespuestaPadron(false, "Solicitud nula", null);
            }

            String cedula = normalizarCedula(solicitud.getCedula());

            if (cedula.isEmpty()) {
                return new RespuestaPadron(false, "Cedula vacia", null);
            }

            Persona persona = repoPadron.buscarPorCedula(cedula);

            if (persona == null) {
                return new RespuestaPadron(false, "Cedula no encontrada", null);
            }

            return new RespuestaPadron(true, "OK", persona);

        } catch (Exception e) {
            return new RespuestaPadron(false, "Error interno", null);
        }
    }

    private String normalizarCedula(String cedula) {
        if (cedula == null) return "";
        return cedula.replaceAll("[^0-9]", "");
    }
    
}
