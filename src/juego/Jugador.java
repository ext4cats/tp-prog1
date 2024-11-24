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
    private int temporizadorBolaFuego;
    private Direccion direccion;
    private double vy;
    private double x;
    private double y;
    private double vx;

    public Jugador(double x, double y) {
        this.temporizadorBolaFuego = 0;
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
    }

    public void actualizar(boolean izquierda, boolean derecha, boolean salto) {
        // Acelerar hacia la izquierda si es la direccion
        if (izquierda) {
            this.vx = Math.max(this.vx - ACELERACION, -VELOCIDAD_MAXIMA);
            this.direccion = Direccion.IZQUIERDA;
        }

        // Lo mismo, pero para la derecha
        if (derecha) {
            this.vx = Math.min(this.vx + ACELERACION, VELOCIDAD_MAXIMA);
            this.direccion = Direccion.DERECHA;
        }

        // Aplicamos la velocidad
        this.x += this.vx;
        this.y += this.vy;

        // Aplicamos friccion
        this.vx -= Math.signum(this.vx) * FRICCION;
        if (Math.abs(this.vx) < 0.02) this.vx = 0;

        // Ajustamos la velocidad del jugador si hay un salto
        if (salto) {
            this.vy = -VELOCIDAD_SALTO;
        }

        // Aplicamos gravedad
        this.vy = Math.min(this.vy + GRAVEDAD, VELOCIDAD_TERMINAL);

        // Recargamos la bola de fuego del jugador
        this.temporizadorBolaFuego = Math.max(0, this.temporizadorBolaFuego - 1);
    }

    public boolean puedeDisparar() {
        return this.temporizadorBolaFuego == 0;
    }

    public BolaFuego disparar() {
        this.temporizadorBolaFuego = 75;
        return new BolaFuego(this.x, this.y, this.direccion);
    }

    public void moverAPosicion(PosicionFutura posicion) {
        this.x = posicion.x();
        this.y = posicion.y();
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
