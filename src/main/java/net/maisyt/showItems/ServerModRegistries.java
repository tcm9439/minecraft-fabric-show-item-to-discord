package net.maisyt.showItems;

import net.maisyt.showItems.command.GenerateDefaultConfigCommand;
import net.maisyt.showItems.command.ReloadCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ServerModRegistries {
    public static void registerModStuffs() {
        CommandRegistrationCallback.EVENT.register(ReloadCommand::register);
        CommandRegistrationCallback.EVENT.register(GenerateDefaultConfigCommand::register);
        // TODO: debug command
    }
}
