package juego;

import entorno.Entorno;

import java.awt.*;

public class Contadores {
    private final int limitePerdidos;
    private final int objetivoSalvados;
    private int gnomosPerdidos;
    private int gnomosSalvados;
    private int enemigosEliminados;

    public Contadores() {
        this.limitePerdidos = 15;
        this.objetivoSalvados = 10;
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
        if (this.gnomosPerdidos >= 15) {
            return EstadoJuego.PERDIDO;
        } else if (this.gnomosSalvados >= 10) {
            return EstadoJuego.GANADO;
        } else {
            return EstadoJuego.EN_PROGRESO;
        }
    }

    public void dibujar(Entorno entorno) {
        entorno.cambiarFont("serif", 16, Color.WHITE);
        entorno.escribirTexto(String.format("PERDIDOS: %d/%d", gnomosPerdidos, limitePerdidos), 30, 50);
        entorno.escribirTexto(String.format("SALVADOS: %d/%d", gnomosSalvados, objetivoSalvados), 200, 50);
        entorno.escribirTexto("ELIMINADOS: " + enemigosEliminados, 450, 50);

        int milisegundos = entorno.tiempo();
        int segundosTotales = milisegundos / 1000;
        int minutos = segundosTotales / 60;
        int segundos = segundosTotales % 60;

        entorno.escribirTexto(String.format("TIEMPO: %02d:%02d", minutos, segundos), 650, 50);
    }
}
