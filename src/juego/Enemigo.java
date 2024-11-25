package juego;

import entorno.Entorno;

import java.awt.*;

/**
 * Define una tortuga, el enemigo mortal de Pep y los gnomos.
 */
public class Enemigo implements Colisionable {
    /**
     * El ancho de la tortuga.
     */
    private static final double ANCHO = 30;

    /**
     * El alto de la tortuga.
     */
    private static final double ALTO = 30;

    /**
     * La velocidad a la cual camina la tortuga.
     */
    private static final double VELOCIDAD_HORIZONTAL = 1;

    /**
     * La velocidad a la cual cae la tortuga.
     */
    private static final double VELOCIDAD_VERTICAL = 1;

    /**
     * La dirección en la cual se mueve la tortuga.
     */
    private Direccion direccion;

    /**
     * La plataforma en la cual la tortuga está parada.
     * Es nulo si la tortuga está cayendo.
     */
    private Plataforma plataforma;

    /**
     * La coordenada x.
     */
    private double x;

    /**
     * La coordenada y.
     */
    private double y;

    /**
     * Crea una nueva tortuga.
     * @param x La coordenada x inicial.
     * @param y La coordenada y inicial.
     */
    public Enemigo(double x, double y) {
        this.direccion = Direccion.DERECHA;
        this.x = x;
        this.y = y;
    }

    /**
     * Mueve a la tortuga basado en su estado interno.
     */
    public void mover() {
        if (this.plataforma == null) {
            this.y += VELOCIDAD_VERTICAL;
        } else if (this.direccion == Direccion.DERECHA) {
            this.x += VELOCIDAD_HORIZONTAL;
            if (this.bordeDerecho() > this.plataforma.bordeDerecho()) {
                this.direccion = Direccion.IZQUIERDA;
            }
        } else {
            this.x -= VELOCIDAD_HORIZONTAL;
            if (this.bordeIzquierdo() < this.plataforma.bordeIzquierdo()) {
                this.direccion = Direccion.DERECHA;
            }
        }
    }

    /**
     * Aterriza a la tortuga en una plataforma.
     * @param plataforma La plataforma en la cual la tortuga ha aterrizado.
     */
    public void aterrizar(Plataforma plataforma) {
        this.plataforma = plataforma;
    }

    /**
     * Devuelve si el enemigo está aterrizado o no.
     * @return Si el enemigo está aterrizado.
     */
    public boolean estaAterrizado() {
        return this.plataforma != null;
    }

    /**
     * Dibuja la tortuga en la pantalla.
     * @param entorno La pantalla a la cual dibujar.
     */
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
