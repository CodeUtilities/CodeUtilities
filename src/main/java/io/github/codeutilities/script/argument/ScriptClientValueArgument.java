package io.github.codeutilities.script.argument;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.codeutilities.event.KeyPressEvent;
import io.github.codeutilities.event.ReceiveChatEvent;
import io.github.codeutilities.event.SendChatEvent;
import io.github.codeutilities.event.system.Event;
import io.github.codeutilities.script.execution.ScriptContext;
import io.github.codeutilities.script.values.ScriptNumberValue;
import io.github.codeutilities.script.values.ScriptTextValue;
import io.github.codeutilities.script.values.ScriptValue;
import io.github.codeutilities.util.ComponentUtil;
import java.lang.reflect.Type;
import java.util.function.BiFunction;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public enum ScriptClientValueArgument implements ScriptArgument {

    EVENT_KEY("KeyPressed","The key code of the key pressed. (KeyPressEvent)", Items.STONE_BUTTON, (event,context) -> {
        if (event instanceof KeyPressEvent e) {
            return new ScriptNumberValue(e.getKey().getValue());
        } else {
            throw new IllegalStateException("Event is not a KeyPressEvent");
        }
    }),

    EVENT_KEY_ACTION("KeyAction","The code of the key action performed. (KeyPressEvent)", Items.OAK_BUTTON, (event,context) -> {
        if (event instanceof KeyPressEvent e) {
            return new ScriptNumberValue(e.getAction());
        } else {
            throw new IllegalStateException("Event is not a KeyPressEvent");
        }
    }),

    EVENT_MESSAGE("ReceivedMessage","The message received. (ReceiveChatEvent)", Items.WRITTEN_BOOK, (event,context) -> {
        if (event instanceof ReceiveChatEvent e) {
            return new ScriptTextValue(ComponentUtil.toFormattedString(e.getMessage()));
        } else {
            throw new IllegalStateException("Event is not a ReceiveChatEvent");
        }
    }),

    ENTERED_MESSAGE("EnteredMessage","The message entered. (SendChatEvent)", Items.WRITABLE_BOOK, (event,context) -> {
        if (event instanceof SendChatEvent e) {
            return new ScriptTextValue(e.getMessage());
        } else {
            throw new IllegalStateException("Event is not a SendChatEvent");
        }
    });

    private final String name;
    private final ItemStack icon;
    private final BiFunction<Event, ScriptContext, ScriptValue> consumer;

    ScriptClientValueArgument(String name, String description, Item type, BiFunction<Event, ScriptContext, ScriptValue> consumer) {
        this.name = name;
        this.icon = new ItemStack(type);
        icon.setHoverName(new TextComponent(name)
            .withStyle(Style.EMPTY
                .withItalic(false)));
        ListTag lore = new ListTag();
        lore.add(StringTag.valueOf(Component.Serializer.toJson(new TextComponent(description)
            .withStyle(Style.EMPTY
                .withColor(ChatFormatting.GRAY)
                .withItalic(false)))));
        icon.getTagElement("display")
            .put("Lore", lore);
        this.consumer = consumer;
    }

    public String getName() {
        return name;
    }

    public ItemStack getIcon() {
        return icon;
    }

    @Override
    public ScriptValue getValue(Event event, ScriptContext context) {
        return consumer.apply(event, context);
    }

    public static class Serializer implements JsonSerializer<ScriptClientValueArgument> {

        @Override
        public JsonElement serialize(ScriptClientValueArgument src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("type","CLIENT_VALUE");
            object.addProperty("value",src.name());
            return object;
        }
    }
}
