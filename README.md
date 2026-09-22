# Biblioteca de Imágenes

Aplicación de escritorio en Java con interfaz gráfica (Swing) para generar, analizar, filtrar y visualizar colecciones de imágenes junto con sus metadatos EXIF y GPS. Proyecto final de la asignatura de Programación Orientada a Objetos II — Universidad San Jorge.

## Características

- Generación aleatoria de estructuras de carpetas con imágenes de prueba, incluyendo metadatos EXIF (fecha de captura) y coordenadas GPS aleatorias.
- Análisis recursivo de directorios para extraer los metadatos de todas las imágenes encontradas.
- Interfaz gráfica con tres vistas sincronizadas:
  - **Árbol de carpetas**, para navegar por la colección de imágenes.
  - **Tabla de metadatos**, ordenable por columnas (nombre, dimensiones, fecha, coordenadas).
  - **Visor de imagen**, que muestra la imagen seleccionada escalada proporcionalmente.
- Sistema de filtros sobre la colección: por tamaño (píxeles), por fecha exacta, por presencia de GPS y por texto en el nombre del archivo.
- Ordenación de resultados por distintos criterios (fecha, ancho, nombre).
- **MiniPaint**, una pequeña herramienta de dibujo integrada para crear imágenes propias desde la interfaz.
- Tests unitarios (JUnit) para la lógica de filtros, ordenación y formato de datos.

## Estructura del proyecto

```
BibliotecaDeImagenes/
├── src/
│   ├── analisisDeImagenes/
│   │   ├── Analizador.java      # Recorre directorios y extrae metadatos de imágenes
│   │   ├── Formato.java         # Modelo de datos: imagen + metadatos
│   │   ├── Filtros.java         # Filtros sobre listas de imágenes
│   │   └── Ordenaciones.java    # Criterios de ordenación
│   ├── coleccionImagenes/
│   │   ├── CreateImages.java    # Genera imágenes aleatorias con EXIF/GPS
│   │   └── FolderStructure.java # Genera estructuras de carpetas recursivas
│   ├── graphicalUserInterface/
│   │   ├── TreeView.java        # Ventana principal (árbol + tabla + visor + menús)
│   │   ├── TableView.java       # Tabla de metadatos ordenable
│   │   ├── ImageView.java       # Visor de imagen individual
│   │   └── MiniPaint.java       # Herramienta de dibujo básica
│   └── tests/
│       ├── FiltrosTest.java
│       ├── FormatoTest.java
│       └── OrdenacionesTest.java
└── lib/
    └── commons-imaging-1.0-alpha3.jar   # Lectura/escritura de metadatos EXIF
```

## Arquitectura

El proyecto separa responsabilidades en tres paquetes:

- **`coleccionImagenes`** — generación de datos de prueba (imágenes con metadatos falsos, estructura de carpetas).
- **`analisisDeImagenes`** — lógica de negocio: análisis, filtrado y ordenación, independiente de la interfaz.
- **`graphicalUserInterface`** — capa de presentación en Swing, que consume las clases de análisis sin conocer cómo se generan ni se guardan los datos.

Esta separación permite testear la lógica de filtros y ordenación (paquete `tests`) sin depender de la interfaz gráfica.

## Requisitos

- **JDK 17** o superior (usa `switch` con patrones/`instanceof` pattern matching).
- La librería [Apache Commons Imaging](https://commons.apache.org/proper/commons-imaging/) (incluida en `lib/`), usada para leer y escribir metadatos EXIF/GPS.
- JUnit (para ejecutar los tests), si se abre el proyecto en Eclipse o IntelliJ con soporte de JUnit configurado.

## Ejecución

### Desde un IDE (Eclipse / IntelliJ)

1. Importa la carpeta `BibliotecaDeImagenes` como proyecto Java existente.
2. Añade `lib/commons-imaging-1.0-alpha3.jar` al *classpath* del proyecto.
3. Ejecuta la clase `graphicalUserInterface.TreeView` (contiene el método `main`).

### Desde línea de comandos

```bash
cd BibliotecaDeImagenes
javac -cp lib/commons-imaging-1.0-alpha3.jar -d bin $(find src -name "*.java")
java -cp bin:lib/commons-imaging-1.0-alpha3.jar graphicalUserInterface.TreeView
```

*(En Windows, sustituye `:` por `;` en el classpath de `java` y usa PowerShell o un script equivalente para recopilar los `.java`.)*

Al arrancar, la aplicación crea automáticamente una carpeta `imagenes-generadas/` en el directorio de ejecución, donde se guardan las imágenes de prueba generadas desde el menú **Archivo → Creación aleatoria de carpetas e imágenes**.

## Uso

1. Genera una colección de prueba desde **Archivo → Creación aleatoria de carpetas e imágenes**, o usa **Archivo → Paint** para crear tus propias imágenes.
2. Navega por el árbol de carpetas de la izquierda para cargar los metadatos de una carpeta en la tabla.
3. Haz clic en una fila de la tabla para previsualizar la imagen correspondiente.
4. Usa el menú **Filtros** para acotar la colección por tamaño, fecha, GPS o nombre; **Quitar filtros** restaura la lista completa.
5. Haz clic en los encabezados de la tabla para ordenar por esa columna.

## Tests

El paquete `tests` incluye pruebas unitarias con JUnit para las clases `Filtros`, `Formato` y `Ordenaciones`. Pueden ejecutarse directamente desde el IDE (clic derecho → Run as → JUnit Test) si el proyecto tiene JUnit añadido al *build path*.

## Autor

**Mario De Juan Sánchez Flor**
Estudiante de Doble Grado en Ingeniería en Ciberseguridad e Ingeniería Informática — Universidad San Jorge
[LinkedIn](https://www.linkedin.com/in/mariodejuan)

## Licencia

Proyecto académico desarrollado con fines educativos para la asignatura de Programación Orientada a Objetos II (POO2).
