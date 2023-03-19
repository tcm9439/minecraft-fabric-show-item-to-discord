package com.maisyt.showItems.core;

import com.maisyt.minecraft.util.entity.player.PlayerInventoryUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.commons.lang3.NotImplementedException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShowItemsMsgHandler {
    // push work to thread pool as getting resource & render image & send it out may take some time
    static public ExecutorService executor = Executors.newSingleThreadExecutor();

    static public void onShowItemMsg(ServerPlayerEntity sender){
        executor.submit(() -> {
            // get item in selected slot
            ItemStack selectedItemStack = PlayerInventoryUtil.getSelectedItemStack(sender);

            // render image

            // pass it to discord bot
        });
    }

    static public void onShowInventoryMsg(ServerPlayerEntity sender){
        // get inventory
        throw new NotImplementedException();
    }
}
