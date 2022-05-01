package io.github.codeutilities.features.streamermode.message.checks;

import io.github.codeutilities.features.streamermode.StreamerModeHandler;
import io.github.codeutilities.features.streamermode.StreamerModeMessageCheck;
import io.github.codeutilities.features.streamermode.message.Message;
import io.github.codeutilities.features.streamermode.message.MessageCheck;
import io.github.codeutilities.features.streamermode.message.MessageType;

public class ScanningCheck extends MessageCheck implements StreamerModeMessageCheck {

    private static final String SCANNING_REGEX = "^Scanning \\w+(.|\n)*\\[Online] \\[Offline] \\[(IP|)Banned]\1*$";

    @Override
    public MessageType getType() {
        return MessageType.SCANNING;
    }

    @Override
    public boolean check(Message message, String stripped) {
        return stripped.matches(SCANNING_REGEX);
    }

    @Override
    public void onReceive(Message message) {

    }

    @Override
    public boolean streamerHideEnabled() {
        return StreamerModeHandler.hideModeration();
    }
}
