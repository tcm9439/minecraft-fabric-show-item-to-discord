package com.maisyt.minecraft.util.entity.player;

import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerSettingUtil {
    static public String getPlayerLanguageCode(ServerPlayerEntity player){
        return getPlayerLanguageCode(player, "en_us");
    }

    static public String getPlayerLanguageCode(ServerPlayerEntity player, String defaultLanguage){
        String language = player.getGameProfile().getProperties().get("locale").iterator().next().getValue();
        if (language == null){
            language = defaultLanguage;
        }
        return language;
        //  LanguageDefinition languageDefinition = this.languageDefs.getOrDefault(DEFAULT_LANGUAGE_CODE, ENGLISH_US);
        // net.minecraft.client.resource.language
    }
}
