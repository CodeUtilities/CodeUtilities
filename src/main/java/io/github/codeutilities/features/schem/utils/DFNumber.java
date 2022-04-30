package io.github.codeutilities.features.schem.utils;

public class DFNumber {

    private final long number;

    public DFNumber(double number) {
        this.number = Math.round(number);
    }

    public String asJson() {
        return "{\"id\":\"num\",\"data\":{\"name\":\"" + number + "\"}}";
    }
}
