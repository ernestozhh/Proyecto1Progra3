package logica;

import AccesoDatos.RepositorioPadron;
import DTO.RespuestaPadron;
import DTO.SolicitudPadron;
import Entidades.Persona;

public class ServicioPadron {

    private static final String RESPUESTA_EXITO = "Consulta exitosa";
    private static final String ERROR_SOLICITUD_INVALIDA = "Solicitud invalida";
    private static final String ERROR_CEDULA_VACIA = "Cedula vacia";
    private static final String ERROR_CEDULA_INVALIDA = "Cedula invalida";
    private static final String ERROR_CEDULA_NO_ENCONTRADA = "Cedula no encontrada";
    private static final String ERROR_INTERNO_CONTROLADO = "Error interno controlado";

    private RepositorioPadron repositorioPadron;

    public ServicioPadron() {
        this.repositorioPadron = new RepositorioPadron();
    }

    public RespuestaPadron buscar(SolicitudPadron solicitud) {
        // La capa logica centraliza validaciones antes de tocar archivos o transporte.
        RespuestaPadron respuesta = new RespuestaPadron();

        if (solicitud == null) {
            return construirError(ERROR_SOLICITUD_INVALIDA);
        }

        String cedulaNormalizada = normalizarCedula(solicitud.getCedula());

        if (cedulaNormalizada == null) {
            return construirError(ERROR_CEDULA_VACIA);
        }

        if (!esCedulaValida(cedulaNormalizada)) {
            return construirError(ERROR_CEDULA_INVALIDA);
        }

        try {
            Persona persona = repositorioPadron.buscarPorCedula(cedulaNormalizada);

            if (persona == null) {
                return construirError(ERROR_CEDULA_NO_ENCONTRADA);
            }

            respuesta.setRecibido(true);
            respuesta.setRespuesta(RESPUESTA_EXITO);
            respuesta.setPersona(persona);
            return respuesta;
        } catch (Exception e) {
            return construirError(ERROR_INTERNO_CONTROLADO);
        }
    }

    private String normalizarCedula(String cedula) {
        if (cedula == null) {
            return null;
        }

        // Se elimina espacio sobrante para tratar de forma uniforme la entrada.
        String cedulaNormalizada = cedula.trim();
        if (cedulaNormalizada.equals("")) {
            return null;
        }

        return cedulaNormalizada;
    }

    private boolean esCedulaValida(String cedula) {
        if (cedula == null) {
            return false;
        }

        // El padron esperado trabaja con cedulas numericas.
        return cedula.matches("\\d{9,12}");
    }

    private RespuestaPadron construirError(String mensaje) {
        RespuestaPadron respuesta = new RespuestaPadron();
        respuesta.setRecibido(false);
        respuesta.setRespuesta(mensaje);
        respuesta.setPersona(null);
        return respuesta;
    }
}
