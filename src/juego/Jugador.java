package juego;

import entorno.Entorno;

import java.awt.*;

/**
 * Define al jugador, Pep.
 */
public class Jugador implements Colisionable {
    /**
     * El ancho del jugador.
     */
    private static final double ANCHO = 30;

    /**
     * El alto del jugador.
     */
    private static final double ALTO = 60;

    /**
     * Define la aceleración del jugador cuando camina.
     */
    private static final double ACELERACION = 0.2;

    /**
     * Define la fricción aplicada para frenar al jugador cuando no camina.
     */
    private static final double FRICCION = 0.04;

    /**
     * Define la velocidad máxima a la cual camina el jugador.
     */
    private static final double VELOCIDAD_MAXIMA = 1.2;

    /**
     * Define la aceleración hacia abajo ejercida al jugador cuando está en el aire.
     */
    private static final double GRAVEDAD = 0.1;

    /**
     * Define la velocidad máxima a la cual puede caer el jugador.
     */
    private static final double VELOCIDAD_TERMINAL = 2;

    /**
     * Define la velocidad a la cual salta el jugador.
     */
    private static final double VELOCIDAD_SALTO = 5.0;

    /**
     * El tiempo a esperar antes de disparar otra bola de fuego.
     */
    private int temporizadorBolaFuego;

    /**
     * Si el jugador está aterrizado o no.
     */
    private boolean aterrizado;

    /**
     * La última dirección en la cual se movió el jugador.
     */
    private Direccion direccion;

    /**
     * La velocidad horizontal del jugador.
     */
    private double vx;

    /**
     * La velocidad vertical del jugador.
     */
    private double vy;

    /**
     * La coordenada x.
     */
    private double x;

    /**
     * La coordenada y.
     */
    private double y;

    /**
     * Crea un nuevo jugador.
     * @param x La posición x inicial.
     * @param y La posición y inicial.
     */
    public Jugador(double x, double y) {
        this.aterrizado = false;
        this.temporizadorBolaFuego = 0;
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
    }

    /**
     * Actualiza el estado del jugador basado en las teclas presionadas.
     * @param izquierda Si el jugador está moviendose a la izquierda.
     * @param derecha Si el jugador está moviendose a la derecha.
     * @param salto Si el jugador está saltando.
     */
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
        if (salto && this.aterrizado) {
            this.vy = -VELOCIDAD_SALTO;
        }

        // Aplicamos gravedad
        this.vy = Math.min(this.vy + GRAVEDAD, VELOCIDAD_TERMINAL);

        // Recargamos la bola de fuego del jugador
        this.temporizadorBolaFuego = Math.max(0, this.temporizadorBolaFuego - 1);
    }

    /**
     * Resuelve colisiones con plataformas "empujando" al jugador fuera de la plataforma.
     * @param plataformas Las plataformas las cuales chequear por colisiones.
     */
    public void resolverColisiones(Plataforma[] plataformas) {
        boolean aterrizado = false;
        for (Plataforma plataforma : plataformas) {
            if (this.colisionaCon(plataforma)) {
                double interseccionX = Math.min(
                        this.bordeDerecho() - plataforma.bordeIzquierdo(),
                        plataforma.bordeDerecho() - this.bordeIzquierdo()
                );

                double interseccionY = Math.min(
                        this.bordeBajo() - plataforma.bordeAlto(),
                        plataforma.bordeBajo() - this.bordeAlto()
                );

                if (Math.abs(interseccionX) < Math.abs(interseccionY)) {
                    if (this.bordeIzquierdo() < plataforma.bordeIzquierdo()) {
                        this.x -= interseccionX;
                    } else {
                        this.x += interseccionX;
                    }
                } else {
                    if (this.bordeAlto() < plataforma.bordeAlto()) {
                        aterrizado = true;
                        this.y -= interseccionY;
                    } else {
                        this.y += interseccionY;
                    }
                }
            }
        }
        this.aterrizado = aterrizado;
    }

    /**
     * Chequea si el jugador puede disparar una bola de fuego.
     * @return Si puede disparar o no.
     */
    public boolean puedeDisparar() {
        return this.temporizadorBolaFuego == 0;
    }

    /**
     * Crea una nueva bola de fuego, basado en la posición y dirección del jugador.
     * @return Una nueva bola de fuego.
     */
    public BolaFuego disparar() {
        this.temporizadorBolaFuego = 75;
        return new BolaFuego(this.x, this.y + 15, this.direccion);
    }

    /**
     * Dibuja al jugador en la pantalla.
     * @param entorno El entorno al cual dibujar el jugador.
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
