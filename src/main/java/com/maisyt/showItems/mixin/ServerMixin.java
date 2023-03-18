package com.maisyt.showItems.mixin;

import com.maisyt.minecraft.util.PathUtil;
import com.maisyt.showItems.ShowItemsMod;
import com.maisyt.showItems.config.ShowItemConfigLoader;
import com.maisyt.showItems.discord.ShowItemDiscordBot;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;

/**
 * Logical Server mixin for setting up & disconnect Discord bot.
 */
@Mixin(MinecraftServer.class)
public abstract class ServerMixin {
    @Inject(at = @At("TAIL"), method = "<init>")
    public void onStartServer(CallbackInfo info) {
        ShowItemsMod.LOGGER.info("[ShowItems-mixin] Minecraft server init.");
        // TODO test load config
        Path configPath = PathUtil.getGlobalConfigDirectory().resolve("ShowItems/show-items-config.yaml");
        ShowItemConfigLoader.loadConfig(configPath);

        ShowItemDiscordBot.createBot();
        ShowItemDiscordBot.getInstance().sendTestMsgToDiscord("Mod init!");
    }

    @Inject(method = "shutdown", at = @At("TAIL"))
    private void onServerStop(CallbackInfo ci) {
        ShowItemsMod.LOGGER.info("[ShowItems-mixin] Minecraft server stop.");
        // TODO disconnect discord bot if connected
        ShowItemDiscordBot.getInstance().disconnect();
    }
}
