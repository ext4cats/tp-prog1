package juego;

import entorno.Entorno;
import entorno.InterfaceJuego;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * Inicializa y define la lógica global del juego.
 */
public class Juego extends InterfaceJuego {
    /**
     * Los contadores globales.
     */
    private final Contadores contadores;

    /**
     * Proveedor de números aleatorios.
     */
    private final Random random;

    /**
     * El entorno a cual dibujar los objetos.
     */
    private final Entorno entorno;

    /**
     * Lista de enemigos en el nivel.
     */
    private final List<Enemigo> enemigos;

    /**
     * Lista de plataformas en el nivel.
     */
    private final Plataforma[] plataformas;

    /**
     * Lista de gnomos en el nivel.
     */
    private final List<Gnomo> gnomos;

    /**
     * Lista de bolas de fuego en el nivel.
     */
    private final List<BolaFuego> bolasFuego;

    /**
     * Ticks necesarios antes de generar un nuevo enemigo.
     */
    private int temporizadorEnemigo;

    /**
     * Ticks necesarios antes de generar un nuevo gnomo.
     */
    private int temporizadorGnomo;

    /**
     * El jugador.
     */
    private Jugador jugador;

    /**
     * Inicializa el estado del juego.
     */
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

    /**
     * Comienza el juego.
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        Juego juego = new Juego();
        juego.iniciar();
    }

    /**
     * Actualiza el estado del juego cada tick.
     */
    @Override
    public void tick() {
        // Si el jugador es nulo, mostrar pantalla game over.
        if (jugador == null) {
            entorno.cambiarFont("serif", 32, Color.RED);
            entorno.escribirTexto("GAME OVER", 300, 300);
            return;
        }

        // Marcar jugador como nulo si cae al precipicio.
        if (jugador.bordeAlto() > 600) {
            jugador = null;
            return;
        }

        // Mostramos las pantallas de victoria o derrota, si se ha llegado a uno de los límites.
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
            // Si el temporizador está en cero, decidimos donde aparecerá el nuevo enemigo.
            // Nos aseguramos que no sea en la plataforma central.
            int lado = random.nextInt(2);
            int spawn;
            if (lado == 0) {
                spawn = random.nextInt(350);
            } else {
                spawn = random.nextInt(350) + 450;
            }

            // Agregamos el enemigo y reseteamos el contador.
            enemigos.add(new Enemigo(spawn, 0));
            temporizadorEnemigo = 650;
        } else {
            temporizadorEnemigo -= 1;
        }

        if (temporizadorGnomo <= 0 && gnomos.size() < 4) {
            // Agregamos un gnomo y reseteamos el contador, si el contador está en cero,
            // y hay menos de tres gnomos en el nivel.
            this.gnomos.add(new Gnomo(400, 50));
            temporizadorGnomo = 400;
        } else if (temporizadorGnomo > 0) {
            temporizadorGnomo -= 1;
        }

        // Actualizamos el estado del jugador, basado en las teclas de movimiento cual ha presionado el usuario.
        boolean moviendoIzquierda = this.entorno.estaPresionada(entorno.TECLA_IZQUIERDA) || this.entorno.estaPresionada('a');
        boolean moviendoDerecha = this.entorno.estaPresionada(entorno.TECLA_DERECHA) || this.entorno.estaPresionada('d');
        boolean saltando = this.entorno.sePresiono(entorno.TECLA_ARRIBA) || this.entorno.sePresiono('w');
        jugador.actualizar(moviendoIzquierda, moviendoDerecha, saltando);

        // Resolvemos las colisiones entre el jugador y las plataformas.
        jugador.resolverColisiones(plataformas);

