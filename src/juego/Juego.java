package juego;

import entorno.Entorno;
import entorno.InterfaceJuego;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class Juego extends InterfaceJuego {
    private final Contadores contadores;
    private final Random random;
    private final Entorno entorno;
    private final List<Enemigo> enemigos;
    private final Plataforma[] plataformas;
    private final List<Gnomo> gnomos;
    private final List<BolaFuego> bolasFuego;
    private int temporizadorEnemigo;
    private int temporizadorGnomo;
    private Jugador jugador;

    private Juego() {
        this.contadores = new Contadores();
        this.random = new Random();
        this.bolasFuego = new ArrayList<>();
        this.temporizadorEnemigo = 0;
        this.temporizadorGnomo = 0;
        this.gnomos = new ArrayList<>();
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

        EstadoJuego estadoJuego = contadores.estadoJuego();
        if (estadoJuego == EstadoJuego.GANADO) {
            entorno.cambiarFont("serif", 32, Color.GREEN);
            entorno.escribirTexto("VICTORIA", 300, 300);
            entorno.cambiarFont("serif", 16, Color.GREEN);
            entorno.escribirTexto("Haz salvado a todos los gnomos.", 250, 350);
            return;
        } else if (estadoJuego == EstadoJuego.PERDIDO) {
            entorno.cambiarFont("serif", 32, Color.RED);
            entorno.escribirTexto("DERROTA", 300, 300);
            entorno.cambiarFont("serif", 16, Color.RED);
            entorno.escribirTexto("Haz perdido demasiados gnomos.", 250, 350);
            return;
        }

        if (temporizadorEnemigo == 0) {
            int lado = random.nextInt(2);
            int spawn;
            if (lado == 0) {
                spawn = random.nextInt(350);
            } else {
                spawn = random.nextInt(350) + 450;
            }
            enemigos.add(new Enemigo(spawn, 0));
            temporizadorEnemigo = 1000;
        } else {
            temporizadorEnemigo -= 1;
        }

        if (temporizadorGnomo <= 0 && gnomos.size() < 4) {
            this.gnomos.add(new Gnomo(400, 50));
            temporizadorGnomo = 1000;
        } else if (temporizadorGnomo > 0) {
            temporizadorGnomo -= 1;
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

        jugador.recargarBolaFuego();
        boolean disparando = this.entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO) || this.entorno.sePresiono('c');
        if (disparando && jugador.puedeDisparar()) {
            bolasFuego.add(jugador.disparar());
        }

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

        ListIterator<BolaFuego> bolasFuegoIterator = bolasFuego.listIterator();
        while (bolasFuegoIterator.hasNext()) {
            BolaFuego bolaFuego = bolasFuegoIterator.next();
            bolaFuego.mover();

            if (bolaFuego.x() > 850 || bolaFuego.x() < -50) {
                bolasFuegoIterator.remove();
                continue;
            }

            boolean explotaContraPlataforma = false;
            for (Plataforma plataforma : plataformas) {
                if (bolaFuego.colisionaCon(plataforma)) {
                    bolasFuegoIterator.remove();
                    explotaContraPlataforma = true;
                }
            }

            if (explotaContraPlataforma) continue;

            ListIterator<Enemigo> enemigosIterator = enemigos.listIterator();
            while (enemigosIterator.hasNext()) {
                Enemigo enemigo = enemigosIterator.next();
                if (bolaFuego.colisionaCon(enemigo)) {
                    contadores.agregarEnemigoEliminado();
                    bolasFuegoIterator.remove();
                    enemigosIterator.remove();
                }
            }
        }

        ListIterator<Gnomo> gnomosIterator = this.gnomos.listIterator();
        while (gnomosIterator.hasNext()) {
            Gnomo gnomo = gnomosIterator.next();
            gnomo.mover();

            if (gnomo.bordeAlto() > 600) {
                gnomosIterator.remove();
                contadores.agregarGnomoPerdido();
                continue;
            }

            if (!gnomo.estaAterrizado()) {
                for (Plataforma plataforma : this.plataformas) {
                    if (gnomo.colisionaCon(plataforma)) {
                        Direccion[] direcciones = Direccion.values();
                        Direccion direccion = direcciones[random.nextInt(direcciones.length)];
                        gnomo.aterrizar(plataforma, direccion);
                        break;
                    }
                }
            }

            if (gnomo.colisionaCon(jugador)) {
                contadores.agregarGnomoSalvado();
                gnomosIterator.remove();
                break;
            }

            for (Enemigo enemigo : enemigos) {
                if (gnomo.colisionaCon(enemigo)) {
                    contadores.agregarGnomoPerdido();
                    gnomosIterator.remove();
                    break;
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
        for (Gnomo gnomo : this.gnomos) gnomo.dibujar(this.entorno);
        for (BolaFuego bolaFuego: this.bolasFuego) bolaFuego.dibujar(entorno);
        this.jugador.dibujar(this.entorno);
        this.contadores.dibujar(entorno);
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