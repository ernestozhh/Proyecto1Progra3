
package Entidades;

public class Persona {

    private String cedula;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String codElectoral;
    private String provincia;
    private String canton;
    private String distrito;

    public Persona() {
        this.cedula = "";
        this.nombre = "";
        this.apellido1 = "";
        this.apellido2 = "";
        this.codElectoral = "";
        this.provincia = "";
        this.canton = "";
        this.distrito = "";
    }

    public Persona(String cedula, String nombre, String apellido1, String apellido2,
            String codElectoral, String provincia, String canton, String distrito) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.codElectoral = codElectoral;
        this.provincia = provincia;
        this.canton = canton;
        this.distrito = distrito;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getCodElectoral() {
        return codElectoral;
    }

    public void setCodElectoral(String codElectoral) {
        this.codElectoral = codElectoral;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }
    
}

