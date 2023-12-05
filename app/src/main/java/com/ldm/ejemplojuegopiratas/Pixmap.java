package com.ldm.ejemplojuegopiratas;

import com.ldm.ejemplojuegopiratas.Graficos.PixmapFormat;

public interface Pixmap {
    public int getWidth();

    public int getHeight();

    public PixmapFormat getFormat();

    public void dispose();
}

