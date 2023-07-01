package net.maisyt.showItems.config;

import discord4j.rest.util.Color;
import net.fabricmc.loader.impl.lib.gson.JsonReader;
import net.maisyt.util.string.NamedPlaceholderString;

import java.io.IOException;

public class MessageConfig {
    public static final String PLAYER_NAME_PLACEHOLDER = "PlayerName";
    public class StartMessageConfig {
        Color embedColor = Color.MEDIUM_SEA_GREEN;
        String message = "Show item bot is online!";

        public StartMessageConfig() {}

        public StartMessageConfig(JsonReader reader) throws IOException {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "embedColor" -> embedColor = getDiscordBotColor(reader.nextString(), embedColor);
                    case "message" -> {
                        String readMessage = reader.nextString();
                        if (readMessage != null && !readMessage.isEmpty()) {
                            message = readMessage;
                        }
                    }
                    default -> reader.skipValue();
                }
            }
            reader.endObject();
        }

        public Color getEmbedColor() {
            return embedColor;
        }

        public String getMessage() {
            return message;
        }
    }

    public class StopMessageConfig {
        Color embedColor = Color.RED;
        String message = "Show item bot is going to sleep...";

        public StopMessageConfig() {}

        public StopMessageConfig(JsonReader reader) throws IOException {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "embedColor" -> embedColor = getDiscordBotColor(reader.nextString(), embedColor);
                    case "message" -> {
                        String readMessage = reader.nextString();
                        if (readMessage != null && !readMessage.isEmpty()) {
                            message = readMessage;
                        }
                    }
                    default -> reader.skipValue();
                }
            }
            reader.endObject();
        }

        public Color getEmbedColor() {
            return embedColor;
        }

        public String getMessage() {
            return message;
        }
    }

    public class showSingleItemMessageConfig {
        Color embedColor = Color.GRAY;
        NamedPlaceholderString title = new NamedPlaceholderString("${PlayerName}'s Item");
        boolean includeTooltip = false;

        public showSingleItemMessageConfig() {}

        public showSingleItemMessageConfig(JsonReader reader) throws IOException {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "embedColor" -> embedColor = getDiscordBotColor(reader.nextString(), embedColor);
                    case "title" -> {
                        String readTitle = reader.nextString();
                        if (readTitle != null && readTitle.contains("${"+PLAYER_NAME_PLACEHOLDER+"}")){
                            title = new NamedPlaceholderString(readTitle);
                        }
                    }
                    case "includeTooltip" -> includeTooltip = reader.nextBoolean();
                    default -> reader.skipValue();
                }
            }
            reader.endObject();
        }

        public Color getEmbedColor() {
            return embedColor;
        }

        public NamedPlaceholderString getTitle() {
            return title;
        }

        public boolean isIncludeTooltip() {
            return includeTooltip;
        }
    }

    public class showInventoryMessageConfig {
        Color embedColor = Color.GRAY;
        NamedPlaceholderString title = new NamedPlaceholderString("${PlayerName}'s Inventory");

        public showInventoryMessageConfig() {}

        public showInventoryMessageConfig(JsonReader reader) throws IOException {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "embedColor" -> embedColor = getDiscordBotColor(reader.nextString(), embedColor);
                    case "title" -> {
                        String readTitle = reader.nextString();
                        if (readTitle != null && readTitle.contains("${"+PLAYER_NAME_PLACEHOLDER+"}")){
                            title = new NamedPlaceholderString(readTitle);
                        }
                    }
                    default -> reader.skipValue();
                }
            }
            reader.endObject();
        }

        public Color getEmbedColor() {
            return embedColor;
        }

        public NamedPlaceholderString getTitle() {
            return title;
        }
    }

    public class ImageConfig {
        int itemDescriptionImageWidth = 350;
        String font;

        public ImageConfig() {}

        public ImageConfig(JsonReader reader) throws IOException {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "itemDescriptionImageWidth" -> setItemDescriptionImageWidth(reader.nextInt());
                    case "font" -> setFont(reader.nextString());
                    default -> reader.skipValue();
                }
            }
            reader.endObject();
        }

        public int getItemDescriptionImageWidth() {
            return itemDescriptionImageWidth;
        }

        public void setItemDescriptionImageWidth(int itemDescriptionImageWidth) {
            this.itemDescriptionImageWidth = itemDescriptionImageWidth;
        }

        public String getFont() {
            return font;
        }

        public void setFont(String font) {
            this.font = font;
        }
    }

    private MessageMode mode = MessageMode.TEXT;
    private StartMessageConfig startMessage = new StartMessageConfig();
    private StopMessageConfig stopMessage = new StopMessageConfig();
    private showSingleItemMessageConfig showSingleItemMessage = new showSingleItemMessageConfig();
//    private showInventoryMessageConfig showInventoryMessage = new showInventoryMessageConfig();
    private ImageConfig imageConfig = new ImageConfig();

    public MessageConfig() {}

    public MessageConfig(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "mode" -> setMode(MessageMode.valueOf(reader.nextString().toUpperCase()));
                case "startMessage" -> setStartMessage(new StartMessageConfig(reader));
                case "stopMessage" -> setStopMessage(new StopMessageConfig(reader));
                case "showSingleItemMessage" -> setShowSingleItemMessage(new showSingleItemMessageConfig(reader));
//                case "showInventoryMessage" -> setShowInventoryMessage(new showInventoryMessageConfig(reader));
                case "image" -> setImageConfig(new ImageConfig(reader));
                default -> reader.skipValue();
            }
        }
        reader.endObject();
    }

    public MessageMode getMode() {
        return mode;
    }

    public void setMode(MessageMode mode) {
        this.mode = mode;
    }

    public StartMessageConfig getStartMessage() {
        return startMessage;
    }

    public void setStartMessage(StartMessageConfig startMessage) {
        this.startMessage = startMessage;
    }

    public StopMessageConfig getStopMessage() {
        return stopMessage;
    }

    public void setStopMessage(StopMessageConfig stopMessage) {
        this.stopMessage = stopMessage;
    }

    public showSingleItemMessageConfig getShowSingleItemMessage() {
        return showSingleItemMessage;
    }

    public void setShowSingleItemMessage(showSingleItemMessageConfig showSingleItemMessage) {
        this.showSingleItemMessage = showSingleItemMessage;
    }

//    public showInventoryMessageConfig getShowInventoryMessage() {
//        return showInventoryMessage;
//    }
//
//    public void setShowInventoryMessage(showInventoryMessageConfig showInventoryMessage) {
//        this.showInventoryMessage = showInventoryMessage;
//    }

    /**
     * Convert a color code to a discord4j Color object.
     */
    Color getDiscordBotColor(String colorCode, Color defaultColor){
        // if not valid, return gray
        if (colorCode == null || !colorCode.startsWith("#") || colorCode.length() != 7){
            return defaultColor;
        }

        int red = Integer.parseInt(colorCode.substring(1, 3), 16);
        int green = Integer.parseInt(colorCode.substring(3, 5), 16);
        int blue = Integer.parseInt(colorCode.substring(5, 7), 16);
        return Color.of(red, green, blue);
    }

    public static MessageConfig load(JsonReader reader) throws IOException {
        return new MessageConfig(reader);
    }

    public ImageConfig getImageConfig() {
        return imageConfig;
    }

    public void setImageConfig(ImageConfig imageConfig) {
        this.imageConfig = imageConfig;
    }
}
