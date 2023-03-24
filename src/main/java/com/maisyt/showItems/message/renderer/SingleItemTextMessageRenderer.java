package com.maisyt.showItems.message.renderer;

import com.maisyt.showItems.message.itemInfo.ItemsInfo;
import com.maisyt.showItems.message.itemInfo.SingleItemInfo;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class SingleItemTextMessageRenderer implements IMessageRenderer {
    @Override
    public Function<GuildMessageChannel, Mono<Message>> renderMessage(ItemsInfo itemsInfo) {
        SingleItemInfo singleItemInfo = (SingleItemInfo) itemsInfo;
        String title = singleItemInfo.getPlayerName()+"'s Item";

        // todo translate item name
        String itemInfo = singleItemInfo.getItemNameTranslationKey();
        if (singleItemInfo.getAmount() > 1){
            itemInfo += " x"+singleItemInfo.getAmount();
        }

        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .color(Color.CYAN)
                .addField(title, itemInfo, false)
//                .addField("tooltips")
                .build();

        return channel -> channel.createMessage(MessageCreateSpec.builder()
                .addEmbed(embed)
                .build());
    }
}
