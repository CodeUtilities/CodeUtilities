package io.github.codeutilities.script.event;

import io.github.codeutilities.event.IEvent;
import io.github.codeutilities.event.impl.ChatReceivedEvent;
import io.github.codeutilities.event.impl.ChatSentEvent;
import io.github.codeutilities.event.impl.TickEvent;
import io.github.codeutilities.event.impl.system.KeyPressEvent;
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

    SEND_CHAT(ChatSentEvent.class,"OnSendChat", "Executed when a player sends a chat message.", Items.BOOK),

    KEY_PRESS(KeyPressEvent.class, "OnKeyPress", "Executed when a player presses a key.", Items.STONE_BUTTON),

    RECEIVE_CHAT(ChatReceivedEvent.class, "OnReceiveChat", "Executed when a player receives a chat message.", Items.BOOK),

    TICK_EVENT(TickEvent.class, "OnTick", "Executed every tick.", Items.CLOCK);

    private final String name;
    private final ItemStack icon;
    private final Class<? extends IEvent> codeutilitiesEvent;

    ScriptEventType(Class<? extends IEvent> codeutilitiesEvent, String name, String description, Item item) {
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

    public Class<? extends IEvent> getCodeutilitiesEvent() {
        return codeutilitiesEvent;
    }
}
