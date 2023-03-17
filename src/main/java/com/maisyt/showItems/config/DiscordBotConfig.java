package com.maisyt.showItems.config;

import discord4j.common.util.Snowflake;

public class DiscordBotConfig {
    private boolean enable;
    private String serverToken;
    private Snowflake channelId;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getServerToken() {
        return serverToken;
    }

    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    public Snowflake getSnowflakeChannelId() {
        return channelId;
    }

    public String getChannelId() {
        return channelId.asString();
    }

    public void setChannelId(String channelId) {
        this.channelId = Snowflake.of(channelId);
    }
}
