package com.inventarios.modelo;

public class Proveedor {
    private int idProveedor;
    private String ruc, razonSocial, contacto, telefono, correo, direccion;
    private boolean estado;

    public Proveedor() {}
    public Proveedor(int idProveedor,String ruc,String razonSocial,String contacto,String telefono,String correo,String direccion,boolean estado){
        this.idProveedor=idProveedor;this.ruc=ruc;this.razonSocial=razonSocial;this.contacto=contacto;this.telefono=telefono;this.correo=correo;this.direccion=direccion;this.estado=estado;
    }
    public int getIdProveedor(){return idProveedor;}
    public void setIdProveedor(int v){idProveedor=v;}
    public String getRuc(){return ruc;}
    public void setRuc(String v){ruc=v;}
    public String getRazonSocial(){return razonSocial;}
    public void setRazonSocial(String v){razonSocial=v;}
    public String getContacto(){return contacto;}
    public void setContacto(String v){contacto=v;}
    public String getTelefono(){return telefono;}
    public void setTelefono(String v){telefono=v;}
    public String getCorreo(){return correo;}
    public void setCorreo(String v){correo=v;}
    public String getDireccion(){return direccion;}
    public void setDireccion(String v){direccion=v;}
    public boolean isEstado(){return estado;}
    public void setEstado(boolean v){estado=v;}
    @Override public String toString(){return razonSocial;}
}
