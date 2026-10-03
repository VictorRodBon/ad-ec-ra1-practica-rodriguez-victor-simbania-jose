package org.educa.dao;

import generated.Producto;
import generated.Productos;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.educa.entity.ProductoEntity;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.List;

public class ProductosDAOImplXML implements ProductosDAO{
    @Override
    public List<Producto> getProductos(String pathXml, String pathXsd) throws JAXBException, SAXException {
        File xml = new File(pathXml);
        File xsd = new File(pathXsd);

        JAXBContext jaxbContext = JAXBContext.newInstance(Productos.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        // Asignar el esquema XSD
        unmarshaller.setSchema(SchemaFactory.newInstance(
                XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(xsd));

        // Si hay un error contra el XSD, saltará una JAXBException directamente
        Productos productos = (Productos) unmarshaller.unmarshal(xml);
        return productos.getProducto();
    }

    @Override
    public void escribirProductos(List<ProductoEntity> productos) throws JAXBException, SAXException {

    }
}
