package net.maisyt.showItems.mixin;

import net.maisyt.showItems.ShowItemsServerMod;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Logical Server mixin
 * for setting up & disconnect Discord bot.
 */
@Mixin(MinecraftServer.class)
public abstract class ServerMixin {
    @Inject(at = @At("TAIL"), method = "<init>")
    public void onStartServer(CallbackInfo info) {
        ShowItemsServerMod.startMod();
    }

    @Inject(method = "shutdown", at = @At("TAIL"))
    private void onServerStop(CallbackInfo ci) {
        ShowItemsServerMod.shutdownMod();
    }
}
