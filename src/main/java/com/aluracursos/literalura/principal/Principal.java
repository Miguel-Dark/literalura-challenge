package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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
                    5 - Listar libros por idioma
                    6 - Mostrar top 10 libros
                    7 - Buscar autor por nombre
                    8 - Generar estadisticas DB
                    0 - Salir
                    """;

            try {
                System.out.println("\n**************************************************************");
                System.out.println(">>> ¿Qué desea consultar hoy?");
                System.out.println("**************************************************************\n");
                System.out.println(menu);
                System.out.print("Por favor, seleccione una opción [0-8]: ");
                var lectura = teclado.nextLine();
                opcion = Integer.parseInt(lectura);

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
                        listarAutoresVivosEnUnDeterminadoAnio();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 6:
                        mostrarTop10Libros();
                        break;
                    case 7:
                        buscarAutorPorNombre();
                        break;
                    case 8:
                        generarEstadisticasDB();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("******************************************");
                System.out.println("ERROR: Por favor, ingresa un número válido.");
                System.out.println("******************************************");
                opcion = -1;
            }
        }
    }

    private void generarEstadisticasDB() {
        List<Libro> todosLosLibros = libroRepository.findAll();

        if (todosLosLibros.isEmpty()) {
            System.out.println("\n[!] No hay libros en la base de datos para generar estadísticas.\n");
            return;
        }

        DoubleSummaryStatistics est = todosLosLibros.stream()
                .filter(l -> l.getNumeroDeDescargas() > 0)
                .mapToDouble(Libro::getNumeroDeDescargas)
                .summaryStatistics();

        System.out.println("\n=================================================");
        System.out.println("   ESTADÍSTICAS GLOBALES DE TU BIBLIOTECA   ");
        System.out.println("=================================================");
        System.out.printf(" > Media de descargas: %.2f%n", est.getAverage());
        System.out.printf(" > Máxima de descargas: %.0f%n", est.getMax());
        System.out.printf(" > Mínima de descargas: %.0f%n", est.getMin());
        System.out.println(" > Total de libros evaluados: " + est.getCount());
        System.out.println("=================================================\n");
    }

    private void buscarAutorPorNombre() {
        System.out.println("Escribe el nombre del autor que deseas buscar:");
        var nombreAutor = teclado.nextLine();
        Optional<Autor> autorEncontrado = autorRepository.findByNombreContainsIgnoreCase(nombreAutor);

        if (autorEncontrado.isPresent()) {
            Autor a = autorEncontrado.get();
            System.out.println("\n--- AUTOR ENCONTRADO ---");
            System.out.println("Autor: " + a.getNombre());
            System.out.println("Fecha de nacimiento: " + (a.getFechaDeNacimiento() != null ? a.getFechaDeNacimiento() : "N/A"));
            System.out.println("Fecha de fallecimiento: " + (a.getFechaDeFallecimiento() != null ? a.getFechaDeFallecimiento() : "Presente"));

            String librosDelAutor = a.getLibros().stream()
                    .map(Libro::getTitulo)
                    .collect(Collectors.joining(", "));

            System.out.println("Libros: [" + librosDelAutor + "]");
            System.out.println("------------------------\n");
        } else {
            System.out.println("\n[!] No se encontró ningún autor con el nombre '" + nombreAutor + "' en nuestra base de datos.\n");
        }
    }

    private void mostrarTop10Libros() {
        List<Libro> topLibros = libroRepository.findTop10ByOrderByNumeroDeDescargasDesc();

        if (topLibros.isEmpty()) {
            System.out.println("\nNo hay libros registrados para calcular el Top 10.");
        } else {
            System.out.println("\n----- LOS 10 LIBROS MÁS DESCARGADOS -----");

            for (int i = 0; i < topLibros.size(); i++) {
                Libro l = topLibros.get(i);
                System.out.println((i + 1) + ". TÍTULO: " + l.getTitulo().toUpperCase());
                System.out.println("   Autor: " + (l.getAutor() != null ? l.getAutor().getNombre() : "Desconocido"));
                System.out.println("   Descargas: " + l.getNumeroDeDescargas());
                System.out.println("   --------------------------------------");
            }
            System.out.println("\n");
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
        } else {
            System.out.println("\n--- LIBROS REGISTRADOS ---");
            libros.forEach(l -> {
                System.out.println("\n---------- LIBRO ----------");
                System.out.println("Título: " + l.getTitulo().toUpperCase());
                System.out.println("Autor: " + (l.getAutor() != null ? l.getAutor().getNombre() : "Desconocido"));
                System.out.println("Idioma: " + l.getIdioma());
                System.out.println("Número de descargas: " + l.getNumeroDeDescargas());
                System.out.println("---------------------------\n");
            });
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            System.out.println("\n--- AUTORES REGISTRADOS ---");
            autores.forEach(a -> {
                System.out.println("Autor: " + a.getNombre());
                System.out.println("Fecha de nacimiento: " + (a.getFechaDeNacimiento() != null ? a.getFechaDeNacimiento() : "N/A"));
                System.out.println("Fecha de fallecimiento: " + (a.getFechaDeFallecimiento() != null ? a.getFechaDeFallecimiento() : "Presente"));

                String librosDelAutor = a.getLibros().stream()
                        .map(Libro::getTitulo)
                        .collect(Collectors.joining(", "));

                System.out.println("Libros: [" + librosDelAutor + "]");
                System.out.println("---------------------------"); // El toque de orden final
            });
        }
    }

    private void listarAutoresVivosEnUnDeterminadoAnio() {
        System.out.println("Ingresa el año que deseas consultar:");
        try {
            var anio = teclado.nextInt();
            teclado.nextLine();

            List<Autor> autoresVivos = autorRepository.findByFechaDeNacimientoLessThanEqualAndFechaDeFallecimientoGreaterThanEqual(anio, anio);

            if (autoresVivos.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + anio);
            } else {
                System.out.println("\n--- AUTORES VIVOS EN EL AÑO " + anio + " ---");
                autoresVivos.forEach(a -> {
                    System.out.println("Autor: " + a.getNombre());
                    System.out.println("Fecha de nacimiento: " + (a.getFechaDeNacimiento() != null ? a.getFechaDeNacimiento() : "N/A"));
                    System.out.println("Fecha de fallecimiento: " + (a.getFechaDeFallecimiento() != null ? a.getFechaDeFallecimiento() : "Presente"));

                    String librosDelAutor = a.getLibros().stream()
                            .map(Libro::getTitulo)
                            .collect(Collectors.joining(", "));

                    System.out.println("Libros: [" + librosDelAutor + "]");
                    System.out.println("----------------------------------");
                });
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Debes ingresar un número válido para el año.");
            teclado.nextLine();
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingrese el idioma para buscar los libros:
                es - español
                en - inglés
                fr - francés
                pt - portugués
                """);

        var idiomaBusqueda = teclado.nextLine();

        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idiomaBusqueda);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("\nNo se encontraron libros registrados en el idioma: " + idiomaBusqueda);
        } else {
            System.out.println("\n--- LIBROS ENCONTRADOS (" + idiomaBusqueda + ") ---");
            librosPorIdioma.forEach(l -> {
                System.out.println("----- LIBRO -----");
                System.out.println("Título: " + l.getTitulo().toUpperCase());
                System.out.println("Autor: " + (l.getAutor() != null ? l.getAutor().getNombre() : "Desconocido"));
                System.out.println("Idioma: " + l.getIdioma());
                System.out.println("Número de descargas: " + l.getNumeroDeDescargas());
                System.out.println("-----------------\n");
            });

            long cantidad = librosPorIdioma.size();
            System.out.println("--- ESTADÍSTICAS ---");
            System.out.println("Total de libros en '" + idiomaBusqueda + "': " + cantidad);
            System.out.println("---------------------\n");
        }
    }

    private void buscarLibroWeb() {
        Datos datos = getDatosLibro();
        Optional<DatosLibro> libroBuscado = datos.resultados().stream().findFirst();

        if (libroBuscado.isPresent()) {
            DatosLibro datosLibro = libroBuscado.get();

            String nombreAutorBusqueda = datosLibro.autor().get(0).nombre();
            Optional<Autor> autorExistente = autorRepository.findByNombreContainsIgnoreCase(nombreAutorBusqueda);

            Autor autor;
            if (autorExistente.isPresent()) {
                autor = autorExistente.get();
                System.out.println("Autor ya registrado en la base de datos: " + autor.getNombre());
            } else {
                autor = new Autor(datosLibro.autor().get(0));
                autorRepository.save(autor);
                System.out.println("Nuevo autor registrado: " + autor.getNombre());
            }

            Optional<Libro> libroExistente = libroRepository.findByTituloContainsIgnoreCase(datosLibro.titulo());

            if (libroExistente.isPresent()) {
                System.out.println("¡Aviso! El libro '" + datosLibro.titulo() + "' ya está en tu base de datos.");
            } else {
                Libro libro = new Libro(datosLibro);
                libro.setAutor(autor);
                libroRepository.save(libro);
                System.out.println("Libro guardado con éxito en la base de datos.");
            }

            System.out.println("\n---- DETALLES DEL LIBRO ENCONTRADO ----");
            System.out.println("Título Oficial: " + datosLibro.titulo().toUpperCase());

            String nombreAutor = datosLibro.autor().isEmpty() ? "Desconocido" : datosLibro.autor().get(0).nombre();
            System.out.println("Autor: " + nombreAutor);

            String idioma = datosLibro.idiomas().isEmpty() ? "Desconocido" : datosLibro.idiomas().get(0);
            System.out.println("Idioma: " + idioma);

            System.out.println("Número de descargas: " + datosLibro.numeroDeDescargas());
            System.out.println("--------------------------------------\n");

        }

        if (!datos.resultados().isEmpty()) {
            DoubleSummaryStatistics est = datos.resultados().stream()
                    .filter(d -> d.numeroDeDescargas() > 0)
                    .mapToDouble(DatosLibro::numeroDeDescargas)
                    .summaryStatistics();

            System.out.println("\n--- ESTADÍSTICAS DE BÚSQUEDA ---");
            System.out.println("Media de descargas: " + est.getAverage());
            System.out.println("Máxima de descargas: " + est.getMax());
            System.out.println("Mínima de descargas: " + est.getMin());
            System.out.println("Total de registros: " + est.getCount());
        } else {
            System.out.println("\nNo se encontraron estadísticas porque no hubo resultados de búsqueda.");
        }

    }

    private Datos getDatosLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar:");
        var nombreLibro = teclado.nextLine();
        try {
            String nombreCodificado = URLEncoder.encode(nombreLibro, StandardCharsets.UTF_8);
            var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreCodificado);
            var datos = conversor.obtenerDatos(json, Datos.class);
            return datos;
        } catch (Exception e) {
            System.out.println("Error al procesar la búsqueda. Intenta con un nombre más sencillo.");
            return new Datos(Collections.emptyList());
        }
    }
}
