package juego;

import entorno.Entorno;

import java.awt.*;

/**
 * Define una bola de fuego disparada por nuestro protagonista Pep.
 */
public class BolaFuego implements Colisionable {
    /**
     * El ancho de la bola de fuego.
     */
    private static final double ANCHO = 8;

    /**
     * El alto de la bola de fuego.
     */
    private static final double ALTO = 8;

    /**
     * La velocidad de movimiento.
     */
    private static final double VELOCIDAD = 2;

    /**
     * La dirección en la cual debe moverse.
     */
    private final Direccion direccion;

    /**
     * Coordenada y.
     */
    private final double y;

    /**
     * Coordenada x.
     */
    private double x;

    /**
     * Crea una nueva bola de fuego con coordenadas (X,Y), y una dirección de movimiento.
     * @param x Coordenada x.
     * @param y Coordenada y.
     * @param direccion Dirección de movimiento.
     */
    public BolaFuego(double x, double y, Direccion direccion) {
        this.x = x;
        this.y = y;
        this.direccion = direccion;
    }

    /**
     * Mueve la bola de fuego, basado en su dirección de movimiento.
     */
    public void mover() {
        if (direccion == Direccion.DERECHA) {
            this.x += VELOCIDAD;
        } else {
            this.x -= VELOCIDAD;
        }
    }

    /**
     * Dibuja la bola de fuego en la pantalla.
     * @param entorno El entorno necesario para dibujar el objeto.
     */
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
