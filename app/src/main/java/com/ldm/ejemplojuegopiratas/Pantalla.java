package com.ldm.ejemplojuegopiratas;

public abstract class Pantalla {
    protected final Juego juego;
    // Recibe la instancia juego y la almacena en un miembro final, accesible desde todas las subclases.
    public Pantalla (Juego juego) {
        this.juego = juego;
    }
    // Define la transición de una pantalla a otra basándose en su estado.
    // Llamados constantemente desde el loop principal, cada vez que haya una iteración del mismo.
    public abstract void update(float deltaTime);

    public abstract void present(float deltaTime);
    // Llamados cuando el juego sea pausado o reanudado.
    public abstract void pause();

    public abstract void resume();
    // Indica que se va a configurar una nueva
    //pantalla, libera recursos y guarda lo que esté en pantalla, para recuperarlo después
    public abstract void dispose();
}

