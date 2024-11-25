package juego;

import entorno.Entorno;

import java.awt.*;

/**
 * Define un Gnomo, una criatura no muy inteligente y el objetivo de rescate del juego.
 */
public class Gnomo implements Colisionable {
    /**
     * El ancho del gnomo.
     */
    private static final double ANCHO = 20;

    /**
     * El alto del gnomo.
     */
    private static final double ALTO = 40;

    /**
     * La velocidad a la cual camina el gnomo.
     */
    private static final double VELOCIDAD_HORIZONTAL = 0.65;

    /**
     * La velocidad a la cual cae el gnomo.
     */
    private static final double VELOCIDAD_VERTICAL = 1;

    /**
     * La plataforma en la cual está parado el gnomo.
     * Es nulo si el gnomo está cayendo.
     */
    private Plataforma plataforma;

    /**
     * La dirección de movimiento del gnomo.
     */
    private Direccion direccion;

    /**
     * La coordenada x.
     */
    private double x;

    /**
     * La coordenada y.
     */
    private double y;

    /**
     * Crea un nuevo gnomo.
     * @param x Su coordenada x inicial.
     * @param y Su coordenada y inicial.
     */
    public Gnomo(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Mueve al gnomo basado en su estado interno.
     */
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

    /**
     * Aterriza al gnomo en una plataforma.
     * @param plataforma La plataforma en la cual se ha aterrizado.
     * @param direccion La nueva dirección a tomar.
     */
    public void aterrizar(Plataforma plataforma, Direccion direccion) {
        this.y = plataforma.bordeAlto() - ALTO / 2;
        this.plataforma = plataforma;
        this.direccion = direccion;
    }

    /**
     * Devuelve si el gnomo está aterrizado.
     * @return Si el gnomo está aterrizado o no.
     */
    public boolean estaAterrizado() {
        return this.plataforma != null;
    }

    /**
     * Dibuja al gnomo en la pantalla.
     * @param entorno El entorno al cual dibujar el gnomo.
     */
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
