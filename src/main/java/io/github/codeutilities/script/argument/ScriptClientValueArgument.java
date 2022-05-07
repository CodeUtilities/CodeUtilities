package io.github.codeutilities.script.argument;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.event.KeyPressEvent;
import io.github.codeutilities.event.ReceiveChatEvent;
import io.github.codeutilities.event.SendChatEvent;
import io.github.codeutilities.event.system.Event;
import io.github.codeutilities.script.action.ScriptActionArgument.ScriptActionArgumentType;
import io.github.codeutilities.script.execution.ScriptContext;
import io.github.codeutilities.script.menu.ScriptMenuClickButtonEvent;
import io.github.codeutilities.script.util.ScriptValueItem;
import io.github.codeutilities.script.values.ScriptListValue;
import io.github.codeutilities.script.values.ScriptNumberValue;
import io.github.codeutilities.script.values.ScriptTextValue;
import io.github.codeutilities.script.values.ScriptValue;
import io.github.codeutilities.util.ComponentUtil;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public enum ScriptClientValueArgument implements ScriptArgument {

    EVENT_KEY("KeyPressed","The key code of the key pressed. (KeyPressEvent)", Items.STONE_BUTTON, ScriptActionArgumentType.NUMBER, (event, context) -> {
        if (event instanceof KeyPressEvent e) {
            return new ScriptNumberValue(e.getKey().getCode());
        } else {
            throw new IllegalStateException("Event is not a KeyPressEvent");
        }
    }),

    EVENT_KEY_ACTION("KeyAction","The code of the key action performed. (KeyPressEvent)", Items.OAK_BUTTON, ScriptActionArgumentType.NUMBER, (event,context) -> {
        if (event instanceof KeyPressEvent e) {
            return new ScriptNumberValue(e.getAction());
        } else {
            throw new IllegalStateException("Event is not a KeyPressEvent");
        }
    }),

    EVENT_MESSAGE("ReceivedMessage","The message received. (ReceiveChatEvent)", Items.WRITTEN_BOOK, ScriptActionArgumentType.TEXT, (event,context) -> {
        if (event instanceof ReceiveChatEvent e) {
            return new ScriptTextValue(ComponentUtil.sectionSignsToAnds(ComponentUtil.toFormattedString(e.getMessage())));
        } else {
            throw new IllegalStateException("Event is not a ReceiveChatEvent");
        }
    }),

    ENTERED_MESSAGE("EnteredMessage","The message entered. (SendChatEvent)", Items.WRITABLE_BOOK, ScriptActionArgumentType.TEXT, (event,context) -> {
        if (event instanceof SendChatEvent e) {
            return new ScriptTextValue(e.getMessage());
        } else {
            throw new IllegalStateException("Event is not a SendChatEvent");
        }
    }),

    TIMESTAMP("Timestamp","The current timestamp in milliseconds.", Items.CLOCK, ScriptActionArgumentType.NUMBER, (event,context) -> new ScriptNumberValue(System.currentTimeMillis())),

    MAIN_HAND_ITEM("MainHandItem","The item in the players main hand.", Items.STONE_BUTTON, ScriptActionArgumentType.DICTIONARY,
        (event,context) -> ScriptValueItem.valueFromItem(CodeUtilities.MC.player.getMainHandStack())
    ),

    OFF_HAND_ITEM("OffHandItem","The item in the players off hand.", Items.OAK_BUTTON, ScriptActionArgumentType.DICTIONARY,
        (event,context) -> ScriptValueItem.valueFromItem(CodeUtilities.MC.player.getOffHandStack())
    ),

    FULL_INVENTORY("FullInventory","The entire inventory items of the player.", Items.OAK_PLANKS, ScriptActionArgumentType.LIST, (event,context) -> {
        List<ScriptValue> list = new ArrayList<>();
        for (int i = 0; i < CodeUtilities.MC.player.getInventory().size(); i++) {
            list.add(ScriptValueItem.valueFromItem(CodeUtilities.MC.player.getInventory().getStack(i)));
        }
        return new ScriptListValue(list);
    }),

    MAIN_INVENTORY("MainInventory", "The main inventory items of the player.", Items.BIRCH_PLANKS, ScriptActionArgumentType.LIST, (event, context) -> {
        List<ScriptValue> list = new ArrayList<>();
        for (ItemStack item : CodeUtilities.MC.player.getInventory().main) {
            list.add(ScriptValueItem.valueFromItem(item));
        }
        return new ScriptListValue(list);
    }),

    ARMOR("Armor", "The armor items of the player.", Items.IRON_CHESTPLATE, ScriptActionArgumentType.LIST, (event, context) -> {
        List<ScriptValue> list = new ArrayList<>();
        for (ItemStack item : CodeUtilities.MC.player.getInventory().armor) {
            list.add(ScriptValueItem.valueFromItem(item));
        }
        return new ScriptListValue(list);
    }),

    HOTBAR_ITEMS("Hotbar Items", "The hotbar items of the player.", Items.IRON_AXE, ScriptActionArgumentType.LIST, (event, context) -> {
        List<ScriptValue> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            list.add(ScriptValueItem.valueFromItem(CodeUtilities.MC.player.getInventory().getStack(i)));
        }
        return new ScriptListValue(list);
    }),

    SELECTED_SLOT("Selected Slot", "The selected hotbar slot.", Items.LIME_DYE, ScriptActionArgumentType.NUMBER,
        (event, context) -> new ScriptNumberValue(CodeUtilities.MC.player.getInventory().selectedSlot)
    ),

    GAME_MODE("Game Mode", "The gamemode the player is in.", Items.BEDROCK, ScriptActionArgumentType.TEXT,
        (event, context) -> new ScriptTextValue(CodeUtilities.MC.interactionManager.getCurrentGameMode().getName())
    ),

    WINDOW_WIDTH("Window Width", "The width of the current window.", Items.STICK, ScriptActionArgumentType.NUMBER,
        (event, context) -> new ScriptNumberValue(CodeUtilities.MC.getWindow().getScaledWidth())
    ),

    WINDOW_HEIGHT("Window Height", "The height of the current window.", Items.STICK, ScriptActionArgumentType.NUMBER,
        (event, context) -> new ScriptNumberValue(CodeUtilities.MC.getWindow().getScaledHeight())
    ),

    MENU_ELEMENT_IDENTIFIER("Menu Element Identifier", "The identifier of the menu element that triggered the event.", Items.NAME_TAG, ScriptActionArgumentType.TEXT,(event, scriptContext) -> {
        if (event instanceof ScriptMenuClickButtonEvent e) {
            return new ScriptTextValue(e.identifier());
        } else {
            throw new IllegalStateException("The event is not a menu click event.");
        }
    });

    private final String name;
    private final ItemStack icon;
    private final BiFunction<Event, ScriptContext, ScriptValue> consumer;
    private final ScriptActionArgumentType type;

    ScriptClientValueArgument(String name, String description, Item type, ScriptActionArgumentType varType, BiFunction<Event, ScriptContext, ScriptValue> consumer) {
        this.name = name;
        this.icon = new ItemStack(type);
        icon.setCustomName(new LiteralText(name)
            .fillStyle(Style.EMPTY
                .withItalic(false)));
        NbtList lore = new NbtList();
        lore.add(NbtString.of(Text.Serializer.toJson(new LiteralText(description)
            .fillStyle(Style.EMPTY
                .withColor(Formatting.GRAY)
                .withItalic(false)))));
        icon.getSubNbt("display")
            .put("Lore", lore);
        this.consumer = consumer;
        this.type = varType;
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

    @Override
    public boolean is(ScriptActionArgumentType type) {
        return type == this.type;
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
