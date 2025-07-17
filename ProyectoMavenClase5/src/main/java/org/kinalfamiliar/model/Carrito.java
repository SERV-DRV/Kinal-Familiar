/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kinalfamiliar.model;

import java.time.LocalDateTime;

/**
 * Modelo para la tabla Carritos.
 * @author olive
 */

public class Carrito {
    private int idCarrito;
    private String estado;
    private LocalDateTime fechaCreacion;
    private int idUsuario;

    public Carrito() {
    }

    public Carrito(int idCarrito, String estado, LocalDateTime fechaCreacion, int idUsuario) {
        this.idCarrito = idCarrito;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.idUsuario = idUsuario;
    }

    public Carrito(int idCarrito, String estado, int idUsuario) {
        this.idCarrito = idCarrito;
        this.estado = estado;
        this.idUsuario = idUsuario;
    }
        
    // --- Getters y Setters ---

    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Carrito #" + idCarrito;
    }
}