package io.github.codeutilities.features.streamermode.message.checks;

import io.github.codeutilities.features.streamermode.StreamerModeHandler;
import io.github.codeutilities.features.streamermode.StreamerModeMessageCheck;
import io.github.codeutilities.features.streamermode.message.Message;
import io.github.codeutilities.features.streamermode.message.MessageCheck;
import io.github.codeutilities.features.streamermode.message.MessageType;

public class PlotAdCheck extends MessageCheck implements StreamerModeMessageCheck {

    private static final String PLOT_AD_REGEX = "^.*\\[ Plot Ad ].*\\n.+\\n.*$";

    @Override
    public MessageType getType() {
        return MessageType.PLOT_AD;
    }

    @Override
    public boolean check(Message message, String stripped) {
        return stripped.matches(PLOT_AD_REGEX);
    }

    @Override
    public void onReceive(Message message) {

    }

    @Override
    public boolean streamerHideEnabled() {
        return StreamerModeHandler.hidePlotAds();
    }
}
