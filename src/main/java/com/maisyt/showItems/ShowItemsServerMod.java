package com.maisyt.showItems;

import net.fabricmc.api.DedicatedServerModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowItemsServerMod implements DedicatedServerModInitializer {
    public static final String MOD_ID = "mt-show-items";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeServer() {
        LOGGER.debug("Init MTShowItems Server Side - Start");
        ServerModRegistries.registerModStuffs();
        LOGGER.debug("Init MTShowItems Server Side - End");
    }
}
