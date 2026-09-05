package com.universidad.compras.notificacion;

import java.util.ArrayList;
import java.util.List;

import com.universidad.compras.modelo.Solicitud;

public class NotificadorEstadoSolicitud {
    private final List<ObservadorEstado> observadores = new ArrayList<>();

    public void suscribir(ObservadorEstado observador) {
        observadores.add(observador);
    }

    public void desuscribir(ObservadorEstado observador) {
        observadores.remove(observador);
    }

    public void notificarCambio(Solicitud solicitud, String estadoAnterior, String nuevoEstado, String detalle) {
        for (ObservadorEstado obs : observadores) {
            obs.alCambiarEstado(solicitud, estadoAnterior, nuevoEstado, detalle);
        }
    }
}