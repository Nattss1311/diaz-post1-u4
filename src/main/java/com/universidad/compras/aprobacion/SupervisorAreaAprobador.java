package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class SupervisorAreaAprobador extends Aprobador {
    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        if (solicitud.getMonto() <= 2000000) {
            solicitud.setEstado("APROBADA");
            solicitud.setNivelResolutor("Supervisor de Área");
            return new ResultadoAprobacion(true, "Supervisor de Área", "Aprobada por Supervisor de Área (hasta $2.000.000)");
        }
        return pasarAlSiguiente(solicitud);
    }
}