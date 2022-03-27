package io.github.codeutilities.script.event;

import io.github.codeutilities.event.KeyPressEvent;
import io.github.codeutilities.event.ReceiveChatEvent;
import io.github.codeutilities.event.SendChatEvent;
import io.github.codeutilities.event.TickEvent;
import io.github.codeutilities.event.system.Event;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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
        icon.setCustomName(new LiteralText(name)
            .setStyle(Style.EMPTY
                .withColor(Formatting.WHITE)
                .withItalic(false)));
        NbtList lore = new NbtList();
        lore.add(NbtString.of(Text.Serializer.toJson(new LiteralText(description)
            .fillStyle(Style.EMPTY
                .withColor(Formatting.GRAY)
                .withItalic(false)))));
        icon.getSubNbt("display")
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
