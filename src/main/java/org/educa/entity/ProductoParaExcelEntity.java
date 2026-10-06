package org.educa.entity;

import java.math.BigDecimal;

public class ProductoParaExcelEntity {
    private String codigo;
    private String numSerie;
    private BigDecimal precio;
    private BigDecimal descuento;
    private BigDecimal precioFinal;
    private BigDecimal costesEnvio;
    private BigDecimal costesAlmacenaje;
    private BigDecimal beneficio;

    public ProductoParaExcelEntity(String codigo, String numSerie, BigDecimal precio, BigDecimal descuento, BigDecimal costesEnvio, BigDecimal costesAlmacenaje) {
        this.codigo = codigo;
        this.numSerie = numSerie;
        this.precio = precio; // Este actúa como tu precioOriginal
        this.descuento = descuento; // Este es tu porcentaje de descuento (ej. 20)
        this.precioFinal = precio.multiply(java.math.BigDecimal.ONE.subtract(descuento.divide(new java.math.BigDecimal("100"), 4, java.math.RoundingMode.HALF_UP))).setScale(2, java.math.RoundingMode.HALF_UP);
        this.costesEnvio = costesEnvio;
        this.costesAlmacenaje = costesAlmacenaje;
        this.beneficio = precioFinal.subtract(costesEnvio.add(costesAlmacenaje));

    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(BigDecimal precioFinal) {
        this.precioFinal = precioFinal;
    }

    public BigDecimal getCostesEnvio() {
        return costesEnvio;
    }

    public void setCostesEnvio(BigDecimal costesEnvio) {
        this.costesEnvio = costesEnvio;
    }

    public BigDecimal getCostesAlmacenaje() {
        return costesAlmacenaje;
    }

    public void setCostesAlmacenaje(BigDecimal costesAlmacenaje) {
        this.costesAlmacenaje = costesAlmacenaje;
    }

    public BigDecimal getBeneficio() {
        return beneficio;
    }

    public void setBeneficio(BigDecimal beneficio) {
        this.beneficio = beneficio;
    }
}

