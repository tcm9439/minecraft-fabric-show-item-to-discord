package net.maisyt.showItems.message.itemInfo;

import net.minecraft.text.Text;

import java.util.List;

public class SingleItemInfo extends ItemsInfo {
    private String playerName;
    private net.maisyt.minecraft.util.text.Text itemName;
    private String itemID;
    private List<net.maisyt.minecraft.util.text.Text> tooltips;
    private boolean hasEnchantments;
    private int amount;
    private int maxDurability;
    private int currentDurability;

    public SingleItemInfo() {
    }

    public SingleItemInfo(String playerName, net.maisyt.minecraft.util.text.Text itemName, String itemID, List<net.maisyt.minecraft.util.text.Text> tooltips,
                          boolean hasEnchantments, int amount) {
        this.playerName = playerName;
        this.itemName = itemName;
        this.itemID = itemID;
        this.tooltips = tooltips;
        this.hasEnchantments = hasEnchantments;
        this.amount = amount;
    }

    public SingleItemInfo(String playerName, net.maisyt.minecraft.util.text.Text itemName, String itemID, List<net.maisyt.minecraft.util.text.Text> tooltips,
                          boolean hasEnchantments, int maxDurability, int currentDurability) {
        this.playerName = playerName;
        this.itemName = itemName;
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

    public net.maisyt.minecraft.util.text.Text getItemName() {
        return itemName;
    }

    public void setItemName(net.maisyt.minecraft.util.text.Text itemName) {
        this.itemName = itemName;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public List<net.maisyt.minecraft.util.text.Text> getTooltips() {
        return tooltips;
    }

    public void setTooltips(List<net.maisyt.minecraft.util.text.Text> tooltips) {
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

    @Override
    public String toString() {
        return "SingleItemInfo{" +
                "playerName='" + playerName + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemID='" + itemID + '\'' +
                ", tooltips=" + tooltips +
                ", hasEnchantments=" + hasEnchantments +
                ", amount=" + amount +
                ", maxDurability=" + maxDurability +
                ", currentDurability=" + currentDurability +
                '}';
    }
}
