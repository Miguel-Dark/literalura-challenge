<h1 align="center">LiterAlura - Catálogo de Libros</h1>

<p align="center">
  <img src="banner_literalura.png" alt="Banner LiterAlura" width="100%">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white" alt="Hibernate">
  <img src="https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white" alt="Maven">
</p>

<p>Este proyecto es una aplicación de consola diseñada para gestionar un catálogo literario personal consumiendo datos reales de la <strong>API Gutendex</strong>. El sistema permite realizar búsquedas de libros en la web, persistir la información en una base de datos relacional y realizar diversas consultas estadísticas y de filtrado sobre los datos almacenados.</p>

<h2>Demostración en Video</h2>

<table align="center">
  <tr>
    <td align="center">
      <a href="https://youtu.be/uaEPHD_mbV8">
        <img src="https://img.youtube.com/vi/uaEPHD_mbV8/0.jpg" width="400px;"/><br />
        <b>Parte 1: Registro y Consultas</b>
      </a>
    </td>
    <td align="center">
      <a href="https://youtu.be/mm4VmIEx9nQ">
        <img src="https://img.youtube.com/vi/mm4VmIEx9nQ/0.jpg" width="400px;"/><br />
        <b>Parte 2: Filtros y Estadísticas</b>
      </a>
    </td>
  </tr>
</table>

<h2>Descripción</h2>
<p>LiterAlura nace como un desafío técnico para demostrar habilidades en el desarrollo Backend con <strong>Java</strong> y <strong>Spring Framework</strong>. La aplicación interactúa con la API de Gutendex, que ofrece acceso a más de 70,000 libros del Proyecto Gutenberg. El objetivo principal es ofrecer una herramienta eficiente para registrar libros y autores, evitando duplicados y proporcionando métricas de interés para el usuario.</p>

<h2>Estado del Proyecto</h2>
<p><strong>Finalizado:</strong> El sistema cumple con todos los requisitos básicos propuestos y añade funcionalidades adicionales de análisis estadístico.</p>

<h2>Capturas de Pantalla</h2>
<ol>
  <li><strong>Autores Registrados en la Base de Datos:</strong><br><img src="consola_autores_registrados_db.png" alt="Captura 1"></li>
  <li><strong>Búsqueda de Autores Vivos:</strong><br><img src="consola_autores_vivos_db.png" alt="Captura 2"></li>
  <li><strong>Libros Filtrados por Idioma:</strong><br><img src="consola_libros_encontrados_por_idioma_db.png" alt="Captura 3"></li>
  <li><strong>Catálogo de Libros Registrados:</strong><br><img src="consola_libros_registrados_db.png" alt="Captura 4"></li>
  <li><strong>Ranking de los 10 Libros más Descargados:</strong><br><img src="consola_top_10_libros_mas_descargados_db.png" alt="Captura 5"></li>
</ol>

<h2>Funcionalidades</h2>
<ul>
  <li><strong>Buscar libro por título:</strong> Realiza una consulta a la API Gutendex. Si el libro se encuentra, se registra automáticamente el autor y el libro en la base de datos local.</li>
  <li><strong>Listar libros registrados:</strong> Muestra todos los títulos almacenados en la base de datos con sus respectivos detalles.</li>
  <li><strong>Listar autores registrados:</strong> Presenta una lista única de autores junto con los libros asociados.</li>
  <li><strong>Listar autores vivos en un determinado año:</strong> Filtra la base de datos para identificar autores con vida en un año específico.</li>
  <li><strong>Listar libros por idioma:</strong> Filtra el catálogo local por códigos (es, en, fr, pt).</li>
  <li><strong>Mostrar top 10 libros:</strong> Genera un ranking de los títulos más descargados.</li>
  <li><strong>Buscar autor por nombre:</strong> Localiza autores específicos mediante coincidencia de caracteres.</li>
  <li><strong>Generar estadísticas:</strong> Proporciona un resumen numérico con media, máximo, mínimo y total de descargas.</li>
</ul>

<h2>Tecnologías Utilizadas</h2>
<ul>
  <li><strong>Java 17:</strong> Lenguaje de programación principal.</li>
  <li><strong>Maven:</strong> Gestor de dependencias.</li>
  <li><strong>Spring Boot 3.2.3:</strong> Framework base.</li>
  <li><strong>Spring Data JPA:</strong> Persistencia de datos mediante Hibernate.</li>
  <li><strong>PostgreSQL 16+:</strong> Base de datos relacional.</li>
  <li><strong>Jackson:</strong> Procesamiento de datos JSON.</li>
  <li><strong>IntelliJ IDEA:</strong> Entorno de desarrollo.</li>
</ul>

<h2>Configuración y Ejecución</h2>
<p>Para ejecutar este proyecto de forma local, es necesario contar con <strong>Java 17</strong> y una instancia de <strong>PostgreSQL</strong> activa.</p>
<ol>
  <li>Clonar el repositorio.</li>
  <li>Configurar <code>application.properties</code> con las credenciales de tu base de datos local (url, username, password).</li>
  <li>Ejecutar la clase principal <code>LiteraluraApplication</code>.</li>
</ol>

<h2>Arquitectura</h2>
<ul>
  <li><strong>Principal:</strong> Lógica de interacción con el usuario vía consola.</li>
  <li><strong>Model:</strong> Entidades de negocio y registros para el mapeo JSON.</li>
  <li><strong>Repository:</strong> Interfaces JpaRepository para comunicación con PostgreSQL.</li>
  <li><strong>Service:</strong> Consumo de API externa y conversión de datos.</li>
</ul>

<h2>Muestra de uso del Sistema</h2>
<p>Durante la ejecución, implementé validaciones para asegurar que el usuario ingrese números válidos en el menú y que no se dupliquen registros, garantizando la integridad de la información.</p>

<hr>
<p align="center"><strong>Autor: Miguel Ángel de la Cruz Lázaro</strong></p>
