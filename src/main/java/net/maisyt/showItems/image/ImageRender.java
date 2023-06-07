package net.maisyt.showItems.image;

import net.maisyt.showItems.ShowItemsMod;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class ImageRender<T> {
    static public String FONT_NAME = null;
//    static public String FONT_NAME = "Courier";
    protected BufferedImage renderedImage;
    protected Graphics2D g2d;

    public abstract BufferedImage render();

    public static InputStream convertToInputStream(BufferedImage image) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            ShowItemsMod.LOGGER.error("Failed to convert BufferedImage to InputStream", e);
        }
        return new ByteArrayInputStream(os.toByteArray());
    }

    protected BufferedImage getFinalRenderedImage() {
        g2d.dispose();
        return renderedImage;
    }

    BufferedImage createBackgroundImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        return image;
    }

    BufferedImage resizeImage(BufferedImage image, int width, int height) {
        return convertedToBufferedImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    BufferedImage convertedToBufferedImage(Image image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return newImage;
    }
}
