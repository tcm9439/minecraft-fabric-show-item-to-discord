package com.maisyt.showItems.mixin;

import com.maisyt.minecraft.util.PathUtil;
import com.maisyt.showItems.ShowItemsMod;
import com.maisyt.showItems.config.ShowItemsConfigManager;
import com.maisyt.showItems.discord.ShowItemsDiscordBot;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;

/**
 * Logical Server mixin
 * for setting up & disconnect Discord bot.
 */
@Mixin(MinecraftServer.class)
public abstract class ServerMixin {
    @Inject(at = @At("TAIL"), method = "<init>")
    public void onStartServer(CallbackInfo info) {
        try {
            Path configPath = PathUtil.getGlobalConfigDirectory().resolve("show-items-config.yaml");
            ShowItemsConfigManager.loadConfig(configPath);
            if (ShowItemsConfigManager.isDiscordEnable()){
                ShowItemsMod.LOGGER.debug("Starting Discord bot.");
                ShowItemsDiscordBot.createBot();
            } else {
                ShowItemsMod.LOGGER.debug("Discord bot is disabled.");
            }
        } catch (Exception e){
            ShowItemsMod.LOGGER.error("Error when init Show Items Mod: {}", e.getMessage(), e);
            ShowItemsConfigManager.setDefaultConfig();
        }
    }

    @Inject(method = "shutdown", at = @At("TAIL"))
    private void onServerStop(CallbackInfo ci) {
        if (ShowItemsConfigManager.isDiscordEnable()){
            ShowItemsDiscordBot.getInstance().disconnect();
        }
    }
}
