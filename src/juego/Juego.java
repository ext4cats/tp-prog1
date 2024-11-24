package juego;

import entorno.Entorno;
import entorno.InterfaceJuego;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Juego extends InterfaceJuego {
    private final Random random;
    private final Entorno entorno;
    private final List<Enemigo> enemigos;
    private final Plataforma[] plataformas;
    private int temporizadorSpawn;
    private Jugador jugador;

    private Juego() {
        this.random = new Random();
        this.temporizadorSpawn = 0;
        this.entorno = new Entorno(this, "Al Rescate de los Gnomos", 800, 600);
        this.jugador = new Jugador(100, 355);
        this.enemigos = new ArrayList<>();
        this.plataformas = this.generarPlataformas();
    }

    public static void main(String[] args) {
        Juego juego = new Juego();
        juego.iniciar();
    }

    @Override
    public void tick() {
        if (jugador == null) {
            entorno.cambiarFont("serif", 32, Color.RED);
            entorno.escribirTexto("GAME OVER", 300, 300);
            return;
        }

        if (temporizadorSpawn == 0) {
            int lado = random.nextInt(2);
            int spawn;
            if (lado == 0) {
                spawn = random.nextInt(350);
            } else {
                spawn = random.nextInt(350) + 450;
            }
            enemigos.add(new Enemigo(spawn, 0));
            temporizadorSpawn = 1000;
        } else {
            temporizadorSpawn -= 1;
        }

        boolean moviendoIzquierda = this.entorno.estaPresionada(entorno.TECLA_IZQUIERDA) || this.entorno.estaPresionada('a');
        boolean moviendoDerecha = this.entorno.estaPresionada(entorno.TECLA_DERECHA) || this.entorno.estaPresionada('d');
        if (moviendoIzquierda) this.jugador.acelerarIzquierda();
        if (moviendoDerecha) this.jugador.acelerarDerecha();
        this.jugador.aplicarVelocidad();
        this.jugador.aplicarFriccion();

        boolean saltando = this.entorno.sePresiono(entorno.TECLA_ARRIBA) || this.entorno.sePresiono('w');
        if (saltando) this.jugador.saltar();
        this.jugador.aplicarGravedad();

        for (Plataforma plataforma : this.plataformas) {
            if (jugador.colisionaCon(plataforma)) {
                double interseccionX = Math.min(jugador.bordeDerecho() - plataforma.bordeIzquierdo(), plataforma.bordeDerecho() - jugador.bordeIzquierdo());
                double interseccionY = Math.min(jugador.bordeBajo() - plataforma.bordeAlto(), plataforma.bordeBajo() - jugador.bordeAlto());
                if (Math.abs(interseccionX) < Math.abs(interseccionY)) {
                    if (jugador.bordeIzquierdo() < plataforma.bordeIzquierdo()) {
                        jugador.moverAPosicion(new PosicionFutura(jugador.x() - interseccionX, jugador.y(), jugador.ancho(), jugador.alto()));
                    } else {
                        jugador.moverAPosicion(new PosicionFutura(jugador.x() + interseccionX, jugador.y(), jugador.ancho(), jugador.alto()));
                    }
                } else {
                    if (jugador.bordeAlto() < plataforma.bordeAlto()) {
                        jugador.moverAPosicion(new PosicionFutura(jugador.x(), jugador.y() - interseccionY, jugador.ancho(), jugador.alto()));
                    } else {
                        jugador.moverAPosicion(new PosicionFutura(jugador.x(), jugador.y() + interseccionY, jugador.ancho(), jugador.alto()));
                    }
                }
            }
        }

        for (Enemigo enemigo : this.enemigos) {
            enemigo.mover();
            if (enemigo.colisionaCon(jugador)) {
                jugador = null;
                return;
            }
            for (Plataforma plataforma : this.plataformas) {
                if (enemigo.colisionaCon(plataforma)) {
                    enemigo.aterrizar(plataforma);
                }
            }
        }

        this.dibujarObjetos();
    }

    private void dibujarObjetos() {
        for (Plataforma plataforma : this.plataformas) plataforma.dibujar(this.entorno);
        for (Enemigo enemigo : this.enemigos) enemigo.dibujar(this.entorno);
        this.jugador.dibujar(this.entorno);
    }

    private Plataforma[] generarPlataformas() {
        int pisos = 5;
        Plataforma[] plataformas = new Plataforma[pisos * (pisos + 1) / 2];

        int y = 0;
        int indice = 0;
        for (int i = 1; i <= pisos; i++) {
            y = y + 100;
            int expansion = -50 * i;
            for (int j = 1; j <= i; j++) {
                int x = (this.entorno.ancho() - expansion) / (i + 1) * j + expansion / 2;
                plataformas[indice] = new Plataforma(x, y, 100, 30);
                indice++;
            }
        }

        return plataformas;
    }

    private void iniciar() {
        this.entorno.iniciar();
    }
}