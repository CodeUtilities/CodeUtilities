package io.github.codeutilities.script.action;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.event.system.CancellableEvent;
import io.github.codeutilities.script.action.ScriptActionArgument.ScriptActionArgumentType;
import io.github.codeutilities.script.argument.ScriptArgument;
import io.github.codeutilities.script.execution.ScriptActionContext;
import io.github.codeutilities.script.values.ScriptDictionaryValue;
import io.github.codeutilities.script.values.ScriptListValue;
import io.github.codeutilities.script.values.ScriptNumberValue;
import io.github.codeutilities.script.values.ScriptTextValue;
import io.github.codeutilities.script.values.ScriptUnknownValue;
import io.github.codeutilities.script.values.ScriptValue;
import io.github.codeutilities.util.ComponentUtil;
import io.github.codeutilities.util.Scheduler;
import io.github.codeutilities.util.chat.ChatUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public enum ScriptActionType {

    DISPLAY_CHAT(builder -> builder.name("DisplayChat")
        .description("Displays a message in the chat.")
        .icon(Items.BOOK)
        .category(ScriptActionCategory.VISUALS)
        .arg("Texts", ScriptActionArgumentType.TEXT, arg -> arg.plural(true))
        .action(ctx -> {
            StringBuilder sb = new StringBuilder();
            for (ScriptValue arg : ctx.pluralValue("Texts")) {
                sb.append(arg.asText())
                    .append(" ");
            }
            sb.deleteCharAt(sb.length() - 1);
            ChatUtil.sendMessage(ComponentUtil.fromString(ComponentUtil.andsToSectionSigns(sb.toString())));
        })),

    ACTIONBAR(builder -> builder.name("ActionBar")
        .description("Displays a message in the action bar.")
        .icon(Items.SPRUCE_SIGN)
        .category(ScriptActionCategory.VISUALS)
        .arg("Texts", ScriptActionArgumentType.TEXT, arg -> arg.plural(true))
        .action(ctx -> {
            StringBuilder sb = new StringBuilder();
            for (ScriptValue arg : ctx.pluralValue("Texts")) {
                sb.append(arg.asText())
                    .append(" ");
            }
            sb.deleteCharAt(sb.length() - 1);
            ChatUtil.sendActionBar(ComponentUtil.fromString(ComponentUtil.andsToSectionSigns(sb.toString())));
        })),

    SEND_CHAT(builder -> builder.name("SendChat")
        .description("Makes the player send a chat message.")
        .icon(Items.PAPER)
        .category(ScriptActionCategory.ACTIONS)
        .arg("Texts", ScriptActionArgumentType.TEXT, arg -> arg.plural(true))
        .action(ctx -> {
            StringBuilder sb = new StringBuilder();
            for (ScriptValue arg : ctx.pluralValue("Texts")) {
                sb.append(arg.asText())
                    .append(" ");
            }
            sb.deleteCharAt(sb.length() - 1);
            CodeUtilities.MC.player.sendChatMessage(sb.toString());
        })),

    REPEAT_MULTIPLE(builder -> builder.name("RepeatMultiple")
        .description("Repeats a specified amount of times.")
        .icon(Items.REDSTONE)
        .category(ScriptActionCategory.NUMBERS)
        .arg("Times", ScriptActionArgumentType.NUMBER)
        .hasChildren(true)
        .action(ctx -> {
            for (int i = 0; i < ctx.value("Times").asNumber(); i++) {
                ctx.scheduleInner();
            }
        })),

    CLOSE_BRACKET(builder -> builder.name("CloseBracket")
        .description("Closes the current code block.")
        .icon(Items.PISTON)
        .category(ScriptActionCategory.MISC)),

    SET_VARIABLE(builder -> builder.name("SetVariable")
        .description("Sets a variable to a value.")
        .icon(Items.IRON_INGOT)
        .category(ScriptActionCategory.VARIABLES)
        .arg("Variable", ScriptActionArgumentType.VARIABLE)
        .arg("Value", ScriptActionArgumentType.ANY)
        .action(ctx -> ctx.context().setVariable(
            ctx.variable("Variable").name(),
            ctx.value("Value")
        ))),

    INCREMENT(builder -> builder.name("Increment")
        .description("Increments a variable by a value.")
        .icon(Items.GLOWSTONE_DUST)
        .category(ScriptActionCategory.NUMBERS)
        .arg("Variable", ScriptActionArgumentType.VARIABLE)
        .arg("Amount", ScriptActionArgumentType.NUMBER, arg -> arg.plural(true))
        .action(ctx -> {
            double value = ctx.value("Variable").asNumber();
            for (ScriptValue val : ctx.pluralValue("Amount")) {
                value += val.asNumber();
            }
            ctx.context().setVariable(
                ctx.variable("Variable").name(),
                new ScriptNumberValue(value)
            );
        })),

    DECREMENT(builder -> builder.name("Decrement")
        .description("Decrements a variable by a value.")
        .icon(Items.REDSTONE)
        .category(ScriptActionCategory.NUMBERS)
        .arg("Variable", ScriptActionArgumentType.VARIABLE)
        .arg("Amount", ScriptActionArgumentType.NUMBER, arg -> arg.plural(true))
        .action(ctx -> {
            double value = ctx.value("Variable").asNumber();
            for (ScriptValue val : ctx.pluralValue("Amount")) {
                value -= val.asNumber();
            }
            ctx.context().setVariable(
                ctx.variable("Result").name(),
                new ScriptNumberValue(value)
            );
        })),

    JOIN_TEXT(builder -> builder.name("JoinText")
        .description("Joins multiple texts into one.")
        .icon(Items.BOOK)
        .category(ScriptActionCategory.TEXTS)
        .arg("Result", ScriptActionArgumentType.VARIABLE)
        .arg("Texts", ScriptActionArgumentType.TEXT, arg -> arg.plural(true))
        .action(ctx -> {
            StringBuilder sb = new StringBuilder();
            for (ScriptValue arg : ctx.pluralValue("Texts")) {
                sb.append(arg.asText());
            }
            ctx.context().setVariable(
                ctx.variable("Result").name(),
                new ScriptTextValue(sb.toString())
            );
        })),

    ADD(builder -> builder.name("Add")
        .description("Sets a variable to the sum of the number(s).")
        .icon(Items.BRICK)
        .category(ScriptActionCategory.NUMBERS)
        .arg("Result", ScriptActionArgumentType.VARIABLE)
        .arg("Numbers", ScriptActionArgumentType.NUMBER, arg -> arg.plural(true))
        .action(ctx -> {
            double value = 0;
            for (ScriptValue val : ctx.pluralValue("Numbers")) {
                value += val.asNumber();
            }
            ctx.context().setVariable(
                ctx.variable("Result").name(),
                new ScriptNumberValue(value)
            );
        })),

    SUBTRACT(builder -> builder.name("Subtract")
        .description("Sets a variable to the difference of the number(s).")
        .icon(Items.NETHER_BRICK)
        .category(ScriptActionCategory.NUMBERS)
        .arg("Result", ScriptActionArgumentType.VARIABLE)
        .arg("Numbers", ScriptActionArgumentType.NUMBER, arg -> arg.plural(true))
        .action(ctx -> {
            double value = ctx.value("Numbers").asNumber();
            boolean first = true;
            for (ScriptValue val : ctx.pluralValue("Numbers")) {
                if (first) {
                    first = false;
                } else {
                    value -= val.asNumber();
                }
            }
            ctx.context().setVariable(
                ctx.variable("Result").name(),
                new ScriptNumberValue(value)
            );
        })),

    MULTIPLY(builder -> builder.name("Multiply")
        .description("Sets a variable to the product of the number(s).")
        .icon(Items.BRICKS)
        .category(ScriptActionCategory.NUMBERS)
        .arg("Result", ScriptActionArgumentType.VARIABLE)
        .arg("Numbers", ScriptActionArgumentType.NUMBER, arg -> arg.plural(true))
        .action(ctx -> {
            double value = 1;
            for (ScriptValue val : ctx.pluralValue("Numbers")) {
                value *= val.asNumber();
            }
            ctx.context().setVariable(
                ctx.variable("Result").name(),
                new ScriptNumberValue(value)
            );
        })),

    DIVIDE(builder -> builder.name("Divide")
        .description("Sets a variable to the quotient of the number(s).")
        .icon(Items.NETHER_BRICKS)
        .category(ScriptActionCategory.NUMBERS)
        .arg("Result", ScriptActionArgumentType.VARIABLE)
        .arg("Numbers", ScriptActionArgumentType.NUMBER, arg -> arg.plural(true))
        .action(ctx -> {
            double value = ctx.value("Numbers").asNumber();
            boolean first = true;
            for (ScriptValue val : ctx.pluralValue("Numbers")) {
                if (first) {
                    first = false;
                } else {
                    value /= val.asNumber();
                }
            }
            ctx.context().setVariable(
                ctx.variable("Result").name(),
                new ScriptNumberValue(value)
            );
        })),

    MODULO(builder -> builder.name("Modulo")
        .description("Sets a variable to the remainder of the numbers.")
        .icon(Items.NETHER_WART)
        .category(ScriptActionCategory.NUMBERS)
        .arg("Result", ScriptActionArgumentType.VARIABLE)
        .arg("Dividend", ScriptActionArgumentType.NUMBER)
        .arg("Divisor", ScriptActionArgumentType.NUMBER)
        .action(ctx -> {
            double dividend = ctx.value("Dividend").asNumber();
            double divisor = ctx.value("Divisor").asNumber();
            ctx.context().setVariable(
                ctx.variable("Result").name(),
                new ScriptNumberValue(dividend % divisor)
            );
        })),

    IF_EQUALS(builder -> builder.name("If Equals")
        .description("Checks if one value is equal to another.")
        .icon(Items.IRON_INGOT)
        .category(ScriptActionCategory.VARIABLES)
        .arg("Value", ScriptActionArgumentType.ANY)
        .arg("Other", ScriptActionArgumentType.ANY)
        .hasChildren(true)
        .action(ctx -> {
            if (ctx.value("Value").valueEquals(ctx.value("Other"))) {
                ctx.scheduleInner();
            }
        })),

    IF_NOT_EQUALS(builder -> builder.name("If Not Equals")
        .description("Checks if one value is not equal to another.")
        .icon(Items.BARRIER)
        .category(ScriptActionCategory.VARIABLES)
        .arg("Value", ScriptActionArgumentType.ANY)
        .arg("Other", ScriptActionArgumentType.ANY)
        .hasChildren(true)
        .action(ctx -> {
            if (!ctx.value("Value").valueEquals(ctx.value("Other"))) {
                ctx.scheduleInner();
            }
        })),

    IF_GREATER(builder -> builder.name("If Greater")
        .description("Checks if one number is greater than another.")
        .icon(Items.BRICK)
        .category(ScriptActionCategory.NUMBERS)
        .arg("Value", ScriptActionArgumentType.NUMBER)
        .arg("Other", ScriptActionArgumentType.NUMBER)
        .hasChildren(true)
        .action(ctx -> {
            if (ctx.value("Value").asNumber() > ctx.value("Other").asNumber()) {
                ctx.scheduleInner();
            }
        })),

    IF_GREATER_EQUALS(builder -> builder.name("If Greater Equals")
        .description("Checks if one number is greater than or equal to another.")
        .icon(Items.BRICKS)
        .category(ScriptActionCategory.NUMBERS)
        .arg("Value", ScriptActionArgumentType.NUMBER)
        .arg("Other", ScriptActionArgumentType.NUMBER)
        .hasChildren(true)
        .action(ctx -> {
            if (ctx.value("Value").asNumber() >= ctx.value("Other").asNumber()) {
                ctx.scheduleInner();
            }
        })),

    IF_LESS(builder -> builder.name("If Less")
        .description("Checks if one number is less than another.")
        .icon(Items.NETHER_BRICK)
        .category(ScriptActionCategory.NUMBERS)
        .arg("Value", ScriptActionArgumentType.NUMBER)
        .arg("Other", ScriptActionArgumentType.NUMBER)
        .hasChildren(true)
        .action(ctx -> {
            if (ctx.value("Value").asNumber() < ctx.value("Other").asNumber()) {
                ctx.scheduleInner();
            }
        })),

    IF_LESS_EQUALS(builder -> builder.name("If Less Equals")
        .description("Checks if one number is less than or equal to another.")
        .icon(Items.NETHER_BRICKS)
        .category(ScriptActionCategory.NUMBERS)
        .arg("Value", ScriptActionArgumentType.NUMBER)
        .arg("Other", ScriptActionArgumentType.NUMBER)
        .hasChildren(true)
        .action(ctx -> {
            if (ctx.value("Value").asNumber() <= ctx.value("Other").asNumber()) {
                ctx.scheduleInner();
            }
        })),

    CANCEL_EVENT(builder -> builder.name("Cancel Event")
        .description("Cancels the event.")
        .icon(Items.BARRIER)
        .category(ScriptActionCategory.MISC)
        .action(ctx -> {
            if (ctx.event() instanceof CancellableEvent ce) {
                ce.setCancelled(true);
            }
        })),

    UNCANCEL_EVENT(builder -> builder.name("Uncancel Event")
        .description("Uncancels the event.")
        .icon(Items.STRUCTURE_VOID)
        .category(ScriptActionCategory.MISC)
        .action(ctx -> {
            if (ctx.event() instanceof CancellableEvent ce) {
                ce.setCancelled(false);
            }
        })),

    CREATE_LIST(builder -> builder.name("Create List")
        .description("Creates a new list.")
        .icon(Items.ENDER_CHEST)
        .category(ScriptActionCategory.LISTS)
        .arg("Variable", ScriptActionArgumentType.VARIABLE)
        .arg("Values", ScriptActionArgumentType.ANY, b -> b.plural(true)
            .optional(true))
        .action(ctx -> {
            ArrayList<ScriptValue> values = new ArrayList<>();
            if (ctx.argMap().containsKey("Values")) {
                for (ScriptArgument v : ctx.argMap().get("Values")) {
                    values.add(v.getValue(ctx.event(), ctx.context()));
                }
            }
            ctx.context().setVariable(ctx.variable("Variable").name(), new ScriptListValue(values));
        })),

    APPEND_VALUE(builder -> builder.name("Append Value")
        .description("Appends values to a list.")
        .icon(Items.FURNACE)
        .category(ScriptActionCategory.LISTS)
        .arg("List", ScriptActionArgumentType.VARIABLE)
        .arg("Values", ScriptActionArgumentType.ANY, b -> b.plural(true))
        .action(ctx -> {
            List<ScriptValue> list = ctx.value("List").asList();
            for (ScriptArgument v : ctx.argMap().get("Values")) {
                list.add(v.getValue(ctx.event(), ctx.context()));
            }
            ctx.context().setVariable(ctx.variable("List").name(), new ScriptListValue(list));
        })),

    GET_LIST_VALUE(builder -> builder.name("Get List Value")
        .description("Gets a value from a list.")
        .icon(Items.BOOK)
        .category(ScriptActionCategory.LISTS)
        .arg("Result", ScriptActionArgumentType.VARIABLE)
        .arg("List", ScriptActionArgumentType.VARIABLE)
        .arg("Index", ScriptActionArgumentType.NUMBER)
        .action(ctx -> {
            List<ScriptValue> list = ctx.value("List").asList();
         // force index consistent with diamondfire indexes
            int index = (int) ctx.value("Index").asNumber() - 1;
            if (index < 0 || index >= list.size()) {
                ctx.context().setVariable(ctx.variable("Result").name(), new ScriptUnknownValue());
            } else {
                ctx.context().setVariable(ctx.variable("Result").name(), list.get(index));
            }
        })),

    SET_LIST_VALUE(builder -> builder.name("Set List Value")
        .description("Sets a value in a list.")
        .icon(Items.WRITABLE_BOOK)
        .category(ScriptActionCategory.LISTS)
        .arg("List", ScriptActionArgumentType.VARIABLE)
        .arg("Index", ScriptActionArgumentType.NUMBER)
        .arg("Value", ScriptActionArgumentType.ANY)
        .action(ctx -> {
            List<ScriptValue> list = ctx.value("List").asList();
            // force index consistent with diamondfire indexes
            int index = (int) ctx.value("Index").asNumber() - 1;
            if (index < 0 || index >= list.size()) {
                return;
            }
            list.set(index, ctx.value("Value"));
            ctx.context().setVariable(ctx.variable("List").name(), new ScriptListValue(list));
        })),

    REMOVE_LIST_AT_INDEX_VALUE(builder -> builder.name("Remove List Value")
        .description("Removes a value from a list.")
        .icon(Items.TNT)
        .category(ScriptActionCategory.LISTS)
        .arg("List", ScriptActionArgumentType.VARIABLE)
        .arg("Index", ScriptActionArgumentType.NUMBER)
        .action(ctx -> {
            List<ScriptValue> list = ctx.value("List").asList();
         // force index consistent with diamondfire indexes
            int index = (int) ctx.value("Index").asNumber() - 1;
            if (index < 0 || index >= list.size()) {
                return;
            }
            list.remove(index);
            ctx.context().setVariable(ctx.variable("List").name(), new ScriptListValue(list));
        })),

    REMOVE_LIST_VALUE(builder -> builder.name("Remove List Value")
        .description("Removes a value from a list.")
        .icon(Items.TNT_MINECART)
        .category(ScriptActionCategory.LISTS)
        .arg("List", ScriptActionArgumentType.VARIABLE)
        .arg("Value", ScriptActionArgumentType.ANY)
        .action(ctx -> {
            List<ScriptValue> list = ctx.value("List").asList();

            list.removeIf(value -> value.valueEquals(ctx.value("Value")));

            ctx.context().setVariable(ctx.variable("List").name(), new ScriptListValue(list));
        })),

    LIST_LENGTH(builder -> builder.name("List Length")
        .description("Returns the length of a list.")
        .icon(Items.BOOKSHELF)
        .category(ScriptActionCategory.LISTS)
        .arg("Result", ScriptActionArgumentType.VARIABLE)
        .arg("List", ScriptActionArgumentType.LIST)
        .action(ctx -> {
            ctx.context().setVariable(ctx.variable("Result").name(), new ScriptNumberValue(ctx.value("List").asList().size()));
        })),

    IF_LIST_CONTAINS(builder -> builder.name("If List Contains")
        .description("Checks if a list contains a value.")
        .icon(Items.BOOKSHELF)
        .category(ScriptActionCategory.LISTS)
        .arg("List", ScriptActionArgumentType.LIST)
        .arg("Value", ScriptActionArgumentType.ANY)
        .hasChildren(true)
        .action(ctx -> {
            List<ScriptValue> list = ctx.value("List").asList();
            if (list.stream().anyMatch(value -> value.valueEquals(ctx.value("Value")))) {
                ctx.scheduleInner();
            }
        })),

    IF_TEXT_CONTAINS(builder -> builder.name("If Text Contains")
        .description("Checks if a text contains a value.")
        .icon(Items.NAME_TAG)
        .category(ScriptActionCategory.TEXTS)
        .arg("Text", ScriptActionArgumentType.TEXT)
        .arg("Subtext", ScriptActionArgumentType.TEXT)
        .hasChildren(true)
        .action(ctx -> {
            String text = ctx.value("Text").asText();
            String subtext = ctx.value("Subtext").asText();
            if (text.contains(subtext)) {
                ctx.scheduleInner();
            }
        })),

    IF_STARTS_WITH(builder -> builder.name("If Starts With")
        .description("Checks if a text starts with an other.")
        .icon(Items.FEATHER)
        .category(ScriptActionCategory.TEXTS)
        .arg("Text", ScriptActionArgumentType.TEXT)
        .arg("Subtext", ScriptActionArgumentType.TEXT)
        .hasChildren(true)
        .action(ctx -> {
            String text = ctx.value("Text").asText();
            String subtext = ctx.value("Subtext").asText();
            if (text.startsWith(subtext)) {
                ctx.scheduleInner();
            }
        })),

    IF_LIST_DOESNT_CONTAIN(builder -> builder.name("If List Doesnt Contain")
        .description("Checks if a list contains a value.")
        .icon(Items.BOOKSHELF)
        .category(ScriptActionCategory.LISTS)
        .arg("List", ScriptActionArgumentType.LIST)
        .arg("Value", ScriptActionArgumentType.ANY)
        .hasChildren(true)
        .action(ctx -> {
            List<ScriptValue> list = ctx.value("List").asList();
            if (list.stream().noneMatch(value -> value.valueEquals(ctx.value("Value")))) {
                ctx.scheduleInner();
            }
        })),

    IF_TEXT_DOESNT_CONTAIN(builder -> builder.name("If Text Doesnt Contain")
        .description("Checks if a text doesnt contain a value.")
        .icon(Items.NAME_TAG)
        .category(ScriptActionCategory.TEXTS)
        .arg("Text", ScriptActionArgumentType.TEXT)
        .arg("Subtext", ScriptActionArgumentType.TEXT)
        .hasChildren(true)
        .action(ctx -> {
            String text = ctx.value("Text").asText();
            String subtext = ctx.value("Subtext").asText();
            if (!text.contains(subtext)) {
                ctx.scheduleInner();
            }
        })),

    IF_DOESNT_START_WITH(builder -> builder.name("If Doesnt Start With")
        .description("Checks if a text doesnt start with an other.")
        .icon(Items.FEATHER)
        .category(ScriptActionCategory.TEXTS)
        .arg("Text", ScriptActionArgumentType.TEXT)
        .arg("Subtext", ScriptActionArgumentType.TEXT)
        .hasChildren(true)
        .action(ctx -> {
            String text = ctx.value("Text").asText();
            String subtext = ctx.value("Subtext").asText();
            if (!text.startsWith(subtext)) {
                ctx.scheduleInner();
            }
        })),

    WAIT(builder -> builder.name("Wait")
        .description("Waits for a given amount of time.")
        .icon(Items.CLOCK)
        .category(ScriptActionCategory.MISC)
        .arg("Ticks", ScriptActionArgumentType.NUMBER)
        .action(ctx -> {
            ctx.task().stop();//Stop the current thread
            ctx.task().stack().increase();//Go to the next action
            Scheduler.schedule((int) ctx.value("Ticks").asNumber(), () -> ctx.task().run());//Resume the task after the given amount of ticks
        })),

    CREATE_DICT(builder -> builder.name("Create Dictionary")
        .description("Creates a new dictionary.")
        .icon(Items.ENDER_CHEST)
        .category(ScriptActionCategory.DICTIONARIES)
        .arg("Result", ScriptActionArgumentType.VARIABLE)
        .action(ctx -> {
            ctx.context().setVariable(ctx.variable("Result").name(), new ScriptDictionaryValue(new HashMap<>()));
        })),

    GET_DICT_VALUE(builder -> builder.name("Get Dictionary Value")
        .description("Gets a value from a dictionary.")
        .icon(Items.BOOK)
        .category(ScriptActionCategory.DICTIONARIES)
        .arg("Result", ScriptActionArgumentType.VARIABLE)
        .arg("Dictionary", ScriptActionArgumentType.DICTIONARY)
        .arg("Key", ScriptActionArgumentType.TEXT)
        .action(ctx -> {
            HashMap<String, ScriptValue> dict = ctx.value("Dictionary").asDictionary();
            String key = ctx.value("Key").asText();
            if (dict.containsKey(key)) {
                ctx.context().setVariable(ctx.variable("Result").name(), dict.get(key));
            } else {
                ctx.context().setVariable(ctx.variable("Result").name(), new ScriptUnknownValue());
            }
        })),

    SET_DICT_VALUE(builder -> builder.name("Set Dictionary Value")
        .description("Sets a value in a dictionary.")
        .icon(Items.WRITABLE_BOOK)
        .category(ScriptActionCategory.DICTIONARIES)
        .arg("Dictionary", ScriptActionArgumentType.VARIABLE)
        .arg("Key", ScriptActionArgumentType.TEXT)
        .arg("Value", ScriptActionArgumentType.ANY)
        .action(ctx -> {
            HashMap<String, ScriptValue> dict = ctx.value("Dictionary").asDictionary();
            String key = ctx.value("Key").asText();
            dict.put(key, ctx.value("Value"));
            ctx.context().setVariable(ctx.variable("Dictionary").name(), new ScriptDictionaryValue(dict));
        })),

    GET_DICT_SIZE(builder -> builder.name("Get Dictionary Size")
        .description("Gets the size of a dictionary.")
        .icon(Items.BOOKSHELF)
        .category(ScriptActionCategory.DICTIONARIES)
        .arg("Result", ScriptActionArgumentType.VARIABLE)
        .arg("Dictionary", ScriptActionArgumentType.DICTIONARY)
        .action(ctx -> {
            HashMap<String, ScriptValue> dict = ctx.value("Dictionary").asDictionary();
            ctx.context().setVariable(ctx.variable("Result").name(), new ScriptNumberValue(dict.size()));
        })),

    IF_DICT_KEY_EXISTS(builder -> builder.name("If Dictionary Key Exists")
        .description("Checks if a key exists in a dictionary.")
        .icon(Items.NAME_TAG)
        .category(ScriptActionCategory.DICTIONARIES)
        .arg("Dictionary", ScriptActionArgumentType.DICTIONARY)
        .arg("Key", ScriptActionArgumentType.TEXT)
        .hasChildren(true)
        .action(ctx -> {
            HashMap<String, ScriptValue> dict = ctx.value("Dictionary").asDictionary();
            String key = ctx.value("Key").asText();
            if (dict.containsKey(key)) {
                ctx.scheduleInner();
            }
        })),

    REMOVE_DICT_ENTRY(builder -> builder.name("Remove Dictionary Entry")
        .description("Removes a key from a dictionary.")
        .icon(Items.TNT_MINECART)
        .category(ScriptActionCategory.DICTIONARIES)
        .arg("Dictionary", ScriptActionArgumentType.VARIABLE)
        .arg("Key", ScriptActionArgumentType.TEXT)
        .action(ctx -> {
            HashMap<String, ScriptValue> dict = ctx.value("Dictionary").asDictionary();
            String key = ctx.value("Key").asText();
            dict.remove(key);
            ctx.context().setVariable(ctx.variable("Dictionary").name(), new ScriptDictionaryValue(dict));
        })),


    FOR_EACH_IN_LIST(builder -> builder.name("For Each In List")
        .description("Iterates over a list.")
        .icon(Items.BOOKSHELF)
        .category(ScriptActionCategory.LISTS)
        .arg("Variable", ScriptActionArgumentType.VARIABLE)
        .arg("List", ScriptActionArgumentType.LIST)
        .hasChildren(true)
        .action(ctx -> {
            List<ScriptValue> list = ctx.value("List").asList();
            for (ScriptValue item : list) {
                ctx.scheduleInner(() -> {
                    ctx.context().setVariable(ctx.variable("Variable").name(), item);
                });
            }
        })),

    DICT_FOR_EACH(builder -> builder.name("For Each In Dictionary")
        .description("Iterates over a dictionary.")
        .icon(Items.BOOKSHELF)
        .category(ScriptActionCategory.DICTIONARIES)
        .arg("Key", ScriptActionArgumentType.VARIABLE)
        .arg("Value", ScriptActionArgumentType.VARIABLE)
        .arg("Dictionary", ScriptActionArgumentType.DICTIONARY)
        .hasChildren(true)
        .action(ctx -> {
            HashMap<String, ScriptValue> dict = ctx.value("Dictionary").asDictionary();
            for (Map.Entry<String, ScriptValue> entry : dict.entrySet()) {
                ctx.scheduleInner(() -> {
                    ctx.context().setVariable(ctx.variable("Key").name(), new ScriptTextValue(entry.getKey()));
                    ctx.context().setVariable(ctx.variable("Value").name(), entry.getValue());
                });
            }
        })),

    ROUND_NUM(builder -> builder.name("Round Number")
        .description("Rounds a number.")
        .icon(Items.QUARTZ_STAIRS)
        .category(ScriptActionCategory.NUMBERS)
        .arg("Result", ScriptActionArgumentType.VARIABLE)
        .arg("Number", ScriptActionArgumentType.NUMBER)
        .action(ctx -> {
            double number = ctx.value("Number").asNumber();
            ctx.context().setVariable(ctx.variable("Result").name(), new ScriptNumberValue(Math.round(number)));
        })),

    FLOOR_NUM(builder -> builder.name("Floor Number")
        .description("Rounds a number down.")
        .icon(Items.OAK_STAIRS)
        .category(ScriptActionCategory.NUMBERS)
        .arg("Result", ScriptActionArgumentType.VARIABLE)
        .arg("Number", ScriptActionArgumentType.NUMBER)
        .action(ctx -> {
            double number = ctx.value("Number").asNumber();
            ctx.context().setVariable(ctx.variable("Result").name(), new ScriptNumberValue(Math.floor(number)));
        })),

    CEIL_NUM(builder -> builder.name("Ceil Number")
        .description("Rounds a number up.")
        .icon(Items.DARK_OAK_STAIRS)
        .category(ScriptActionCategory.NUMBERS)
        .arg("Result", ScriptActionArgumentType.VARIABLE)
        .arg("Number", ScriptActionArgumentType.NUMBER)
        .action(ctx -> {
            double number = ctx.value("Number").asNumber();
            ctx.context().setVariable(ctx.variable("Result").name(), new ScriptNumberValue(Math.ceil(number)));
        })),

    REGISTER_CMD(builder -> builder.name("Register Command")
        .description("Registers a /cmd completion.")
        .icon(Items.COMMAND_BLOCK)
        .category(ScriptActionCategory.MISC)
        .arg("Command", ScriptActionArgumentType.TEXT)
        .action(ctx -> {
            ClientCommandManager.DISPATCHER.register(LiteralArgumentBuilder.literal(ctx.value("Command").asText()));

            ClientPlayNetworkHandler nh = CodeUtilities.MC.getNetworkHandler();
            nh.onCommandTree(new CommandTreeS2CPacket(nh.getCommandDispatcher().getRoot()));
        })),

    IF_GUI_OPEN(builder -> builder.name("If GUI Open")
        .description("Executes if a gui is open.")
        .icon(Items.BOOK)
        .hasChildren(true)
        .category(ScriptActionCategory.MISC)
        .action(ctx -> {
            if (CodeUtilities.MC.currentScreen != null) {
                ctx.scheduleInner();
            }
        })),

    IF_GUI_CLOSED(builder -> builder.name("If GUI Not Open")
        .description("Executes if no gui is open.")
        .icon(Items.BOOK)
        .hasChildren(true)
        .category(ScriptActionCategory.MISC)
        .action(ctx -> {
            if (CodeUtilities.MC.currentScreen == null) {
                ctx.scheduleInner();
            }
        })),

    COPY_TEXT(builder -> builder.name("Copy Text")
        .description("Copies the text to the clipboard.")
        .icon(Items.PAPER)
        .category(ScriptActionCategory.TEXTS)
        .arg("Text", ScriptActionArgumentType.TEXT)
        .action(ctx -> {
            CodeUtilities.MC.keyboard.setClipboard(ctx.value("Text").asText());
        })),

    SPLIT_TEXT(builder -> builder.name("Split Text")
        .description("Splits a text into a list of texts.")
        .icon(Items.SHEARS)
        .category(ScriptActionCategory.TEXTS)
        .arg("Result", ScriptActionArgumentType.VARIABLE)
        .arg("Text", ScriptActionArgumentType.TEXT)
        .arg("Separator", ScriptActionArgumentType.TEXT)
        .action(ctx -> {
            String text = ctx.value("Text").asText();
            String separator = ctx.value("Separator").asText();
            List<ScriptValue> split = new ArrayList<>();

            for (String s : text.split(separator)) {
                split.add(new ScriptTextValue(s));
            }

            ctx.context().setVariable(ctx.variable("Result").name(), new ScriptListValue(split));
        })),

    STOP(builder -> builder.name("Stop")
        .description("Stops the current codeline.")
        .icon(Items.BARRIER)
        .category(ScriptActionCategory.MISC)
        .action(ctx -> {
            ctx.task().stop();
        })),

    PLAY_SOUND(builder -> builder.name("Play Sound")
        .description("Plays a sound.")
        .icon(Items.NAUTILUS_SHELL)
        .category(ScriptActionCategory.VISUALS)
        .arg("Sound", ScriptActionArgumentType.TEXT)
        .arg("Volume", ScriptActionArgumentType.NUMBER, b -> b.optional(true))
        .arg("Pitch", ScriptActionArgumentType.NUMBER, b -> b.optional(true))
        .action(ctx -> {
            String sound = ctx.value("Sound").asText();
            double volume = 1;
            double pitch = 1;

            if (ctx.argMap().containsKey("Volume")) {
                volume = ctx.value("Volume").asNumber();
            }

            if (ctx.argMap().containsKey("Pitch")) {
                pitch = ctx.value("Pitch").asNumber();
            }

            SoundEvent snd = Registry.SOUND_EVENT.get(new Identifier(sound));

            if (snd != null) {
                CodeUtilities.MC.getSoundManager().play(PositionedSoundInstance.master(snd, (float) volume, (float) pitch));
            }
        })),

    DISPLAY_TITLE(builder -> builder.name("Display Title")
        .description("Displays a title.")
        .icon(Items.WARPED_SIGN)
        .category(ScriptActionCategory.VISUALS)
        .arg("Title", ScriptActionArgumentType.TEXT)
        .arg("Subtitle", ScriptActionArgumentType.TEXT, b -> b.optional(true))
        .arg("Fade In", ScriptActionArgumentType.NUMBER, b -> b.optional(true))
        .arg("Stay", ScriptActionArgumentType.NUMBER, b -> b.optional(true))
        .arg("Fade Out", ScriptActionArgumentType.NUMBER, b -> b.optional(true))
        .action(ctx -> {
            String title = ctx.value("Title").asText();
            String subtitle = "";
            int fadeIn = 20;
            int stay = 60;
            int fadeOut = 20;

            if (ctx.argMap().containsKey("Subtitle")) {
                subtitle = ctx.value("Subtitle").asText();
            }

            if (ctx.argMap().containsKey("Fade In")) {
                fadeIn = (int) ctx.value("Fade In").asNumber();
            }

            if (ctx.argMap().containsKey("Stay")) {
                stay = (int) ctx.value("Stay").asNumber();
            }

            if (ctx.argMap().containsKey("Fade Out")) {
                fadeOut = (int) ctx.value("Fade Out").asNumber();
            }

            CodeUtilities.MC.inGameHud.setTitle(ComponentUtil.fromString(ComponentUtil.andsToSectionSigns(title)));
            CodeUtilities.MC.inGameHud.setSubtitle(ComponentUtil.fromString(ComponentUtil.andsToSectionSigns(subtitle)));
            CodeUtilities.MC.inGameHud.setTitleTicks(fadeIn, stay, fadeOut);
        })),

    JOIN_LIST_TO_TEXT(builder -> builder.name("Join List to Text")
        .description("Joins a list into a single text.")
        .icon(Items.SLIME_BALL)
        .category(ScriptActionCategory.LISTS)
        .arg("Result", ScriptActionArgumentType.VARIABLE)
        .arg("List", ScriptActionArgumentType.LIST)
        .arg("Separator", ScriptActionArgumentType.TEXT, b -> b.optional(true))
        .action(ctx -> {
            String separator = ", ";

            if (ctx.argMap().containsKey("Separator")) {
                separator = ctx.value("Separator").asText();
            }

            String result = ctx.value("List")
                .asList().stream()
                .map(ScriptValue::asText)
                .collect(Collectors.joining(separator));

            ctx.context().setVariable(ctx.variable("Result").name(), new ScriptTextValue(result));
        })),

    TEXT_INDEX_OF(builder -> builder.name("Index Of Text")
        .description("Gets the index of the first occurrence of a text within another text.")
        .icon(Items.FLINT)
        .category(ScriptActionCategory.TEXTS)
        .arg("Result",ScriptActionArgumentType.VARIABLE)
        .arg("Text",ScriptActionArgumentType.TEXT)
        .arg("Subtext",ScriptActionArgumentType.TEXT)
        .action(ctx -> {
            int result = ctx.value("Text").asText().indexOf(ctx.value("Subtext").asText());
            ctx.context().setVariable(ctx.variable("Result").name(), new ScriptNumberValue(result));
        })),

    TEXT_SUBTEXT(builder -> builder.name("Get Subtext")
        .description("Gets a piece of text within another text.")
        .icon(Items.KNOWLEDGE_BOOK)
        .category(ScriptActionCategory.TEXTS)
        .arg("Result",ScriptActionArgumentType.VARIABLE)
        .arg("Text",ScriptActionArgumentType.TEXT)
        .arg("First Index",ScriptActionArgumentType.NUMBER)
        .arg("Last Index",ScriptActionArgumentType.NUMBER)
        .action(ctx -> {
            String text = ctx.value("Text").asText();
            int start = (int)ctx.value("First Index").asNumber();
            int end = (int)ctx.value("Last Index").asNumber()+1;
            String result = text.substring(start, end);
            ctx.context().setVariable(ctx.variable("Result").name(), new ScriptTextValue(result));
        }));

    private Consumer<ScriptActionContext> action = (ctx) -> {
    };
    private Item icon = Items.STONE;
    private String name = "Unnamed Action";
    private boolean hasChildren = false;
    private ScriptActionCategory category = ScriptActionCategory.MISC;
    private String description = "No description provided.";
    private final List<ScriptActionArgument> arguments = new ArrayList<>();

    ScriptActionType(Consumer<ScriptActionType> builder) {
        builder.accept(this);
    }

    public ItemStack getIcon() {
        ItemStack item = new ItemStack(icon);
        item.setCustomName(new LiteralText(name)
            .fillStyle(Style.EMPTY
                .withColor(Formatting.WHITE)
                .withItalic(false)));

        NbtList lore = new NbtList();
        lore.add(NbtString.of(Text.Serializer.toJson(new LiteralText(description)
            .fillStyle(Style.EMPTY
                .withColor(Formatting.GRAY)
                .withItalic(false)))));

        lore.add(NbtString.of(Text.Serializer.toJson(new LiteralText(""))));

        for (ScriptActionArgument arg : arguments) {
            lore.add(NbtString.of(Text.Serializer.toJson(arg.text())));
        }

        item.getSubNbt("display")
            .put("Lore", lore);

        return item;
    }

    public String getName() {
        return name;
    }

    public boolean hasChildren() {
        return hasChildren;
    }

    public ScriptActionCategory getCategory() {
        return category;
    }

    private ScriptActionType action(Consumer<ScriptActionContext> action) {
        this.action = action;
        return this;
    }

    private ScriptActionType icon(Item icon) {
        this.icon = icon;
        return this;
    }

    private ScriptActionType name(String name) {
        this.name = name;
        return this;
    }

    private ScriptActionType hasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
        return this;
    }

    private ScriptActionType category(ScriptActionCategory category) {
        this.category = category;
        return this;
    }

    private ScriptActionType description(String description) {
        this.description = description;
        return this;
    }

    public ScriptActionType arg(String name, ScriptActionArgumentType type, Consumer<ScriptActionArgument> builder) {
        ScriptActionArgument arg = new ScriptActionArgument(name, type);
        builder.accept(arg);
        arguments.add(arg);
        return this;
    }

    public ScriptActionType arg(String name, ScriptActionArgumentType type) {
        return arg(name, type, (arg) -> {
        });
    }

    public void run(ScriptActionContext ctx) {
        List<List<ScriptActionArgument>> possibilities = new ArrayList<>();

        generatePossibilities(possibilities, new ArrayList<>(), arguments, 0);

        search:
        for (List<ScriptActionArgument> possibility : possibilities) {
            int pos = 0;
            ctx.argMap().clear();
            for (ScriptActionArgument arg : possibility) {
                List<ScriptArgument> args = new ArrayList<>();
                if (pos >= ctx.arguments().size()) {
                    continue search;
                }
                if (arg.type().is(ctx.arguments().get(pos))) {
                    args.add(ctx.arguments().get(pos));
                    pos++;
                }
                if (arg.plural()) {
                    while (pos < ctx.arguments().size()) {
                        if (arg.type().is(ctx.arguments().get(pos))) {
                            args.add(ctx.arguments().get(pos));
                            pos++;
                        } else {
                            break;
                        }
                    }
                }
                ctx.setArg(arg.name(), args);
            }
            if (pos == ctx.arguments().size()) {
                action.accept(ctx);
                return;
            }
        }

        ChatUtil.error("Invalid arguments for " + name + ".");
    }

    private void generatePossibilities(List<List<ScriptActionArgument>> possibilities, ArrayList<ScriptActionArgument> current, List<ScriptActionArgument> arguments, int pos) {
        if (pos >= arguments.size()) {
            possibilities.add(new ArrayList<>(current));
            return;
        }

        ScriptActionArgument arg = arguments.get(pos);
        if (arg.optional()) {
            generatePossibilities(possibilities, new ArrayList<>(current), arguments, pos + 1);
        }
        current.add(arg);
        generatePossibilities(possibilities, current, arguments, pos + 1);
    }
}
