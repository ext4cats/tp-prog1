package juego;

import entorno.Entorno;

import java.awt.*;

public class Gnomo implements Colisionable {
    private static final double ANCHO = 20;
    private static final double ALTO = 40;
    private static final double VELOCIDAD_HORIZONTAL = 0.65;
    private static final double VELOCIDAD_VERTICAL = 1;
    private Plataforma plataforma;
    private Direccion direccion;
    private double x;
    private double y;

    public Gnomo(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void mover() {
        if (this.plataforma == null) {
            this.y += VELOCIDAD_VERTICAL;
        }

        if (this.direccion == Direccion.DERECHA) {
            this.x += VELOCIDAD_HORIZONTAL;
            if (this.plataforma != null && this.bordeIzquierdo() > plataforma.bordeDerecho()) this.plataforma = null;
        } else {
            this.x -= VELOCIDAD_HORIZONTAL;
            if (this.plataforma != null && this.bordeDerecho() < plataforma.bordeIzquierdo()) this.plataforma = null;
        }
    }

    public void aterrizar(Plataforma plataforma, Direccion direccion) {
        this.y = plataforma.bordeAlto() - ALTO / 2;
        this.plataforma = plataforma;
        this.direccion = direccion;
    }

    public boolean estaAterrizado() {
        return this.plataforma != null;
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.x, this.y, ANCHO, ALTO, 0, Color.CYAN);
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
