package com.universidad.compras.ejecucion;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.universidad.compras.modelo.Solicitud;

class EjecucionSolicitudTest {

    @Test
    void ejecutarReservaPresupuestoYGeneraOrden() {
        Solicitud s = new Solicitud("S-010", "ana@udes.edu.co", 3000000, "SOFTWARE", "CC-100");
        s.setEstado("APROBADA");
        
        PresupuestoService presupuestoService = new PresupuestoService();
        OrdenCompraService ordenService = new OrdenCompraService();
        GestorEjecucionSolicitud ejecutor = new GestorEjecucionSolicitud(s);

        ejecutor.ejecutarComando(new ComandoReservarPresupuesto(presupuestoService, s));
        ejecutor.ejecutarComando(new ComandoGenerarOrdenCompra(ordenService, s, "Proveedor Software S.A."));

        assertEquals("EJECUTADA", s.getEstado());
    }

    @Test
    void deshacerSoloLaUltimaOperacionNoAfectaLaAnterior() {
        Solicitud s = new Solicitud("S-011", "luis@udes.edu.co", 4000000, "MATERIAL_OFICINA", "CC-200");
        s.setEstado("APROBADA");
        
        PresupuestoService presupuestoService = new PresupuestoService();
        OrdenCompraService ordenService = new OrdenCompraService();
        GestorEjecucionSolicitud ejecutor = new GestorEjecucionSolicitud(s);

        assertDoesNotThrow(() -> {
            ejecutor.ejecutarComando(new ComandoReservarPresupuesto(presupuestoService, s));
            ejecutor.ejecutarComando(new ComandoGenerarOrdenCompra(ordenService, s, "Papelería Central"));
            
            // Deshacer la última (orden de compra)
            ejecutor.deshacerUltimoComando();
            
            // El historial debe tener solo 1 elemento (la reserva de presupuesto)
            assertEquals(1, ejecutor.getHistorial().size());
        });
    }

    @Test
    void elHistorialConservaTodasLasOperacionesNoSoloLaUltima() {
        Solicitud s = new Solicitud("S-012", "carla@udes.edu.co", 2500000, "EQUIPO", "CC-300");
        s.setEstado("APROBADA");
        
        PresupuestoService presupuestoService = new PresupuestoService();
        OrdenCompraService ordenService = new OrdenCompraService();
        GestorEjecucionSolicitud ejecutor = new GestorEjecucionSolicitud(s);

        assertDoesNotThrow(() -> {
            ejecutor.ejecutarComando(new ComandoReservarPresupuesto(presupuestoService, s));
            ejecutor.ejecutarComando(new ComandoGenerarOrdenCompra(ordenService, s, "Tech Supply"));

            assertEquals(2, ejecutor.getHistorial().size());
        });
    }
}