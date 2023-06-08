package net.maisyt.showItems.image;

import net.maisyt.minecraft.util.resource.manager.ServerTextureManager;
import net.maisyt.showItems.ShowItemsMod;
import net.maisyt.showItems.message.itemInfo.ItemType;
import net.maisyt.showItems.message.itemInfo.SingleItemInfo;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ItemImageRender extends ImageRender<SingleItemInfo> {
    private static final int RENDER_ITEM_IMAGE_LENGTH = 32;
    private static final int RENDER_BACKGROUND_IMAGE_LENGTH = 34;
    private static final int TWO_DIGIT_COUNT_X_POS = 16; // RENDER_ITEM_IMAGE_LENGTH/2
    private static final int ONE_DIGIT_COUNT_X_POS = 24; // RENDER_ITEM_IMAGE_LENGTH/4 * 3
    private static final int COUNT_Y_POS = RENDER_ITEM_IMAGE_LENGTH;
    private static final float SHIFT_OF_SHADOW = -0.5f;
    private static final int ITEM_COUNT_SHADOW_FONT_SIZE = 16;
    private static final int ITEM_COUNT_FONT_SIZE = 15;
    private static final int ITEM_DURABILITY_BAR_LENGTH = 30;

    private SingleItemInfo singleItemInfo;
    private BufferedImage itemImage;

    public ItemImageRender(SingleItemInfo singleItemInfo, BufferedImage itemImage) {
        this.singleItemInfo = singleItemInfo;
        this.itemImage = itemImage;
    }

    @Override
    public BufferedImage render() {
        itemImage = resizeImage(itemImage, RENDER_ITEM_IMAGE_LENGTH, RENDER_ITEM_IMAGE_LENGTH);

        if (singleItemInfo.getItemType() == ItemType.TOOL) {
            // means it is a tool, cannot be stacked
            return renderBackground().renderEnchantment().renderDurability().getFinalRenderedImage();
        }
        // may be stackable & is not damageable
        return renderBackground().renderEnchantment().renderItemCount().getFinalRenderedImage();
    }

    private ItemImageRender renderBackground() {
        ShowItemsMod.LOGGER.trace("Rendering background image");
        renderedImage = createBackgroundImage(RENDER_BACKGROUND_IMAGE_LENGTH, RENDER_BACKGROUND_IMAGE_LENGTH);
        g2d.drawImage(itemImage, 0, 0, null);
        return this;
    }

    private ItemImageRender renderItemCount() {
        int itemCount = singleItemInfo.getAmount();
        ShowItemsMod.LOGGER.trace("Rendering item count: {}", itemCount);

        if (!singleItemInfo.isStackable()) {
            return this;
        }

        g2d.setPaintMode();
        int countXPos = ONE_DIGIT_COUNT_X_POS;
        if (itemCount > 9) {
            countXPos = TWO_DIGIT_COUNT_X_POS;
        }

        g2d.setFont(font.deriveFont(Font.PLAIN, ITEM_COUNT_SHADOW_FONT_SIZE));
        g2d.setColor(Color.BLACK);
        g2d.drawString(String.valueOf(itemCount), countXPos+SHIFT_OF_SHADOW, COUNT_Y_POS+SHIFT_OF_SHADOW);

        g2d.setFont(font.deriveFont(Font.PLAIN, ITEM_COUNT_FONT_SIZE));
        g2d.setColor(Color.WHITE);
        g2d.drawString(String.valueOf(itemCount), countXPos, COUNT_Y_POS);

        return this;
    }

    private ItemImageRender renderEnchantment() {
//        if (!singleItemInfo.isStackable()){
//            return this;
//        }

        ShowItemsMod.LOGGER.trace("Rendering enchantment image");
        BufferedImage outputImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) outputImage.getGraphics();

        float opacity = 0.45f;
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g.drawImage(resizeImage(ServerTextureManager.getEnchantmentTexture(),32,32), 0, 0, null);

        g.setComposite(AlphaComposite.DstIn);
        g.drawImage(itemImage, 0, 0, 32, 32, 0, 0, 32, 32, null);
        g.dispose();

        BufferedImage outputImage2 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D) outputImage2.getGraphics();
        g2d.drawImage(itemImage, 0, 0, null);
        g2d.drawImage(outputImage, 0, 0, null);
        renderedImage = outputImage2;

        return this;
    }

    private ItemImageRender renderDurability() {
        ShowItemsMod.LOGGER.trace("Rendering durability image");
        if (!singleItemInfo.isDamaged()) {
            ShowItemsMod.LOGGER.trace("Item is not damaged");
            return this;
        }

        ShowItemsMod.LOGGER.trace("Item is damaged");
        double durabilityPercentage = singleItemInfo.getDurabilityPercentage();
        if (durabilityPercentage < 1) {
            int hue = (int) (125 * durabilityPercentage);
            int length = (int) Math.round(ITEM_DURABILITY_BAR_LENGTH * durabilityPercentage);
            Color color = Color.getHSBColor((float) hue / 360, 1, 1);

            ShowItemsMod.LOGGER.trace("Durability percentage: {}/{} = {}",
                    singleItemInfo.getCurrentDurability(), singleItemInfo.getMaxDurability(), durabilityPercentage);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

            g2d.setColor(Color.BLACK);
            g2d.fillRect(1, 28, ITEM_DURABILITY_BAR_LENGTH, 2);
            g2d.setColor(color);
            g2d.fillRect(1, 28, length, 2);
        }

        return this;
    }
}
