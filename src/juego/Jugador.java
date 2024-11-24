package juego;

import entorno.Entorno;

import java.awt.*;

public class Jugador implements Colisionable {
    private static final double ANCHO = 30;
    private static final double ALTO = 60;
    private static final double ACELERACION = 0.2;
    private static final double FRICCION = 0.04;
    private static final double VELOCIDAD_MAXIMA = 1.2;
    private static final double GRAVEDAD = 0.1;
    private static final double VELOCIDAD_TERMINAL = 2;
    private static final double VELOCIDAD_SALTO = 5.0;
    private double vy;
    private double x;
    private double y;
    private double vx;

    public Jugador(double x, double y) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
    }

    public void acelerarIzquierda() {
        this.vx = Math.max(this.vx - ACELERACION, -VELOCIDAD_MAXIMA);
    }

    public void acelerarDerecha() {
        this.vx = Math.min(this.vx + ACELERACION, VELOCIDAD_MAXIMA);
    }

    public void saltar() {
        this.vy = -VELOCIDAD_SALTO;
    }

    public void aplicarGravedad() {
        this.vy = Math.min(this.vy + GRAVEDAD, VELOCIDAD_TERMINAL);
    }

    public void aplicarFriccion() {
        this.vx -= Math.signum(this.vx) * FRICCION;
        if (Math.abs(this.vx) < 0.02) this.vx = 0;
    }

    public void aplicarVelocidad() {
        this.x += this.vx;
        this.y += this.vy;
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
