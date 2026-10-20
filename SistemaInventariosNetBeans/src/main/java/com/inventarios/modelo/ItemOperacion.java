package com.inventarios.modelo;
public class ItemOperacion {
 private int idProducto,cantidad; private String codigo,nombre; private double precio;
 public ItemOperacion(){}
 public ItemOperacion(Producto p,int cantidad,double precio){idProducto=p.getIdProducto();codigo=p.getCodigo();nombre=p.getNombre();this.cantidad=cantidad;this.precio=precio;}
 public int getIdProducto(){return idProducto;} public String getCodigo(){return codigo;} public String getNombre(){return nombre;}
 public int getCantidad(){return cantidad;} public void setCantidad(int v){cantidad=v;}
 public double getPrecio(){return precio;} public void setPrecio(double v){precio=v;}
 public double getSubtotal(){return cantidad*precio;}
}
