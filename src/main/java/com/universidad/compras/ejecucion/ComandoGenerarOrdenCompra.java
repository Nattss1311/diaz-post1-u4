package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;

public class ComandoGenerarOrdenCompra implements ComandoEjecucion {
    private final OrdenCompraService servicio;
    private final Solicitud solicitud;
    private final String proveedor;
    private String numeroOrden;

    public ComandoGenerarOrdenCompra(OrdenCompraService servicio, Solicitud solicitud, String proveedor) {
        this.servicio = servicio;
        this.solicitud = solicitud;
        this.proveedor = proveedor;
    }

    @Override
    public void ejecutar() {
        this.numeroOrden = servicio.generar(solicitud.getId(), proveedor);
    }

    @Override
    public void deshacer() {
        if (numeroOrden != null) {
            servicio.cancelar(numeroOrden);
            numeroOrden = null;
        }
    }

    @Override
    public String getNombre() {
        return "Generar Orden de Compra (" + (numeroOrden != null ? numeroOrden : "Pendiente") + ")";
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }
}