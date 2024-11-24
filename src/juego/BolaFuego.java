package juego;

import entorno.Entorno;

import java.awt.*;

public class BolaFuego implements Colisionable {
    private static final double ANCHO = 8;
    private static final double ALTO = 8;
    private static final double VELOCIDAD = 1;
    private final Direccion direccion;
    private final double y;
    private double x;

    public BolaFuego(double x, double y, Direccion direccion) {
        this.x = x;
        this.y = y;
        this.direccion = direccion;
    }

    public void mover() {
        if (direccion == Direccion.DERECHA) {
            this.x += VELOCIDAD;
        } else {
            this.x -= VELOCIDAD;
        }
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.x, this.y, ANCHO, ALTO, 0, Color.YELLOW);
    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double ancho() {
        return ANCHO;
    }

    @Override
    public double alto() {
        return ALTO;
    }
}
