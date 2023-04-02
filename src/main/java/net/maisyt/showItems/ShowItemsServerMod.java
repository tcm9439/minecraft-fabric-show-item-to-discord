package net.maisyt.showItems;

import net.fabricmc.api.DedicatedServerModInitializer;

/**
 * Init on physical server only
 */
public class ShowItemsServerMod implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        ShowItemsMod.LOGGER.info("Init MTShowItems Physical Server Side - Start");
        ServerModRegistries.registerModStuffs();
        ShowItemsMod.LOGGER.info("Init MTShowItems Physical Server Side - End");
    }
}
