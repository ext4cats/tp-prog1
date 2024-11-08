package juego;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
    private final Entorno entorno;
    private final Jugador jugador;
    private final Plataforma[] plataformas;

    private Juego() {
        this.entorno = new Entorno(this, "Al Rescate de los Gnomos", 800, 600);
        this.jugador = new Jugador(400, 0);
        this.plataformas = this.generarPlataformas();
    }

    public static void main(String[] args) {
        Juego juego = new Juego();
        juego.iniciar();
    }

    @Override
    public void tick() {
        boolean moviendoDerecha = entorno.estaPresionada(entorno.TECLA_DERECHA) || entorno.estaPresionada('d');
        boolean moviendoIzquierda = entorno.estaPresionada(entorno.TECLA_IZQUIERDA) || entorno.estaPresionada('a');
        boolean saltando = entorno.sePresiono(entorno.TECLA_ARRIBA) || entorno.estaPresionada('w');
        if (moviendoDerecha) jugador.moverDerecha();
        if (moviendoIzquierda) jugador.moverIzquierda();
        if (saltando) jugador.saltar();
        jugador.actualizar();
        if (jugador.y() >= 255) jugador.aterrizar();
        if (jugador.x() >= entorno.ancho() + 30) jugador.teletransportar(-29, jugador.y());
        if (jugador.x() <= -30) jugador.teletransportar(entorno.ancho() + 29, jugador.y());
        this.dibujarObjetos();
    }

    private void dibujarObjetos() {
        for (Plataforma plataforma : this.plataformas) plataforma.dibujar(this.entorno);
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