package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public abstract class Aprobador {
    protected Aprobador siguiente;

    public void setSiguiente(Aprobador siguiente) {
        this.siguiente = siguiente;
    }

    public abstract ResultadoAprobacion evaluar(Solicitud solicitud);

    protected ResultadoAprobacion pasarAlSiguiente(Solicitud solicitud) {
        if (siguiente != null) {
            return siguiente.evaluar(solicitud);
        }
        solicitud.setEstado("RECHAZADA");
        solicitud.setNivelResolutor("Sin Nivel");
        return new ResultadoAprobacion(false, "Sin Nivel", "Monto supera los niveles de aprobación disponibles");
    }
}