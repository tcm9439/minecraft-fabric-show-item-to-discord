package com.maisyt.minecraft.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import com.maisyt.minecraft.util.Exception.IllegalCallerException;

public class ClientSideMCUtil {
    public static MinecraftClient getClient(){
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null){
            throw new IllegalCallerException.IllegalCallFromServerException();
        }
        return client;
    }

    /**
     * Return a logical server
     */
    public static MinecraftServer getServerFromClient(){
        return getClient().getServer();
    }

    public static String getRemoteServerAddress(){
        MinecraftClient client = ClientSideMCUtil.getClient();
        if (!client.isInSingleplayer()) {
            return client.getServer().getServerIp();
        }
        throw new IllegalCallerException.IllegalCallFromLocalClientException();
    }
}
