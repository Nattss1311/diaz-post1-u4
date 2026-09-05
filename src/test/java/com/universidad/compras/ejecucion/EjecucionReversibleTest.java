package com.universidad.compras.ejecucion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.universidad.compras.modelo.Solicitud;

class EjecucionReversibleTest {

    @Test
    void ejecutarComandosCambiaEstadoAEjecutadaYGuardaHistorial() {
        Solicitud s = new Solicitud("S-100", "pedro@udes.edu.co", 500000, "MATERIAL_OFICINA", "CC-01");
        s.setEstado("APROBADA");

        PresupuestoService presupuestoService = new PresupuestoService();
        OrdenCompraService ordenService = new OrdenCompraService();

        GestorEjecucionSolicitud gestor = new GestorEjecucionSolicitud(s);

        ComandoEjecucion cmdPresupuesto = new ComandoReservarPresupuesto(presupuestoService, s);
        ComandoEjecucion cmdOrden = new ComandoGenerarOrdenCompra(ordenService, s, "Proveedor ABC");

        gestor.ejecutarComando(cmdPresupuesto);
        gestor.ejecutarComando(cmdOrden);

        assertEquals("EJECUTADA", s.getEstado());
        assertEquals(2, gestor.getHistorial().size());
    }

    @Test
    void deshacerComandoIndividualmenteFunciona() {
        Solicitud s = new Solicitud("S-101", "maria@udes.edu.co", 1200000, "SOFTWARE", "CC-02");
        s.setEstado("APROBADA");

        PresupuestoService presupuestoService = new PresupuestoService();
        OrdenCompraService ordenService = new OrdenCompraService();

        GestorEjecucionSolicitud gestor = new GestorEjecucionSolicitud(s);

        ComandoEjecucion cmdPresupuesto = new ComandoReservarPresupuesto(presupuestoService, s);
        ComandoEjecucion cmdOrden = new ComandoGenerarOrdenCompra(ordenService, s, "TechCorp");

        gestor.ejecutarComando(cmdPresupuesto);
        gestor.ejecutarComando(cmdOrden);

        // Deshacer solo la orden de compra (índice 1)
        gestor.deshacerComando(1);

        assertEquals(1, gestor.getHistorial().size());
        assertEquals("Reservar Presupuesto (CC-02 - $1200000.0)", gestor.getHistorial().get(0).getNombre());
    }
}