package com.inventarios.modelo;

public class Usuario {

    private int idUsuario;
    private String usuario;
    private String password;
    private String nombreCompleto;
    private String correo;
    private int idRol;
    private String nombreRol;
    private boolean estado;

    public Usuario() {
    }

    public Usuario(int idUsuario, String usuario, String password,
                   String nombreCompleto, String correo, int idRol,
                   String nombreRol, boolean estado) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.idRol = idRol;
        this.nombreRol = nombreRol;
        this.estado = estado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
