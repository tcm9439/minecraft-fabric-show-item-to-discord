package com.maisyt.showItems.message.renderer;

import com.maisyt.showItems.message.itemInfo.ItemsInfo;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface IMessageRenderer {
    Function<GuildMessageChannel, Mono<Message>> renderMessage(ItemsInfo itemsInfo);
}
