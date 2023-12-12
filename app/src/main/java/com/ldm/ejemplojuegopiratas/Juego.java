package com.ldm.ejemplojuegopiratas;

public interface Juego {
    // A través de los métodos
    //get() se accede a los módulos de bajo nivel (instancias) y se les hace seguimiento.
    public Input getInput();

    public FileIO getFileIO();

    public Graficos getGraphics();

    public Audio getAudio();

    // Permite configurar cuál es la pantalla actual del juego.
    public void setScreen(Pantalla pantalla);

    public Pantalla getCurrentScreen();

    public Pantalla getStartScreen();
}
