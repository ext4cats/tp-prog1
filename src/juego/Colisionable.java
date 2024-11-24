package juego;

public interface Colisionable {
    double x();

    double y();

    double ancho();

    double alto();

    default double bordeAlto() {
        return this.y() - this.alto();
    }

    default double bordeBajo() {
        return this.y() + this.alto();
    }

    default double bordeIzquierdo() {
        return this.x() - this.ancho();
    }

    default double bordeDerecho() {
        return this.x() + this.ancho();
    }

    default boolean colisionaCon(Colisionable otro) {
        return this.bordeIzquierdo() < otro.bordeDerecho() &&
                this.bordeDerecho() > otro.bordeIzquierdo() &&
                this.bordeAlto() < otro.bordeBajo() &&
                this.bordeBajo() > otro.bordeAlto();
    }
}