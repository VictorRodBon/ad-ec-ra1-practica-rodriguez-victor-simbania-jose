package org.educa.dao;

import generated.Producto;
import generated.Productos;
import jakarta.xml.bind.JAXBException;
import org.educa.entity.ProductoEntity;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

public interface ProductosDAO {
    Productos getProductos(String pathXml, String pathXsd) throws JAXBException;
    void escribirProductos(String path, Object content) throws JAXBException, SAXException, IOException;
}
