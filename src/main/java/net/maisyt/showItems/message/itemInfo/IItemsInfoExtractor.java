package net.maisyt.showItems.message.itemInfo;

import net.minecraft.server.network.ServerPlayerEntity;

public interface IItemsInfoExtractor {
    public ItemsInfo extract(ServerPlayerEntity player);
}
