package ar.edu.utn.frba.Server.Fuente_Dinamica.Service;

public interface IDetectorDeSpam {
    boolean esSpam(String texto);
    public boolean justificacionRepetida(String justificacion, String justificacionAntigua);
}
