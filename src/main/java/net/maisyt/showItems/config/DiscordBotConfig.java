package net.maisyt.showItems.config;

import discord4j.common.util.Snowflake;
import net.fabricmc.loader.impl.lib.gson.JsonReader;

import java.io.IOException;

public class DiscordBotConfig {
    private String serverToken;
    private Snowflake channelId;

    public DiscordBotConfig() {}

    public String getServerToken() {
        return serverToken;
    }

    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    public Snowflake getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = Snowflake.of(channelId);
    }

    public boolean validate(){
        return serverToken != null && !serverToken.isEmpty()
                && channelId != null && !channelId.asString().isEmpty();
    }

    public static DiscordBotConfig load(JsonReader reader) throws IOException {
        DiscordBotConfig config = new DiscordBotConfig();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "serverToken" -> config.setServerToken(reader.nextString());
                case "channelId" -> config.setChannelId(reader.nextString());
                default -> reader.skipValue();
            }
        }
        reader.endObject();
        return config;
    }
}
