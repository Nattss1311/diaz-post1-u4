package com.universidad.compras.estado;

import com.universidad.compras.modelo.Solicitud;

class EstadoPendiente implements EstadoSolicitud {
    @Override
    public String getNombre() {
        return "PENDIENTE";
    }

    @Override
    public String aprobar(ContextoSolicitud ctx) {
        ctx.setEstadoActual(new EstadoAprobada());
        return "Solicitud aprobada con éxito.";
    }

    @Override
    public String rechazar(ContextoSolicitud ctx) {
        ctx.setEstadoActual(new EstadoRechazada());
        return "Solicitud rechazada.";
    }

    @Override
    public String ejecutar(ContextoSolicitud ctx) {
        return "Error: No se puede ejecutar una solicitud en estado PENDIENTE. Debe estar APROBADA.";
    }

    @Override
    public String cancelar(ContextoSolicitud ctx) {
        ctx.setEstadoActual(new EstadoCancelada());
        return "Solicitud cancelada.";
    }
}

class EstadoAprobada implements EstadoSolicitud {
    @Override
    public String getNombre() {
        return "APROBADA";
    }

    @Override
    public String aprobar(ContextoSolicitud ctx) {
        return "La solicitud ya se encuentra en estado APROBADA.";
    }

    @Override
    public String rechazar(ContextoSolicitud ctx) {
        return "Error: No se puede rechazar una solicitud que ya ha sido APROBADA.";
    }

    @Override
    public String ejecutar(ContextoSolicitud ctx) {
        ctx.setEstadoActual(new EstadoEjecutada());
        return "Solicitud ejecutada con éxito.";
    }

    @Override
    public String cancelar(ContextoSolicitud ctx) {
        ctx.setEstadoActual(new EstadoCancelada());
        return "Solicitud cancelada antes de su ejecución.";
    }
}

class EstadoEjecutada implements EstadoSolicitud {
    @Override
    public String getNombre() {
        return "EJECUTADA";
    }

    @Override
    public String aprobar(ContextoSolicitud ctx) {
        return "Error: La solicitud ya fue EJECUTADA.";
    }

    @Override
    public String rechazar(ContextoSolicitud ctx) {
        return "Error: La solicitud ya fue EJECUTADA.";
    }

    @Override
    public String ejecutar(ContextoSolicitud ctx) {
        return "Error: La solicitud ya se encuentra EJECUTADA.";
    }

    @Override
    public String cancelar(ContextoSolicitud ctx) {
        return "Error: No se puede cancelar una solicitud que ya fue EJECUTADA.";
    }
}

class EstadoRechazada implements EstadoSolicitud {
    @Override
    public String getNombre() {
        return "RECHAZADA";
    }

    @Override
    public String aprobar(ContextoSolicitud ctx) {
        return "Error: No se puede aprobar una solicitud RECHAZADA.";
    }

    @Override
    public String rechazar(ContextoSolicitud ctx) {
        return "La solicitud ya se encuentra RECHAZADA.";
    }

    @Override
    public String ejecutar(ContextoSolicitud ctx) {
        return "Error: No se puede ejecutar una solicitud RECHAZADA.";
    }

    @Override
    public String cancelar(ContextoSolicitud ctx) {
        return "Error: No se puede cancelar una solicitud RECHAZADA.";
    }
}

class EstadoCancelada implements EstadoSolicitud {
    @Override
    public String getNombre() {
        return "CANCELADA";
    }

    @Override
    public String aprobar(ContextoSolicitud ctx) {
        return "Error: No se puede aprobar una solicitud CANCELADA.";
    }

    @Override
    public String rechazar(ContextoSolicitud ctx) {
        return "Error: No se puede rechazar una solicitud CANCELADA.";
    }

    @Override
    public String ejecutar(ContextoSolicitud ctx) {
        return "Error: No se puede ejecutar una solicitud CANCELADA.";
    }

    @Override
    public String cancelar(ContextoSolicitud ctx) {
        return "La solicitud ya se encuentra CANCELADA.";
    }
}