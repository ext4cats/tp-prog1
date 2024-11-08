package juego;

import entorno.Entorno;

import java.awt.*;

public class Jugador {
    private static final double ANCHO = 30;
    private static final double ALTO = 60;
    private static final double GRAVEDAD = 0.05;
    private static final double FRICCION = 0.05;
    private static final double ACELERACION = 0.25;
    private static final double VELOCIDAD_SALTO = 3.5;
    private static final double VELOCIDAD_VERTICAL_MAXIMA = 4;
    private static final double VELOCIDAD_HORIZONTAL_MAXIMA = 1.5;
    private boolean enVuelo;
    private double x;
    private double y;
    private double vx;
    private double vy;

    public Jugador(double xInicial, double yInicial) {
        this.enVuelo = true;
        this.x = xInicial;
        this.y = yInicial;
        this.vx = 0.0;
        this.vy = 0.0;
    }

    public void moverIzquierda() {
        this.vx = Math.max(this.vx - ACELERACION, -VELOCIDAD_HORIZONTAL_MAXIMA);
    }

    public void moverDerecha() {
        this.vx = Math.min(this.vx + ACELERACION, VELOCIDAD_HORIZONTAL_MAXIMA);
    }

    public void saltar() {
        if (enVuelo) return;
        this.enVuelo = true;
        this.vy = -VELOCIDAD_SALTO;
    }

    public void aterrizar() {
        this.enVuelo = false;
        this.vy = 0.0;
    }

    public void teletransportar(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void actualizar() {
        if (enVuelo) this.vy = Math.min(this.vy + GRAVEDAD, VELOCIDAD_VERTICAL_MAXIMA);
        double nuevoVx = vx - FRICCION * Math.signum(vx);
        if (nuevoVx * this.vx < 0) nuevoVx = 0;
        this.vx = nuevoVx;
        this.x += this.vx;
        this.y += this.vy;
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.x, this.y, ANCHO, ALTO, 0, Color.YELLOW);
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }
}