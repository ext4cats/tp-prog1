package juego;

import entorno.Entorno;

import java.awt.*;

public record Plataforma(double x, double y, double ancho, double alto) implements Colisionable {
    void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.GRAY);
    }
}