        // Agregamos una nueva bola de fuego si el usuario ha decidido disparar,
        // y si el juego permite disparar una bola de fuego.
        boolean disparando = this.entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO) || this.entorno.sePresiono('c');
        if (disparando && jugador.puedeDisparar()) {
            bolasFuego.add(jugador.disparar());
        }

        // Iteramos sobre las bolas de fuego, calculando sus interacciones.
        ListIterator<BolaFuego> bolasFuegoIterator = bolasFuego.listIterator();
        while (bolasFuegoIterator.hasNext()) {
            BolaFuego bolaFuego = bolasFuegoIterator.next();

            // Movemos la bola de fuego.
            bolaFuego.mover();

            // Si la bola de fuego está fuera de la pantalla, la removemos.
            if (bolaFuego.x() > 850 || bolaFuego.x() < -50) {
                bolasFuegoIterator.remove();
                continue;
            }

            // Si la bola de fuego choca contra una plataforma, la removemos.
            boolean explotaContraPlataforma = false;
            for (Plataforma plataforma : plataformas) {
                if (bolaFuego.colisionaCon(plataforma)) {
                    bolasFuegoIterator.remove();
                    explotaContraPlataforma = true;
                }
            }
            if (explotaContraPlataforma) continue;

            // Iteramos todos los enemigos para chequear si golpea a uno.
            ListIterator<Enemigo> enemigosIterator = enemigos.listIterator();
            while (enemigosIterator.hasNext()) {
                Enemigo enemigo = enemigosIterator.next();

                // Si golpeamos un enemigo, removemos la bola de fuego y el enemigo,
                // y también aumentamos el contador de enemigos eliminados.
                if (bolaFuego.colisionaCon(enemigo)) {
                    contadores.agregarEnemigoEliminado();
                    bolasFuegoIterator.remove();
                    enemigosIterator.remove();
                }
            }
        }

        // Iteramos sobre los gnomos, calculando sus interacciones.
        ListIterator<Gnomo> gnomosIterator = this.gnomos.listIterator();
        while (gnomosIterator.hasNext()) {
            Gnomo gnomo = gnomosIterator.next();

            // Movemos al gnomo.
            gnomo.mover();

            // Si el gnomo cae al precipicio, removerlo.
            if (gnomo.bordeAlto() > 600) {
                gnomosIterator.remove();
                contadores.agregarGnomoPerdido();
                continue;
            }

            // Mientras tanto el gnomo no esté aterrizado, chequeamos si el gnomo aterriza en una plataforma.
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

            // Si el gnomo colisiona con el jugador, y se encuentra en las filas bajas,
            // lo removemos y agregamos un gnomo salvado.
            if (gnomo.colisionaCon(jugador) && gnomo.bordeBajo() > 350) {
                contadores.agregarGnomoSalvado();
                gnomosIterator.remove();
                break;
            }

            // Chequeamos si el gnomo colisiona con cualquier enemigo.
            // Si hay colisión, contamos el gnomo como perdido.
            for (Enemigo enemigo : enemigos) {
                if (gnomo.colisionaCon(enemigo)) {
                    contadores.agregarGnomoPerdido();
                    gnomosIterator.remove();
                    break;
                }
            }
        }

        // Iteramos sobre los enemigos, calculando sus interacciones.
        for (Enemigo enemigo : this.enemigos) {
            // Movemos al enemigo
            enemigo.mover();

            // Si el enemigo colisiona con el jugador, removemos el jugador.
            // Esto implica un game over.
            if (enemigo.colisionaCon(jugador)) {
                jugador = null;
                return;
            }

            // Si el enemigo no está aterrizado, chequeamos colisiones para ver si aterriza.
            if (!enemigo.estaAterrizado()) {
                for (Plataforma plataforma : this.plataformas) {
                    if (enemigo.colisionaCon(plataforma)) {
                        enemigo.aterrizar(plataforma);
                    }
                }
            }
        }

        // Dibujamos los objetos a la pantalla.
        this.dibujarObjetos();
    }

    /**
     * Dibuja todos los objetos a la pantalla.
     */
    private void dibujarObjetos() {
        entorno.dibujarRectangulo(400, 350, 800, 2, 0, Color.DARK_GRAY);
        for (Plataforma plataforma : this.plataformas) plataforma.dibujar(this.entorno);
        for (Enemigo enemigo : this.enemigos) enemigo.dibujar(this.entorno);
        for (Gnomo gnomo : this.gnomos) gnomo.dibujar(this.entorno);
        for (BolaFuego bolaFuego: this.bolasFuego) bolaFuego.dibujar(entorno);
        this.jugador.dibujar(this.entorno);
        this.contadores.dibujar(entorno);
    }

    /**
     * Genera las plataformas para plantar en el nivel.
     * @return Las plataformas generadas.
     */
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

    // Inicia el juego.
    private void iniciar() {
        this.entorno.iniciar();
    }
}