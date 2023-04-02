package net.maisyt.minecraft.util.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.maisyt.minecraft.util.text.SimpleText;
import net.maisyt.minecraft.util.text.TextFactory;
import net.maisyt.minecraft.util.text.TranslatableText;
import net.maisyt.showItems.ShowItemsMod;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.util.*;

public class ItemUtil {
    static final Style LORE_STYLE = Style.EMPTY.withColor(Formatting.DARK_PURPLE).withItalic(true);
    static final net.maisyt.minecraft.util.text.Text DISABLED_TEXT = new TranslatableText("item.disabled", Formatting.RED);
    static final UUID ATTACK_DAMAGE_MODIFIER_ID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    static final UUID ATTACK_SPEED_MODIFIER_ID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
    static final String UNBREAKABLE_KEY = "Unbreakable";
    static final String CAN_DESTROY_KEY = "CanDestroy";
    static final String CAN_PLACE_ON_KEY = "CanPlaceOn";
    static final String HIDE_FLAGS_KEY = "HideFlags";

    /**
     * Copy from ItemStack
     */
    private static boolean isSectionVisible(int flags, ItemStack.TooltipSection tooltipSection) {
        return (flags & tooltipSection.getFlag()) == 0;
    }

    /**
     * Copy from ItemStack
     */
    private static List<net.maisyt.minecraft.util.text.Text> parseBlockTag(String tag) {
        try {
            return BlockArgumentParser.blockOrTag(Registries.BLOCK.getReadOnlyWrapper(), tag, true)
                    .map(blockResult -> Lists.newArrayList(new TranslatableText(blockResult.blockState().getBlock().getTranslationKey(), Formatting.DARK_GRAY)),
                         tagResult -> tagResult.tag().stream().map(registryEntry -> (net.maisyt.minecraft.util.text.Text) new TranslatableText(registryEntry.value().getTranslationKey(), Formatting.DARK_GRAY)).toList());

//            BlockArgumentParser.blockOrTag(Registries.BLOCK.getReadOnlyWrapper(), tag, true)
//                    .map(blockResult -> Lists.newArrayList(blockResult.blockState().getBlock().getName().formatted(Formatting.DARK_GRAY)),
//                    tagResult -> tagResult.tag().stream().map(registryEntry -> ((Block)registryEntry.value()).getName().formatted(Formatting.DARK_GRAY)).collect(Collectors.toList()));

        } catch (CommandSyntaxException commandSyntaxException) {
            return Lists.newArrayList(new SimpleText("UNKNOWN", Formatting.DARK_GRAY));
        }
    }

    /**
     * Copy from ItemStack
     */
    static public int getHideFlags(ItemStack itemStack) {
        if (itemStack.hasNbt() && itemStack.getNbt().contains(HIDE_FLAGS_KEY, NbtElement.NUMBER_TYPE)) {
            return itemStack.getNbt().getInt(HIDE_FLAGS_KEY);
        }
        return 0;
    }

    /**
     * Get translatable name of the item. If custom name exists, use it instead.
     */
    static public net.maisyt.minecraft.util.text.Text getName(ItemStack itemStack) {
        // default formatting
        Formatting itemRarityFormat = itemStack.getRarity().formatting;

        NbtCompound nbtCompound = itemStack.getSubNbt(ItemStack.DISPLAY_KEY);
        if (nbtCompound != null && nbtCompound.contains(ItemStack.NAME_KEY, NbtElement.STRING_TYPE)) {
            // if custom name exists
            String customNameNBT = nbtCompound.getString(ItemStack.NAME_KEY);
            // custom name default ITALIC
            Style style = Style.EMPTY.withFormatting(itemRarityFormat, Formatting.ITALIC);
            return TextFactory.createTextFromJsonNBT(customNameNBT, style);
        }
        // use item id, that can be translated
        return new TranslatableText(itemStack.getTranslationKey(), itemRarityFormat);
    }

