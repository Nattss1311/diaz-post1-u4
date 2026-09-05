package com.universidad.compras.aprobacion;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compras.modelo.Solicitud;

// Ya en producción — no modificar. Depende únicamente de ServicioAprobacion.
@RestController
@RequestMapping("/api/solicitudes")
public class ControladorSolicitudes {
    private final ServicioAprobacion servicioAprobacion;

    public ControladorSolicitudes(ServicioAprobacion servicioAprobacion) {
        this.servicioAprobacion = servicioAprobacion;
    }

    @PostMapping("/evaluar")
    public ResponseEntity<ResultadoAprobacion> evaluar(@RequestBody Solicitud solicitud) {
        ResultadoAprobacion resultado = servicioAprobacion.evaluar(solicitud);
        return resultado.isAprobada() ? ResponseEntity.ok(resultado) : ResponseEntity.status(422).body(resultado);
    }
}