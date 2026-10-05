package org.educa.dao;

import generated.Producto;
import jakarta.xml.bind.JAXBException;
import org.educa.entity.ProductoEntity;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;



public class ProductosDAOImplTXT implements ProductosDAO{
    private static final String PATH = "src/main/resources/export/result_";

    @Override
    public List<Producto> getProductos(String pathXml, String pathXsd) {
        return null;
    }

    /**
     *
     * @param date fecha obtenida del fichero original
     * @param content Contenido a introducir en el fichero txt
     */
    @Override
    public void escribirProductos(String date, String content) throws IOException{
        File f = new File(PATH+date+".txt");

        System.out.println("Fichero creado");

        if (f.getParentFile() != null && !f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }

        // Escribir usando UTF-8 explícito (disponible en Java 11+)
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f, StandardCharsets.UTF_8))) {
            bw.write(content);
        }
    }
}
