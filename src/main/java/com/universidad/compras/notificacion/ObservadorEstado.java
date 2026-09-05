package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

public interface ObservadorEstado {
    void alCambiarEstado(Solicitud solicitud, String estadoAnterior, String nuevoEstado, String detalle);
}