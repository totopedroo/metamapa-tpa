package ar.edu.utn.frba;

import java.util.List;



public class Coleccion {
  private List<Hecho> hechos; //verificar nombre
  private String nombre;
  private String descripcion;
  private String criterioDeDependencia;

public Coleccion(List<Hecho> hechos, String nombre, String descripcion, String criterioDeDependencia){
  this.hechos = hechos;
  this.nombre = nombre;
  this.descripcion = descripcion;
  this.criterioDeDependencia = criterioDeDependencia;
}


  public List<Hecho> getHechos() {
    return hechos;
  }

  public void setHechos(List<Hecho> hechos) {
    this.hechos = hechos;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getCriterioDeDependencia() {
    return criterioDeDependencia;
  }

  public void setCriterioDeDependencia(String criterioDeDependencia) {
    this.criterioDeDependencia = criterioDeDependencia;
  }


  }
