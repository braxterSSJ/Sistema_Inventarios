package com.inventarios.modelo;

public class Producto {

    private int idProducto;
    private int idCategoria;
    private int idProveedor;
    private int stock;
    private int stockMinimo;
    private int stockMaximo;

    private String codigo;
    private String nombre;
    private String descripcion;
    private String nombreCategoria;
    private String nombreProveedor;
    private String unidadMedida;

    private double precioCompra;
    private double precioVenta;

    private boolean estado;

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int v) {
        idProducto = v;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String v) {
        codigo = v;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String v) {
        nombre = v;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String v) {
        descripcion = v;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int v) {
        idCategoria = v;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String v) {
        nombreCategoria = v;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int v) {
        idProveedor = v;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String v) {
        nombreProveedor = v;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String v) {
        unidadMedida = v;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double v) {
        precioCompra = v;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double v) {
        precioVenta = v;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int v) {
        stock = v;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int v) {
        stockMinimo = v;
    }

    public int getStockMaximo() {
        return stockMaximo;
    }

    public void setStockMaximo(int v) {
        stockMaximo = v;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean v) {
        estado = v;
    }

    // Mostrar correctamente el producto en los JComboBox
    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}