package com.ldm.ejemplojuegopiratas.androidimpl;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

import com.ldm.ejemplojuegopiratas.Audio;
import com.ldm.ejemplojuegopiratas.FileIO;
import com.ldm.ejemplojuegopiratas.Juego;
import com.ldm.ejemplojuegopiratas.Graficos;
import com.ldm.ejemplojuegopiratas.Input;
import com.ldm.ejemplojuegopiratas.Pantalla;
import com.ldm.ejemplojuegopiratas.juego.R;

public abstract class AndroidJuego extends Activity implements Juego { //activity
    AndroidFastRenderView renderView; //mantener el loop principal
    Graficos graficos; //instancias
    Audio audio;
    Input input;
    FileIO fileIO;
    Pantalla pantalla; // pantalla activa en cada momento
    WakeLock wakeLock; //

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // Hace que la activity se muestre en pantalla completa

        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth = isLandscape ? 480 : 320;
        int frameBufferHeight = isLandscape ? 320 : 480;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Config.RGB_565); //pantalla a 420*320

        float scaleX = (float) frameBufferWidth
                / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) frameBufferHeight
                / getWindowManager().getDefaultDisplay().getHeight();  //Transforma las coordenadas del sistema fijo de coordenadas adaptadas al de la pantalla del dispositivo

        renderView = new AndroidFastRenderView(this, frameBuffer);
        graficos = new AndroidGraficos(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(getAssets());
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY); // Instancia los argumentos del constructor necesarios en cada caso.
        pantalla = getStartScreen(); // implementa el comienzo del juego
        setContentView(renderView);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "GLGame"); // Informa a la pantalla actual de que la Activity acaba de ser reanudada.
    }

    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        pantalla.resume();
        renderView.resume(); // fastRenderView reanuda el hilo de renderización que a su vez reanudará el hilo principal de nuestro juego.
    }

    @Override
    public void onPause() {
        super.onPause();
        wakeLock.release();
        renderView.pause();
        pantalla.pause();  // Evita problemas de concurrencia si el hilo de la IU y el hilo del loop principal acceden a la pantalla al mismo tiempo.

        if (isFinishing())
            pantalla.dispose(); // Informa en caso de que la pantalla vaya a ser destruida.
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graficos getGraphics() {
        return graficos;
    }

    @Override
    public Audio getAudio() {
        return audio;
    } // llama a sus respectivas implementacinoes

    @Override
    public void setScreen(Pantalla pantalla) {
        if (pantalla == null)
            throw new IllegalArgumentException("Pantalla no debe ser null");

        this.pantalla.pause();
        this.pantalla.dispose(); // Hace sitio para la nueva pantalla.
        pantalla.resume();
        pantalla.update(0);
        this.pantalla = pantalla; // Le dice a la pantalla que se reanude y actualice a ella misma con un delta time 0 la primera vez.
    }

    public Pantalla getCurrentScreen() {
        return pantalla;
    } // Devuelve la pantalla que está activa en cualquier momento.
}