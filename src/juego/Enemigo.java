package juego;

import entorno.Entorno;

import java.awt.*;

public class Enemigo implements Colisionable {
    private static final double ANCHO = 30;
    private static final double ALTO = 30;
    private static final double VELOCIDAD_HORIZONTAL = 1;
    private static final double VELOCIDAD_VERTICAL = 1;
    private Direccion direccion;
    private Plataforma plataforma;
    private double x;
    private double y;

    public Enemigo(double x, double y) {
        this.direccion = Direccion.DERECHA;
        this.x = x;
        this.y = y;
    }

    public void mover() {
        if (this.plataforma == null) {
            this.y += VELOCIDAD_VERTICAL;
        } else if (this.direccion == Direccion.DERECHA) {
            this.x += VELOCIDAD_HORIZONTAL;
            if (this.bordeDerecho() == this.plataforma.bordeDerecho()) {
                this.direccion = Direccion.IZQUIERDA;
            }
        } else {
            this.x -= VELOCIDAD_HORIZONTAL;
            if (this.bordeIzquierdo() == this.plataforma.bordeIzquierdo()) {
                this.direccion = Direccion.DERECHA;
            }
        }
    }

    public void aterrizar(Plataforma plataforma) {
        this.plataforma = plataforma;
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.x, this.y, ANCHO, ALTO, 0, Color.RED);
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
