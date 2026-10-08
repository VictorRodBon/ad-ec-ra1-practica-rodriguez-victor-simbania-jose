package org.educa.dao;

import generated.Productos;
import jakarta.xml.bind.JAXBException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.educa.entity.ProductoParaExcelEntity;
import org.xml.sax.SAXException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ProductosDAOImplXLSX implements ProductosDAO{

    @Override
    public Productos getProductos(String pathXml, String pathXsd) throws JAXBException {
        return null;
    }

    @Override
    public void escribirProductos(String path, Object content) throws JAXBException, SAXException, IOException {
        List<ProductoParaExcelEntity> productos = (List<ProductoParaExcelEntity>) content;

        try (Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Productos");

            //Creación de la fuente para la cabecera y la primera columna
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);

            //Creación de la fuente normal
            Font normalFont = workbook.createFont();
            normalFont.setBold(false);

            //Color para filas impares
            short colorImpar = IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex();

            //Estilo cabecera
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(boldFont);
            headerStyle.setFillForegroundColor(colorImpar);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            //Estilo primera columna filas pares
            CellStyle firstColEvenStyle = workbook.createCellStyle();
            firstColEvenStyle.setFont(boldFont);

            //Estilo primera columna filas impares
            CellStyle firstColOddStyle = workbook.createCellStyle();
            firstColOddStyle.setFont(boldFont);
            firstColOddStyle.setFillForegroundColor(colorImpar);
            firstColOddStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            //Estilo resto de datos filas pares
            CellStyle dataEvenStyle = workbook.createCellStyle();
            dataEvenStyle.setFont(normalFont);

            //Estilo resto de datos filas impares
            CellStyle dataOddStyle = workbook.createCellStyle();
            dataOddStyle.setFont(normalFont);
            dataOddStyle.setFillForegroundColor(colorImpar);
            dataOddStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            //Array con los encabezados de la tabla
            String[] header = {"Código", "Número de serie", "Precio", "Descuento", "Precio final", "Costes de envío", "Costes almacenaje", "Beneficio"};
            Row headerRow = sheet.createRow(0);

            //Rellenar las celdas del encabezado de la tabla
            for (int i = 0; i < header.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(header[i]);
                cell.setCellStyle(headerStyle);
            }
             int rowIndex = 1;
            //Introducir datos en celda
            for (ProductoParaExcelEntity producto : productos) {
                Row row = sheet.createRow(rowIndex);
                boolean esImpar = (rowIndex % 2 != 0);

                //Datos de las celdas de la primera columna
                Cell cellCodigo = row.createCell(0);
                cellCodigo.setCellValue(producto.getCodigo());
                cellCodigo.setCellStyle(esImpar ? firstColOddStyle : firstColEvenStyle);

                //Datos de las celdad de la segunda columna
                Cell cellNumSerie = row.createCell(1);
                cellNumSerie.setCellValue(producto.getNumSerie());
                cellNumSerie.setCellStyle(esImpar ? dataOddStyle : dataEvenStyle);

                //Datos de las celdad de la tercera columna
                Cell cellPrecio = row.createCell(2);
                cellPrecio.setCellValue(producto.getPrecio().doubleValue());
                cellPrecio.setCellStyle(esImpar ? dataOddStyle : dataEvenStyle);

                //Datos de las celdad de la cuarta columna
                Cell cellDescuento = row.createCell(3);
                cellDescuento.setCellValue(producto.getDescuento().doubleValue());
                cellDescuento.setCellStyle(esImpar ? dataOddStyle : dataEvenStyle);

                //Datos de las celdad de la quinta columna
                Cell cellPrecioFinal = row.createCell(4);
                cellPrecioFinal.setCellValue(producto.getPrecioFinal().doubleValue());
                cellPrecioFinal.setCellStyle(esImpar ? dataOddStyle : dataEvenStyle);

                //Datos de las celdad de la sexta columna
                Cell cellCostesEnvio = row.createCell(5);
                cellCostesEnvio.setCellValue(producto.getCostesEnvio().doubleValue());
                cellCostesEnvio.setCellStyle(esImpar ? dataOddStyle : dataEvenStyle);

                //Datos de las celdad de la septima columna
                Cell cellCostesAlmacenaje = row.createCell(6);
                cellCostesAlmacenaje.setCellValue(producto.getCostesAlmacenaje().doubleValue());
                cellCostesAlmacenaje.setCellStyle(esImpar ? dataOddStyle : dataEvenStyle);

                //Datos de las celdad de la octava columna
                Cell cellBeneficio = row.createCell(7);
                cellBeneficio.setCellValue(producto.getBeneficio().doubleValue());
                cellBeneficio.setCellStyle(esImpar ? dataOddStyle : dataEvenStyle);
                rowIndex++;
            }

            try(FileOutputStream fos = new FileOutputStream(path)) {
                workbook.write(fos);
            }
        }
    }
}
