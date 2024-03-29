package io.github.codeutilities.mixin.render;

import static net.minecraft.client.gui.DrawableHelper.fill;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import io.github.codeutilities.config.Config;
import io.github.codeutilities.features.sidedchat.ChatRule;
import io.github.codeutilities.util.SoundUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatHud.class)
public abstract class MChatHud {

    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    private int scrolledLines;
    @Shadow
    @Final
    private List<ChatHudLine<OrderedText>> visibleMessages;
    @Shadow
    @Final
    private Deque<Text> messageQueue;
    @Shadow
    private boolean hasUnreadNewMessages;
    private int oldSideChatWidth = -1;
    private int sideScrolledLines;

    @Shadow
    public static int getWidth(double widthOption) {
        return 0;
    }

    @Shadow
    private static double getMessageOpacityMultiplier(int age) {
        return 0;
    }

    @Shadow
    protected abstract boolean isChatFocused();

    @Shadow
    protected abstract boolean isChatHidden();

    @Shadow
    protected abstract void processMessageQueue();

    @Shadow
    public abstract double getChatScale();

    @Shadow
    public abstract int getVisibleLineCount();

    @Shadow
    public abstract int getWidth();

    @Shadow
    public abstract void reset();

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(MatrixStack matrices, int tickDelta, CallbackInfo ci) {
        this.processMessageQueue();
        int renderedLines = renderChat(matrices, tickDelta, visibleMessages, 0, getWidth(),
                scrolledLines);
        renderChat(matrices, tickDelta, ChatRule.sideVisibleMessages, getSideChatStartX(),
                getSideChatWidth(), sideScrolledLines);
        renderOthers(matrices, renderedLines);
        ci.cancel();
    }

    /**
     * Renders a chat box, drawn into its own function so I don't repeat code for side chat Most
     * params are just stuff the code needs and I don't have the confidence to change
     *
     * @param displayX      X to display at
     * @param width         Width of the chat to display
     * @param scrolledLines The amount of lines scrolled on this chat
     * @return The amount of lines actually rendered. Other parts of rendering need to know this
     */
    @SuppressWarnings("deprecation")
    private int renderChat(MatrixStack matrices, int tickDelta,
                           List<ChatHudLine<OrderedText>> visibleMessages, int displayX, int width,
                           int scrolledLines) {
        // reset chat (re-allign all text) whenever calculated size for side chat changes
        int newSideChatWidth = getSideChatWidth();
        if (newSideChatWidth != oldSideChatWidth) {
            oldSideChatWidth = newSideChatWidth;
            reset();
        }

        // will apologise - most code is taken from deobfuscated minecraft jar
        // have attempted to make it as readable as possible but some lines idk man no clue
        int visibleLineCount = this.getVisibleLineCount();
        int visibleMessagesSize = visibleMessages.size();
        int renderedLines = 0;
        if (visibleMessagesSize > 0) {
            boolean chatFocused = this.isChatFocused();

            double d = this.getChatScale();
            int k = MathHelper.ceil((double) width / d);
            matrices.push();
            matrices.translate(4.0, 8.0, 0.0);
            matrices.scale((float) d, (float) d, 1.0f);
            double opacity = this.client.options.chatOpacity * 0.8999999761581421D + 0.10000000149011612D;
            double backgroundOpacity = this.client.options.textBackgroundOpacity;
            double lineSpacing = 9.0D * (this.client.options.chatLineSpacing + 1.0D);
            double lineSpacing2 = -8.0D * (this.client.options.chatLineSpacing + 1.0D)
                    + 4.0D * this.client.options.chatLineSpacing;

            for (int i = 0; i + scrolledLines < visibleMessages.size() && i < visibleLineCount;
                 ++i) {
                ChatHudLine<OrderedText> chatHudLine = visibleMessages.get(i + scrolledLines);
                if (chatHudLine != null) {
                    int ticksSinceCreation = tickDelta - chatHudLine.getCreationTick();
                    if (ticksSinceCreation < 200 || chatFocused) {
                        double o =
                                chatFocused ? 1.0D : getMessageOpacityMultiplier(ticksSinceCreation);
                        int aa = (int) (255.0D * o * opacity);
                        int ab = (int) (255.0D * o * backgroundOpacity);
                        ++renderedLines;
                        if (aa > 3) {
                            double s = (double) (-i) * lineSpacing;
                            matrices.push();
                            matrices.translate(displayX, 0, 50.0D);
                            fill(matrices, -2, (int) (s - lineSpacing), k + 4, (int) s, ab << 24);
                            RenderSystem.enableBlend();
                            matrices.translate(0.0D, 0.0D, 50.0D);
                            this.client.textRenderer.drawWithShadow(matrices, chatHudLine.getText(),
                                    0.0F, (float) ((int) (s + lineSpacing2)), 16777215 + (aa << 24));
                            RenderSystem.disableBlend();
                            matrices.pop();
                        }
                    }
                }
            }
            // in case you're wondering, the splitting of the text by width is done as its received, not upon rendering
            matrices.pop();
        }
        return renderedLines;
    }

