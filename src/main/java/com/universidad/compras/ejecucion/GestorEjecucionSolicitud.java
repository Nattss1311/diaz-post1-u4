package com.universidad.compras.ejecucion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.universidad.compras.modelo.Solicitud;

public class GestorEjecucionSolicitud {
    private final Solicitud solicitud;
    private final List<ComandoEjecucion> historial = new ArrayList<>();

    public GestorEjecucionSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    public void ejecutarComando(ComandoEjecucion comando) {
        comando.ejecutar();
        historial.add(comando);
        solicitud.setEstado("EJECUTADA");
    }

    public void deshacerUltimoComando() {
        if (!historial.isEmpty()) {
            ComandoEjecucion ultimo = historial.remove(historial.size() - 1);
            ultimo.deshacer();
        }
    }

    public void deshacerComando(int indice) {
        if (indice >= 0 && indice < historial.size()) {
            ComandoEjecucion comando = historial.remove(indice);
            comando.deshacer();
        }
    }

    public List<ComandoEjecucion> getHistorial() {
        return Collections.unmodifiableList(historial);
    }
}