package com.ldm.ejemplojuegopiratas.juego;

import java.util.ArrayList;
import java.util.List;

public class JollyRoger {
    /*
     * Si el valor de la dirección se va fuera del rango, si está por debajo de ARRIBA = 0,
     * entonces pasamos a DERECHA, si está por encima de DERECHA por tanto por encima de 3,
     * pasamos ARRIBA que vuelve a ser 0. Con esto nos aseguramos los cuatro giros posibles.
     * */
    public static final int ARRIBA = 0;
    public static final int IZQUIERDA= 1;
    public static final int ABAJO = 2;
    public static final int DERECHA = 3;


    // Tripulacion contiene todas las partes de JollyRoger.
    public List<Tripulacion> partes = new ArrayList<Tripulacion>();
    // Contiene la dirección en la que el barco se está moviendo en un determinado momento.
    public int direccion;

    // En dirección ARRIBA el barco y su tripulación avanzará hacia arriba una celda la próxima vez que se le pida que avance.
    public JollyRoger() {
        direccion = ARRIBA;
        partes.add(new Tripulacion(5, 6));
        partes.add(new Tripulacion(5, 7));
        partes.add(new Tripulacion(5, 8));
    }

    // FUNCIONES NUEVAS!"
    public void paArriba(){
        direccion = ARRIBA;
    }

    public void paAbajo(){
        direccion = ABAJO;
    }
    public void paIzquierda(){
        direccion = IZQUIERDA;
    }
    public void paDerecha(){
        direccion = DERECHA;
    }
    // FUNCIONES NUEVAS!

    // Modifican la dirección si el barco gira a la izquierda (+1) o si lo hace hacia la derecha (-1).
    public void girarIzquierda() {
        direccion += 1;
        if(direccion > DERECHA)
            direccion = ARRIBA;
    }

    // Añade un nuevo tripulante al final de la lista y esta nueva parte
    // tendrá siempre la misma posición que la última parte que lo era hasta el momento.
    public void girarDerecha() {
        direccion -= 1;
        if(direccion < ARRIBA)
            direccion = DERECHA;
    }

    public void abordaje() {
        Tripulacion end = partes.get(partes.size()-1); //recibe las coordenadas edel último
        partes.add(new Tripulacion(end.x, end.y));
    }

    // Hace que se mueva JollyRoger
    public void avance() {
        Tripulacion barco = partes.get(0);

        /*
        * Primero se mueve cada parte a la posición de la anterior y siempre empeza por la última parte.
        * Excluye el barco que es la primera de las partes, por eso se utiliza el tamaño -1.
        * */
        int len = partes.size() - 1;

        /*Crea la tripulación antes y tripulación parte y va colocando cada
        una de las partes en el sitio que estaba la anterior, llamada y./
         */
        for(int i = len; i > 0; i--) {
            Tripulacion antes = partes.get(i-1);
            Tripulacion parte = partes.get(i);
            parte.x = antes.x;
            parte.y = antes.y;
        }
        /*Mueve el barco, de acuerdo a la dirección actual que tenga.
        * */
        if(direccion == ARRIBA)
            barco.y -= 1;
        if(direccion == IZQUIERDA)
            barco.x -= 1;
        if(direccion == ABAJO)
            barco.y += 1;
        if(direccion == DERECHA)
            barco.x += 1;

        /*Comprueba que el barco no se sale de los límites del mundo. Si es así,
        hace que aparezca por el lado contrario, con la idea de mundo redondo.
        * */
        if(barco.x < 0)
            barco.x = 9;
        if(barco.x > 9)
            barco.x = 0;
        if(barco.y < 0)
            barco.y = 12;
        if(barco.y > 12)
            barco.y = 0;
    }

    /*Comprueba si el barco ha golpeado a alguno de sus tripulantes y verifica que no haya un tripulante
    (la línea que va antes del return true), en la misma posición que el barco.
    Si es así, sabremos que ha habido un choque y con esto finalizará el juego.
    * */
    public boolean comprobarChoque() {
        int len = partes.size();
        Tripulacion barco = partes.get(0);
        for(int i = 1; i < len; i++) {
            Tripulacion parte = partes.get(i);
            if(parte.x == barco.x && parte.y == barco.y)
                return true;
        }
        return false;
    }
}

