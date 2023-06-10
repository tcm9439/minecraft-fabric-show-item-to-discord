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
                SimpleText.create("Item1"),
                "item.minecraft.bread",
                "minecraft:bread",
                false, 3, true, ItemType.ITEM);
    }

    /**
     * Create a basic item with enchantments and no tooltips.
     */
    public static SingleItemInfo createSimpleSingleItemInfoWithEnchantments(){
        SingleItemInfo itemInfo = createSimpleSingleItemInfo();
        itemInfo.setEnchantments(true);
        itemInfo.setStackable(false);
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
                SimpleText.create("tooltip1", style),
                SimpleText.create("1234567890123456789012345678901234567890", style2),
                SimpleText.create("§8==========§2§l§n強化特性§8===========", style3),
                SimpleText.create("あいうえお", style4),
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

    /**
     * Create a item with complex tooltips.
     * Example from pangu map.
     */
    public static SingleItemInfo createComplexItemInfo(){
        SingleItemInfo item = new SingleItemInfo();
        item.setPlayerName("MaisyT");
        item.setItemName(new TranslatableText("pl.item.name.sword4"));
        item.setItemID("carrot_on_a_stick");
        item.setTranslationKey("item.minecraft.carrot_on_a_stick");
        item.setItemType(ItemType.ITEM);
        item.setTooltips(List.of(
                new TranslatableText("pl.lore.rare4"),
                Text.createText(new TranslatableText("pl.lore.limit0"),
                    SimpleText.create("  "),
                        new TranslatableText("pl.lore.limit_lvl_30")),
                new TranslatableText("pl.item.lore.sword4a"),
                new TranslatableText("pl.item.lore.sword4b"),
                new TranslatableText("pl.lore.weapon_attribute"),
                Text.createText(new TranslatableText("pl.attribute.weapon_attack0"),
                    SimpleText.create("7.1", Style.EMPTY.withColor(Formatting.WHITE).withItalic(false)),
                        SimpleText.create("  "),
                            new TranslatableText("pl.attribute.weapon_attack_speed"),
                                    SimpleText.create("1.6", Style.EMPTY.withColor(Formatting.WHITE).withItalic(false))),
                new TranslatableText("pl.lore.warrior1"),
                new TranslatableText("pl.lore.weapon_skill"),
                new TranslatableText("pl.lore.weapon_skill.sword4.1"),
                new TranslatableText("pl.lore.weapon_skill.sword4.2"),
                new TranslatableText("pl.lore.weapon_skill.sword4.3"),
                new TranslatableText("pl.lore.weapon_skill.sword4.4"),
                new TranslatableText("pl.lore.weapon_skill.sword4.5"),
                new TranslatableText("pl.lore.weapon_enhance"),
                new TranslatableText("pl.lore.unlock_tag1"),
                new TranslatableText("pl.lore.unlock_tag2"),
                new TranslatableText("pl.lore.unlock_tag3"),
                Text.createText(new TranslatableText("pl.enchant"),
                        new TranslatableText("pl.enchant.canenchant2")),
                new TranslatableText("pl.lore.weapon_end"),
                new TranslatableText("pl.lore.weaponinfo2a")
        ));
        item.setEnchantments(false);
        item.setAmount(1);
        item.setStackable(false);
        item.setCustomModelData(24);
        return item;
    }
}