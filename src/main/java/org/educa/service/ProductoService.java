package org.educa.service;

import jakarta.xml.bind.JAXBException;
import org.educa.dao.ProductosDAO;
import org.educa.dao.ProductosDAOImplTXT;
import org.educa.dao.ProductosDAOImplXLSX;
import org.educa.dao.ProductosDAOImplXML;
import org.educa.entity.ProductoEntity;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import generated.Producto;
import org.educa.entity.SummaryEntity;
import org.xml.sax.SAXException;

public class ProductoService {
    private static final String fileXSD = "src/main/resources/xsd/inventario_junio2026.xsd";
    private final ProductosDAO productosDAOxml = new ProductosDAOImplXML();
    private final ProductosDAO productosDAOtxt = new ProductosDAOImplTXT();

    /**
     * Obtener datos de un fichero XML
     * @param fileXml Ruta donde se encuentra el archivo XML
     * @return Devuelve una lista con los productos
     * @throws JAXBException Lanza la excepción si ocurre un error al procesar el XML
     */
    public List<ProductoEntity> readFile(String fileXml) throws JAXBException {
        List<Producto> productos = productosDAOxml.getProductos(fileXml, fileXSD);

        List<ProductoEntity> listaProductos = setProductEntity(productos);

        return listaProductos;
    }

    /**
     * Exportar información de un XML a un TXT en formato factura
     * @param path Ruta en la que se va crear el .txt
     * @param fileXml Ruta donde se encuentra el archivo XML
     * @throws JAXBException Lanza una excepción si ocurre un error al procesar el XML
     * @throws IOException Lanza una excepción si el archivo no existe, no se puede leer o hay un error de acceso
     */
    public void exportSummary(String path, String fileXml) throws JAXBException, IOException {
        String date = extractDate(fileXml);

        List<ProductoEntity> productos = readFile(fileXml);

        int numeroProductos = productos.size();
        BigDecimal beneficioTotal = BigDecimal.ZERO;
        for (ProductoEntity producto : productos) {
            // BigDecimal es inmutable: hay que reasignar el resultado de add()
            beneficioTotal = beneficioTotal.add(producto.getProfit());
        }

        File f = new File(fileXml);
        String name = f.getName().split("\\.")[0];
        long size = f.length();
//        contenidoFichero.append("Fecha: ").append(date).append("\n").
//                append("Numero de productos: ").append(numeroProductos).append("\n").
//                append("Beneficio total: ").append().append("\n").
//                append("Ruta del fichero: ").append(fileXml).append("\n").
//                append("Nombre del fichero: ").append(name).append("\n").
//                append("Tamaño del fichero: ").append(size).append("bytes");
        SummaryEntity summary = new SummaryEntity(date, numeroProductos, beneficioTotal, fileXml, name, size);

        try {
            // Se envía la entidad (no su representación en texto) porque el DAO espera un SummaryEntity
            productosDAOtxt.escribirProductos(date, summary);
        } catch (SAXException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Exportar infrmación de un XML a un XLSX
     * @param path Ruta en la que se va crear el .xlsx
     * @param fileXml Ruta donde se encuentra el archivo XML
     * @throws JAXBException Lanza una excepción si ocurre un error al procesar el XML
     * @throws IOException Lanza una excepción si el archivo no existe, no se puede leer o hay un error de acceso
     * @throws ParseException Lanza una excepción si hay un error en algún formato al extraer el XML
     */
    public void exportExcel(String path, String fileXml) throws JAXBException, IOException, ParseException {
        String date = extractDate(fileXml);
        List<Producto> productos = productosDAOxml.getProductos(fileXml, FILE_XSD).getProducto();

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

        // El DAO necesita la ruta completa del fichero, no la carpeta de destino
        String outputFile = new File(path, "result_" + date + ".xlsx").getPath();

        try {
            productosDAOxlsx.escribirProductos(outputFile, data);

        }catch (SAXException e){
            // No se traga el error: se reenvía como IOException (declarado en la firma)
            throw new IOException("No se pudo generar el fichero Excel: " + outputFile, e);
        }
    }

    /**
     * Obtiene la fecha contenida en el nombre del fichero XML
     * (p. ej. inventario_junio2026.xml -> junio2026)
     */
    private String extractDate(String fileXml) {
        String name = new File(fileXml).getName();
        int punto = name.lastIndexOf('.');
        if (punto > 0) {
            name = name.substring(0, punto);
        }
        int guion = name.lastIndexOf('_');
        return guion >= 0 ? name.substring(guion + 1) : name;
    }

    private List<ProductoEntity> setProductEntity(List<Producto> productos){
        List<ProductoEntity> listaProductos = new java.util.ArrayList<>();


        for (Producto p : productos) {
            ProductoEntity prod=new ProductoEntity();

            // Precio final = precio - (precio * descuento / 100)
            BigDecimal descuento = p.getDescuento().divide(new BigDecimal(100));
            BigDecimal precioFinal = p.getPrecio()
                    .subtract(p.getPrecio().multiply(descuento))
                    .setScale(2, RoundingMode.HALF_UP);

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