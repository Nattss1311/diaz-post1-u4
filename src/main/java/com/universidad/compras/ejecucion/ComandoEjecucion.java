package com.universidad.compras.ejecucion;

public interface ComandoEjecucion {
    void ejecutar();
    void deshacer();
    String getNombre();
}