
package DTO;

import Entidades.Persona;


public class RespuestaPadron {
    
    private boolean recibido;
    private String respuesta;
    private Persona persona;

    public RespuestaPadron() {
    }

    public RespuestaPadron(boolean recibido, String respuesta, Persona persona) {
        this.recibido = recibido;
        this.respuesta = respuesta;
        this.persona = persona;
    }

    public boolean isRecibido() {
        return recibido;
    }

    public void setRecibido(boolean recibido) {
        this.recibido = recibido;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
    
}