    @SuppressWarnings("deprecation")
    private void renderOthers(MatrixStack matrices, int renderedLines) {
        int visibleMessagesSize = this.visibleMessages.size();
        if (visibleMessagesSize == 0) {
            return;
        }
        boolean chatFocused = this.isChatFocused();

        double chatScale = this.getChatScale();
        int k = MathHelper.ceil((double) this.getWidth() / chatScale);
        matrices.push();
        matrices.translate(4.0, 8.0, 0.0);
        matrices.scale((float) chatScale, (float) chatScale, 1.0f);
        double opacity =
                this.client.options.chatOpacity * 0.8999999761581421D + 0.10000000149011612D;
        double backgroundOpacity = this.client.options.textBackgroundOpacity;

        if (!this.messageQueue.isEmpty()) {
            int m = (int) (128.0D * opacity);
            int w = (int) (255.0D * backgroundOpacity);
            matrices.push();
            matrices.translate(0.0D, 0.0D, 50.0D);
            fill(matrices, -2, 0, k + 4, 9, w << 24);
            RenderSystem.enableBlend();
            matrices.translate(0.0D, 0.0D, 50.0D);
            this.client.textRenderer.drawWithShadow(matrices,
                    new TranslatableText("chat.queue", this.messageQueue.size()), 0.0F, 1.0F,
                    16777215 + (m << 24));
            matrices.pop();
            RenderSystem.disableBlend();
        }

        if (chatFocused) {
            int v = 9;
            matrices.translate(-3.0F, 0.0F, 0.0F);
            int w = visibleMessagesSize * v + visibleMessagesSize;
            int x = renderedLines * v + renderedLines;
            int y = scrolledLines * x / visibleMessagesSize;
            int z = x * x / w;
            if (w != x) {
                int aa = y > 0 ? 170 : 96;
                int ab = this.hasUnreadNewMessages ? 13382451 : 3355562;
                fill(matrices, 0, -y, 2, -y - z, ab + (aa << 24));
                fill(matrices, 2, -y, 1, -y - z, 13421772 + (aa << 24));
            }
        }
        matrices.pop();
    }

    private int getSideChatStartX() {
        return (int) ((this.client.getWindow().getScaledWidth() - getSideChatWidth())
                / getSideChatScale()) - 6;
    }

    private int getSideChatWidth() {
        int configWidth = Config.getInteger("sidechat_width");

        // if the width in config is valid
        if (configWidth > 0) {
            return configWidth;
        } else { // else if 0 or less, auto size the side chat
            int rawWidth = Math.min(
                    (this.client.getWindow().getScaledWidth() - getWidth() - 14),
                    getWidth()
            );
            // if the calculated width <= 0 (window really small), have 1 as a failsafe value
            return rawWidth > 0 ? rawWidth : 1;
        }
    }

    // just incase i want to re-add the option to change side chat scale
    private double getSideChatScale() {
        return getChatScale();
    }

    @Inject(method = "clear", at = @At("TAIL"))
    private void clear(boolean clearHistory, CallbackInfo ci) {
        ChatRule.sideVisibleMessages.clear();
    }


