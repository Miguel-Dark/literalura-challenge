package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/";

    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository){
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    0 - Salir
                    """;

            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroWeb();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEnUnDeterminadoAno();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }
    
    private void listarLibrosRegistrados() {
    }

    private void listarAutoresRegistrados() {
    }

    private void listarAutoresVivosEnUnDeterminadoAno() {
    }

    private void buscarLibroWeb() {
        Datos datos = getDatosLibro();
        Optional<DatosLibro> libroBuscado = datos.resultados().stream().findFirst();

        if (libroBuscado.isPresent()) {
            DatosLibro datosLibro = libroBuscado.get();

            // 1. Convertir DatosAutor (DTO) a Autor (Entidad)
            Autor autor = new Autor(datosLibro.autor().get(0));

            // 2. Convertir DatosLibro (DTO) a Libro (Entidad)
            Libro libro = new Libro(datosLibro);

            // 3. Establecer la relación (Vincularlos)
            libro.setAutor(autor);

            // 4. PERSISTIR (Guardar en la DB)
            // Guardamos el autor y, gracias al CascadeType.ALL, se guarda el libro
            autorRepository.save(autor);

            System.out.println("Libro guardado con éxito en la base de datos.");
            // ... mostrar datos por consola como ya lo hacías ...

            System.out.println("\n--- LIBRO ENCONTRADO ---");
            System.out.println("Título: " + datosLibro.titulo());

            // Requisito: Primer autor de la lista
            String nombreAutor = datosLibro.autor().isEmpty() ? "Desconocido" : datosLibro.autor().get(0).nombre();
            System.out.println("Autor: " + nombreAutor);

            // Requisito: Solo el primer idioma de la lista
            String idioma = datosLibro.idiomas().isEmpty() ? "Desconocido" : datosLibro.idiomas().get(0);
            System.out.println("Idioma: " + idioma);

            System.out.println("Descargas: " + datosLibro.numeroDeDescargas());
            System.out.println("--------------------------------------\n");

        }

        DoubleSummaryStatistics est = datos.resultados().stream()
                .filter(d -> d.numeroDeDescargas() > 0)
                .mapToDouble(DatosLibro::numeroDeDescargas)
                .summaryStatistics();

        System.out.println("\n--- ESTADÍSTICAS DE BÚSQUEDA ---");
        System.out.println("Media de descargas: " + est.getAverage());
        System.out.println("Máxima de descargas: " + est.getMax());
        System.out.println("Mínima de descargas: " + est.getMin());
        System.out.println("Total de registros: " + est.getCount());

    }

    private Datos getDatosLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar:");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        var datos = conversor.obtenerDatos(json, Datos.class);
        return datos;
    }

    // Preparando el terreno para las Derived Queries (Listar por idioma)
    private void listarLibrosPorIdioma() {
        System.out.println("Introduce el código del idioma (es, en, fr, pt...):");
        var idiomaBusqueda = teclado.nextLine();

        // Aquí es donde entraría tu Repository en el futuro:
        // repositorio.findByIdioma(idiomaBusqueda);

        System.out.println("Buscando libros en el idioma: " + idiomaBusqueda);
        // (Aquí iría la lógica de impresión de la lista)
    }
}
