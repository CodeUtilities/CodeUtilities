package io.github.codeutilities.screen.script;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CButton;
import io.github.codeutilities.screen.widget.CItem;
import io.github.codeutilities.screen.widget.CScrollPanel;
import io.github.codeutilities.screen.widget.CText;
import io.github.codeutilities.screen.widget.CWidget;
import io.github.codeutilities.script.Script;
import io.github.codeutilities.script.ScriptManager;
import io.github.codeutilities.script.ScriptPart;
import io.github.codeutilities.script.action.ScriptAction;
import io.github.codeutilities.script.action.ScriptActionType;
import io.github.codeutilities.script.event.ScriptEvent;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;

public class ScriptEditScreen extends CScreen {

    private final Script script;
    private static int scroll = 0;
    private final CScrollPanel panel;
    private final List<CWidget> contextMenu = new ArrayList<>();

    public ScriptEditScreen(Script script) {
        super(125, 100);
        this.script = script;
        panel = new CScrollPanel(0, 3, 120, 94);
        widgets.add(panel);

        int y = 5;
        int index = 0;
        int indent = 0;
        for (ScriptPart part : script.getParts()) {
            if (part instanceof ScriptEvent se) {
                panel.add(new CItem(5, y, se.getType().getIcon()));
                panel.add(new CText(15, y + 2, new LiteralText(se.getType().getName())));
                indent = 5;

                int currentIndex = index;
                panel.add(new CButton(5, y-1, 115, 10, "",() -> {}) {
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

                            if (button != 0) {
                                CButton insertBefore = new CButton((int) x, (int) y, 40, 8, "Insert Before", () -> {
                                    CodeUtilities.MC.setScreen(new ScriptActionCategoryScreen(script, currentIndex));
                                });
                                CButton insertAfter = new CButton((int) x, (int) y+8, 40, 8, "Insert After", () -> {
                                    CodeUtilities.MC.setScreen(new ScriptActionCategoryScreen(script, currentIndex + 1));
                                });
                                CButton delete = new CButton((int) x, (int) y+16, 40, 8, "Delete", () -> {
                                    script.getParts().remove(currentIndex);
                                    scroll = panel.getScroll();
                                    CodeUtilities.MC.setScreen(new ScriptEditScreen(script));
                                });
                                CodeUtilities.MC.send(() -> {
                                    panel.add(insertBefore);
                                    panel.add(insertAfter);
                                    panel.add(delete);
                                    contextMenu.add(insertBefore);
                                    contextMenu.add(insertAfter);
                                    contextMenu.add(delete);
                                });
                            }
                            return true;
                        }
                        return false;
                    }
                });
            } else if (part instanceof ScriptAction sa) {
                if (sa.getType() == ScriptActionType.CLOSE_BRACKET) {
                    indent -= 5;
                }

                panel.add(new CItem(5 + indent, y, sa.getType().getIcon()));
                panel.add(new CText(15 + indent, y + 2, new LiteralText(sa.getType().getName())));

                for (int i = 0; i < indent; i += 5) {
                    int xpos = 8 + i;
                    int ypos = y;
                    panel.add(new CWidget() {
                        @Override
                        public void render(MatrixStack stack, int mouseX, int mouseY, float tickDelta) {
                            DrawableHelper.fill(stack, xpos, ypos, xpos + 1, ypos + 8, 0xFF333333);
                        }

                        @Override
                        public Rectangle getBounds() {
                            return new Rectangle(0,0,0,0);
                        }
                    });
                }

                int currentIndex = index;
                panel.add(new CButton(5, y-1, 115, 10, "",() -> {}) {
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
                                if (sa.getType() != ScriptActionType.CLOSE_BRACKET) {
                                    scroll = panel.getScroll();
                                    CodeUtilities.MC.setScreen(new ScriptEditActionScreen(sa, script));
                                }
                            } else {
                                CButton insertBefore = new CButton((int) x, (int) y, 40, 8, "Insert Before", () -> {
                                    CodeUtilities.MC.setScreen(new ScriptActionCategoryScreen(script, currentIndex));
                                });
                                CButton insertAfter = new CButton((int) x, (int) y+8, 40, 8, "Insert After", () -> {
                                    CodeUtilities.MC.setScreen(new ScriptActionCategoryScreen(script, currentIndex + 1));
                                });
                                CButton delete = new CButton((int) x, (int) y+16, 40, 8, "Delete", () -> {
                                    script.getParts().remove(currentIndex);
                                    scroll = panel.getScroll();
                                    CodeUtilities.MC.setScreen(new ScriptEditScreen(script));
                                });
                                CodeUtilities.MC.send(() -> {
                                    panel.add(insertBefore);
                                    panel.add(insertAfter);
                                    panel.add(delete);
                                    contextMenu.add(insertBefore);
                                    contextMenu.add(insertAfter);
                                    contextMenu.add(delete);
                                });
                            }
                            return true;
                        }
                        return false;
                    }
                });

                if (sa.getType().hasChildren()) {
                    indent += 5;
                }
            } else {
                throw new IllegalArgumentException("Unknown script part type");
            }

            y += 10;
            index++;
        }

        CButton add = new CButton(40, y, 45, 8, "Add", () -> {
            CodeUtilities.MC.setScreen(new ScriptActionCategoryScreen(script, script.getParts().size()));
        });
        panel.add(add);
        panel.setScroll(scroll);
    }

    @Override
    public void close() {
        scroll = panel.getScroll();
        ScriptManager.getInstance().saveScript(script);
        CodeUtilities.MC.setScreen(new ScriptListScreen());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean b = super.mouseClicked(mouseX, mouseY, button);
        clearContextMenu();
        return b;
    }

    private void clearContextMenu() {
        for (CWidget w : contextMenu) {
            panel.remove(w);
        }
        contextMenu.clear();
    }
}
