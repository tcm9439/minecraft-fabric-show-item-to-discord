package com.maisyt.showItems;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Init on both physical server and client
 */
public class ShowItemsMod implements ModInitializer {
    public static final String MOD_ID = "mt-show-items";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.trace("Init MTShowItems - Start");
        LOGGER.trace("Init MTShowItems - End");
    }
}