    /**
     * Copy from itemStack.getTooltip, remove some logic so that it fit our need
     *
     * - With no item name as it can be get by getName()
     */
    static public List<net.maisyt.minecraft.util.text.Text> getTooltip(PlayerEntity player, ItemStack itemStack) {
        ArrayList<net.maisyt.minecraft.util.text.Text> tooltipsList = Lists.newArrayList();

        try {
            // the flag that indicate which tooltip entry is hidden
            int hideFlags = getHideFlags(itemStack);

            if (itemStack.hasNbt()) {
                // enchantment
                if (isSectionVisible(hideFlags, ItemStack.TooltipSection.ENCHANTMENTS)) {
                    NbtList enchantments = itemStack.getEnchantments();
                    // ref: ItemStack.appendEnchantments(tooltipsList, itemStack.getEnchantments());
                    for (int i = 0; i < enchantments.size(); ++i) {
                        NbtCompound nbtCompound = enchantments.getCompound(i);
                        Registries.ENCHANTMENT.getOrEmpty(EnchantmentHelper.getIdFromNbt(nbtCompound))
                                .ifPresent(enchantment -> {
                                    String translationKey = enchantment.getTranslationKey();
                                    Style style = Style.EMPTY.withFormatting(enchantment.isCursed() ? Formatting.RED : Formatting.GRAY);
                                    TranslatableText enchantmentName = new TranslatableText(translationKey, style);

                                    // level
                                    int level = EnchantmentHelper.getLevelFromNbt(nbtCompound);
                                    if (level != 1 || enchantment.getMaxLevel() != 1) {
                                        TranslatableText levelText = new TranslatableText("enchantment.level." + level);
                                        enchantmentName.setNextComponent(levelText);
                                    }
                                    tooltipsList.add(enchantmentName);
                                });
                    }
                }

                // "display" NBT
                if (itemStack.getNbt() != null && itemStack.getNbt().contains(ItemStack.DISPLAY_KEY, NbtElement.COMPOUND_TYPE)) {
                    NbtCompound nbtCompound = itemStack.getNbt().getCompound(ItemStack.DISPLAY_KEY);

                    // dye
                    if (isSectionVisible(hideFlags, ItemStack.TooltipSection.DYE) && nbtCompound.contains(ItemStack.COLOR_KEY, NbtElement.NUMBER_TYPE)) {
                        tooltipsList.add(new TranslatableText("item.dyed", Formatting.GRAY, Formatting.ITALIC));
                    }

                    // lore list
                    if (nbtCompound.getType(ItemStack.LORE_KEY) == NbtElement.LIST_TYPE) {
                        NbtList nbtList = nbtCompound.getList(ItemStack.LORE_KEY, NbtElement.STRING_TYPE);
                        for (int j = 0; j < nbtList.size(); ++j) {
                            String string = nbtList.getString(j);
                            tooltipsList.add(TextFactory.createTextFromJsonNBT(string, LORE_STYLE));
                        }
                    }
                }
            }

            // modifiers
            if (isSectionVisible(hideFlags, ItemStack.TooltipSection.MODIFIERS)) {
                for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                    Multimap<EntityAttribute, EntityAttributeModifier> multimap = itemStack.getAttributeModifiers(equipmentSlot);
                    // no modifiers for this slot, skip
                    if (multimap.isEmpty()) continue;

                    List<net.maisyt.minecraft.util.text.Text> modifiersList = new ArrayList<>();

                    for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : multimap.entries()) {
                        EntityAttributeModifier entityAttributeModifier = entry.getValue();
                        double entityAttributeModifierValue = entityAttributeModifier.getValue();
                        boolean isAttackDamageOrSpeedModifier = false;

                        if (player != null) {
                            // compute attack damage or speed modifier value base on player attribute
                            if (entityAttributeModifier.getId().equals(ATTACK_DAMAGE_MODIFIER_ID)) {
                                entityAttributeModifierValue += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                                entityAttributeModifierValue += EnchantmentHelper.getAttackDamage(itemStack, EntityGroup.DEFAULT);
                                isAttackDamageOrSpeedModifier = true;
                            } else if (entityAttributeModifier.getId().equals(ATTACK_SPEED_MODIFIER_ID)) {
                                entityAttributeModifierValue += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED);
                                isAttackDamageOrSpeedModifier = true;
                            }
                        }

                        // compute final modifier value
                        double finalModifierValue;
                        EntityAttributeModifier.Operation operation = entityAttributeModifier.getOperation();
                        if (operation == EntityAttributeModifier.Operation.MULTIPLY_BASE || operation == EntityAttributeModifier.Operation.MULTIPLY_TOTAL) {
                            finalModifierValue = entityAttributeModifierValue * 100.0;
                        } else {
                            if (entry.getKey().equals(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) {
                                finalModifierValue = entityAttributeModifierValue * 10.0;
                            } else {
                                finalModifierValue = entityAttributeModifierValue;
                            }
                        }

                        net.maisyt.minecraft.util.text.Text modifierText;

                        if (isAttackDamageOrSpeedModifier) {
                            modifierText = new SimpleText(" ")
                                    .setNextComponent(new TranslatableText("attribute.modifier.equals." + entityAttributeModifier.getOperation().getId(), Formatting.DARK_GREEN))
                                    .setNextComponent(new SimpleText(ItemStack.MODIFIER_FORMAT.format(finalModifierValue))
                                    .setNextComponent(new TranslatableText(entry.getKey().getTranslationKey())));
                            modifiersList.add(modifierText);
//=                            Text.literal(" ").append(Text.translatable("attribute.modifier.equals." + entityAttributeModifier.getOperation().getId(),
//                                    ItemStack.MODIFIER_FORMAT.format(finalModifierValue), Text.translatable(entry.getKey().getTranslationKey()))).formatted(Formatting.DARK_GREEN));
                        } else {
                            // modifier with positive value (beneficial)
                            if (entityAttributeModifierValue > 0.0) {
                                modifierText = new TranslatableText("attribute.modifier.equals." + entityAttributeModifier.getOperation().getId(), Formatting.BLUE)
                                                .setNextComponent(new SimpleText(ItemStack.MODIFIER_FORMAT.format(finalModifierValue))
                                                .setNextComponent(new TranslatableText(entry.getKey().getTranslationKey())));
                                modifiersList.add(modifierText);
//                                tooltipsList.add(Text.translatable("attribute.modifier.plus." + entityAttributeModifier.getOperation().getId(),
//                                        ItemStack.MODIFIER_FORMAT.format(finalModifierValue), Text.translatable(entry.getKey().getTranslationKey())).formatted(Formatting.BLUE));
                            } else if ((entityAttributeModifierValue < 0.0)) {
                                // modifier with negative value (harmful)
                                modifierText = new TranslatableText("attribute.modifier.take." + entityAttributeModifier.getOperation().getId(), Formatting.BLUE)
                                        .setNextComponent(new SimpleText(ItemStack.MODIFIER_FORMAT.format(finalModifierValue))
                                                .setNextComponent(new TranslatableText(entry.getKey().getTranslationKey())));

                                modifiersList.add(modifierText);
//                                tooltipsList.add(Text.translatable("attribute.modifier.take." + entityAttributeModifier.getOperation().getId(),
//                                        ItemStack.MODIFIER_FORMAT.format(finalModifierValue *= -1.0), Text.translatable(entry.getKey().getTranslationKey())).formatted(Formatting.RED));
                            }
                            // else, entityAttributeModifierValue == 0 => skip
                        }
                    }

                    // don't show the slot indicator text, e.g. "when in main hand:" if no modifiers to print
                    if (modifiersList.size() > 0){
                        // with modifiers, add a new line
                        tooltipsList.add(new SimpleText(""));
                        // add "when in main hand:"
                        tooltipsList.add(new TranslatableText("item.modifiers." + equipmentSlot.getName(), Formatting.GRAY));
                        // add all modifiers
                        tooltipsList.addAll(modifiersList);
                    }
                }
            }

