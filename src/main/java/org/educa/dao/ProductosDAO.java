package org.educa.dao;

import generated.Producto;
import generated.Productos;
import jakarta.xml.bind.JAXBException;
import org.educa.entity.ProductoEntity;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ProductosDAO {
    /**
     * Lee y deserializa un archivo XML que contiene la información de productos,
     * validando su estructura contra un archivo de esquema XSD
     *
     * @param pathXml Ruta del archivo XML que contiene los datos de los productos
     * @param pathXsd Ruta del archivo de esquema XSD utilizado para validar el XML
     * @return Una instancia de {@link Productos} con los datos mapeados desde el XML
     * @throws JAXBException Lanza una excepción si ocurre un error al procesas el XML
     */
    Productos getProductos(String pathXml, String pathXsd) throws JAXBException;
    /**
     * Serializa un objeto y lo escribe en un archivo en la ruta especificada.
     *
     * @param path Ruta donde donde se creará el nuevo archivo
     * @param content O
     *             bjeto con la información que se va a transformar y erscribir
     * @throws JAXBException Lanza una excepción si ocurre un error al procesar el XML
     * @throws SAXException Si se produce un error durante la validación o procesamiento de la estructura XML/XSD.
     * @throws IOException Lanza una excepción si el archivo no existe, no se puede leer o hay un error de acceso
     */
    void escribirProductos(String path, Object content) throws JAXBException, SAXException, IOException;
}
