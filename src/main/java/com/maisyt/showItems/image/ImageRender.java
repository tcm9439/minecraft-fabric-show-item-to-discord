package com.maisyt.showItems.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

// ref: net.minecraft.client.gui.screen.ingame.InventoryScreen
public abstract class ImageRender<T> {
    static public String FONT_NAME = "Courier";
    protected BufferedImage renderedImage;
    protected Graphics2D g2d;

    public abstract BufferedImage render();

    public static InputStream convertToInputStream(BufferedImage image) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(os.toByteArray());
    }

    public BufferedImage getFinalRenderedImage() {
        g2d.dispose();
        return renderedImage;
    }

    BufferedImage createBackgroundImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        return null;
    }

    BufferedImage resizeImage(BufferedImage image, int width, int height) {
        return (BufferedImage) image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
}
