package com.ldm.ejemplojuegopiratas.juego;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.ldm.ejemplojuegopiratas.FileIO;

public class Configuraciones {
    public static boolean sonidoHabilitado = true; // Determina si los efectos de sonido están habilitados.
    public static int[] maxPuntuaciones = new int[] { 100, 80, 50, 30, 10 }; // Almacenan las puntucaciones más altas en un array de cinco elementos, ordenados del más alto al menor.

    public static void cargar(FileIO files) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    files.leerArchivo(".piratas")));
            sonidoHabilitado = Boolean.parseBoolean(in.readLine());
            for (int i = 0; i < 5; i++) {
                maxPuntuaciones[i] = Integer.parseInt(in.readLine());
            }
        } catch (IOException e) {
            // :( Está bien aquí debería ir algo
        } catch (NumberFormatException e) {
            // :/ Nadie es perfecto
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    } // Carga las configuraciones del archivo piratas que se encuentra en almacenamiento externo, mediante una instancia de FileIO que se le pasa al método.

    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    files.escribirArchivo(".piratas")));
            out.write(Boolean.toString(sonidoHabilitado));
            out.write("\n");
            for (int i = 0; i < 5; i++) {
                out.write(Integer.toString(maxPuntuaciones[i]));
                out.write("\n");
            }

        } catch (IOException e) {
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
        }
    } // Guarda las configuraciones que haya en pantalla y las serializa dentro del archivo piratas en el almacenamiento interno.

    public static void addScore(int score) {
        for (int i = 0; i < 5; i++) {
            if (maxPuntuaciones[i] < score) {
                for (int j = 4; j > i; j--)
                    maxPuntuaciones[j] = maxPuntuaciones[j - 1];
                maxPuntuaciones[i] = score;
                break;
            }
        }// Añade una nueva puntuación dentro de máximas puntuaciones y las reordena automáticamente dependiendo del valor insertado, para que sigan apareciendo en orden de mayor a menor.
    }
}