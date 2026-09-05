package com.universidad.compras.estado;

import com.universidad.compras.modelo.Solicitud;

public class ContextoSolicitud {
    private final Solicitud solicitud;
    private EstadoSolicitud estadoActual;

    public ContextoSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
        
        // Asignación de estado inicial según el estado guardado en la solicitud
        String est = solicitud.getEstado();
        if (est == null || est.equalsIgnoreCase("PENDIENTE")) {
            solicitud.setEstado("PENDIENTE");
            this.estadoActual = new EstadoPendiente();
        } else if (est.equalsIgnoreCase("APROBADA")) {
            this.estadoActual = new EstadoAprobada();
        } else if (est.equalsIgnoreCase("EJECUTADA")) {
            this.estadoActual = new EstadoEjecutada();
        } else if (est.equalsIgnoreCase("RECHAZADA")) {
            this.estadoActual = new EstadoRechazada();
        } else if (est.equalsIgnoreCase("CANCELADA")) {
            this.estadoActual = new EstadoCancelada();
        } else {
            this.estadoActual = new EstadoPendiente();
        }
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public EstadoSolicitud getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(EstadoSolicitud nuevoEstado) {
        this.estadoActual = nuevoEstado;
        if (nuevoEstado != null) {
            this.solicitud.setEstado(nuevoEstado.getNombre());
        }
    }

    public String aprobar() {
        return estadoActual.aprobar(this);
    }

    public String rechazar() {
        return estadoActual.rechazar(this);
    }

    public String ejecutar() {
        return estadoActual.ejecutar(this);
    }

    public String cancelar() {
        return estadoActual.cancelar(this);
    }
}