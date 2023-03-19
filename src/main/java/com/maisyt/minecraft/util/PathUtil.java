package com.maisyt.minecraft.util;

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
     * Get the game path for this save: .../minecraft/saves/
     * For client only
     */
    public static Path getSavesRoot() throws IllegalCallFromServerException {
        return PhysicalClientUtil.getClient().getLevelStorage().getSavesDirectory();
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

    /**
     * For client only.
     * Get the save path for current save.
     *      Local server client: .../minecraft/saves/save-name/
     *      Remote server client: .../minecraft/remoteServerSaves/server-ip
     */
    public static Path getSavePathFromClient(){
        MinecraftClient client = PhysicalClientUtil.getClient();
        if (client.isInSingleplayer()){
            return client.getServer().getSavePath(WorldSavePath.ROOT);
        } else {
            return getRemoteServerLocalSavePath();
        }
    }

    /**
     * For non-local client only.
     * Get path to .../minecraft/remoteServerSaves/server-ip
     */
    public static Path getRemoteServerLocalSavePath(){
        return getGameRootDirectory().resolve("remoteServerSaves").resolve(PhysicalClientUtil.getRemoteServerAddress());
    }
}
