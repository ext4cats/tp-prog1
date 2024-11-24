package juego;

import entorno.Entorno;

import java.awt.*;

public class Contadores {
    private int gnomosPerdidos;
    private int gnomosSalvados;
    private int enemigosEliminados;

    public Contadores() {
        this.gnomosPerdidos = 0;
        this.gnomosSalvados = 0;
        this.enemigosEliminados = 0;
    }

    public void agregarGnomoPerdido() {
        this.gnomosPerdidos += 1;
    }

    public void agregarGnomoSalvado() {
        this.gnomosSalvados += 1;
    }

    public void agregarEnemigoEliminado() {
        this.enemigosEliminados += 1;
    }

    public EstadoJuego estadoJuego() {
        if (this.gnomosPerdidos >= 1) {
            return EstadoJuego.PERDIDO;
        } else if (this.gnomosSalvados >= 1) {
            return EstadoJuego.GANADO;
        } else {
            return EstadoJuego.EN_PROGRESO;
        }
    }

    public void dibujar(Entorno entorno) {
        entorno.cambiarFont("serif", 16, Color.WHITE);
        entorno.escribirTexto("PERDIDOS: " + gnomosPerdidos, 30, 50);
        entorno.escribirTexto("SALVADOS: " + gnomosSalvados, 200, 50);
        entorno.escribirTexto("ELIMINADOS: " + enemigosEliminados, 450, 50);

        int milisegundos = entorno.tiempo();
        int segundosTotales = milisegundos / 1000;
        int minutos = segundosTotales / 60;
        int segundos = segundosTotales % 60;

        entorno.escribirTexto(String.format("TIEMPO: %02d:%02d", minutos, segundos), 650, 50);
    }
}
