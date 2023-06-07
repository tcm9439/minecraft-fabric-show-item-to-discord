package net.maisyt.showItems.message.renderer;

import net.maisyt.showItems.ShowItemsMod;
import net.maisyt.showItems.config.MessageConfig;
import net.maisyt.showItems.config.ShowItemsConfigManager;
import net.maisyt.showItems.message.itemInfo.ItemsInfo;
import net.maisyt.showItems.message.itemInfo.SingleItemInfo;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static net.maisyt.showItems.config.MessageConfig.PLAYER_NAME_PLACEHOLDER;

public class SingleItemTextMessageRenderer implements IItemsMessageRenderer {
    @Override
    public Function<GuildMessageChannel, Mono<Message>> renderMessage(ItemsInfo itemsInfo) {
        SingleItemInfo singleItemInfo = (SingleItemInfo) itemsInfo;

        ShowItemsMod.LOGGER.debug("rendering single item message for {}", singleItemInfo);

        // get the message string from the config
        MessageConfig.showSingleItemMessageConfig msgConfig = ShowItemsConfigManager.getModConfig().getMessage().getShowSingleItemMessage();

        // title: player's item
        Map<String, String> placeholderValues = new HashMap<>();
        placeholderValues.put(PLAYER_NAME_PLACEHOLDER, singleItemInfo.getPlayerName());
        String title = msgConfig.getTitle().format(placeholderValues);

        // item info: itemName xAmount
        String itemInfo = singleItemInfo.getItemTranslatedName();

        // iff the item is countable, append the amount
        if (singleItemInfo.isStackable()){
            itemInfo += " x"+singleItemInfo.getAmount();
        }

        // get tooltips
        String tooltips = getTooltips(singleItemInfo);

        // build the embed
        EmbedCreateSpec.Builder embedBuilder = EmbedCreateSpec.builder()
                .color(msgConfig.getEmbedColor())
                .addField(title, itemInfo, false);

        if (tooltips != null && tooltips.length() > 0) {
            embedBuilder.addField("", tooltips, false);
        }

        EmbedCreateSpec embed = embedBuilder.build();
        // send out the message
        return channel -> channel.createMessage(MessageCreateSpec.builder()
                .addEmbed(embed)
                .build());
    }

    /**
     * Generate a string paragraph from the tooltips Text list.
     */
    public String getTooltips(SingleItemInfo singleItemInfo){
        List<String> tooltips = new ArrayList<>();
        List<net.maisyt.minecraft.util.text.Text> extractedTooltips = singleItemInfo.getTooltips();
        if (extractedTooltips == null || extractedTooltips.size() == 0){
            return null;
        }

        for (net.maisyt.minecraft.util.text.Text tooltip : extractedTooltips){
            String tooltipString;
            try {
                tooltipString = tooltip.getDisplayString();
            } catch (Exception e){
                ShowItemsMod.LOGGER.warn("failed to get tooltip string from {}", tooltip);
                continue;
            }
            if (tooltipString == null || tooltipString.length() == 0){
                tooltips.add("");
            }
            tooltips.add(tooltipString);
        }

        return String.join("\n", tooltips);
    }
}
