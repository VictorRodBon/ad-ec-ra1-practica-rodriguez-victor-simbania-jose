package org.educa.dao;

import generated.Producto;
import jakarta.xml.bind.JAXBException;
import org.educa.entity.ProductoEntity;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.List;



public class ProductosDAOImplTXT implements ProductosDAO{
    private static final String PATH = "src/main/resources/export/result_";

    @Override
    public List<Producto> getProductos(String pathXml, String pathXsd) {
        return null;
    }

    /**
     *
     * @param productos Lista de productos obtenida del XML
     * @param content Contenido a introducir en el fichero txt
     */
    @Override
    public void escribirProductos(String date, String content){
        File f = new File(PATH+date+".txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))){
            bw.write(content.toString());
        }catch (IOException e){
            System.out.println("no se ha podido guardar la información: "+e.getMessage());
        }
    }
}
