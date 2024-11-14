package juego;

import entorno.Entorno;

import java.awt.*;

public class Jugador {
    private static final double ANCHO = 30;
    private static final double ALTO = 60;
    private final double x;
    private final double y;

    public Jugador(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.x, this.y, ANCHO, ALTO, 0, Color.YELLOW);
    }
}
