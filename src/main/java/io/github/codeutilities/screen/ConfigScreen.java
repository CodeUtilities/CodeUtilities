package io.github.codeutilities.screen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.config.ConfigManager;
import io.github.codeutilities.screen.widget.CScrollPanel;
import io.github.codeutilities.screen.widget.CText;
import io.github.codeutilities.screen.widget.CTextField;
import io.github.codeutilities.screen.widget.CWidget;
import net.minecraft.text.LiteralText;

public class ConfigScreen extends CScreen {

    CScrollPanel scroll;
    CTextField focused;

    public ConfigScreen() {
        super(120, 100);

        JsonObject json = ConfigManager.getConfig().json();

        scroll = new CScrollPanel(0, 0, 120, 100);

        generate(json,5, new int[]{5});

        widgets.add(scroll);
    }

    @Override
    public void close() {
        ConfigManager.getConfig().saveToFile();
        super.close();
    }

    private void generate(JsonObject json, int x, int[] y) {
        for (String key : json.keySet()) {
            if (key.startsWith("desc:")) {
                continue;
            }
            JsonElement element = json.get(key);
            String description = "";
            if (json.has("desc:" + key)) {
                description = " (" + json.get("desc:" + key).getAsString() + ")";
            }
            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                CText title = new CText(x,y[0],new LiteralText(key + description + ":"));
                scroll.add(title);
                y[0] += 5;
                generate(object, x+10, y);
            } else if (element instanceof JsonPrimitive prim) {
                CText title = new CText(x,y[0],new LiteralText(key + description + ":"));
                scroll.add(title);
                y[0] += 5;
                String value = prim.getAsString();
                CTextField field = new CTextField(value,x,y[0],120-x,8,false);
                scroll.add(field);
                y[0] += 10;

                field.setChangedListener(() -> {
                    try {
                        if (prim.isBoolean()) {
                            if (field.getText().equalsIgnoreCase("true")) {
                                json.add(key, new JsonPrimitive(true));
                            } else if (field.getText().equalsIgnoreCase("false")) {
                                json.add(key, new JsonPrimitive(false));
                            } else {
                                throw new Exception("Invalid boolean value");
                            }
                        } else if (prim.isNumber()) {
                            json.add(key, new JsonPrimitive(Double.parseDouble(field.getText())));
                        } else {
                            json.add(key, new JsonPrimitive(field.getText()));
                        }
                        field.textColor = 0xFFFFFF;
                    } catch (Exception e) {
                        field.textColor = 0xFF0000;
                    }
                });
            } else {
                CodeUtilities.LOGGER.warn("Unknown json element type: " + element.getClass().getName());
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (focused != null) {
            focused.setEditable(false);
        }
        focused = null;

        double x = translateMouseX(mouseX);
        double y = translateMouseY(mouseY)- scroll.getScroll();

        for (CWidget widget : scroll.getChildren()) {
            if (widget instanceof CTextField field) {
                if (field.getBounds().contains(x,y)) {
                    field.setEditable(true);
                    focused = field;
                    break;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (focused != null) {
            focused.mouseScrolled(translateMouseX(mouseX), translateMouseY(mouseY), amount);
        } else {
            scroll.mouseScrolled(translateMouseX(mouseX), translateMouseY(mouseY), amount);
        }
        return false;
    }
}
