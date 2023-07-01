package net.maisyt.showItems;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.maisyt.showItems.command.GenerateDefaultConfigCommand;
import net.maisyt.showItems.command.ReloadCommand;
import net.maisyt.showItems.command.ShutdownCommand;

/**
 * Init on physical server only
 */
public class ShowItemsServerMod implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        ShowItemsMod.LOGGER.info("Init MTShowItems Physical Server Side - Start");
        registerServerCommand();
        ShowItemsMod.LOGGER.info("Init MTShowItems Physical Server Side - End");
    }

    public void registerServerCommand() {
        CommandRegistrationCallback.EVENT.register(ReloadCommand::register);
        CommandRegistrationCallback.EVENT.register(GenerateDefaultConfigCommand::register);
        CommandRegistrationCallback.EVENT.register(ShutdownCommand::register);
    }
}
