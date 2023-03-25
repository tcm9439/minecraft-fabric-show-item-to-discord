package net.maisyt.showItems.image;

import net.minecraft.item.ItemStack;

import java.awt.image.BufferedImage;

// regular item
// single item
public class ItemImageRender extends ImageRender<ItemStack> {
    private static int RENDER_ITEM_IMAGE_LENGTH = 32;
    private static int RENDER_BACKGROUND_IMAGE_LENGTH = 34;
    private static int TWO_DIGIT_COUNT_X_POS = 16; // RENDER_ITEM_IMAGE_LENGTH/2
    private static int ONE_DIGIT_COUNT_X_POS = 24; // RENDER_ITEM_IMAGE_LENGTH/4 * 3
    private static int COUNT_Y_POS = RENDER_ITEM_IMAGE_LENGTH;
    private static float SHIFT_OF_SHADOW = 0.5f;
    private static int ITEM_COUNT_SHADOW_FONT_SIZE = 16;
    private static int ITEM_COUNT_FONT_SIZE = 15;

    private ItemStack itemStack;
    private BufferedImage itemImage;

    public ItemImageRender(ItemStack itemStack, BufferedImage itemImage) {
        this.itemStack = itemStack;
        this.itemImage = itemImage;
    }

    @Override
    public BufferedImage render() {
        itemImage = resizeImage(itemImage, RENDER_ITEM_IMAGE_LENGTH, RENDER_ITEM_IMAGE_LENGTH);

        if (itemStack.isDamageable()){
            // means it is a tool, cannot be stacked
            return renderBackground().renderEnchantment().renderDurability().getFinalRenderedImage();
        }
        // may be stackable & is not damageable
        return renderBackground().renderEnchantment().renderItemCount().getFinalRenderedImage();
    }

    private ItemImageRender renderBackground() {
        renderedImage = createBackgroundImage(RENDER_BACKGROUND_IMAGE_LENGTH, RENDER_BACKGROUND_IMAGE_LENGTH);
        g2d.drawImage(itemImage, 0, 0, null);
        return this;
    }

    private ItemImageRender renderItemCount() {
//        g2d.setPaintMode();
//        int countXPos = ONE_DIGIT_COUNT_X_POS;
//        if (itemStack.getCount() > 9) {
//            countXPos = TWO_DIGIT_COUNT_X_POS;
//        }
//
//        g2d.setFont(new Font(FONT_NAME, Font.PLAIN, ITEM_COUNT_SHADOW_FONT_SIZE));
//        g2d.setColor(Color.BLACK);
//        g2d.drawString(String.valueOf(itemStack.getCount()), countXPos+SHIFT_OF_SHADOW, COUNT_Y_POS+SHIFT_OF_SHADOW);
//
//        g2d.setFont(new Font(FONT_NAME, Font.PLAIN, ITEM_COUNT_FONT_SIZE));
//        g2d.setColor(Color.WHITE);
//        g2d.drawString(String.valueOf(itemStack.getCount()), countXPos, COUNT_Y_POS);

        return this;
    }

    private ItemImageRender renderEnchantment() {
//        BufferedImage outputImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g = (Graphics2D) outputImage.getGraphics();
//
//        float opacity = 0.5f;
//        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
//        g.drawImage(resize(enchantedImage,32,32), 0, 0, null);
//
//        // paint original with composite
//        g.setComposite(AlphaComposite.DstIn);
//        g.drawImage(itemImage, 0, 0, 32, 32, 0, 0, 32, 32, null);
//        g.dispose();
//
//        BufferedImage outputImage2 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
//        g = (Graphics2D) outputImage2.getGraphics();
//        g.drawImage(itemImage, 0, 0, null);
//        g.drawImage(outputImage, 0, 0, null);
//        g.dispose();
//        renderedImage = outputImage2;
        return this;
    }

    private ItemImageRender renderDurability() {
//        if (!itemStack.isDamaged()) {
//            return this;
//        }
//        int maxDurability = itemStack.getMaxDamage();
//        int currentDurability = maxDurability - itemStack.getDamage();
//        double durabilityPercentage = Math.max(0.0, Math.min(1.0, ((double) currentDurability / (double) maxDurability)));
//        if (durabilityPercentage < 1) {
//            int hue = (int) (125 * durabilityPercentage);
//            int length = (int) Math.round(RENDER_ITEM_IMAGE_LENGTH * durabilityPercentage);
//            Color color = Color.getHSBColor((float) hue / 360, 1, 1);
//
//            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
//            g2d.setColor(Color.BLACK);
//            g2d.fillRect(1, 28, 30, 2);
//            g2d.setColor(color);
//            g2d.fillRect(1, 28, length, 2);
//        }

        return this;
    }
}
