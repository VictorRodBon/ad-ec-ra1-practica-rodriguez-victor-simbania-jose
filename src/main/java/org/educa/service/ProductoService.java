package org.educa.service;

import generated.Productos;
import jakarta.xml.bind.JAXBException;
import org.educa.dao.ProductosDAO;
import org.educa.dao.ProductosDAOImplTXT;
import org.educa.dao.ProductosDAOImplXML;
import org.educa.entity.ProductoEntity;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import generated.Producto;
import org.educa.entity.ProductoParaExcelEntity;
import org.educa.entity.SummaryEntity;
import org.xml.sax.SAXException;

public class ProductoService {
    private static final String FILE_XSD = "src/main/resources/xsd/inventario_junio2026.xsd";
    private final ProductosDAO productosDAOxml = new ProductosDAOImplXML();
    private final ProductosDAO productosDAOtxt = new ProductosDAOImplTXT();
    private final ProductosDAO productosDAOxlsx = new ProductosDAOImplXLSX();

    public List<ProductoEntity> readFile(String fileXml) throws JAXBException {
        Productos productos = productosDAOxml.getProductos(fileXml, FILE_XSD);

        List<ProductoEntity> listaProductos = setProductEntity(productos.getProducto());

        return listaProductos;
    }

    public void exportSummary(String path, String fileXml) throws JAXBException, IOException {
        String date = fileXml.split("\\.")[0].split("_")[1];
        //StringBuilder contenidoFichero = new StringBuilder();

        int numeroProductos;
        BigDecimal beneficioTotal = new BigDecimal(0);
        try {
            List<ProductoEntity> productos = readFile(fileXml);
            numeroProductos = productos.size();
            for(ProductoEntity producto : productos) {
                beneficioTotal.add(producto.getProfit());
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        File f = new File(fileXml);
        String name = f.getName().split("\\.")[0];
        long size = f.length();

        SummaryEntity summary = new SummaryEntity(date, numeroProductos, beneficioTotal, fileXml, name, size);

        try {
            productosDAOtxt.escribirProductos(date,summary.toPrint());
        } catch (SAXException e) {
            System.out.println(e.getMessage());
        }

    }

    public void exportExcel(String path, String fileXml) throws JAXBException, IOException, ParseException {
        List<Producto> productos = productosDAOxml.getProductos(fileXml, FILE_XSD);

        List <ProductoParaExcelEntity> data = new ArrayList<>();
        for (Producto p : productos){
            data.add(new ProductoParaExcelEntity(
                    p.getCodigo(),
                    p.getNumeroSerie(),
                    p.getPrecio(),
                    p.getDescuento(),
                    p.getCostes().getCostesEnvio(),
                    p.getCostes().getCostesAlmacenaje()
                    )
            );
        }
        try {
            productosDAOxlsx.escribirProductos(path, data);

        }catch (IOException e){
            System.err.println(e.getMessage());
        }
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