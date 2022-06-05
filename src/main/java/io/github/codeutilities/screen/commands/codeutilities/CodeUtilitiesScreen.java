package io.github.codeutilities.screen.commands.codeutilities;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.config.menu.ConfigScreen;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CButton;
import io.github.codeutilities.screen.widget.CImage;
import io.github.codeutilities.screen.widget.CPlainPanel;
import io.github.codeutilities.screen.widget.CText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmChatLinkScreen;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Util;

public class CodeUtilitiesScreen extends CScreen {
    private static final String CODEUTILS_LOGO = "codeutilities:icon.png";

    public CodeUtilitiesScreen(String... args) {
        super(110, 95);
        CPlainPanel root = new CPlainPanel(0, 0, 110, 110);

        CImage cImage = new CImage(23, -5, 64, 64, CODEUTILS_LOGO);
        root.add(cImage);
//(220 - MinecraftClient.getInstance().textRenderer.getWidth("CodeUtilities")) / 2
        root.add(new CText(55, 57,
                new LiteralText("CodeUtilities"), 0x333333, 1.5f, true, false));
        //(220 - MinecraftClient.getInstance().textRenderer.getWidth("v" + CodeUtilities.MOD_VERSION)) / 2
        root.add(new CText(55, 63,
                new LiteralText("v" + CodeUtilities.MOD_VERSION), 0x333333, 1f, true, false));

            addButtons(root);
        widgets.add(root);
    }

    private void addButtons(CPlainPanel panel) {
        // ------------------------ Features Button ------------------------
        CButton featuresButton = new CButton(5, 70, 50, 10, 0.8f, "Help / Features", () -> {
            FeaturesScreen gui_1 = new FeaturesScreen();
            CodeUtilities.MC.send(() -> CodeUtilities.MC.setScreen(gui_1));
        });
        panel.add(featuresButton);

        // ------------------------ Contributors Button ------------------------
        CButton contributorsButton = new CButton(5, 80, 50, 10, 0.8f, "Contributors", () -> {
            ContributorsScreen gui_2 = ContributorsScreen.getInstance();
            CodeUtilities.MC.send(() -> CodeUtilities.MC.setScreen(gui_2));
        });
        panel.add(contributorsButton);

        // ------------------------ Bug Report Button ------------------------
        CButton bugReport = new CButton(55, 70, 50, 10, 0.8f, "Bug Report", () -> {
            String link = "https://github.com/CodeUtilities/CodeUtilities/issues";

            ConfirmChatLinkScreen gui_3 = new ConfirmChatLinkScreen((bool) -> {
                if (bool) {
                    Util.getOperatingSystem().open(link);
                }
                CodeUtilitiesScreen gui = new CodeUtilitiesScreen();
                CodeUtilities.MC.send(() -> CodeUtilities.MC.setScreen(gui));
            }, link, false);
            CodeUtilities.MC.send(() -> CodeUtilities.MC.setScreen(gui_3));
        });
        panel.add(bugReport);

        // ------------------------ Options Button ------------------------
        CButton options = new CButton(55, 80, 50, 10, 0.8f, "Options", () -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            CodeUtilities.MC.send(() -> CodeUtilities.MC.setScreen(
                    ConfigScreen.getScreen(MinecraftClient.getInstance().currentScreen)));
        });
        panel.add(options);

    }


}