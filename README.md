# Páctica RA1 - Ficheros

## Tecnologías

- Java 21
- Maven 21
- Apache-POI 5.5.1

## ¿Qué resuelve el programa?

El programa se encarga de obtener datos de un fichero XML y operar con los datos que se encuentran en él. Entre las tareas que se piden se encuentran:
- Mostrar el contenido por la terminal (Actividad 1).
- Almacenar información relativa al fichero en un fichero TXT (Actividad 2).
- Almacenar información obtenida del fichero XML en un fichero XLSX (Actividad 3)

## ¿Cómo se han solucionado los problemas?

Para solucionar los problemas propuestos en la actividad se han realizado varios servicios (3 aportados por el profesor y 1 creado de manera propia), cada servicio soluciona una tarea específica.

También se ha creado una estructura DAO para las operaciones relacionadas con el manejo de ficheros (lectura, creación y escritura). Se ha creado una interfaz con la declaración de los métodos comunes y 3 implementaciones, una por cada tipo de fichero, que implementan a la interfaz.

Para terminar, se han utilizado 3 clases diferentes a las autogeneradas con el XSD (2 aportadas por el profesor y una de creación propia). Cada una de las entidades se ha utilizado para resolver, al menos, uno de los ejercicios.

## Estructura de directorios

La estructura del proyecto es la siguiente.

```declarative
.
├── pom.xml
├── prueba.txt
├── README.md
├── src
│   └── main
│       ├── java
│       │   └── org
│       │       └── educa
│       │           ├── app
│       │           │   ├── Activity1.java
│       │           │   ├── Activity2.java
│       │           │   └── Activity3.java
│       │           ├── dao
│       │           │   ├── ProductosDAOImplTXT.java
│       │           │   ├── ProductosDAOImplXLSX.java
│       │           │   ├── ProductosDAOImplXML.java
│       │           │   └── ProductosDAO.java
│       │           ├── entity
│       │           │   ├── ProductoEntity.java
│       │           │   └── SummaryEntity.java
│       │           └── service
│       │               └── ProductoService.java
│       └── resources
│           ├── export
│           │   ├── result_junio2026.txt
│           │   └── result_junio2026.xlsx
│           ├── xml
│           │   └── inventario_junio2026.xml
│           └── xsd
│               └── inventario_junio2026.xsd
└── target
    ├── classes
    │   ├── export
    │   │   ├── result_junio2026.txt
    │   │   └── result_junio2026.xlsx
    │   ├── META-INF
    │   │   └── JAXB
    │   │       └── episode_xjc.xjb
    │   ├── xml
    │   │   └── inventario_junio2026.xml
    │   └── xsd
    │       └── inventario_junio2026.xsd
    ├── generated-sources
    │   ├── annotations
    │   └── jaxb
    │       ├── generated
    │       │   ├── Costes.java
    │       │   ├── ObjectFactory.java
    │       │   ├── Producto.java
    │       │   ├── Productos.java
    │       │   └── Proveedor.java
```

- En el directorio *src/main/resources/exports* se almacenan los ficheros creados por el programa (TXT y XLSX).
- En el directorio *src/main/resources/xml* se almacenan los XML de los que se obtiene la información con la que se va a trabajar.
- En el directorio *src/main/resources/xsd* se almacenan los XSD que cuentan con la estructura de los ficheros XML. Através de estos ficheros se crean las clases autogeneradas
- En el directorio *target/generated-sources/jaxb/generated/* se encuentran las clases autogeneradas con JAXB apartir del XSD.
- En el directorio *src/main/java/org/educa/app/* se encuentran las actividades, cada una en un fichero con un psvm y una llamada a un servicio.
- En el directorio *src/main/java/org/educa/entity/* se encuentran las entidades utilizadas para resolver los problemas propuestos.
- En el directorio *src/main/java/org/educa/services/* se encuentran los servicios que son llamados por los psvm de las actividades y uno más creado para aligerar la carga a uno de los servicios que estaban ya creados.
- En el directorio *src/main/java/org/educa/dao/* se encuentran los ficheros que manejan la interacción con los ficheros, los cuales son: una entidad que indica los métodos que todos los ficheros DAO deben implementar y un fichero que implementa la interfaz por cada tipo de fichero (esto porque cada tipo de fichero tiene una forma especial para acceder a ellos, ya sea en modo lectura o escritura).

## Solución de problemas

### Actividad 1: Lectura de datos de un fichero XML y mostrarlo por pantalla

1. En el main se llama a un servicio llamado *readFile()* y se le pasa la ruta del fichero XML por parámetro.
2. El servicio llama a *ProductosDAOImplXML* con el método *getProductos()* pasando la ruta del XML y del XSD por parámetro.
3. El servicio *getProductos()* realiza el *unmarshal* del fichero XML y devuelve un objeto de tipo *PRODUCTOS*.
4. El servicio *readFile()* devuelve los datos obtenidos ralizando un *PRODUCTOS.getProducto*, lo que devuelve un *List<PRODUCTO>*.
5. Llamamos a la función *setProductoEntity* pasándo por parámetro la lista de productos.
6. El método *setProductoEntity* devuelve una lista de *productoEntity*, que es la que se devuelve al main.
7. El main muestra por terminal una llamada al método *toPrint()* para cada *ProductoEntity*.

### Actividad 2: Escritura de datos en un fichero TXT

1. En el main se llama a un servicio llamado *exportSummary* al que se le pasan las rutas del XML y la ruta en la que hay que guardar el fichero TXT obtenido.
2. En el servicio se crea una lista de *PRODUCTO* y se almacena una llamada al servicio *readFile* pasando por parámetro el fichero XML obtenido.
3. Se calcula *beneficioTotal* con los productos obtenidos.
4. Obtenemos el nombre del fichero sin la extensión.
5. Obtenemos la fecha con el métoso *extractFecha*, a la que se le pasa el fichero XML por parámetro.
6. Creamos un objeto de tipo _SummaryEntity_ con los datos que hemos obtenido.
7. Una vez obtenidos los datos, se llama al método *escribirProductos* pasando por parámetros: la fecha que tiene el xml, la cantidad de productos, el beneficio total, la ruta del XML, el nombre completo y el tamaño en bytes.
8. El método *escribirProductos* ecriben un TXT con la información pasada por perámetro

### Actividad 3: Escritura de datos en un fichero XLSX

1. En el main se llama a un servicio llamado *exportExcel* al que se pasa por parámetro un path y una ruta de un XML.
2. El servicio llama al DAO de XML para obtener los datos del XML y almacenamos el resultado en una lista.
3. Cuando tenemos la lista, creamos otra lista de la entidad *ProductoParaExcelEntity*, a la que le vamos a añadir los datos que necesitemos mediante un for.
4. Ahora terminamos creando la ruta del fichero y llamando al metodo escribirProductos del DAO para XLSX.
5. En el fichero DAO para XLSX creamos los estilos para las celdas (uno para las cabeceras, uno en negrita con color de fondo, uno en negrita sin color de fondo, uno con color de fondo y otro sin color de fondo).
6. Creamos la cabecera con los estilos que corresponde.
7. Creamos cada una de las celdas dentro de un for, asignando a cada celda el estilo y contenido adecuado.
8. Una ve terminado escribimos el fichero de forma definitiva con un *FileOutputStram*.