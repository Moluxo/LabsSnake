package com.ldm.ejemplojuegopiratas.juego;

import java.util.Random;

public class Mundo {
    // Definición de constantes
    static final int MUNDO_ANCHO = 10;
    static final int MUNDO_ALTO = 13;
    static final int INCREMENTO_PUNTUACION = 10;
    static final float TICK_INICIAL = 0.5f;
    static final float TICK_DECREMENTO = 0.05f;

    // Definición de una instancia JollyRoger, una instancia botin,
    // un booleano que almacena si el juego se ha terminado y la puntuación actual.
    public JollyRoger jollyroger;
    public Botin botin;
    public boolean finalJuego = false;
    public int puntuacion = 0;

    // Array 2D para colocar un nuevo tripulante
    boolean campos[][] = new boolean[MUNDO_ANCHO][MUNDO_ALTO];
    // Instancia de la clase Random para producir números aleatorios para colocar el botin y generar su tipo.
    Random random = new Random();
    // Variable acumuladora de tiempo tiempoTick a la cual se le añade el frame deltaTime.
    float tiempoTick = 0;
    // Duración actual de un Tick que define la velocidad a la que tendrá que ir avanzando JollyRoger.
    static float tick = TICK_INICIAL;

    public Mundo() {
        jollyroger = new JollyRoger();
        colocarBotin();
    }
    // Estrategia de colocación del botín.
    private void colocarBotin() {
        for (int x = 0; x < MUNDO_ANCHO; x++) {
            for (int y = 0; y < MUNDO_ALTO; y++) {
                campos[x][y] = false;
            }
        }
        // Limpia todas las celdas, configura todas las celdas ocupadas por partes de JollyRoger,
        // para que aparezcan como true dentro de ese array bidimensional.
        int len = jollyroger.partes.size();
        for (int i = 0; i < len; i++) {
            Tripulacion parte = jollyroger.partes.get(i);
            campos[parte.x][parte.y] = true;  //true es cuando está la cosa esta
        }
        // Terminamos escaneando el array buscando una celda libre, empezando por una posición aleatoria
        int botinX = random.nextInt(MUNDO_ANCHO);
        int botinY = random.nextInt(MUNDO_ALTO);
        while (true) {
            if (campos[botinX][botinY] == false)
                break;
            botinX += 1;
            if (botinX >= MUNDO_ANCHO) {
                botinX = 0;
                botinY += 1;
                if (botinY >= MUNDO_ALTO) {
                    botinY = 0;
                }
            }
        } ///para que no se pete si está la seripiente

        // Creamos un nuevo botín al que le damos un tipo aleatorio de los tres que puede tener.
        botin = new Botin(botinX, botinY, random.nextInt(3));
    }
    // Comprueba si el juego ha finalizado. Si es así no hay que actualizar nada y devuelve return.
    public void update(float deltaTime) {
        if (finalJuego)

            return;
        // Si no es así añadimos deltaTime a nuestro acumulador
        tiempoTick += deltaTime;
        // Sustrae el intervalo tick desde el acumulador 11 12
        //y vuelve a empezar a contar el tiempo desde cero hasta tener un nuevo deltaTime.
        //jollyRoger avanza y comprueba si se ha abordado a sí mismo. Si es así, termina el juego.
        while (tiempoTick > tick) {
            tiempoTick -= tick;
            jollyroger.avance();
            if (jollyroger.comprobarChoque()) {
                finalJuego = true;
                return;
            }
            // Comprueba si jollyRoger está compuesto por tantas celdas como partes hay en el mundo.
            // Si es así, el juego tiene que finalizar y hace un return para volver desde la función.
            Tripulacion head = jollyroger.partes.get(0);
            if (head.x == botin.x && head.y == botin.y) {
                puntuacion += INCREMENTO_PUNTUACION;
                jollyroger.abordaje();
                if (jollyroger.partes.size() == MUNDO_ANCHO * MUNDO_ALTO) {
                    finalJuego = true;
                    return;
                // Si no es así coloca un nuevo botín dentro del mundo.
                } else {
                    colocarBotin();
                }
                //Comprueba si jollyRoger ha abordado 10 botines más. Si es así nuestro
                //umbral se irá reduciendo, mediante tick_decremento al que le dimos un valor de 0,5.
                // El tick será más corto, lo que hará que JollyRoger vaya más rápido.
                if (puntuacion % 100 == 0 && tick - TICK_DECREMENTO > 0) {
                    tick -= TICK_DECREMENTO;
                }
            }
        }
    }
}

