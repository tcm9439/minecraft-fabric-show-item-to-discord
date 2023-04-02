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
import net.minecraft.text.Text;
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

        ShowItemsMod.LOGGER.info("rendering single item message for {}", singleItemInfo);

        MessageConfig.showSingleItemMessageConfig msgConfig = ShowItemsConfigManager.getModConfig().getMessage().getShowSingleItemMessage();
        Map<String, String> placeholderValues = new HashMap<>();
        placeholderValues.put(PLAYER_NAME_PLACEHOLDER, singleItemInfo.getPlayerName());
        String title = msgConfig.getTitle().format(placeholderValues);

        String itemInfo;
        try {
            itemInfo = singleItemInfo.getItemName().getDisplayString();
        } catch (Exception e){
            ShowItemsMod.LOGGER.warn("failed to get item name string from {}", singleItemInfo.getItemName());
            itemInfo = singleItemInfo.getItemID();
        }

        if (singleItemInfo.getAmount() > 1){
            itemInfo += " x"+singleItemInfo.getAmount();
        }

        String tooltips = getTooltips(singleItemInfo);
        EmbedCreateSpec embed;
        if (tooltips != null) {
            embed = EmbedCreateSpec.builder()
                    .color(msgConfig.getEmbedColor())
                    .addField(title, itemInfo, false)
                    .addField("Tooltips", tooltips, false) // todo make this configurable + remove it if there are no tooltips
                    .build();
        } else {
            embed = EmbedCreateSpec.builder()
                    .color(msgConfig.getEmbedColor())
                    .addField(title, itemInfo, false)
                    .build();
        }

        EmbedCreateSpec finalEmbed = embed;
        return channel -> channel.createMessage(MessageCreateSpec.builder()
                .addEmbed(finalEmbed)
                .build());
    }

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
