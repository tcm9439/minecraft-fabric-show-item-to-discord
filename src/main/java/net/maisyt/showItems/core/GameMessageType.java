package net.maisyt.showItems.core;

import net.minecraft.text.Text;

public enum GameMessageType {
    ADVANCEMENT,
    JOIN_LEAVE,
    DEATH;

    public static GameMessageType GameChatMessageTypeFromMessage(Text gameMessage){
        String message = gameMessage.toString();
        if (message.contains("chat.type.advancement.task")){
            return ADVANCEMENT;
        } else if (message.contains("multiplayer.player.joined") || message.contains("multiplayer.player.left")){
            return JOIN_LEAVE;
        } else if (message.contains("translation{key='death.")){
            return DEATH;
        } else {
            return null;
        }
    }
}
