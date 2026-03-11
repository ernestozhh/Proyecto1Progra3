
package DTO;


public class SolicitudPadron {
    
    private String cedula;
    private FormatoSalida formato;

    public SolicitudPadron() {
    }

    public SolicitudPadron(String cedula, FormatoSalida formato) {
        this.cedula = cedula;
        this.formato = formato;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public FormatoSalida getFormato() {
        return formato;
    }

    public void setFormato(FormatoSalida formato) {
        this.formato = formato;
    }
    
}

