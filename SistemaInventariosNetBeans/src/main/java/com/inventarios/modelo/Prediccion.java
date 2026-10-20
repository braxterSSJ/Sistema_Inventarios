package com.inventarios.modelo;

import java.util.Date;

public class Prediccion {

    private int idPrediccion;
    private int idProducto;
    private String codigoProducto;
    private String nombreProducto;
    private String periodo;
    private int ventasHistoricas;
    private double promedioDiario;
    private double demandaEstimada;
    private int stockActual;
    private Double diasCobertura;
    private String recomendacion;
    private Date fechaPrediccion;

    public Prediccion() {
    }

    public int getIdPrediccion() {
        return idPrediccion;
    }

    public void setIdPrediccion(int idPrediccion) {
        this.idPrediccion = idPrediccion;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public int getVentasHistoricas() {
        return ventasHistoricas;
    }

    public void setVentasHistoricas(int ventasHistoricas) {
        this.ventasHistoricas = ventasHistoricas;
    }

    public double getPromedioDiario() {
        return promedioDiario;
    }

    public void setPromedioDiario(double promedioDiario) {
        this.promedioDiario = promedioDiario;
    }

    public double getDemandaEstimada() {
        return demandaEstimada;
    }

    public void setDemandaEstimada(double demandaEstimada) {
        this.demandaEstimada = demandaEstimada;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public Double getDiasCobertura() {
        return diasCobertura;
    }

    public void setDiasCobertura(Double diasCobertura) {
        this.diasCobertura = diasCobertura;
    }

    public String getRecomendacion() {
        return recomendacion;
    }

    public void setRecomendacion(String recomendacion) {
        this.recomendacion = recomendacion;
    }

    public Date getFechaPrediccion() {
        return fechaPrediccion;
    }

    public void setFechaPrediccion(Date fechaPrediccion) {
        this.fechaPrediccion = fechaPrediccion;
    }
}