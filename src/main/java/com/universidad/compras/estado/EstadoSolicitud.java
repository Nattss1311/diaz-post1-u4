package com.universidad.compras.estado;

import com.universidad.compras.modelo.Solicitud;

public interface EstadoSolicitud {
    String aprobar(ContextoSolicitud contexto);
    String rechazar(ContextoSolicitud contexto);
    String ejecutar(ContextoSolicitud contexto);
    String cancelar(ContextoSolicitud contexto);
    String getNombre();
}