package io.github.codeutilities.commands.text;

import java.awt.Color;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.util.chat.ChatType;
import io.github.codeutilities.util.chat.ChatUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;

public class ColorCommand implements Command {

    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(literal("color")
                /*
                .then(ArgBuilder.literal("pick")).executes((context -> {
                    ColorPickerMenu colorPickerMenu = new ColorPickerMenu();
                    colorPickerMenu.openAsync(colorPickerMenu);
                    return 1; //TODO command for the menu?
                }))
                 */
                .then(literal("rgb")
                        .then(argument("r", IntegerArgumentType.integer(0, 255))
                                .then(argument("g", IntegerArgumentType.integer(0, 255)).
                                        then(argument("b", IntegerArgumentType.integer(0, 255)).executes((context) -> {

                                            int r = context.getArgument("r", Integer.class);
                                            int g = context.getArgument("g", Integer.class);
                                            int b = context.getArgument("b", Integer.class);

                                            copyColor(new Color(r, g, b));
                                            return 1;

                                        })))
                        ))
                .then(literal("hex")
                        .then(argument("color", StringArgumentType.greedyString()).executes((context) -> {
                            String color = context.getArgument("color", String.class);
                            Color hex;
                            try {
                                hex = Color.decode(color);
                            } catch (NumberFormatException e) {
                                ChatUtil.sendMessage("Invalid Hex!", ChatType.FAIL);
                                return -1;
                            }
                            copyColor(hex);
                            return 1;
                        })))
                .then(literal("hsb")
                        .then(argument("h", IntegerArgumentType.integer(0, 360))
                                .then(argument("s", IntegerArgumentType.integer(0, 360)).
                                        then(argument("b", IntegerArgumentType.integer(0, 360)).executes((context) -> {

                                            float h = context.getArgument("h", Integer.class) / 360.0f;
                                            float s = context.getArgument("s", Integer.class) / 360.0f;
                                            float b = context.getArgument("b", Integer.class) / 360.0f;

                                            copyColor(Color.getHSBColor(h, s, b));
                                            return 1;
                                        })))
                        )));

    }

    @Override
    public String getDescription() {
        return "[blue]/color rgb <r> <g> <b>[reset]\n"
            + "[blue]/color hex <hex>[reset]\n"
            + "[blue]/color hsb <h> <s> <b>[reset]\n"
            + "\n"
            + "Copies the specified color in DiamondFire hex color format.\n"
            + "The max number is [green]256[reset] for RGB colors, and [green]360[reset] for HSB colors.\n"
            + "[yellow]Example[reset]: /color 255 0 0 -> &x&f&f&0&0&0&0";
    }

    @Override
    public String getName() {
        return "/color";
    }


    private void copyColor(Color color) {
        String colorName = Integer.toHexString(color.getRGB()).substring(2);

        String colorNameReal = "#" + Integer.toHexString(color.getRGB()).substring(2);
        Style colorStyle = Style.EMPTY.withColor(TextColor.fromRgb(color.getRGB()));

        LiteralText text = new LiteralText("Copied Color! ");
        LiteralText preview = new LiteralText("█");
        LiteralText hover = new LiteralText(colorNameReal);
        hover.append("\n§7Click to copy!");
        hover.setStyle(colorStyle);
        preview.styled((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/color hex " + colorNameReal)));
        preview.styled((style) -> style.withHoverEvent(HoverEvent.Action.SHOW_TEXT.buildHoverEvent(hover)));

        
        
        CodeUtilities.MC.keyboard.setClipboard("&x&" + String.join("&", colorName.split("")));
        ChatUtil.sendMessage(text.append(ChatUtil.setColor(preview, color)), ChatType.INFO_BLUE);
    }
}
