package domain;

public class Contribuyente {
    public String nombre;
    public String apellido;
    public Integer edad;

    public Contribuyente(String nombre, String apellido, Integer edad) {
        this.nombre = nombre;
    }

    public void CrearSolicitudEliminacion(Hecho hecho, String justificacion){
        SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(justificacion);
        hecho.agregarSolicitud(solicitud);
    }

}
