package net.maisyt.showItems.command;

import net.maisyt.showItems.config.ShowItemsConfigManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * Server command to generate default config file
 */
public class GenerateDefaultConfigCommand {
    public static final String OVERRIDE_IF_EXISTS_PARAM = "override_if_exists";
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(
            literal("show_item_to_discord")
            .then(literal("generate_config")
            .then(argument(OVERRIDE_IF_EXISTS_PARAM, BoolArgumentType.bool())
            .executes(GenerateDefaultConfigCommand::run)
        )));
    }

    public static int run(CommandContext<ServerCommandSource> context){
        boolean overrideIfExists = BoolArgumentType.getBool(context, OVERRIDE_IF_EXISTS_PARAM);
        boolean success = ShowItemsConfigManager.generateDefaultConfig(overrideIfExists);
        if (success){
            context.getSource().sendFeedback(Text.literal("Create / override config file wit the template successfully"), true);
        } else {
            context.getSource().sendFeedback(Text.literal("Failed to create / override config file wit the template. Probably the file exists and override_if_exists is disable"), true);
        }
        return Command.SINGLE_SUCCESS;
    }
}