package ar.edu.utn.frba.server.servicioAgregador.services;

public interface IDetectorDeSpam {
    boolean esSpam(String texto);
    public boolean justificacionRepetida(String justificacion, String justificacionAntigua);
}
