package com.ldm.ejemplojuegopiratas.juego;

import java.util.List;
import android.graphics.Color;
import android.util.Log;

import com.ldm.ejemplojuegopiratas.Juego;
import com.ldm.ejemplojuegopiratas.Graficos;
import com.ldm.ejemplojuegopiratas.Input.TouchEvent;
import com.ldm.ejemplojuegopiratas.Pixmap;
import com.ldm.ejemplojuegopiratas.Pantalla;

public class PantallaJuego extends Pantalla {
    // Codifica los cuatro estados posibles del juego.
    enum EstadoJuego {
        Preparado,
        Ejecutandose,
        Pausado,
        FinJuego
    }
    // Contiene el estado actual de la pantalla.
    // Contiene la instancia del mundo y otros dos miembros más que contienen
    // la puntuación como un integer y como un string (antiguaPuntuacion) para
    // llevar a cabo operaciones cuando haya que aumentar la puntuación
    EstadoJuego estado = EstadoJuego.Preparado;
    Mundo mundo;
    int antiguaPuntuacion = 0;
    String puntuacion = "0";


    //VARIABLES adicionales
    float x1 = 0,x2 = 0,y1=0,y2=0, deltaX=0,deltaY=0;
    final int MIN_DISTANCE = 50;
    ///VARIABLES

    public PantallaJuego(Juego juego) {
        super(juego);
        mundo = new Mundo();
    }

    // Obtiene los eventos TouchEvent y KeyEvent que tengan lugar.
    // Lo podemos hacer desde el módulo Input y luego delega la actualización en uno de los cuatro métodos
    // update().
    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = juego.getInput().getTouchEvents();
        juego.getInput().getKeyEvents();

