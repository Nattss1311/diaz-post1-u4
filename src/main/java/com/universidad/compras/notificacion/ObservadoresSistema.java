package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

public class ObservadoresSistema {

    public static class NotificadorEmail implements ObservadorEstado {
        @Override
        public void alCambiarEstado(Solicitud solicitud, String estadoAnterior, String nuevoEstado, String detalle) {
            ClientesNotificacion.enviarCorreo(
                solicitud.getSolicitanteEmail(),
                "Actualización de Solicitud " + solicitud.getId(),
                "Su solicitud cambió de " + estadoAnterior + " a " + nuevoEstado
            );
        }
    }

    public static class ActualizadorContabilidad implements ObservadorEstado {
        @Override
        public void alCambiarEstado(Solicitud solicitud, String estadoAnterior, String nuevoEstado, String detalle) {
            ClientesNotificacion.actualizarDashboardContabilidad(
                solicitud.getId(),
                nuevoEstado,
                solicitud.getMonto()
            );
        }
    }

    public static class RegistradorAuditoria implements ObservadorEstado {
        @Override
        public void alCambiarEstado(Solicitud solicitud, String estadoAnterior, String nuevoEstado, String detalle) {
            ClientesNotificacion.registrarAuditoria(
                solicitud.getId(),
                nuevoEstado,
                detalle != null ? detalle : "Cambio de estado registrado"
            );
        }
    }
}