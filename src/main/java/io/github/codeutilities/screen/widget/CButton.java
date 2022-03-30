package io.github.codeutilities.screen.widget;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.util.RenderUtil;
import java.awt.Rectangle;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;

public class CButton implements CWidget {

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private String text;
    private Runnable onClick;

    public CButton(int x, int y, int width, int height, String text, Runnable onClick) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.onClick = onClick;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
        stack.push();
        stack.translate(x, y, 0);

        stack.push();
        stack.scale(0.5f, 0.5f, 0.5f);

        Rectangle rect = new Rectangle(x, y, width, height);

        RenderUtil.renderButton(stack, 0, 0, width * 2, height * 2, rect.contains(mouseX, mouseY), false);
        stack.pop();

        TextRenderer f = CodeUtilities.MC.textRenderer;

        stack.translate(rect.width / 2f, rect.height / 2f, 0);
        stack.scale(0.5f, 0.5f, 0.5f);
        stack.translate(-f.getWidth(text) / 2f, -f.fontHeight / 2f, 0);

        f.drawWithShadow(stack, text, 0, 0, 0xFFFFFF);

        stack.pop();
    }

    @Override
    public void mouseClicked(double x, double y, int button) {
        Rectangle rect = new Rectangle(this.x, this.y, width, height);

        if (rect.contains(x, y)) {
            CodeUtilities.MC.getSoundManager().play(PositionedSoundInstance.ambient(SoundEvents.UI_BUTTON_CLICK, 1f,1f));
            onClick.run();
        }

        CWidget.super.mouseClicked(x, y, button);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    public void setText(String text) {
        this.text = text;
    }
}