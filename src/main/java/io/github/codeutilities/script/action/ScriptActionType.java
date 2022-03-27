package io.github.codeutilities.script.action;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.event.system.CancellableEvent;
import io.github.codeutilities.script.execution.ScriptActionContext;
import io.github.codeutilities.script.values.ScriptDictionaryValue;
import io.github.codeutilities.script.values.ScriptListValue;
import io.github.codeutilities.script.values.ScriptNumberValue;
import io.github.codeutilities.script.values.ScriptTextValue;
import io.github.codeutilities.script.values.ScriptUnknownValue;
import io.github.codeutilities.script.values.ScriptValue;
import io.github.codeutilities.util.ComponentUtil;
import io.github.codeutilities.util.chat.ChatUtil;
import net.minecraft.block.Material;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public enum ScriptActionType {

    DISPLAY_CHAT("DisplayChat", "Displays a message in the chat. Takes in text(s) for the message to send.", Items.BOOK, ctx -> {
        ctx.minArguments(1);
        TextComponent msg = ComponentUtil.fromString(ctx.argValue(0).asText());
        for (int i = 1; i < ctx.arguments().size(); i++) {
            msg.add(ComponentUtil.fromString(ctx.argValue(i).asText()));
        }
        ChatUtil.sendMessage(msg);
    }),

    ACTIONBAR("ActionBar", "Displays a message in the action bar. Takes in text(s) for the message to send.", Items.SPRUCE_SIGN, ctx -> {
        ctx.minArguments(1);
        MutableComponent msg = ComponentUtil.fromString(ctx.argValue(0).asText());
        for (int i = 1; i < ctx.arguments().size(); i++) {
            msg.append(ComponentUtil.fromString(ctx.argValue(i).asText()));
        }
        ChatUtil.sendActionBar(msg);
    }),

    SEND_CHAT("SendChat", "Makes the player send a chat message. Takes in text(s) for the message to send.", Items.PAPER, ctx -> {
        ctx.minArguments(1);
        StringBuilder msg = new StringBuilder(ctx.argValue(0).asText());
        for (int i = 1; i < ctx.arguments().size(); i++) {
            msg.append(" ").append(ctx.argValue(i).asText());
        }
        CodeUtilities.MC.player.chat(msg.toString());
    }),

    REPEAT_MULTIPLE("RepeatMultiple", "Repeats a specified amount of times. Takes in a number for the amount of times to repeat.", Items.REDSTONE, ctx -> {
        ctx.minArguments(1);
        double times = ctx.argValue(0).asNumber();
        System.out.println(times);
        for (int i = 0; i < times; i++) {
            ctx.inner().run();
        }
    }, true),

    CLOSE_BRACKET("CloseBracket", "Closes the current code block.", Items.PISTON, ctx -> ctx.exactArguments(0)),

    SET_VARIABLE("SetVariable", "Sets a variable to an other value. Takes in a variable and a value.", Items.IRON_INGOT, ctx -> {
        ctx.exactArguments(2);
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            ctx.argValue(1)
        );
    }),

    INCREMENT("Increment", "Increments a variable by a specified amount. Takes in a variable and number(s).", Items.GLOWSTONE_DUST, ctx -> {
        ctx.minArguments(1);
        double value = ctx.argValue(0).asNumber();
        if (ctx.arguments().size() > 1) {
            for (int i = 1; i < ctx.arguments().size(); i++) {
                value += ctx.argValue(i).asNumber();
            }
        } else {
            value++;
        }
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            new ScriptNumberValue(value)
        );
    }),

    DECREMENT("Decrement", "Decrements a variable by a specified amount. Takes in a variable and number(s).", Items.REDSTONE, ctx -> {
        ctx.minArguments(1);
        double value = ctx.argValue(0).asNumber();
        if (ctx.arguments().size() > 1) {
            for (int i = 1; i < ctx.arguments().size(); i++) {
                value -= ctx.argValue(i).asNumber();
            }
        } else {
            value--;
        }
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            new ScriptNumberValue(value)
        );
    }),

    ADD("Add", "Sets a variable to the sum of the number(s)", Items.BRICK, ctx -> {
        ctx.minArguments(2);
        double sum = 0;
        for (int i = 1; i < ctx.arguments().size(); i++) {
            sum += ctx.argValue(i).asNumber();
        }
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            new ScriptNumberValue(sum)
        );
    }),

    SUBTRACT("Subtract", "Sets a variable to the difference of the number(s)", Items.NETHER_BRICK, ctx -> {
        ctx.minArguments(2);
        double difference = ctx.argValue(1).asNumber();
        for (int i = 2; i < ctx.arguments().size(); i++) {
            difference -= ctx.argValue(i).asNumber();
        }
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            new ScriptNumberValue(difference)
        );
    }),

    MULTIPLY("Multiply", "Sets a variable to the product of the number(s)", Items.BRICKS, ctx -> {
        ctx.minArguments(2);
        double product = 1;
        for (int i = 1; i < ctx.arguments().size(); i++) {
            product *= ctx.argValue(i).asNumber();
        }
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            new ScriptNumberValue(product)
        );
    }),

    DIVIDE("Divide", "Sets a variable to the quotient of the number(s)", Items.NETHER_BRICKS, ctx -> {
        ctx.minArguments(2);
        double quotient = ctx.argValue(1).asNumber();
        for (int i = 2; i < ctx.arguments().size(); i++) {
            quotient /= ctx.argValue(i).asNumber();
        }
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            new ScriptNumberValue(quotient)
        );
    }),

    MODULO("Modulo", "Sets a variable to the remainder of the number(s)", Items.NETHER_WART, ctx -> {
        ctx.minArguments(2);
        double remainder = ctx.argValue(1).asNumber();
        for (int i = 2; i < ctx.arguments().size(); i++) {
            remainder %= ctx.argValue(i).asNumber();
        }
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            new ScriptNumberValue(remainder)
        );
    }),

    TIMESTAMP("Timestamp", "Sets the variable to the current timestamp in seconds with ms accuracy.", Items.CLOCK, ctx -> {
        ctx.exactArguments(1);
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            new ScriptNumberValue(System.currentTimeMillis() / 1000.0)
        );
    }),

    IF_EQUALS("IfEquals", "Executes if the first value is equal to the second value.", Items.IRON_INGOT, ctx -> {
        ctx.exactArguments(2);
        if (ctx.argValue(0).valueEquals(ctx.argValue(1))) {
            ctx.inner().run();
        }
    }, true),

    IF_NOT_EQUALS("IfNotEquals", "Executes if the first value is not equal to the second value.", Items.BARRIER, ctx -> {
        ctx.exactArguments(2);
        if (!ctx.argValue(0).valueEquals(ctx.argValue(1))) {
            ctx.inner().run();
        }
    }, true),

    IF_GREATER("IfGreater", "Executes if the first number is greater than the second number.", Items.BRICK, ctx -> {
        ctx.exactArguments(2);
        if (ctx.argValue(0).asNumber() > ctx.argValue(1).asNumber()) {
            ctx.inner().run();
        }
    }, true),

    IF_GREATER_EQUALS("IfGreaterEquals", "Executes if the first number is greater than or equal to the second number.", Items.BRICKS, ctx -> {
        ctx.exactArguments(2);
        if (ctx.argValue(0).asNumber() >= ctx.argValue(1).asNumber()) {
            ctx.inner().run();
        }
    }, true),

    IF_LESS("IfLess", "Executes if the first number is less than the second number.", Items.NETHER_BRICK, ctx -> {
        ctx.exactArguments(2);
        if (ctx.argValue(0).asNumber() < ctx.argValue(1).asNumber()) {
            ctx.inner().run();
        }
    }, true),

    IF_LESS_EQUALS("IfLessEquals", "Executes if the first number is less than or equal to the second number.", Items.NETHER_BRICKS, ctx -> {
        ctx.exactArguments(2);
        if (ctx.argValue(0).asNumber() <= ctx.argValue(1).asNumber()) {
            ctx.inner().run();
        }
    }, true),

    CANCEL_EVENT("CancelEvent", "Cancels the event.", Items.BARRIER, ctx -> {
        ctx.exactArguments(0);
        if (ctx.event() instanceof CancellableEvent ce) {
            ce.setCancelled(true);
        } else {
            throw new IllegalStateException("Cannot cancel event as it is not cancellable.");
        }
    }),

    UNCANCEL_EVENT("UncancelEvent", "Uncancels the event.", Items.STRUCTURE_VOID, ctx -> {
        ctx.exactArguments(0);
        if (ctx.event() instanceof CancellableEvent ce) {
            ce.setCancelled(false);
        } else {
            throw new IllegalStateException("Cannot cancel event as it is not cancellable.");
        }
    }),

    CREATE_LIST("CreateList", "Creates a new list from values.", Items.ENDER_CHEST, ctx -> {
        ctx.minArguments(1);
        List<ScriptValue> content = new ArrayList<>();
        for (int i = 1; i < ctx.arguments().size(); i++) {
            content.add(ctx.argValue(i));
        }
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            new ScriptListValue(content)
        );
    }),

    APPEND_VALUE("AppendValue", "Appends values to a list.", Items.FURNACE, ctx -> {
        ctx.minArguments(2);
        List<ScriptValue> list = ctx.argValue(0).asList();
        for (int i = 1; i < ctx.arguments().size(); i++) {
            list.add(ctx.argValue(i));
        }
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            new ScriptListValue(list)
        );
    }),

    GET_LIST_VALUE("GetListValue", "Gets the value at the specified index of a list.", Items.BOOK, ctx -> {
        ctx.minArguments(2);
        List<ScriptValue> list = ctx.argValue(1).asList();
        int index = (int) ctx.argValue(2).asNumber();
        if (index < 0 || index >= list.size()) {
            ctx.context().setVariable(
                ctx.argVariable(0).name(),
                new ScriptUnknownValue()
            );
        } else {
            ctx.context().setVariable(
                ctx.argVariable(0).name(),
                list.get(index)
            );
        }
    }),

    SET_LIST_VALUE("SetListValue", "Sets the value at the specified index of a list.", Items.WRITABLE_BOOK, ctx -> {
        ctx.minArguments(3);
        List<ScriptValue> list = ctx.argValue(0).asList();
        int index = (int) ctx.argValue(1).asNumber();
        if (index < 0 || index >= list.size()) {
            throw new IllegalArgumentException("List index out of bounds.");
        }
        list.set(index, ctx.argValue(2));
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            new ScriptListValue(list)
        );
    }),

    REMOVE_LIST_VALUE("RemoveListValue", "Removes the value at the specified index of a list.", Items.TNT_MINECART, ctx -> {
        ctx.minArguments(2);
        List<ScriptValue> list = ctx.argValue(0).asList();
        int index = (int) ctx.argValue(1).asNumber();
        if (index < 0 || index >= list.size()) {
            throw new IllegalArgumentException("List index out of bounds.");
        }
        list.remove(index);
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            new ScriptListValue(list)
        );
    }),

    LIST_LENGTH("ListLength", "Sets the variable to the length of the list.", Items.BOOKSHELF, ctx -> {
        ctx.exactArguments(2);
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            new ScriptNumberValue(ctx.argValue(1).asList().size())
        );
    }),

    FOR_EACH("ForEach", "Executes the code for each value of a list and sets the variable to the value.", Items.ENDER_CHEST, ctx -> {
        ctx.exactArguments(2);
        List<ScriptValue> list = ctx.argValue(1).asList();
        for (ScriptValue scriptValue : list) {
            ctx.context().setVariable(
                ctx.argVariable(0).name(),
                scriptValue
            );
            ctx.inner().run();
        }
    }, true),

    IF_TEXT_CONTAINS("IfTextContains", "Executes the code if the text contains the specified text.", Items.NAME_TAG, ctx -> {
        ctx.exactArguments(2);
        String text = ctx.argValue(0).asText();
        String contains = ctx.argValue(1).asText();
        if (text.contains(contains)) {
            ctx.inner().run();
        }
    }, true),

    IF_LIST_CONTAINS("IfListContains", "Executes the code if the list contains the specified value.", Items.BOOKSHELF, ctx -> {
        ctx.exactArguments(2);
        List<ScriptValue> list = ctx.argValue(0).asList();
        ScriptValue contains = ctx.argValue(1);
        if (list.contains(contains)) {
            ctx.inner().run();
        }
    },true),

    CREATE_DICTIONARY("CreateDictionary", "Creates a new dictionary.", Items.BOOKSHELF, ctx -> {
        ctx.exactArguments(1);
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            new ScriptDictionaryValue(new HashMap<>())
        );
    }),

    GET_DICT_VALUE("GetDictValue", "Gets a value of a dictionary.", Items.BOOK, ctx -> {
        ctx.exactArguments(2);
        HashMap<String, ScriptValue> dict = ctx.argValue(1).asDictionary();
        String key = ctx.argValue(2).asText();
        if (!dict.containsKey(key)) {
            ctx.context().setVariable(
                ctx.argVariable(0).name(),
                new ScriptUnknownValue()
            );
        } else {
            ctx.context().setVariable(
                ctx.argVariable(0).name(),
                dict.get(key)
            );
        }
    }),
    
    SET_DICT_VALUE("SetDictValue", "Sets a value of a dictionary.", Items.FURNACE, ctx -> {
        ctx.exactArguments(3);
        HashMap<String, ScriptValue> dict = ctx.argValue(0).asDictionary();
        String key = ctx.argValue(1).asText();
        dict.put(key, ctx.argValue(2));
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            new ScriptDictionaryValue(dict)
        );
    }),

    DICTIONARY_SIZE("DictionarySize", "Sets the variable to the size of the dictionary.", Items.BOOKSHELF, ctx -> {
        ctx.exactArguments(2);
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            new ScriptNumberValue(ctx.argValue(1).asDictionary().size())
        );
    }),

    IF_DICT_KEY("IfDictKey", "Executes the code if the dictionary contains the specified key.", Items.CHEST_MINECART, ctx -> {
       ctx.exactArguments(2);
       HashMap<String, ScriptValue> dict = ctx.argValue(0).asDictionary();
       String key = ctx.argValue(1).asText();
       if (dict.containsKey(key)) {
           ctx.inner().run();
       }
    }, true),

    REMOVE_DICT_ENTRY("RemoveDictEntry", "Removes an entry from a dictionary by the key.", Items.TNT_MINECART, ctx -> {
        ctx.exactArguments(2);
        HashMap<String, ScriptValue> dict = ctx.argValue(0).asDictionary();
        String key = ctx.argValue(1).asText();
        dict.remove(key);
        ctx.context().setVariable(
            ctx.argVariable(0).name(),
            new ScriptDictionaryValue(dict)
        );
    }),

    DICT_FOR_EACH("DictForEach", "Executes the code for each key, value in the dictionary. Takes in 2 variables for key and value, and a dictionary", Items.CHEST_MINECART, ctx -> {
        ctx.exactArguments(3);
        HashMap<String, ScriptValue> dict = ctx.argValue(0).asDictionary();
        for (String key : dict.keySet()) {
            ctx.context().setVariable(
                ctx.argVariable(1).name(),
                new ScriptTextValue(key)
            );
            ctx.context().setVariable(
                ctx.argVariable(2).name(),
                dict.get(key)
            );
            ctx.inner().run();
        }
    },true);

    private final Consumer<ScriptActionContext> consumer;
    private final ItemStack icon;
    private final String name;
    private final boolean hasChildren;

    ScriptActionType(String name, String description, Item type, Consumer<ScriptActionContext> consumer, boolean hasChildren) {
        this.consumer = consumer;
        this.name = name;
        this.hasChildren = hasChildren;
        icon = new ItemStack(type);
        icon.setHoverName(new TextComponent(name)
            .withStyle(Style.EMPTY
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

    ScriptActionType(String name, String description, Item type, Consumer<ScriptActionContext> consumer) {
        this(name, description, type, consumer, false);
    }

    public ItemStack getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public boolean hasChildren() {
        return hasChildren;
    }

    public Consumer<ScriptActionContext> getConsumer() {
        return consumer;
    }
}
