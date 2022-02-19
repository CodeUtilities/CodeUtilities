package io.github.codeutilities.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.widget.CWidget;
import io.github.codeutilities.util.RenderUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

public class CScreen extends Screen {

    private final int width, height;
    public final List<CWidget> widgets = new ArrayList<>();

    protected CScreen(int width, int height) {
        super(new TextComponent("CodeUtilities Screen"));
        this.width = width;
        this.height = height;
        CodeUtilities.MC.keyboardHandler.setSendRepeatsToGui(true);
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float tickDelta) {
        renderBackground(stack);
        stack.pushPose();
        Minecraft mc = CodeUtilities.MC;

        stack.translate(mc.screen.width/2f, mc.screen.height/2f, 0);

        float s = (float) mc.getWindow().getGuiScale();
        stack.scale(s,s,0);

        stack.translate(-width/2f, -height/2f, 0);

        RenderUtil.renderGui(stack,0,0,width,height);

        mouseX += -mc.screen.width/2;
        mouseY += -mc.screen.height/2;

        mouseX /= s;
        mouseY /= s;

        mouseX += width/2;
        mouseY += height/2;

        for (CWidget cWidget : widgets) {
            cWidget.render(stack, mouseX, mouseY, tickDelta);
        }
        for (CWidget cWidget : widgets) {
            cWidget.renderOverlay(stack, mouseX, mouseY, tickDelta);
        }
        stack.popPose();
        super.render(stack, mouseX, mouseY, tickDelta);
    }

    @Override
    public boolean charTyped(char ch, int keyCode) {
        for (CWidget cWidget : widgets) {
            cWidget.charTyped(ch, keyCode);
        }
        return super.charTyped(ch, keyCode);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (CWidget cWidget : widgets) {
            cWidget.keyPressed(keyCode, scanCode, modifiers);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        mouseX = translateMouseX(mouseX);
        mouseY = translateMouseY(mouseY);

        for (CWidget cWidget : widgets) {
            cWidget.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        mouseX = translateMouseX(mouseX);
        mouseY = translateMouseY(mouseY);

        for (CWidget cWidget : widgets) {
            cWidget.mouseScrolled(mouseX, mouseY, amount);
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    public double translateMouseX(double mouseX) {
        Minecraft mc = CodeUtilities.MC;
        float s = (float) mc.getWindow().getGuiScale();
        mouseX += -mc.screen.width/2f;
        mouseX /= s;
        mouseX += width/2f;
        return mouseX;
    }

    public double translateMouseY(double mouseY) {
        Minecraft mc = CodeUtilities.MC;
        float s = (float) mc.getWindow().getGuiScale();
        mouseY += -mc.screen.height/2f;
        mouseY /= s;
        mouseY += height/2f;
        return mouseY;
    }
}
