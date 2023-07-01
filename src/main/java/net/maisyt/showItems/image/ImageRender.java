package net.maisyt.showItems.image;

import net.maisyt.showItems.ShowItemsMod;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;

public abstract class ImageRender<T> {
    static public Font font;
    protected BufferedImage renderedImage;
    protected Graphics2D g2d;

    public abstract BufferedImage render();

    public static void init(java.util.List<Path> fontPaths, String fontName) {
        ShowItemsMod.LOGGER.debug("Loading extra font.");
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (Path fontPath : fontPaths){
            try {
                ShowItemsMod.LOGGER.debug("Loading font from {}", fontPath);
                Font newFont = Font.createFont(Font.TRUETYPE_FONT, fontPath.toFile());
                ge.registerFont(newFont);
            } catch (IOException|FontFormatException e) {
                ShowItemsMod.LOGGER.error("Failed to load font from {}", fontPath, e);
            }
            // print out all available fonts in a single line for debugging
            ShowItemsMod.LOGGER.info("Available fonts: {}", String.join(", ", ge.getAvailableFontFamilyNames()));
        }
        // fontName is nullable
        font = new Font(fontName, Font.PLAIN, 16);
        ShowItemsMod.LOGGER.debug("Using font {} (expected: {}).", font.getName(), fontName);
    }

    public static void init(){
        font = new Font(null, Font.PLAIN, 16);
    }

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
        ShowItemsMod.LOGGER.trace("Finalizing image. Size: {}x{}", renderedImage.getWidth(), renderedImage.getHeight());
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
