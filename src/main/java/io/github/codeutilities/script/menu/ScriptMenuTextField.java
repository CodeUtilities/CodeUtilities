package io.github.codeutilities.script.menu;

import io.github.codeutilities.screen.widget.CTextField;

public class ScriptMenuTextField extends CTextField implements ScriptWidget {

    private final String identifier;

    public ScriptMenuTextField(String text, int x, int y, int width, int height, boolean editable, String identifier) {
        super(text, x, y, width, height, editable);
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }
}
