package net.maisyt.showItems.message.renderer;

import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import net.maisyt.minecraft.util.resource.manager.ServerTextureManager;
import net.maisyt.showItems.ShowItemsMod;
import net.maisyt.showItems.config.MessageConfig;
import net.maisyt.showItems.config.ShowItemsConfigManager;
import net.maisyt.showItems.image.ImageRender;
import net.maisyt.showItems.image.ItemDescriptionRender;
import net.maisyt.showItems.image.ItemImageRender;
import net.maisyt.showItems.message.itemInfo.ItemsInfo;
import net.maisyt.showItems.message.itemInfo.SingleItemInfo;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import reactor.core.publisher.Mono;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static net.maisyt.showItems.config.MessageConfig.PLAYER_NAME_PLACEHOLDER;

public class SingleItemImageMessageRenderer implements IItemsMessageRenderer {
    @Override
    public Function<GuildMessageChannel, Mono<Message>> renderMessage(ItemsInfo itemsInfo) {
        SingleItemInfo singleItemInfo = (SingleItemInfo) itemsInfo;
        ShowItemsMod.LOGGER.debug("rendering single item message for {}", singleItemInfo);

        // get the message string from the config
        MessageConfig.ShowSingleItemMessageConfig msgConfig = ShowItemsConfigManager.getModConfig().getMessage().getShowSingleItemMessage();

        // title: player's item
        Map<String, String> placeholderValues = new HashMap<>();
        placeholderValues.put(PLAYER_NAME_PLACEHOLDER, singleItemInfo.getPlayerName());
        String title = msgConfig.getTitle().format(placeholderValues);

        BufferedImage texture = ServerTextureManager.getInstance().getItemTexture(singleItemInfo.getTranslationKey(), singleItemInfo.getCustomModelData());
        BufferedImage itemImage = null, tooltipsImage = null;

        if (texture != null && !ServerTextureManager.getInstance().isDummyTexture(texture)){
            ItemImageRender itemImageRender = new ItemImageRender(singleItemInfo, texture);
            itemImage = itemImageRender.render();
        }

        // get tooltips image
        if (singleItemInfo.hasTooltips()){
            ItemDescriptionRender imageRender = new ItemDescriptionRender(singleItemInfo);
            tooltipsImage = imageRender.render();
        }

        String thumbnailFileName = singleItemInfo.getPlayerName() + "-item.png";
        String tooltipsFileName = singleItemInfo.getPlayerName() + "-item-tooltips.png";

        // item info: itemName xAmount
        String itemInfo = singleItemInfo.getItemTranslatedName();
        // iff the item is countable, append the amount
        if (singleItemInfo.isStackable()){
            itemInfo += " x"+singleItemInfo.getAmount();
        }

        // build the embed
        MessageCreateSpec.Builder msgBuilder = MessageCreateSpec.builder();
        EmbedCreateSpec.Builder embedBuilder = EmbedCreateSpec.builder()
                .color(msgConfig.getEmbedColor())
                .addField(title, itemInfo, false);

        if (itemImage != null){
            ShowItemsMod.LOGGER.debug("Add item image to message");
            msgBuilder.addFile(thumbnailFileName, ImageRender.convertToInputStream(itemImage));
            embedBuilder.thumbnail("attachment://" + thumbnailFileName);
        }

        if (tooltipsImage != null){
            ShowItemsMod.LOGGER.debug("Add tooltips image to message");
            msgBuilder.addFile(tooltipsFileName, ImageRender.convertToInputStream(tooltipsImage));
            embedBuilder.image("attachment://" + tooltipsFileName);
        }

        EmbedCreateSpec embed = embedBuilder.build();
        MessageCreateSpec msg = msgBuilder.addEmbed(embed).build();
        // send out the message
        return channel -> channel.createMessage(msg);
    }
}
