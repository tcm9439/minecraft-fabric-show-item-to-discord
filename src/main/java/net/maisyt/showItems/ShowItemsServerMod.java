package net.maisyt.showItems;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.maisyt.showItems.command.GenerateDefaultConfigCommand;
import net.maisyt.showItems.command.ReloadCommand;
import net.maisyt.showItems.command.ShutdownCommand;
import net.maisyt.showItems.core.ServerOnChatMessage;

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
        ServerMessageEvents.CHAT_MESSAGE.register(ServerOnChatMessage::onChatMessage);
    }

    public void registerServerCommand() {
        CommandRegistrationCallback.EVENT.register(ReloadCommand::register);
        CommandRegistrationCallback.EVENT.register(GenerateDefaultConfigCommand::register);
        CommandRegistrationCallback.EVENT.register(ShutdownCommand::register);
    }
}