    @Inject(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", at = @At("TAIL"), cancellable = true)
    private void addMessage(Text message, int messageId, int timestamp, boolean refresh,
                            CallbackInfo ci) {
        boolean matchedARule = false;
        for (ChatRule chatRule : ChatRule.getChatRules()) {
            // compare against all rules
            if (chatRule.matches(message)) {
                // also don't add to chat if the chat side is either
                if (!matchedARule) {
                    addToChat(chatRule.getChatSide(), message, messageId, timestamp);
                    matchedARule = true;
                }

                // dont play sound if message is just being refreshed (ie, when window changes size)
                // & dont try to play a null sound (when the sound is set to 'None')
                if (!refresh && chatRule.getChatSound() != null) {
                    SoundUtil.playSound(chatRule.getChatSound());
                }
            }
        }

        // if matched rule, remove last from
        if (matchedARule) {
            int i = MathHelper.floor((double) this.getWidth() / this.getChatScale());
            int addedMessageCount = ChatMessages.breakRenderedChatMessageLines(message, i,
                    this.client.textRenderer).size();
            // remove the last addedMessageCount messages from the visible messages
            // this has the effect of removing last message sent to main (to go to side instead)
            visibleMessages.subList(0, addedMessageCount).clear();
        }
    }

    public void addToChat(ChatRule.ChatSide side, Text message, int chatLineId,
                           int updateCounter) {
        int i;
        switch (side) {
            case MAIN:
            default:
                i = MathHelper.floor((double) this.getWidth() / this.getChatScale());
                break;
            case SIDE:
                i = MathHelper.floor((double) this.getSideChatWidth() / this.getSideChatScale());
                break;
        }

        List<ChatHudLine<OrderedText>> outputChatLines =
                ChatMessages.breakRenderedChatMessageLines(message, i, this.client.textRenderer)
                        .stream()
                        .map(iTextComponent -> new ChatHudLine<>(updateCounter, iTextComponent, chatLineId))
                        .collect(Collectors.toList());
        Collections.reverse(outputChatLines);
        this.getChatLines(side).addAll(0, outputChatLines);
    }

    private List<ChatHudLine<OrderedText>> getChatLines(ChatRule.ChatSide chatSide) {
        switch (chatSide) {
            case MAIN:
            default:
                return visibleMessages;
            case SIDE:
                return ChatRule.sideVisibleMessages;
        }
    }

    @Inject(method = "removeMessage", at = @At("TAIL"))
    private void removeMessage(int messageId, CallbackInfo ci) {
        ChatRule.sideVisibleMessages.removeIf((message) ->
                message.getId() == messageId
        );
    }

    @Inject(method = "reset", at = @At("HEAD"))
    private void reset(CallbackInfo ci) {
        ChatRule.sideVisibleMessages.clear();
    }

    // another copy from minecraft decompiled code
    // the main difference is switching references from main to side chat
    // and subtracting getSideChatStartX() from the adjusted x
    @Inject(method = "getText", at = @At("HEAD"), cancellable = true)
    private void getText(double x, double y, CallbackInfoReturnable<Style> cir) {
        if (this.isChatFocused() && !this.client.options.hudHidden && !this.isChatHidden()) {
            double scale = this.getSideChatScale();
            double adjustedX = (x - 2.0D) - getSideChatStartX();
            double adjustedY = (double) this.client.getWindow().getScaledHeight() - y - 40.0D;
            adjustedX = MathHelper.floor(adjustedX / scale);
            adjustedY = MathHelper.floor(
                    adjustedY / (scale * (this.client.options.chatLineSpacing + 1.0D)));
            if (!(adjustedX < 0.0D) && !(adjustedY < 0.0D)) {
                int size = Math.min(this.getVisibleLineCount(), ChatRule.sideVisibleMessages.size());
                if (adjustedX <= (double) MathHelper.floor(
                        (double) this.getSideChatWidth() / scale)) {
                    if (adjustedY < (double) (9 * size + size)) {
                        int line = (int) (adjustedY / 9.0D + (double) sideScrolledLines);
                        if (line >= 0 && line < ChatRule.sideVisibleMessages.size()) {
                            ChatHudLine<OrderedText> chatHudLine = ChatRule.sideVisibleMessages.get(
                                    line);
                            cir.setReturnValue(this.client.textRenderer.getTextHandler()
                                    .getStyleAt(chatHudLine.getText(), (int) adjustedX));
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "resetScroll", at = @At("TAIL"))
    private void resetScroll(CallbackInfo ci) {
        sideScrolledLines = 0;
    }

    @Inject(method = "scroll", at = @At("TAIL"))
    private void scroll(int amount, CallbackInfo ci) {
        sideScrolledLines = (int) ((double) this.scrolledLines + amount);
        int i = ChatRule.sideVisibleMessages.size();
        if (sideScrolledLines > i - this.getVisibleLineCount()) {
            sideScrolledLines = i - this.getVisibleLineCount();
        }

        if (sideScrolledLines <= 0) {
            sideScrolledLines = 0;
        }
    }
}