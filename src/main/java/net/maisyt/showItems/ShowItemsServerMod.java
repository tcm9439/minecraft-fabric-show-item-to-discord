package net.maisyt.showItems;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.maisyt.showItems.command.GenerateDefaultConfigCommand;
import net.maisyt.showItems.command.ReloadCommand;
import net.maisyt.showItems.command.ShutdownCommand;
import net.maisyt.showItems.config.ShowItemsConfigManager;
import net.maisyt.showItems.core.ShowItemsMsgHandler;
import net.maisyt.showItems.message.ShowItemsMsgType;


/**
 * Init on physical server only
 */
public class ShowItemsServerMod implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        ShowItemsMod.LOGGER.info("Init MTShowItems Physical Server Side - Start");
        registerServerCommand();
        registerEvent();
        ShowItemsMod.LOGGER.info("Init MTShowItems Physical Server Side - End");
    }

    public void registerEvent() {
        ServerMessageEvents.CHAT_MESSAGE.register((message, player, param) -> {
            ShowItemsMod.LOGGER.trace("Got message: " + message);
            if (!ShowItemsConfigManager.isEnable()){
                ShowItemsMod.LOGGER.info("ShowItems is disabled. Skip message.");
                // is not enabled, do nothing
                return;
            }

            String msgContent = message.getContent().getString();
            ShowItemsMsgType msgType;
            if (msgContent.contains("[i]")) {
                msgType = ShowItemsMsgType.SHOW_ITEM_IN_HAND;
                ShowItemsMod.LOGGER.trace("Got item msg");
            } else {
                return;
            }

            ShowItemsMsgHandler.handleMessage(player, msgType);
        });
    }

    public void registerServerCommand() {
        CommandRegistrationCallback.EVENT.register(ReloadCommand::register);
        CommandRegistrationCallback.EVENT.register(GenerateDefaultConfigCommand::register);
        CommandRegistrationCallback.EVENT.register(ShutdownCommand::register);
    }
}
