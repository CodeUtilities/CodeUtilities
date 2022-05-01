package io.github.codeutilities.features.streamermode.message.checks;

import io.github.codeutilities.features.streamermode.StreamerModeHandler;
import io.github.codeutilities.features.streamermode.StreamerModeMessageCheck;
import io.github.codeutilities.features.streamermode.message.Message;
import io.github.codeutilities.features.streamermode.message.MessageCheck;
import io.github.codeutilities.features.streamermode.message.MessageType;

public class SilentPunishmentCheck extends MessageCheck implements StreamerModeMessageCheck {

    @Override
    public MessageType getType() {
        return MessageType.SILENT_PUNISHMENT;
    }

    @Override
    public boolean check(Message message, String stripped) {
        return stripped.startsWith("[Silent]");
    }

    @Override
    public void onReceive(Message message) {

    }

    @Override
    public boolean streamerHideEnabled() {
        return StreamerModeHandler.hideModeration();
    }
}
