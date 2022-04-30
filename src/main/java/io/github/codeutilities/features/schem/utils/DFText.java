package io.github.codeutilities.features.schem.utils;

public class DFText {

    public final String text;

    public DFText(String text) {
        this.text = text;
    }

    public String asJson() {
        return "{\"id\":\"txt\",\"data\":{\"name\":\"" + this.text + "\"}}";
    }

}
