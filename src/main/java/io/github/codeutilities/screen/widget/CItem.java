package io.github.codeutilities.screen.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.util.RenderUtil;
import java.awt.Rectangle;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag.Default;

public class CItem implements CWidget {

    private final int x;
    private final int y;
    private final ItemStack item;
    private Consumer<Integer> onClick;

    public CItem(int x, int y, ItemStack item) {
        this.x = x;
        this.y = y;
        this.item = item;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float tickDelta) {
        stack.pushPose();
        stack.translate(x, y, 0);
        RenderUtil.renderGuiItem(stack, item);
        stack.popPose();
    }

    @Override
    public void renderOverlay(PoseStack stack, int mouseX, int mouseY, float tickDelta) {
        Rectangle rect = new Rectangle(x, y,8, 8);

        if (rect.contains(mouseX, mouseY)) {
            stack.pushPose();
            stack.translate(mouseX, mouseY, 0);
            stack.scale(0.5f, 0.5f, 1f);
            CodeUtilities.MC.screen.renderTooltip(stack, item.getTooltipLines(
                CodeUtilities.MC.player, Default.NORMAL
            ), Optional.empty(), 0, 0);
            stack.popPose();
        }
    }

    @Override
    public void mouseClicked(double x, double y, int button) {
        Rectangle rect = new Rectangle(this.x, this.y, 8, 8);

        if (rect.contains(x, y)) {
            if (onClick != null) {
                CodeUtilities.MC.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1f));
                onClick.accept(button);
            }
        }

        CWidget.super.mouseClicked(x, y, button);
    }

    public void setClickListener(Consumer<Integer> onClick) {
        this.onClick = onClick;
    }

}
