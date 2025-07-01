package ar.edu.utn.frba.Servicio_Agregador.Domain;

import ar.edu.utn.frba.Fuente_Proxy.Dtos.DesastreDto;

import java.util.List;

public class ApiDesastresResponse {
  private int current_page;
  private List<DesastreDto> data;

  public List<ar.edu.utn.frba.Servicio_Agregador.Dtos.DesastreDto> getData() {
    return data;
  }

  public void setData(List<DesastreDto> data) {
    this.data = data;
  }

  public int getCurrent_page() {
    return current_page;
  }

  public void setCurrent_page(int current_page) {
    this.current_page = current_page;
  }
}
