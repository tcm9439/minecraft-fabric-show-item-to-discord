package com.maisyt.showItems.message.itemInfo;

import net.minecraft.text.Text;

import java.util.List;

public class SingleItemInfo extends ItemsInfo {
    private String playerName;
    private String itemNameTranslationKey;
    private String itemID;
    private List<Text> tooltips;
    private boolean hasEnchantments;
    private int amount;
    private int maxDurability;
    private int currentDurability;

    public SingleItemInfo() {
    }

    public SingleItemInfo(String playerName, String itemNameTranslationKey, String itemID, List<Text> tooltips,
                          boolean hasEnchantments, int amount) {
        this.playerName = playerName;
        this.itemNameTranslationKey = itemNameTranslationKey;
        this.itemID = itemID;
        this.tooltips = tooltips;
        this.hasEnchantments = hasEnchantments;
        this.amount = amount;
    }

    public SingleItemInfo(String playerName, String itemNameTranslationKey, String itemID, List<Text> tooltips,
                          boolean hasEnchantments, int maxDurability, int currentDurability) {
        this.playerName = playerName;
        this.itemNameTranslationKey = itemNameTranslationKey;
        this.itemID = itemID;
        this.tooltips = tooltips;
        this.hasEnchantments = hasEnchantments;
        this.maxDurability = maxDurability;
        this.currentDurability = currentDurability;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getItemNameTranslationKey() {
        return itemNameTranslationKey;
    }

    public void setItemNameTranslationKey(String itemNameTranslationKey) {
        this.itemNameTranslationKey = itemNameTranslationKey;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public List<Text> getTooltips() {
        return tooltips;
    }

    public void setTooltips(List<Text> tooltips) {
        this.tooltips = tooltips;
    }

    public boolean getEnchantments() {
        return hasEnchantments;
    }

    public void setEnchantments(boolean hasEnchantment) {
        this.hasEnchantments = hasEnchantment;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public void setMaxDurability(int maxDurability) {
        this.maxDurability = maxDurability;
    }

    public int getCurrentDurability() {
        return currentDurability;
    }

    public void setCurrentDurability(int currentDurability) {
        this.currentDurability = currentDurability;
    }
}
