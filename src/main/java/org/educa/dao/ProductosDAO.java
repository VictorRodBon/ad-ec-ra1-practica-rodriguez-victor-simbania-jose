package org.educa.dao;

import generated.Producto;
import jakarta.xml.bind.JAXBException;
import org.educa.entity.ProductoEntity;
import org.xml.sax.SAXException;

import java.util.List;

public interface ProductosDAO {
    List<Producto> getProductos(String pathXml, String pathXsd) throws JAXBException;
    void escribirProductos(List<ProductoEntity> productos) throws JAXBException, SAXException;
}
