package ar.edu.utn.frba.Fuente_Proxy.Service.Service;

public interface IDetectorDeSpam {
    boolean esSpam(String texto);
    public boolean justificacionRepetida(String justificacion, String justificacionAntigua);
}
