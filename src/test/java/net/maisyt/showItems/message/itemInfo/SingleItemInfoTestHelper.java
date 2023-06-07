package net.maisyt.showItems.message.itemInfo;

import net.maisyt.minecraft.util.text.SimpleText;
import net.maisyt.minecraft.util.text.Text;
import net.maisyt.minecraft.util.text.TranslatableText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.util.List;

public class SingleItemInfoTestHelper {
    /**
     * Create a basic item with no enchantments and no tooltips.
     */
    public static SingleItemInfo createSimpleSingleItemInfo(){
        return new SingleItemInfo("Player1",
                new SimpleText("Item1"),
                "item.minecraft.bread",
                "minecraft:bread",
                false, 3, ItemType.ITEM);
    }

    /**
     * Create a basic item with enchantments and no tooltips.
     */
    public static SingleItemInfo createSimpleSingleItemInfoWithEnchantments(){
        SingleItemInfo itemInfo = createSimpleSingleItemInfo();
        itemInfo.setEnchantments(true);
        itemInfo.setAmount(1);
        return itemInfo;
    }

    /**
     * Create tool with translation key name & tooltips.
     */
    public static SingleItemInfo createSingleToolItemInfo() {
        Style style = Style.EMPTY.withItalic(true).withColor(0xff00ff);
        Style style2 = Style.EMPTY.withBold(true).withColor(0x00cc00);
        Style style3 = Style.EMPTY.withUnderline(true).withColor(Formatting.RED);
        Style style4 = Style.EMPTY.withStrikethrough(true).withColor(Formatting.YELLOW);
        Style style5 = Style.EMPTY.withColor(Formatting.GRAY);
        List<Text> tooltips = List.of(
                new SimpleText("tooltip1", style),
                new SimpleText("1234567890123456789012345678901234567890", style2),
                new SimpleText("§8==========§2§l§n強化特性§8===========", style3),
                new SimpleText("あいうえお", style4),
                new TranslatableText("item.minecraft.iron_sword", style5)
        );

        return new SingleItemInfo("Player1",
                new TranslatableText("item.minecraft.iron_sword"),
                "item.minecraft.iron_sword",
                "minecraft:iron_sword",
                tooltips,
                false,
                250, 5);
    }

    /**
     * Create tool with enchantments.
     */
    public static SingleItemInfo createSingleToolItemInfoWithEnchantments() {
        SingleItemInfo itemInfo = createSingleToolItemInfo();
        itemInfo.setEnchantments(true);
        return itemInfo;
    }
}