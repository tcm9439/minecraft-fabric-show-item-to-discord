package net.maisyt.showItems.message.renderer;

import net.maisyt.showItems.message.itemInfo.ItemsInfo;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface IItemsMessageRenderer {
    Function<GuildMessageChannel, Mono<Message>> renderMessage(ItemsInfo itemsInfo);
}
