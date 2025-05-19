package com.jonathan.coordinapp.domain.model;

public class User {
    private String nombre;
    private String usuario;
    private String tipo;

    public String getNombre() {
        return nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
