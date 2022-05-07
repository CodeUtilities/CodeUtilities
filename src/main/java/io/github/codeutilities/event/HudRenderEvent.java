package io.github.codeutilities.event;

import io.github.codeutilities.event.system.Event;
import net.minecraft.client.util.math.MatrixStack;

public record HudRenderEvent(MatrixStack stack) implements Event {

}
