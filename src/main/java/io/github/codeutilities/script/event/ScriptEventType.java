package io.github.codeutilities.script.event;

import io.github.codeutilities.event.KeyPressEvent;
import io.github.codeutilities.event.ReceiveChatEvent;
import io.github.codeutilities.event.SendChatEvent;
import io.github.codeutilities.event.TickEvent;
import io.github.codeutilities.event.system.Event;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public enum ScriptEventType {

    SEND_CHAT(SendChatEvent.class,"OnSendChat", "Executed when a player sends a chat message.", Items.BOOK),

    KEY_PRESS(KeyPressEvent.class, "OnKeyPress", "Executed when a player presses a key.", Items.STONE_BUTTON),

    RECEIVE_CHAT(ReceiveChatEvent.class, "OnReceiveChat", "Executed when a player receives a chat message.", Items.BOOK),

    TICK_EVENT(TickEvent.class, "OnTick", "Executed every tick.", Items.CLOCK);

    private final String name;
    private final ItemStack icon;
    private final Class<? extends Event> codeutilitiesEvent;

    ScriptEventType(Class<? extends Event> codeutilitiesEvent, String name, String description, Item item) {
        this.codeutilitiesEvent = codeutilitiesEvent;
        this.name = name;
        icon = new ItemStack(item);
        icon.setHoverName(new TextComponent(name)
            .setStyle(Style.EMPTY
                .withColor(ChatFormatting.WHITE)
                .withItalic(false)));
        ListTag lore = new ListTag();
        lore.add(StringTag.valueOf(Component.Serializer.toJson(new TextComponent(description)
            .withStyle(Style.EMPTY
                .withColor(ChatFormatting.GRAY)
                .withItalic(false)))));
        icon.getTagElement("display")
            .put("Lore", lore);
    }

    public ItemStack getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public Class<? extends Event> getCodeutilitiesEvent() {
        return codeutilitiesEvent;
    }
}
