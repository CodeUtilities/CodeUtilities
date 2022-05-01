package io.github.codeutilities.config.internal;

import io.github.codeutilities.config.types.IConfigEnum;

public enum DfButtonTextLocations implements IConfigEnum {
    NONE(),
    TOP_LEFT(),
    BOTTOM_RIGHT();

    @Override
    public String getKey() {
        return "dfButtonsLocation";
    }
}
