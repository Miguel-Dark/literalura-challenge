package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.model.DatosLibro;
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

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por título
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroWeb();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibroWeb() {
        Datos datos = getDatosLibro();

        Optional<DatosLibro> libroBuscado = datos.resultados().stream()
                .findFirst();

        if (libroBuscado.isPresent()) {
            var libro = libroBuscado.get();
            System.out.println("\n--- LIBRO ENCONTRADO ---");
            System.out.println("Título: " + libro.titulo());

            // Requisito: Primer autor de la lista
            String autor = libro.autor().isEmpty() ? "Desconocido" : libro.autor().get(0).nombre();
            System.out.println("Autor: " + autor);

            // Requisito: Solo el primer idioma de la lista
            String idioma = libro.idiomas().isEmpty() ? "Desconocido" : libro.idiomas().get(0);
            System.out.println("Idioma: " + idioma);

            System.out.println("Descargas: " + libro.numeroDeDescargas());
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
