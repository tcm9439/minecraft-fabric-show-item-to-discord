package net.maisyt.showItems.config;

import discord4j.common.util.Snowflake;
import net.fabricmc.loader.impl.lib.gson.JsonReader;

import java.io.IOException;

public class DiscordBotConfig {
    private String botToken;
    private Snowflake channelId;

    public DiscordBotConfig() {}

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public Snowflake getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = Snowflake.of(channelId);
    }

    public boolean validate(){
        return botToken != null && !botToken.isEmpty()
                && channelId != null && !channelId.asString().isEmpty();
    }

    public static DiscordBotConfig load(JsonReader reader) throws IOException {
        DiscordBotConfig config = new DiscordBotConfig();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "botToken" -> config.setBotToken(reader.nextString());
                case "channelId" -> config.setChannelId(reader.nextString());
                default -> reader.skipValue();
            }
        }
        reader.endObject();
        return config;
    }
}
