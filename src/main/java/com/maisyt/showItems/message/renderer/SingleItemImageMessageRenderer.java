package com.maisyt.showItems.message.renderer;

import com.maisyt.showItems.image.ImageRender;
import com.maisyt.showItems.image.ItemImageRender;
import com.maisyt.showItems.message.itemInfo.ItemsInfo;
import com.maisyt.showItems.message.itemInfo.SingleItemInfo;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;
import net.minecraft.item.ItemStack;
import reactor.core.publisher.Mono;

import java.awt.image.BufferedImage;
import java.util.function.Function;

public class SingleItemImageMessageRenderer implements IMessageRenderer {
    @Override
    public Function<GuildMessageChannel, Mono<Message>> renderMessage(ItemsInfo itemsInfo) {
        SingleItemInfo singleItemInfo = (SingleItemInfo) itemsInfo;
//        ImageRender<ItemStack> imageRender = new ItemImageRender();
//        BufferedImage image = imageRender.render();

//        String title = playerName+"'s Item";
//        String itemInfo = itemName;
//        if (itemAmount > 1){
//            itemInfo += " x"+itemAmount;
//        }
//        String thumbnailFileName = playerName + "-item.png";
//
//        EmbedCreateSpec embed = EmbedCreateSpec.builder()
//                .color(Color.CYAN)
//                .addField(title, itemInfo, false)
//                .thumbnail("attachment://" + thumbnailFileName)
//                .build();
//
//        return channel -> channel.createMessage(MessageCreateSpec.builder()
//                .addFile(thumbnailFileName, renderedItemImage)
//                .addEmbed(embed)
//                .build());
        return null;
    }
}
