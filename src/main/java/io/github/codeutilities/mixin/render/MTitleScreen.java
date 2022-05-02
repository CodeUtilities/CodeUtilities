package io.github.codeutilities.mixin.render;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.config.Config;
import io.github.codeutilities.config.internal.DfButtonTextLocations;
import io.github.codeutilities.util.hypercube.HypercubeServer;
import io.github.codeutilities.util.render.BlendableTexturedButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MTitleScreen extends Screen {

    protected MTitleScreen(LiteralText title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "initWidgetsNormal")
    public void drawMenuButton(int y, int spacingY, CallbackInfo info) {
        Identifier identifier_main = new Identifier(CodeUtilities.MOD_ID + ":textures/gui/df.png");
        Identifier identifier_beta = new Identifier(CodeUtilities.MOD_ID + ":textures/gui/beta.png");
        Identifier identifier_node1 = new Identifier(CodeUtilities.MOD_ID + ":textures/gui/node1.png");
        Identifier identifier_node2 = new Identifier(CodeUtilities.MOD_ID + ":textures/gui/node2.png");
        Identifier identifier_node3 = new Identifier(CodeUtilities.MOD_ID + ":textures/gui/node3.png");
        Identifier identifier_node4 = new Identifier(CodeUtilities.MOD_ID + ":textures/gui/node4.png");
        Identifier identifier_node5 = new Identifier(CodeUtilities.MOD_ID + ":textures/gui/node5.png");
        Identifier identifier_node6 = new Identifier(CodeUtilities.MOD_ID + ":textures/gui/node6.png");
        Identifier identifier_node7 = new Identifier(CodeUtilities.MOD_ID + ":textures/gui/node7.png");

        if (Config.getBoolean("dfButton")) {
            if (!Config.getBoolean("dfNodeButtons")) {
                // Default Server Join
                this.addDrawableChild(new BlendableTexturedButtonWidget(this.width / 2 + 104, y + spacingY, 20, 20, 0, 0, 20, identifier_main, 20, 40,
                        (button) -> HypercubeServer.PROXY.connect()));
            }
        }

        if (Config.getBoolean("dfNodeButtons")) {
            // Default Server Join
            this.addDrawableChild(new BlendableTexturedButtonWidget(this.width / 2 + 104, y - spacingY, 20, 20, 0, 0, 20, identifier_main, 20, 40,
                    (button) -> HypercubeServer.PROXY.connect()));

            // Node Beta
            this.addDrawableChild(new BlendableTexturedButtonWidget(this.width / 2 + 104 + 22, y - spacingY, 20, 20, 0, 0, 20, identifier_beta, 20, 40,
                    (button) -> HypercubeServer.NODE_BETA.connect()));

            // Node 1
            this.addDrawableChild(new BlendableTexturedButtonWidget(this.width / 2 + 104 + 44, y - spacingY, 20, 20, 0, 0, 20, identifier_node1, 20, 40,
                    (button) -> HypercubeServer.NODE_1.connect()));

            // Node 2
            this.addDrawableChild(new BlendableTexturedButtonWidget(this.width / 2 + 104, y, 20, 20, 0, 0, 20, identifier_node2, 20, 40,
                    (button) -> HypercubeServer.NODE_2.connect()));

            // Node 3
            this.addDrawableChild(new BlendableTexturedButtonWidget(this.width / 2 + 104 + 22, y, 20, 20, 0, 0, 20, identifier_node3, 20, 40,
                    (button) -> HypercubeServer.NODE_3.connect()));

            // Node 4
            this.addDrawableChild(new BlendableTexturedButtonWidget(this.width / 2 + 104 + 44, y, 20, 20, 0, 0, 20, identifier_node4, 20, 40,
                    (button) -> HypercubeServer.NODE_4.connect()));

            // Node 5
            this.addDrawableChild(new BlendableTexturedButtonWidget(this.width / 2 + 104, y + spacingY, 20, 20, 0, 0, 20, identifier_node5, 20, 40,
                    (button) -> HypercubeServer.NODE_5.connect()));

            // Node 6
            this.addDrawableChild(new BlendableTexturedButtonWidget(this.width / 2 + 104 + 22, y + spacingY, 20, 20, 0, 0, 20, identifier_node6, 20, 40,
                    (button) -> HypercubeServer.NODE_6.connect()));

            // Node 7
            this.addDrawableChild(new BlendableTexturedButtonWidget(this.width / 2 + 104 + 44, y + spacingY, 20, 20, 0, 0, 20, identifier_node7, 20, 40,
                    (button) -> HypercubeServer.NODE_7.connect()));
        }
    }

}