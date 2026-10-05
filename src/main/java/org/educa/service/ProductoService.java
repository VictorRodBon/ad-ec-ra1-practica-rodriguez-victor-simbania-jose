package org.educa.service;

import jakarta.xml.bind.JAXBException;
import org.educa.dao.ProductosDAO;
import org.educa.dao.ProductosDAOImplXML;
import org.educa.entity.ProductoEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import generated.Producto;

public class ProductoService {
    private static final String fileXSD = "src/main/resources/xsd/inventario_junio2026.xsd";
    private final ProductosDAO productosDAO = new ProductosDAOImplXML();

    public List<ProductoEntity> readFile(String fileXml) throws JAXBException {
        List<Producto> productos = productosDAO.getProductos(fileXml, fileXSD);

        List<ProductoEntity> listaProductos = setProductEntity(productos);

        return listaProductos;
    }

    public void exportSummary(String path, String fileXml) throws JAXBException, IOException {
        String date = fileXml.split(".")[0].split("_")[1];
        StringBuilder contenidoFichero = new StringBuilder();

        try {
            List<ProductoEntity> productos = readFile(fileXml);
            for (ProductoEntity prodcuto : productos) {
                contenidoFichero.append(producto.toPrint).append("\n");
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        productosDAO.escribirProductos(contenidoFichero.toString(), path);

    }

    public void exportExcel(String path, String fileXml) throws JAXBException, IOException, ParseException {
        //TODO: Implementar
    }

    private List<ProductoEntity> setProductEntity(List<Producto> productos){
        List<ProductoEntity> listaProductos = new java.util.ArrayList<>();


        for (Producto p : productos) {
            ProductoEntity prod=new ProductoEntity();

            BigDecimal precioFinal = p.getPrecio().divide(new BigDecimal(100)).multiply(p.getDescuento());

            BigDecimal cost = p.getCostes().getCostesAlmacenaje().add(p.getCostes().getCostesEnvio());


            prod.setProducto(p);
            prod.setPrecioFinal(precioFinal);
            prod.setCost(cost);
            prod.setProfit(precioFinal.subtract(cost));

            listaProductos.add(prod);
        }
        return listaProductos;
    }
}