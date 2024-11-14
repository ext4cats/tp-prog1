package juego;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
    private final Entorno entorno;
    private final Plataforma[] plataformas;

    private Juego() {
        this.entorno = new Entorno(this, "Al Rescate de los Gnomos", 800, 600);
        this.plataformas = this.generarPlataformas();
    }

    public static void main(String[] args) {
        Juego juego = new Juego();
        juego.iniciar();
    }

    @Override
    public void tick() {
        this.dibujarObjetos();
    }

    private void dibujarObjetos() {
        for (Plataforma plataforma : plataformas) plataforma.dibujar(this.entorno);
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