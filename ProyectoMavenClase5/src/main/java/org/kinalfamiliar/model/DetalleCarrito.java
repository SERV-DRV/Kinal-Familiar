/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kinalfamiliar.model;

/**
 *
 * @author olive
 */
public class DetalleCarrito {
    int idDetalleCarrito, idCarrito, idProducto, cantidad;

    public DetalleCarrito() {
    }
    
    public DetalleCarrito(int idDetalleCarrito, int idCarrito, int idProducto, int cantidad) {
        this.idDetalleCarrito = idDetalleCarrito;
        this.idCarrito = idCarrito;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public int getIdDetalleCarrito() {
        return idDetalleCarrito;
    }

    public int getIdCarrito() {
        return idCarrito;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setIdDetalleCarrito(int idDetalleCarrito) {
        this.idDetalleCarrito = idDetalleCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
        
}
