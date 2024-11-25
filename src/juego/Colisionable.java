package juego;

/**
 * Define una Axis Aligned Bounding Box utilizada para chequear colisiones.
 */
public interface Colisionable {
    /**
     * Devuelve la coordenada x del objeto.
     * @return La coordenada x.
     */
    double x();

    /**
     * Devuelve la coordenada y del objeto.
     * @return La coordenada y.
     */
    double y();

    /**
     * Devuelve el ancho del objeto.
     * @return El ancho del objeto.
     */
    double ancho();

    /**
     * Devuelve el alto del objeto.
     * @return El alto del objeto.
     */
    double alto();

    /**
     * Devuelve la coordenada y del borde alto.
     * @return El borde alto del objeto.
     */
    default double bordeAlto() {
        return this.y() - this.alto() / 2;
    }

    /**
     * Devuelve la coordenada y del borde bajo.
     * @return El borde bajo del objeto.
     */
    default double bordeBajo() {
        return this.y() + this.alto() / 2;
    }

    /**
     * Devuelve la coordenada x del borde izquierdo.
     * @return El borde izquierdo.
     */
    default double bordeIzquierdo() {
        return this.x() - this.ancho() / 2;
    }

    /**
     * Devuelva la coordenada x del borde derecho.
     * @return El borde derecho.
     */
    default double bordeDerecho() {
        return this.x() + this.ancho() / 2;
    }

    /**
     * Chequea si este objeto colisiona con otro objeto.
     * @param otro El otro objeto a chequear.
     * @return Si ambos objetos colisionan o no.
     */
    default boolean colisionaCon(Colisionable otro) {
        return this.bordeIzquierdo() < otro.bordeDerecho() &&
                this.bordeDerecho() > otro.bordeIzquierdo() &&
                this.bordeAlto() < otro.bordeBajo() &&
                this.bordeBajo() > otro.bordeAlto();
    }
}
