package io.github.codeutilities.mixin.render;

import io.github.codeutilities.config.Config;
import io.github.codeutilities.features.PlayerState;
import io.github.codeutilities.features.commands.schem.sk89q.worldedit.math.Vector3;
import io.github.codeutilities.features.commands.search.CodeSearcher;
import net.minecraft.block.BlockState;
import net.minecraft.block.SignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.text.OrderedText;
import net.minecraft.util.SignType;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mixin(SignBlockEntityRenderer.class)
public class MSignRenderer {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Shadow
    @Final
    private Map<SignType, SignBlockEntityRenderer.SignModel> typeToModel;

    @Shadow
    @Final
    private TextRenderer textRenderer;

    @Shadow
    private static boolean shouldRender(SignBlockEntity sign, int signColor) {return true;}

    @Shadow
    private static int getColor(SignBlockEntity sign) {return 0;}

    /**
     * @author CodeUtilities
     * @reason yea
     */
    @Overwrite
    public void render(SignBlockEntity signBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {

        // TODO re-enable if we add back sign render distance
        //if (!signBlockEntity.getPos().isWithinDistance(mc.cameraEntity.getBlockPos(), Config.getInteger("signRenderDistance")))
        //    return;

        TextRenderer textRenderer = mc.textRenderer;

        if (CodeSearcher.shouldGlow(signBlockEntity) && PlayerState.getMode() == PlayerState.Mode.DEV && mc.player.isCreative()) {
            double distance = Math.sqrt(signBlockEntity.getPos().getSquaredDistance(mc.cameraEntity.getBlockPos()));
            double dist = MathHelper.clamp(distance, 1, 15);

            OutlineVertexConsumerProvider outlineVertexConsumerProvider = mc.getBufferBuilders().getOutlineVertexConsumers();
            outlineVertexConsumerProvider.setColor(255, 255, 255, (int) (dist * 17));
            vertexConsumerProvider = outlineVertexConsumerProvider;
        }

        BlockState blockState = signBlockEntity.getCachedState();
        matrixStack.push();
        float g = 0.6666667F;
        SignType signType = SignBlockEntityRenderer.getSignType(blockState.getBlock());
        SignBlockEntityRenderer.SignModel signModel = (SignBlockEntityRenderer.SignModel)this.typeToModel.get(signType);
        float h;
        if (blockState.getBlock() instanceof SignBlock) {
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            h = -((float)((Integer)blockState.get(SignBlock.ROTATION) * 360) / 16.0F);
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(h));
            signModel.stick.visible = true;
        } else {
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            h = -((Direction)blockState.get(WallSignBlock.FACING)).asRotation();
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(h));
            matrixStack.translate(0.0D, -0.3125D, -0.4375D);
            signModel.stick.visible = false;
        }

        matrixStack.push();
        matrixStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
        SpriteIdentifier spriteIdentifier = TexturedRenderLayers.getSignTextureId(signType);
        Objects.requireNonNull(signModel);
        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumerProvider, signModel::getLayer);
        signModel.root.render(matrixStack, vertexConsumer, i, j);
        matrixStack.pop();
        float k = 0.010416667F;
        matrixStack.translate(0.0D, 0.3333333432674408D, 0.046666666865348816D);
        matrixStack.scale(0.010416667F, -0.010416667F, 0.010416667F);
        int l = getColor(signBlockEntity);
        OrderedText[] orderedTexts = signBlockEntity.updateSign(MinecraftClient.getInstance().shouldFilterText(), (text) -> {
            List<OrderedText> list = this.textRenderer.wrapLines(text, 90);
            return list.isEmpty() ? OrderedText.EMPTY : (OrderedText)list.get(0);
        });
        int n;
        boolean bl;
        int o;
        if (signBlockEntity.isGlowingText()) {
            n = signBlockEntity.getTextColor().getSignColor();
            bl = shouldRender(signBlockEntity, n);
            o = 15728880;
        } else {
            n = l;
            bl = false;
            o = i;
        }

        for(int p = 0; p < 4; ++p) {
            OrderedText orderedText = orderedTexts[p];
            float q = (float)(-this.textRenderer.getWidth(orderedText) / 2);
            if (bl) {
                this.textRenderer.drawWithOutline(orderedText, q, (float)(p * 10 - 20), n, l, matrixStack.peek().getPositionMatrix(), vertexConsumerProvider, o);
            } else {
                this.textRenderer.draw(orderedText, q, (float)(p * 10 - 20), n, false, matrixStack.peek().getPositionMatrix(), vertexConsumerProvider, false, 0, o);
            }
        }

        matrixStack.pop();
    }

}
