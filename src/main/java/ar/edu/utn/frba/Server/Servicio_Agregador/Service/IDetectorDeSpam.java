package ar.edu.utn.frba.Server.Servicio_Agregador.Service;

public interface IDetectorDeSpam {
    boolean esSpam(String texto);
    public boolean justificacionRepetida(String justificacion, String justificacionAntigua);
}
