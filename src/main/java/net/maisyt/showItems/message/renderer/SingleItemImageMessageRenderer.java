package net.maisyt.showItems.message.renderer;

import net.maisyt.showItems.message.itemInfo.ItemsInfo;
import net.maisyt.showItems.message.itemInfo.SingleItemInfo;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class SingleItemImageMessageRenderer implements IItemsMessageRenderer {
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
