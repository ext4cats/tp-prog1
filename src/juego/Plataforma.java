package juego;

import entorno.Entorno;

import java.awt.*;

/**
 * Define una plataforma.
 * @param x La posición x.
 * @param y La posición y.
 * @param ancho El ancho.
 * @param alto El alto.
 */
public record Plataforma(double x, double y, double ancho, double alto) implements Colisionable {
    /**
     * Dibuja la plataforma en la pantalla.
     * @param entorno El entorno al cual dibujar.
     */
    void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.GRAY);
    }
}
