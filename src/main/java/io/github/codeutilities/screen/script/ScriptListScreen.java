package io.github.codeutilities.screen.script;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CButton;
import io.github.codeutilities.screen.widget.CScrollPanel;
import io.github.codeutilities.screen.widget.CText;
import io.github.codeutilities.screen.widget.CWidget;
import io.github.codeutilities.script.Script;
import io.github.codeutilities.script.ScriptManager;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

public class ScriptListScreen extends CScreen {

    private final List<CWidget> contextMenu = new ArrayList<>();

    public ScriptListScreen() {
        super(90, 100);
        CScrollPanel panel = new CScrollPanel(0, 5, 120, 90);
        widgets.add(panel);

        int y = 0;
        for (Script s : ScriptManager.getInstance().getScripts()) {
            MutableText text = new LiteralText(s.getName());
            if (s.disabled()) {
                text = text.formatted(Formatting.GRAY);
            }
            panel.add(new CText(6, y + 2, text));

            panel.add(new CButton(3, y-1, 82, 10, "",() -> {}) {
                @Override
                public void render(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
                    Rectangle b = getBounds();
                    if (b.contains(mouseX, mouseY)) {
                        DrawableHelper.fill(stack, b.x, b.y, b.x + b.width, b.y + b.height, 0x33000000);
                    }
                }

                @Override
                public boolean mouseClicked(double x, double y, int button) {
                    if (getBounds().contains(x, y)) {
                        CodeUtilities.MC.getSoundManager().play(PositionedSoundInstance.ambient(SoundEvents.UI_BUTTON_CLICK, 1f,1f));

                        if (button == 0) {
                            CodeUtilities.MC.setScreen(new ScriptEditScreen(s));
                        } else {
                            CButton delete = new CButton((int) x, (int) y, 40, 8, "Delete", () -> {
                                CodeUtilities.MC.setScreen(new ScriptDeletionScreen(s));
                            });
                            CButton enableDisable;
                            if (s.disabled()) {
                                enableDisable = new CButton((int) x, (int) y + 8, 40, 8, "Enable", () -> {
                                    s.setDisabled(false);
                                    ScriptManager.getInstance().saveScript(s);
                                    CodeUtilities.MC.setScreen(new ScriptListScreen());
                                });
                            } else {
                                enableDisable = new CButton((int) x, (int) y + 8, 40, 8, "Disable", () -> {
                                    s.setDisabled(true);
                                    ScriptManager.getInstance().saveScript(s);
                                    CodeUtilities.MC.setScreen(new ScriptListScreen());
                                });
                            }
                            CodeUtilities.MC.send(() -> {
                                widgets.add(delete);
                                widgets.add(enableDisable);
                                contextMenu.add(delete);
                                contextMenu.add(enableDisable);
                            });
                        }
                        return true;
                    }
                    return false;
                }
            });

            y += 12;
        }

        CButton add = new CButton(25, y, 40, 8, "Add", () -> {
            CodeUtilities.MC.setScreen(new ScriptCreationScreen());
        });
        panel.add(add);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean b = super.mouseClicked(mouseX, mouseY, button);
        clearContextMenu();
        return b;
    }

    private void clearContextMenu() {
        for (CWidget w : contextMenu) {
            widgets.remove(w);
        }
        contextMenu.clear();
    }
}
