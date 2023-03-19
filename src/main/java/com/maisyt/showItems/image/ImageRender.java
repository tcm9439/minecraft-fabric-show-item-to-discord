package com.maisyt.showItems.image;

import java.awt.*;
import java.awt.image.BufferedImage;

// ref: net.minecraft.client.gui.screen.ingame.InventoryScreen
public abstract class ImageRender<T> {
    protected BufferedImage renderedImage;
    protected Graphics2D g2d;

    abstract BufferedImage render();

    public BufferedImage getFinalRenderedImage() {
        g2d.dispose();
        return renderedImage;
    }

    BufferedImage createBackgroundImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        return null;
    }
}
