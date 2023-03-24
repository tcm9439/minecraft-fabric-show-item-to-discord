package com.maisyt.minecraft.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import com.maisyt.minecraft.util.exception.IllegalCallerException.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.nio.file.Path;

/**
 * Util functions for Minecraft game file path.
 */
public class PathUtil {
    /**
     * Get the game global config file path.
     *      client: .../minecraft/config/
     *      server: ?
     */
    public static Path getGlobalConfigDirectory(){
        return FabricLoader.getInstance().getConfigDir();
    }

    /**
     * Get the game root path.
     *      client: .../minecraft/
     *      server: ?
     */
    public static Path getGameRootDirectory(){
        return FabricLoader.getInstance().getGameDir();
    }

    /**
     * For both server side & client.
     * Get the save path for current save.
     * Server: ./level-name/
     * Client: ./saves/save-name/
     */
    public static Path getSavePath(MinecraftServer server){
        return server.getSavePath(WorldSavePath.ROOT);
    }
}
