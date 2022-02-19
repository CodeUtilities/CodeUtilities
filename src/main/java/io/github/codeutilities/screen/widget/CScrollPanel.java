package io.github.codeutilities.screen.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector4f;
import io.github.codeutilities.util.RenderUtil;
import java.util.ArrayList;
import java.util.List;

public class CScrollPanel implements CWidget {

    private final List<CWidget> children = new ArrayList<>();
    private int scroll = 0;
    private final int x, y, width, height;

    public CScrollPanel(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float tickDelta) {
        stack.pushPose();
        stack.translate(x, y, 0);

        Vector4f begin = new Vector4f(0, 0, 1, 1);
        Vector4f end = new Vector4f(width, height, 1, 1);
        begin.transform(stack.last().pose());
        end.transform(stack.last().pose());

        RenderUtil.setScissor(
            (int) begin.x()*2,
            (int) begin.y()*2,
            (int) (end.x() - begin.x())*2,
            (int) (end.y() - begin.y())*2
        );

        stack.translate(0, scroll, 0);
        mouseY -= scroll;

        for (CWidget child : children) {
            child.render(stack, mouseX, mouseY, tickDelta);
        }

        RenderUtil.clearScissor();
        stack.popPose();
    }

    @Override
    public void mouseClicked(double x, double y, int button) {
        y -= scroll;
        for (CWidget child : children) {
            child.mouseClicked(x, y, button);
        }
    }

    @Override
    public void charTyped(char ch, int keyCode) {
        for (CWidget child : children) {
            child.charTyped(ch, keyCode);
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        for (CWidget child : children) {
            child.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double amount) {
        for (CWidget child : children) {
            child.mouseScrolled(mouseX, mouseY, amount);
        }
        scroll += amount * 5;
    }

    public void add(CWidget child) {
        children.add(child);
    }

    @Override
    public void renderOverlay(PoseStack stack, int mouseX, int mouseY, float tickDelta) {
        mouseY -= scroll;

        for (CWidget child : children) {
            child.renderOverlay(stack, mouseX, mouseY, tickDelta);
        }
    }
}