        if(estado == EstadoJuego.Preparado)
            updateReady(touchEvents);
        if(estado == EstadoJuego.Ejecutandose)
            updateRunning(touchEvents, deltaTime);
        if(estado == EstadoJuego.Pausado)
            updatePaused(touchEvents);
        if(estado == EstadoJuego.FinJuego)
            updateGameOver(touchEvents);

    }
    // Comprueba si se ha pulsado sobre la pantalla y si es así cambia al estado ejecutándose updaterunning.
    private void updateReady(List<TouchEvent> touchEvents) {
        if(touchEvents.size() > 0)
            estado = EstadoJuego.Ejecutandose;
    }
    // Comprueba si se ha pulsado el botón Pausa y cambia a estado Pausado.
    // Luego comprueba si uno de los dos botones del controlador ha sido pulsado y si es así, se comprueba el TouchDown.
    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
        int len = touchEvents.size();


        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x < 64 && event.y < 64) {
                    if(Configuraciones.sonidoHabilitado)
                        Assets.pulsar.play(1);
                    estado = EstadoJuego.Pausado;
                    return;
                }
                x2 = event.x;
                y2 = event.y;
                deltaX = x2 - x1;
                deltaY = y2 - y1;
                Log.i("MOLUXO","x2: "+x2+" y2: "+y2);
                Log.i("MOLUXO","deltaX: "+deltaX+" deltaY: "+deltaY);
                if(Math.abs(deltaX)>=Math.abs(deltaY)){
                    if(Math.abs(deltaX)>MIN_DISTANCE){
                        //eventos de izq o derecha
                        if(deltaX>=0){
                            Log.i("MOLUXO","Derecha");
                            mundo.jollyroger.paDerecha();
                        }else{
                            Log.i("MOLUXO","Izq");
                            mundo.jollyroger.paIzquierda();
                        }
                    }

                }else{
                    if(Math.abs(deltaY)>MIN_DISTANCE){
                        if(deltaY>=0){
                            Log.i("MOLUXO","Abajo");
                            mundo.jollyroger.paAbajo();
                        }else{
                            Log.i("MOLUXO","Arriba");
                            mundo.jollyroger.paArriba();
                        }
                    }

                }

            }
            if(event.type == TouchEvent.TOUCH_DOWN) {
                if(event.x < 64 && event.y > 416) {
                    mundo.jollyroger.girarIzquierda();
                }
                if(event.x > 256 && event.y > 416) {
                    mundo.jollyroger.girarDerecha();
                }
                //si tiene
                x1 = event.x;
                y1 = event.y;
                Log.i("MOLUXO","x1: "+x1+" y1: "+y1);
            }

        }
        // Actualiza el deltaTime Si el juego ha finalizado cambia el estado a FinJuego y ejecuta el sonido derrota.ogg.
        mundo.update(deltaTime);
        if(mundo.finalJuego) {
            if(Configuraciones.sonidoHabilitado)
                Assets.derrota.play(1);
            estado = EstadoJuego.FinJuego;
        }
        // Comprueba si la puntuación antigua es diferente a la que almacena el mundo.
        // Si es así JollyRoger ha capturado un nuevo botín, cambia la puntuación y suena el ataque.ogg.
        if(antiguaPuntuacion != mundo.puntuacion) {
            antiguaPuntuacion = mundo.puntuacion;
            puntuacion = "" + antiguaPuntuacion;
            if(Configuraciones.sonidoHabilitado)
                Assets.ataque.play(1);
        }
    }

    // Comprueba si se ha pulsado sobre alguno de ellos y lleva al estado concreto
    // que se le indique y hace que suene el sonido de cuando se pulsa un botón.
    private void updatePaused(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x > 80 && event.x <= 240) {
                    if(event.y > 100 && event.y <= 148) {
                        if(Configuraciones.sonidoHabilitado)
                            Assets.pulsar.play(1);
                        estado = EstadoJuego.Ejecutandose;
                        return;
                    }
                    if(event.y > 148 && event.y < 196) {
                        if(Configuraciones.sonidoHabilitado)
                            Assets.pulsar.play(1);
                        juego.setScreen(new MainMenuScreen(juego));
                        return;
                    }
                }
            }
        }
    }
    // Comprueba si el botón que se encuentra en medio de la pantalla con una X ha sido pulsado
    // y si es así inicia el proceso de transición a la pantalla del menú principal de nuevo.
    private void updateGameOver(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x >= 128 && event.x <= 192 &&
                        event.y >= 200 && event.y <= 264) {
                    if(Configuraciones.sonidoHabilitado)
                        Assets.pulsar.play(1);
                    juego.setScreen(new MainMenuScreen(juego));
                    return;
                }
            }
        }
    }

    // Renderiza el contenido de la
    //pantalla. Empieza dibujando el fondo y prepara el dibujo
    // para cada uno de los cuatro estados del juego, utilizando drawReadyUI().drawPaused UI y drawGameOver()
    @Override
    public void present(float deltaTime) {
        Graficos g = juego.getGraphics();

        g.drawPixmap(Assets.fondo, 0, 0);
        drawWorld(mundo);
        if(estado == EstadoJuego.Preparado)
            drawReadyUI();
        if(estado == EstadoJuego.Ejecutandose)
            drawRunningUI();
        if(estado == EstadoJuego.Pausado)
            drawPausedUI();
        if(estado == EstadoJuego.FinJuego)
            drawGameOverUI();


        drawText(g, puntuacion, g.getWidth() / 2 - puntuacion.length()*20 / 2, g.getHeight() - 42);
    }

    private void drawWorld(Mundo mundo) {
        Graficos g = juego.getGraphics();
        JollyRoger jollyroger = mundo.jollyroger;
        Tripulacion head = jollyroger.partes.get(0);
        Botin botin = mundo.botin;

        // Muestra la imagen del botín correspondiente y también la tripulación.
        Pixmap stainPixmap = null;
        if(botin.tipo== Botin.TIPO_1)
            stainPixmap = Assets.botin1;
        if(botin.tipo == Botin.TIPO_2)
            stainPixmap = Assets.botin2;
        if(botin.tipo == Botin.TIPO_3)
            stainPixmap = Assets.botin3;
        int x = botin.x * 32;
        int y = botin.y * 32;
        g.drawPixmap(stainPixmap, x, y);

        int len = jollyroger.partes.size();
        for(int i = 1; i < len; i++) {
            Tripulacion part = jollyroger.partes.get(i);
            x = part.x * 32;
            y = part.y * 32;
            g.drawPixmap(Assets.tripulacion, x, y);
        }
        // Comprueba la dirección del barco y muestra la imagen correspondiente.
        Pixmap headPixmap = null;
        if(jollyroger.direccion == JollyRoger.ARRIBA)
            headPixmap = Assets.barcoarriba;
        if(jollyroger.direccion == JollyRoger.IZQUIERDA)
            headPixmap = Assets.barcoizquierda;
        if(jollyroger.direccion == JollyRoger.ABAJO)
            headPixmap = Assets.barcoabajo;
        if(jollyroger.direccion == JollyRoger.DERECHA)
            headPixmap = Assets.barcoderecha;
        x = head.x * 32 + 16;
        y = head.y * 32 + 16;
        g.drawPixmap(headPixmap, x - headPixmap.getWidth() / 2, y - headPixmap.getHeight() / 2);
    }
    // Dibuja las imágenes de las pantallas para los diferentes estados del juego
    private void drawReadyUI() {
        Graficos g = juego.getGraphics();

        g.drawPixmap(Assets.preparado, 47, 100);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }
    // Dibuja las puntuaciones.
    private void drawRunningUI() {
        Graficos g = juego.getGraphics();

        g.drawPixmap(Assets.botones, 0, 0, 64, 128, 64, 64);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
        g.drawPixmap(Assets.botones, 0, 416, 64, 64, 64, 64);
        g.drawPixmap(Assets.botones, 256, 416, 0, 64, 64, 64);
    }

    private void drawPausedUI() {
        Graficos g = juego.getGraphics();

        g.drawPixmap(Assets.menupausa, 80, 100);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }

    private void drawGameOverUI() {
        Graficos g = juego.getGraphics();

        g.drawPixmap(Assets.finjuego, 62, 100);
        g.drawPixmap(Assets.botones, 128, 200, 0, 128, 64, 64);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }

    public void drawText(Graficos g, String line, int x, int y) {
        int len = line.length();
        for (int i = 0; i < len; i++) {
            char character = line.charAt(i);

            if (character == ' ') {
                x += 20;
                continue;
            }

            int srcX = 0;
            int srcWidth = 0;
            if (character == '.') {
                srcX = 200;
                srcWidth = 10;
            } else {
                srcX = (character - '0') * 20;
                srcWidth = 20;
            }

            g.drawPixmap(Assets.numeros, x, y, srcX, 0, srcWidth, 32);
            x += srcWidth;
        }
    }
    // Llamado cuando la Activity es
    //pausada o la pantalla del juego es reemplazada por otra pantalla.
    // Es el lugar perfecto para salvar configuraciones. Comprueba si el juego ha finalizado y si es así,
    // añade la puntuación a las máximas puntuaciones, siempre que supere a las que ya se encuentran allí
    // y las salva al almacenamiento externo.
    @Override
    public void pause() {
        if(estado == EstadoJuego.Ejecutandose)
            estado = EstadoJuego.Pausado;

        if(mundo.finalJuego) {
            Configuraciones.addScore(mundo.puntuacion);
            Configuraciones.save(juego.getFileIO());
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}