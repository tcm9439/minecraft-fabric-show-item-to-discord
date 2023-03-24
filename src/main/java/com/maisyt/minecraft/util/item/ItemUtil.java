package com.maisyt.minecraft.util.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.maisyt.showItems.ShowItemsMod;
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
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;

import java.lang.reflect.Method;
import java.util.*;

public class ItemUtil {
    static final Style LORE_STYLE = Style.EMPTY.withColor(Formatting.DARK_PURPLE).withItalic(true);
    static final Text DISABLED_TEXT = Text.translatable("item.disabled").formatted(Formatting.RED);
    static final UUID ATTACK_DAMAGE_MODIFIER_ID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    static final UUID ATTACK_SPEED_MODIFIER_ID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
    static final String UNBREAKABLE_KEY = "Unbreakable";
    static final String CAN_DESTROY_KEY = "CanDestroy";
    static final String CAN_PLACE_ON_KEY = "CanPlaceOn";

    static public int getStackCount(ItemStack itemStack){
        return itemStack.getCount();
    }

    /**
     * Get the translation key e.g. block.minecraft.acacia_door
     */
    static public String getTranslationKey(ItemStack itemStack){
        return itemStack.getItem().getTranslationKey();
    }

    /**
     * Copy from itemStack.getTooltip, remove some logic so that it can be used in server side
     */
    static public List<Text> getTooltip(PlayerEntity player, ItemStack itemStack) {
        try {
            Method isSectionVisible = itemStack.getClass().getDeclaredMethod("isSectionVisible", int.class, ItemStack.TooltipSection.class);
            isSectionVisible.setAccessible(true);

            Method parseBlockTag = itemStack.getClass().getDeclaredMethod("parseBlockTag", String.class);
            parseBlockTag.setAccessible(true);

            ArrayList<Text> list = Lists.newArrayList();
            MutableText mutableText = Text.empty().append(itemStack.getName()).formatted(itemStack.getRarity().formatting);
            if (itemStack.hasCustomName()) {
                mutableText.formatted(Formatting.ITALIC);
            }
            list.add(mutableText);

            if (itemStack.hasNbt()) {
                if ((boolean) isSectionVisible.invoke(itemStack, 1, ItemStack.TooltipSection.ENCHANTMENTS)) {
                    ItemStack.appendEnchantments(list, itemStack.getEnchantments());
                }

                if (itemStack.getNbt().contains(ItemStack.DISPLAY_KEY, NbtElement.COMPOUND_TYPE)) {
                    NbtCompound nbtCompound = itemStack.getNbt().getCompound(ItemStack.DISPLAY_KEY);

                    if (nbtCompound.getType(ItemStack.LORE_KEY) == NbtElement.LIST_TYPE) {
                        NbtList nbtList = nbtCompound.getList(ItemStack.LORE_KEY, NbtElement.STRING_TYPE);
                        for (int j = 0; j < nbtList.size(); ++j) {
                            String string = nbtList.getString(j);
                            try {
                                MutableText mutableText2 = Text.Serializer.fromJson(string);
                                if (mutableText2 == null) continue;
                                list.add(Texts.setStyleIfAbsent(mutableText2, LORE_STYLE));
                            } catch (Exception exception) {
                                nbtCompound.remove(ItemStack.LORE_KEY);
                            }
                        }
                    }
                }
            }
            if ((boolean) isSectionVisible.invoke(itemStack, 1, ItemStack.TooltipSection.MODIFIERS)) {
                for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                    Multimap<EntityAttribute, EntityAttributeModifier> multimap = itemStack.getAttributeModifiers(equipmentSlot);
                    if (multimap.isEmpty()) continue;
                    list.add(ScreenTexts.EMPTY);
                    list.add(Text.translatable("item.modifiers." + equipmentSlot.getName()).formatted(Formatting.GRAY));
                    for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : multimap.entries()) {
                        EntityAttributeModifier entityAttributeModifier = entry.getValue();
                        double d = entityAttributeModifier.getValue();
                        boolean bl = false;
                        if (player != null) {
                            if (entityAttributeModifier.getId() == ATTACK_DAMAGE_MODIFIER_ID) {
                                d += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                                d += (double) EnchantmentHelper.getAttackDamage(itemStack, EntityGroup.DEFAULT);
                                bl = true;
                            } else if (entityAttributeModifier.getId() == ATTACK_SPEED_MODIFIER_ID) {
                                d += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED);
                                bl = true;
                            }
                        }
                        double e = entityAttributeModifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_BASE ||
                                entityAttributeModifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_TOTAL ?
                                d * 100.0 : (entry.getKey().equals(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE) ? d * 10.0 : d);
                        if (bl) {
                            list.add(Text.literal(" ").append(Text.translatable("attribute.modifier.equals." + entityAttributeModifier.getOperation().getId(),
                                    ItemStack.MODIFIER_FORMAT.format(e), Text.translatable(entry.getKey().getTranslationKey()))).formatted(Formatting.DARK_GREEN));
                            continue;
                        }
                        if (d > 0.0) {
                            list.add(Text.translatable("attribute.modifier.plus." + entityAttributeModifier.getOperation().getId(),
                                    ItemStack.MODIFIER_FORMAT.format(e), Text.translatable(entry.getKey().getTranslationKey())).formatted(Formatting.BLUE));
                            continue;
                        }
                        if (!(d < 0.0)) continue;
                        list.add(Text.translatable("attribute.modifier.take." + entityAttributeModifier.getOperation().getId(),
                                ItemStack.MODIFIER_FORMAT.format(e *= -1.0), Text.translatable(entry.getKey().getTranslationKey())).formatted(Formatting.RED));
                    }
                }
            }
            if (itemStack.hasNbt()) {
                NbtList nbtList2;
                if ((boolean) isSectionVisible.invoke(itemStack, 1, ItemStack.TooltipSection.UNBREAKABLE) &&
                        itemStack.getNbt().getBoolean(UNBREAKABLE_KEY)) {
                    list.add(Text.translatable("item.unbreakable").formatted(Formatting.BLUE));
                }
                if ((boolean) isSectionVisible.invoke(itemStack, 1, ItemStack.TooltipSection.CAN_DESTROY) &&
                        itemStack.getNbt().contains(CAN_DESTROY_KEY, NbtElement.LIST_TYPE) &&
                        !(nbtList2 = itemStack.getNbt().getList(CAN_DESTROY_KEY, NbtElement.STRING_TYPE)).isEmpty()) {
                    list.add(ScreenTexts.EMPTY);
                    list.add(Text.translatable("item.canBreak").formatted(Formatting.GRAY));
                    for (int k = 0; k < nbtList2.size(); ++k) {
                        list.addAll((Collection<Text>) parseBlockTag.invoke(null, nbtList2.getString(k)));
                    }
                }
                if ((boolean) isSectionVisible.invoke(itemStack, 1, ItemStack.TooltipSection.CAN_PLACE) &&
                        itemStack.getNbt().contains(CAN_PLACE_ON_KEY, NbtElement.LIST_TYPE) &&
                        !(nbtList2 = itemStack.getNbt().getList(CAN_PLACE_ON_KEY, NbtElement.STRING_TYPE)).isEmpty()) {
                    list.add(ScreenTexts.EMPTY);
                    list.add(Text.translatable("item.canPlace").formatted(Formatting.GRAY));
                    for (int k = 0; k < nbtList2.size(); ++k) {
                        list.addAll((Collection<Text>) parseBlockTag.invoke(null, nbtList2.getString(k)));
                    }
                }
            }
            if (itemStack.isDamaged()) {
                list.add(Text.translatable("item.durability", itemStack.getMaxDamage() - itemStack.getDamage(), itemStack.getMaxDamage()));
            }
            list.add(Text.literal(Registries.ITEM.getId(itemStack.getItem()).toString()).formatted(Formatting.DARK_GRAY));

            if (player != null && !itemStack.getItem().isEnabled(player.getWorld().getEnabledFeatures())) {
                list.add(DISABLED_TEXT);
            }
            return list;
        } catch (Exception e) {
            ShowItemsMod.LOGGER.error("Error while getting tooltip for item {}", itemStack, e);
            return new ArrayList<>();
        }
    }

}
