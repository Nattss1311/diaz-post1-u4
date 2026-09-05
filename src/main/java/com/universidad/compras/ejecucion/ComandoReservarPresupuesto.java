package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;

public class ComandoReservarPresupuesto implements ComandoEjecucion {
    private final PresupuestoService servicio;
    private final Solicitud solicitud;
    private boolean reservado = false;

    public ComandoReservarPresupuesto(PresupuestoService servicio, Solicitud solicitud) {
        this.servicio = servicio;
        this.solicitud = solicitud;
    }

    @Override
    public void ejecutar() {
        this.reservado = servicio.reservar(solicitud.getCentroCosto(), solicitud.getMonto());
    }

    @Override
    public void deshacer() {
        if (reservado) {
            servicio.liberar(solicitud.getCentroCosto(), solicitud.getMonto());
            reservado = false;
        }
    }

    @Override
    public String getNombre() {
        return "Reservar Presupuesto (" + solicitud.getCentroCosto() + " - $" + solicitud.getMonto() + ")";
    }
}