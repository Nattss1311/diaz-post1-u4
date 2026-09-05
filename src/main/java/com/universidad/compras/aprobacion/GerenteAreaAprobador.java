package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class GerenteAreaAprobador extends Aprobador {
    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        if (solicitud.getMonto() <= 10000000) {
            solicitud.setEstado("APROBADA");
            solicitud.setNivelResolutor("Gerente de Área");
            return new ResultadoAprobacion(true, "Gerente de Área", "Aprobada por Gerente de Área (hasta $10.000.000)");
        }
        return pasarAlSiguiente(solicitud);
    }
}