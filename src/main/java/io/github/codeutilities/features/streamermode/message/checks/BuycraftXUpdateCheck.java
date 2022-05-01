package io.github.codeutilities.features.streamermode.message.checks;

import io.github.codeutilities.features.streamermode.StreamerModeHandler;
import io.github.codeutilities.features.streamermode.StreamerModeMessageCheck;
import io.github.codeutilities.features.streamermode.message.Message;
import io.github.codeutilities.features.streamermode.message.MessageCheck;
import io.github.codeutilities.features.streamermode.message.MessageType;

public class BuycraftXUpdateCheck extends MessageCheck implements StreamerModeMessageCheck {

    private static final String BUYCRAFT_UPDATE_REGEX = "^A new version of BuycraftX \\([0-9.]+\\) is available\\. Go to your server panel at https://server.tebex.io/plugins to download the update\\.$";

    @Override
    public MessageType getType() {
        return null;
    }

    @Override
    public boolean check(Message message, String stripped) {
        return stripped.matches(BUYCRAFT_UPDATE_REGEX);
    }

    @Override
    public void onReceive(Message message) {

    }

    @Override
    public boolean streamerHideEnabled() {
        return StreamerModeHandler.hideBuycraftUpdate();
    }
}
