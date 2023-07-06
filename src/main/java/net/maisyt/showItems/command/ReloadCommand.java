package net.maisyt.showItems.command;

import net.maisyt.showItems.ShowItemsServerMod;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

/**
 * Server command to reload the mod
 */
public class ReloadCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(
            literal("show_item_to_discord")
            .then(literal("reload")
            .executes(ReloadCommand::run)
        ));
    }

    public static int run(CommandContext<ServerCommandSource> context){
        ShowItemsServerMod.reloadMod();
        context.getSource().sendFeedback(() -> Text.literal("Reloaded show-item-to-discord."), true);
        return Command.SINGLE_SUCCESS;
    }
}