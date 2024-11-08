package juego;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
    private final Entorno entorno;

    private Juego() {
        this.entorno = new Entorno(this, "Al Rescate de los Gnomos", 800, 600);
    }

    public static void main(String[] args) {
        Juego juego = new Juego();
        juego.iniciar();
    }

    private void iniciar() {
        this.entorno.iniciar();
    }
}