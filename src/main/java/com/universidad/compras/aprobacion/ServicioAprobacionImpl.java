package com.universidad.compras.aprobacion;

import org.springframework.stereotype.Service;

import com.universidad.compras.modelo.Solicitud;

@Service
public class ServicioAprobacionImpl implements ServicioAprobacion {

    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        Aprobador supervisor = new SupervisorAreaAprobador();
        Aprobador gerente = new GerenteAreaAprobador();
        Aprobador director = new DirectorFinancieroAprobador();

        if ("INTERNACIONAL".equalsIgnoreCase(solicitud.getCategoria())) {
            Aprobador revisor = new RevisorCumplimientoAprobador();
            revisor.setSiguiente(supervisor);
            supervisor.setSiguiente(gerente);
            gerente.setSiguiente(director);
            return revisor.evaluar(solicitud);
        } else {
            supervisor.setSiguiente(gerente);
            gerente.setSiguiente(director);
            return supervisor.evaluar(solicitud);
        }
    }
}