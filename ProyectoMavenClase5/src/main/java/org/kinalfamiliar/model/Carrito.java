/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kinalfamiliar.model;

import java.time.LocalDateTime;

/**
 *
 * @author olive
 */
public class Carrito {
    int idCarrito;
    String estado;
    LocalDateTime fechaCreacion;
    int idUsuario;

    public Carrito() {
    }

    public Carrito(int idCarrito, String estado, LocalDateTime fechaCreacion, int idUsuario) {
        this.idCarrito = idCarrito;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.idUsuario = idUsuario;
    }

    public int getIdCarrito() {
        return idCarrito;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    
}
