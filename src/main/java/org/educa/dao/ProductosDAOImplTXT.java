package org.educa.dao;

import generated.Producto;
import generated.Productos;
import jakarta.xml.bind.JAXBException;
import org.educa.entity.ProductoEntity;
import org.educa.entity.SummaryEntity;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;



public class ProductosDAOImplTXT implements ProductosDAO{
    private static final String PATH = "src/main/resources/export/result_";

    @Override
    public Productos getProductos(String pathXml, String pathXsd) {
        return null;
    }

    @Override
    public void escribirProductos(String date, Object content) throws IOException{
        File f = new File(PATH+date+".txt");


        System.out.println("Fichero creado");

        if (f.getParentFile() != null && !f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f, StandardCharsets.UTF_8))) {
            SummaryEntity c = (SummaryEntity) content;
            bw.write(c.toPrint());
        }
    }
}
