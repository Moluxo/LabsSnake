package com.ldm.ejemplojuegopiratas.juego;

public class Botin {
    public static final int TIPO_1 = 0;
    public static final int TIPO_2 = 1;
    public static final int TIPO_3 = 2; // cosntante que codifican el tipo de botín

    public int x, y; //Coordendas XY que cada botín tendrá asignadas paraq ue el ocupe una velda en el oceano
    public int tipo;

    public Botin(int x, int y, int tipo) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;
    }
}
