package com.universidad.compras.notificacion;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.universidad.compras.modelo.Solicitud;

class NotificacionEstadoTest {

    @Test
    void cambiarEstadoDisparaLasTresReaccionesSinLanzarExcepcion() {
        Solicitud s = new Solicitud("S-020", "ana@udes.edu.co", 2500000, "SOFTWARE", "CC-100");
        NotificadorEstadoSolicitud mecanismo = new NotificadorEstadoSolicitud();

        // Registrar las tres reacciones principales
        mecanismo.suscribir(new ObservadoresSistema.NotificadorEmail());
        mecanismo.suscribir(new ObservadoresSistema.ActualizadorContabilidad());
        mecanismo.suscribir(new ObservadoresSistema.RegistradorAuditoria());

        assertDoesNotThrow(() -> {
            mecanismo.notificarCambio(s, "PENDIENTE", "APROBADA", "Aprobada por nivel competente");
        });
    }

    @Test
    void agregarUnCuartoSuscriptorDePruebaNoRequiereModificarElMecanismo() {
        NotificadorEstadoSolicitud mecanismo = new NotificadorEstadoSolicitud();

        // Registrar las tres reacciones principales
        mecanismo.suscribir(new ObservadoresSistema.NotificadorEmail());
        mecanismo.suscribir(new ObservadoresSistema.ActualizadorContabilidad());
        mecanismo.suscribir(new ObservadoresSistema.RegistradorAuditoria());

        // Registrar un cuarto suscriptor adicional (colector de prueba)
        AtomicBoolean cuartaReaccionEjecutada = new AtomicBoolean(false);
        mecanismo.suscribir((solicitud, estadoAnterior, nuevoEstado, detalle) -> {
            cuartaReaccionEjecutada.set(true);
        });

        Solicitud s = new Solicitud("S-021", "luis@udes.edu.co", 1200000, "MATERIAL_OFICINA", "CC-200");

        assertDoesNotThrow(() -> {
            mecanismo.notificarCambio(s, "PENDIENTE", "RECHAZADA", "Rechazada por presupuesto insuficiente");
        });

        assertTrue(cuartaReaccionEjecutada.get(), "El cuarto suscriptor debe ejecutarse tras la notificación.");
    }
}