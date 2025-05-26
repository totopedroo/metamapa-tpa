package ar.edu.utn.frba.domain;

public class main {

  public static void main(String[] args) {
    //Importador importador = new Importador();
    //importador.importarFromCSV("prueba1.csv");
    //importador.visualizarHechos();
    /*
    Administrador admin = new Administrador("Juan", "juan@metamapa.org");

    //Escenario 1
  List<CriterioDePertenencia> criterios = new ArrayList<>();
    Coleccion coleccion1 = admin.crearColeccion("Coleccion prueba", "esto es una prueba", criterios);
  coleccion1.leerSegunCriterios(criterios,"prueba1.csv");
    System.out.println("Colección creada: " + coleccion1.getTitulo());
    //Escenario 1.2
    System.out.println("...");
    System.out.println("...");
    System.out.println("Puebo escenario 1.2: Criterios de pertenencia");
    List<CriterioDePertenencia> criterios2 = new ArrayList<>();
    CriterioDePertenencia criterioFecha1 = new CriterioDePertenencia(
        "fecha del hecho",
        LocalDate.of(2000, 1, 1),
        LocalDate.of(2010, 1, 1)
    );
    criterios2.add(criterioFecha1);
    Coleccion coleccion2 = admin.crearColeccion("HECHOS 2001-2010", "esta es la segunda prueba", criterios2);
    coleccion2.leerSegunCriterios(criterios2, "prueba1.csv");
    System.out.println("CREADA LA COLECCION:  " + coleccion2.getTitulo());

    System.out.println("HECHOS EN COLECCIÓN TRAS LECTURA CON CRITERIO DE FECHA:");
    coleccion2.getHechos().forEach(h -> System.out.println(h.getTitulo())); // Debug


    CriterioDePertenencia criterioCategoria = new CriterioDePertenencia("Categoria", "Caida de aeronave");
    criterios2.add(criterioCategoria);

    System.out.println("FILTRO POR FECHA Y CATEGORIA");
    coleccion2.leerSegunCriterios(criterios2, "prueba1.csv");
    // Esperado: El segundo hecho se excluye, porque no es de esa categoría



    System.out.println("APLICO FILTRO POR CATEGORÍA (NO VUELVO A LEER EL ARCHIVO)");
    List<Hecho> filtradosPorCategoria = coleccion2.getHechos().stream()
        .filter(h -> h.getCategoria().equalsIgnoreCase("Caida de aeronave"))
        .collect(Collectors.toList());

    System.out.println("HECHOS QUE CUMPLEN CATEGORÍA:");
    filtradosPorCategoria.forEach(h -> System.out.println(h.getTitulo()));


    //Escenario 1.3 (Filtros de visualizador)
    System.out.println("...");
    System.out.println("...");
    System.out.println("Puebo escenario 1.3");

    System.out.println("\n========== (visualizador navega todos los hechos disponibles) TODOS LOS HECHOS DE LA COLECCION 'HECHOS 2001-2010' VISTOS POR VISUALIZADOR 'fausto' ==========");
    Visualizador visitante = new Visualizador("Fausto");
    visitante.visualizarHechos(coleccion2);

    System.out.println("\n========== (visualizador aplica filtros) FILTRO CON VISUALIZADOR 'fausto' EN COLECCION 'HECHOS 2001-2010' (titulo contiene palabra 'Incidente') ==========");
    visitante.agregarFiltro(new CriterioDePertenencia("Titulo", "incidente"));
    visitante.visualizarHechosFiltrados(coleccion2);

    System.out.println("\n========== FILTRO POR CATEGORIA CON VISUALIZADOR 'bauti': Caida de aeronave y Titulo untitulo: ==========");
    CriterioDePertenencia criterioBautista = new CriterioDePertenencia("Categoria", "Caida de aeronave");
    CriterioDePertenencia criterioBautista2 = new CriterioDePertenencia("Titulo", "untitulo");
    Visualizador bautista = new Visualizador("Bautista");

    bautista.agregarFiltro(criterioBautista);
    bautista.agregarFiltro(criterioBautista2);
    bautista.visualizarHechosFiltrados(coleccion2);

    //Escenario 1.4: Etiquetas

    // Buscar el hecho por título dentro de la colección
    System.out.println("...");
    System.out.println("...");
    System.out.println("Pruebo escenario 1.4: Etiquetas");

    Hecho hechoOlavarria = null;
    for (Hecho h : coleccion2.getHechos()) {
      System.out.println("TITULO: " + h.getTitulo());
      if (h.getTitulo().equalsIgnoreCase("Caída de aeronave impacta en Olavarría")) {
        hechoOlavarria = h;
        break;
      }
    }

    if (hechoOlavarria != null) {
      // Etiquetar
      hechoOlavarria.agregarEtiqueta("Olavarría");
      hechoOlavarria.agregarEtiqueta("Grave");

      // Mostrar etiquetas
      System.out.println("Etiquetas del hecho: " + hechoOlavarria.getEtiquetas());

      // Validación
      if (hechoOlavarria.getEtiquetas().contains("Olavarría") && hechoOlavarria.getEtiquetas().contains("Grave")) {
        System.out.println("El hecho tiene ambas etiquetas correctamente asociadas: OK");
      } else {
        System.out.println("ERROR: Las etiquetas no se asignaron correctamente.");
      }
    } else {
      System.out.println("No se encontró el hecho con título 'Caída de aeronave impacta en Olavarría'");
    }
    Importador importador = new Importador();
    //Escenario 2
    System.out.println("...");
    System.out.println("...");
    System.out.println("Pruebo escenario 2: Importacion de hechos por csv");
    //esto lo podemos hacer de dos formas, o ceando un nuevo metodo en administrador, o usando el metodo de coleccion, pero sin agregar filtros y se crearía una "supercoloeccion"
     importador.leerCSV("archivodefinitivo.csv");


    //Escenario 3
    // Crear una solicitud de eliminación asociada a este hecho. Quedará en estado pendiente.
    System.out.println("...");
    System.out.println("...");
    System.out.println("Pruebo escenario 3: Solicitudes de eliminacion");

    Hecho hechoSanLorenzo = new Hecho(
        "Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe",
        "Grave brote de enfermedad contagiosa ocurrió en las inmediaciones de San Lorenzo, Santa Fe. El incidente dejó varios heridos y daños materiales. Se ha declarado estado de emergencia en la región para facilitar la asistencia.",
        "Evento sanitario",
        null,
        -32.786098,
        -60.741543,
        LocalDate.of(2005, 7, 5),
        LocalDate.now()
    );

    // Creo solicitud de eliminación con mas de 500 caracteres
    String justificacionLarga = "Este hecho contiene datos sensibles, información inexacta y puede generar pánico innecesario en la población. " +
        "Además, la publicación de este hecho en la plataforma sin el consentimiento adecuado de las autoridades sanitarias y de los involucrados " +
        "podría constituir una violación de los protocolos establecidos. Se solicita su eliminación inmediata por el bien de la comunidad. " +
        "Cabe destacar que varios vecinos han expresado su malestar al respecto, por lo que se eleva esta solicitud de forma urgente y prioritaria.";

    SolicitudDeEliminacion solicitud1 = new SolicitudDeEliminacion(justificacionLarga);
    hechoSanLorenzo.agregarSolicitud(solicitud1);
    System.out.println("Solicitud creada con estado: " + solicitud1.estado); // Esperado: Pendiente

    // Rechazar esta solicitud un día después de su creación. Dado que fue rechazada, el hecho puede ser agregado a cualquier colección.
    solicitud1.rechazarSolicitud();
    System.out.println("Solicitud rechazada, nuevo estado: " + solicitud1.estado); // Esperado: Rechazado

    // Verificar que aún se puede agregar a una colección
    List<CriterioDePertenencia> criterios3 = new ArrayList<>();
    Coleccion coleccion3 = admin.crearColeccion("Colección con hecho rechazado", "Prueba con hecho válido", criterios3);
    coleccion3.agregarHecho(hechoSanLorenzo); // Esperado: "Hecho agregado a la colección: ..."

    //Verificar que la solicitud haya quedado en estado rechazada.
    System.out.println("Estado solicitud: " + solicitud1.estado); // Esperado: Rechazado

    // Generar otra solicitud para el mismo hecho
    SolicitudDeEliminacion solicitud2 = new SolicitudDeEliminacion(justificacionLarga);
    hechoSanLorenzo.agregarSolicitud(solicitud2);
    System.out.println("Segunda solicitud creada. Estado: " + solicitud2.estado); // Pendiente

    // Aceptar la segunda solicitud (después de 2 horas)
    //admin.aceptarSolicitudDeEliminacion(hechoSanLorenzo, solicitud2);

    //Esta vez el hecho no debería poder agregarse a una colección, puesto que este fue eliminado.
    coleccion3.agregarHecho(hechoSanLorenzo); // Esperado: "No se puede agregar el hecho '...' porque fue eliminado."

    //Verificar que la solicitud haya quedado en estado aceptada.
    System.out.println("Segunda solicitud aceptada. Estado actual: " + solicitud2.estado); // Aceptado
  */
  }
}






