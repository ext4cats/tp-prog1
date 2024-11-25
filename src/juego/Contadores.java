package juego;

import entorno.Entorno;

import java.awt.*;

/**
 * Define el estado global del juego, y dibuja el mismo en la interfaz.
 */
public class Contadores {
    /**
     * El límite de gnomos perdidos antes de perder el juego.
     */
    private final int limitePerdidos;

    /**
     * El objetivo de gnomos salvados necesario para ganar el juego.
     */
    private final int objetivoSalvados;

    /**
     * La cantidad actual de gnomos perdidos.
     */
    private int gnomosPerdidos;

    /**
     * La cantidad actual de gnomos salvados.
     */
    private int gnomosSalvados;

    /**
     * La cantidad de enemigos eliminados.
     */
    private int enemigosEliminados;

    /**
     * Crea una nueva instancia.
     */
    public Contadores() {
        this.limitePerdidos = 15;
        this.objetivoSalvados = 10;
        this.gnomosPerdidos = 0;
        this.gnomosSalvados = 0;
        this.enemigosEliminados = 0;
    }

    /**
     * Agrega un gnomo perdido al contador.
     */
    public void agregarGnomoPerdido() {
        this.gnomosPerdidos += 1;
    }

    /**
     * Agrega un gnomo salvado al contador.
     */
    public void agregarGnomoSalvado() {
        this.gnomosSalvados += 1;
    }

    /**
     * Agrega un enemigo eliminado al contador.
     */
    public void agregarEnemigoEliminado() {
        this.enemigosEliminados += 1;
    }

    /**
     * Decide el estado del juego, basado en el valor de los contadores.
     * @return Si el juego está en progreso, ganado o perdido.
     */
    public EstadoJuego estadoJuego() {
        if (this.gnomosPerdidos >= 15) {
            return EstadoJuego.PERDIDO;
        } else if (this.gnomosSalvados >= 10) {
            return EstadoJuego.GANADO;
        } else {
            return EstadoJuego.EN_PROGRESO;
        }
    }

    /**
     * Dibuja los contadores en la interfaz.
     * @param entorno El entorno al cual dibujar la interfaz.
     */
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
