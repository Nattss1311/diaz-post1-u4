package com.universidad.compras.estado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.universidad.compras.modelo.Solicitud;

class TransicionEstadoTest {

    @Test
    void ejecutarUnaSolicitudAprobadaLaDejaEjecutada() {
        Solicitud s = new Solicitud("S-030", "luis@udes.edu.co", 3000000, "MATERIAL_OFICINA", "CC-200");
        
        ContextoSolicitud contexto = new ContextoSolicitud(s);
        // Llevar la solicitud al estado APROBADA
        contexto.aprobar();

        // Invocar la operación de ejecutar sobre "contexto"
        contexto.ejecutar();

        assertEquals("EJECUTADA", s.getEstado());
    }

    @Test
    void ejecutarUnaSolicitudPendienteSeRechazaSinCambiarElEstado() {
        Solicitud s = new Solicitud("S-031", "ana@udes.edu.co", 1000000, "SOFTWARE", "CC-100");
        ContextoSolicitud contexto = new ContextoSolicitud(s);

        // Invocar la operación de ejecutar sobre "contexto" estando en PENDIENTE
        String resultado = contexto.ejecutar();

        assertTrue(resultado.contains("Error"));
        assertEquals("PENDIENTE", s.getEstado());
    }

    @Test
    void unaSolicitudEjecutadaNoPuedeVolverAEjecutarse() {
        Solicitud s = new Solicitud("S-032", "ana@udes.edu.co", 1000000, "SOFTWARE", "CC-100");
        ContextoSolicitud contexto = new ContextoSolicitud(s);

        // Transicionar a APROBADA y luego a EJECUTADA
        contexto.aprobar();
        contexto.ejecutar();

        // Intentar ejecutar nuevamente
        String resultado = contexto.ejecutar();

        assertTrue(resultado.contains("Error"));
        assertEquals("EJECUTADA", s.getEstado());
    }
}