            // unbreakable, can destroy, can place
            if (itemStack.hasNbt()) {
                NbtList attributeNbtList;
                if (isSectionVisible(hideFlags, ItemStack.TooltipSection.UNBREAKABLE) &&
                        itemStack.getNbt().getBoolean(UNBREAKABLE_KEY)) {
                    tooltipsList.add(new TranslatableText("item.unbreakable", Formatting.BLUE));
                }
                if (isSectionVisible(hideFlags, ItemStack.TooltipSection.CAN_DESTROY) && itemStack.getNbt().contains(CAN_DESTROY_KEY, NbtElement.LIST_TYPE) &&
                        !(attributeNbtList = itemStack.getNbt().getList(CAN_DESTROY_KEY, NbtElement.STRING_TYPE)).isEmpty()) {
                    tooltipsList.add(new SimpleText(""));
                    tooltipsList.add(new TranslatableText("item.canBreak", Formatting.GRAY));
                    for (int k = 0; k < attributeNbtList.size(); ++k) {
                        tooltipsList.addAll(parseBlockTag(attributeNbtList.getString(k)));
                    }
                }
                if (isSectionVisible(hideFlags, ItemStack.TooltipSection.CAN_PLACE) && itemStack.getNbt().contains(CAN_PLACE_ON_KEY, NbtElement.LIST_TYPE) &&
                        !(attributeNbtList = itemStack.getNbt().getList(CAN_PLACE_ON_KEY, NbtElement.STRING_TYPE)).isEmpty()) {
                    tooltipsList.add(new SimpleText(""));
                    tooltipsList.add(new TranslatableText("item.canPlace", Formatting.GRAY));
                    for (int k = 0; k < attributeNbtList.size(); ++k) {
                        tooltipsList.addAll(parseBlockTag(attributeNbtList.getString(k)));
                    }
                }
            }

            // durability
            if (itemStack.isDamaged()) {
                tooltipsList.add(new TranslatableText("item.durability")
                        .setNextComponent(new SimpleText(String.format(" %d/%d", itemStack.getMaxDamage() - itemStack.getDamage(), itemStack.getMaxDamage()))));
            }

            // if item is disabled
            if (player != null && !itemStack.getItem().isEnabled(player.getWorld().getEnabledFeatures())) {
                tooltipsList.add(DISABLED_TEXT);
            }
            return tooltipsList;
        } catch (Exception e) {
            ShowItemsMod.LOGGER.warn("Error while getting tooltip for item {}", itemStack, e);
            return new ArrayList<>();
        }
    }

}
