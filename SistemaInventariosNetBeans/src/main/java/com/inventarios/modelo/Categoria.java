package com.inventarios.modelo;

public class Categoria {
    private int idCategoria;
    private String nombre;
    private String descripcion;
    private boolean estado;

    public Categoria() {}
    public Categoria(int idCategoria, String nombre, String descripcion, boolean estado) {
        this.idCategoria=idCategoria; this.nombre=nombre; this.descripcion=descripcion; this.estado=estado;
    }
    public int getIdCategoria(){return idCategoria;}
    public void setIdCategoria(int v){idCategoria=v;}
    public String getNombre(){return nombre;}
    public void setNombre(String v){nombre=v;}
    public String getDescripcion(){return descripcion;}
    public void setDescripcion(String v){descripcion=v;}
    public boolean isEstado(){return estado;}
    public void setEstado(boolean v){estado=v;}
    @Override public String toString(){return nombre;}
}
