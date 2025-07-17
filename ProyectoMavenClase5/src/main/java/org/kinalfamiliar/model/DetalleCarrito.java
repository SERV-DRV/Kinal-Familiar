/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kinalfamiliar.model;

/**
 * Modelo para la tabla DetalleCarritos.
 * @author olive
 */

public class DetalleCarrito {
    private int idDetalleCarrito;
    private int idCarrito;
    private int idProducto;
    private int cantidad;

    public DetalleCarrito() {
    }

    public DetalleCarrito(int idDetalleCarrito, int idCarrito, int idProducto, int cantidad) {
        this.idDetalleCarrito = idDetalleCarrito;
        this.idCarrito = idCarrito;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    // --- Getters y Setters ---

    public int getIdDetalleCarrito() {
        return idDetalleCarrito;
    }

    public void setIdDetalleCarrito(int idDetalleCarrito) {
        this.idDetalleCarrito = idDetalleCarrito;
    }

    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    @Override
    public String toString() {
        return "Detalle #" + idDetalleCarrito;
    }
}