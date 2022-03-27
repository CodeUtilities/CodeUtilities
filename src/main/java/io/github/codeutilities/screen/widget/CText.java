package io.github.codeutilities.screen.widget;

import io.github.codeutilities.CodeUtilities;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class CText implements CWidget {

    int x;
    int y;
    Text text;
    int color;
    float scale;
    boolean centered;
    boolean shadow;

    public CText(int x, int y, Text text, int color, float scale, boolean centered, boolean shadow) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
        this.scale = scale;
        this.centered = centered;
        this.shadow = shadow;
    }

    public CText(int x, int y, Text text) {
        this(x, y, text, 0x333333, 1, false, false);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        stack.push();
        stack.translate(x, y, 0);

        stack.scale(0.5f, 0.5f, 0.5f);

        TextRenderer f = CodeUtilities.MC.textRenderer;

        if (centered) {
            stack.translate(-f.getWidth(text) / 2f, -f.fontHeight / 2f, 0);
        }

        if (shadow) {
            f.drawWithShadow(stack, text, 0, 0, color);
        } else {
            f.draw(stack, text, 0, 0, color);
        }
        stack.pop();
    }

    public void setText(Text t) {
        text = t;
    }
}
