package com.maisyt.showItems;

import com.maisyt.showItems.discord.ShowItemDiscordBot;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowItemsMod implements ModInitializer {
    public static final String MOD_ID = "mt-show-items";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.debug("Init MTShowItems - Start");
        ShowItemDiscordBot.createBot();
        ShowItemDiscordBot.getInstance().sendToDiscord("Hello World");
        LOGGER.debug("Init MTShowItems - End");
    }
}
