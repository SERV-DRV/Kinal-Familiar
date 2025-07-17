package org.kinalfamiliar.controller;

public class UsuarioAutenticado {

    private static UsuarioAutenticado instancia;

    private String nombreUsuario;
    private String apellidoUsuario;
    private String correoUsuario;

    private UsuarioAutenticado() {
    }

    public static UsuarioAutenticado getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioAutenticado();
        }
        return instancia;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public void cerrarSesion() {
        instancia = null;
    }
}
