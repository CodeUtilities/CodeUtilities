package io.github.codeutilities.util;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentUtil {

    public static Text fromString(String message) {
        LiteralText result = new LiteralText("");

        try {
            Pattern pattern = Pattern.compile("(§[a-f0-9lonmkrA-FLONMRK]|§x(§[a-f0-9A-F]){6})");
            Matcher matcher = pattern.matcher(message);

            Style s = Style.EMPTY;

            int lastIndex = 0;
            while (matcher.find()) {
                int start = matcher.start();
                String text = message.substring(lastIndex, start);
                if (text.length() != 0) {
                    Text t = new LiteralText(text);
                    t.getWithStyle(s);
                    result.append(t);
                }
                String col = matcher.group();

                if (col.length() == 2) {
                    s = s.withFormatting(Formatting.byCode(col.charAt(1)));
                } else {
                    s = Style.EMPTY.withColor(
                        TextColor.parse("#" + col.replaceAll("§", "").substring(1)));
                }
                lastIndex = matcher.end();
            }
            String text = message.substring(lastIndex);
            if (text.length() != 0) {
                LiteralText t = new LiteralText(text);
                t.setStyle(s);
                result.append(t);
            }
        } catch (Exception err) {
            err.printStackTrace();
            return new LiteralText("CodeUtilities Text Error");
        }

        return result;
    }

    public static String toFormattedString(LiteralText message) {
        StringBuilder result = new StringBuilder();

        Style style = message.getStyle();

        String format = "";

        if (style.getColor() != null) {
            format += "§x§" + String.join("§", String.format("%06X", style.getColor().getName()).split(""));
        }

        if (style.isBold()) {
            format += "§l";
        }
        if (style.isItalic()) {
            format += "§o";
        }
        if (style.isUnderlined()) {
            format += "§n";
        }
        if (style.isStrikethrough()) {
            format += "§m";
        }
        if (style.isObfuscated()) {
            format += "§k";
        }

        result.append(format);
        result.append(message.asString());

        for (Text sibling : message.getSiblings()) {
            result.append(toFormattedString(new LiteralText(sibling.toString())));
        }

        return result.toString();
    }
}
