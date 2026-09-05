package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class DirectorFinancieroAprobador extends Aprobador {
    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        solicitud.setEstado("APROBADA");
        solicitud.setNivelResolutor("Director Financiero");
        return new ResultadoAprobacion(true, "Director Financiero", "Aprobada por Director Financiero (sin límite)");
    }
}