package com.maisyt.minecraft.util.entity.player;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerInventoryUtil {
    static public ItemStack getSelectedItemStack(ServerPlayerEntity player){
        PlayerInventory playerInventory = player.getInventory();
        int selectedSlot = playerInventory.selectedSlot;
        return playerInventory.getStack(selectedSlot);
    }

    static public PlayerInventory getInventory(ServerPlayerEntity player){
        return player.getInventory();
    }
}
