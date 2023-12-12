package com.ldm.ejemplojuegopiratas.juego;
import java.util.List;
import com.ldm.ejemplojuegopiratas.Juego;
import com.ldm.ejemplojuegopiratas.Graficos;
import com.ldm.ejemplojuegopiratas.Input.TouchEvent;
import com.ldm.ejemplojuegopiratas.Pantalla;

public class MainMenuScreen extends Pantalla {
    public MainMenuScreen(Juego juego) {
        super(juego);
    }

    @Override
    public void update(float deltaTime) {
        Graficos g = juego.getGraphics();
        List<TouchEvent> touchEvents = juego.getInput().getTouchEvents();
        juego.getInput().getKeyEvents(); // Comprueban todos los touchEvents.

        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) { //ORIGINAL TOUCH_UP
                if(inBounds(event, 0, g.getHeight() - 64, 64, 64)) { //sonido habilitado
                    Configuraciones.sonidoHabilitado = !Configuraciones.sonidoHabilitado;
                    if(Configuraciones.sonidoHabilitado)
                        Assets.pulsar.play(1); //esto hace que suene el sonidito ese
                }
                if(inBounds(event, 64, 220, 192, 42) ) {
                    juego.setScreen(new PantallaJuego(juego)); //juego
                    if(Configuraciones.sonidoHabilitado)
                    {Assets.pulsar.play(1);
                    }

                    return;
                }
                if(inBounds(event, 64, 220 + 42, 192, 42) ) {
                    juego.setScreen(new PantallaMaximasPuntuaciones(juego)); //puntutaciones
                    if(Configuraciones.sonidoHabilitado)
                        Assets.pulsar.play(1);
                    return;
                }
                if(inBounds(event, 64, 220 + 84, 192, 42) ) {
                    juego.setScreen(new PantallaAyuda(juego)); //ayuda
                    if(Configuraciones.sonidoHabilitado)
                        Assets.pulsar.play(1);
                    return;
                }
            } // Detecta el evento de toque TOUCH_UP, comprueba dónde se ha producido, en botón de sonido o en una de las entradas del menú.
        }
    }

    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if(event.x > x && event.x < x + width - 1 && //aqui te mide las coordenadas del evento tocado
                event.y > y && event.y < y + height - 1)
            return true;
        else
            return false; // Comprueba las coordenadas del área de toque.
    }

    @Override
    public void present(float deltaTime) {
        // Hace que aparezca la pantalla con su contenido
        Graficos g = juego.getGraphics();

        g.drawPixmap(Assets.fondo, 0, 0); // Borra el contenido del framebuffer.
        g.drawPixmap(Assets.logo, 32, 20);
        g.drawPixmap(Assets.menuprincipal, 64, 220); // Dibuja los logos y el menú principal
        if(Configuraciones.sonidoHabilitado)
            g.drawPixmap(Assets.botones, 0, 416, 0, 0, 64, 64);
        else
            g.drawPixmap(Assets.botones, 0, 416, 64, 0, 64, 64); // Dibuja una porción del botón sonido, dependiendo de si está activado o no.
    }

    @Override
    public void pause() {
        Configuraciones.save(juego.getFileIO());
    } // Guarda la configuración o el estado del sonido, de esta forma nos aseguramos de que ha persistido.

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}

