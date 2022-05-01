package io.github.codeutilities.features.streamermode.message.checks;

import io.github.codeutilities.config.Config;
import io.github.codeutilities.features.streamermode.StreamerModeHandler;
import io.github.codeutilities.features.streamermode.StreamerModeMessageCheck;
import io.github.codeutilities.features.streamermode.message.Message;
import io.github.codeutilities.features.streamermode.message.MessageCheck;
import io.github.codeutilities.features.streamermode.message.MessageType;
import io.github.codeutilities.util.chat.ChatUtil;

public class IncomingReportCheck extends MessageCheck implements StreamerModeMessageCheck {

    @Override
    public MessageType getType() {
        return MessageType.INCOMING_REPORT;
    }

    @Override
    public boolean check(Message message, String stripped) {
        return stripped.startsWith("! Incoming Report ");
    }

    @Override
    public void onReceive(Message message) {
        ChatUtil.playSound(Config.getSound("incomingReportSound"));
    }

    @Override
    public boolean streamerHideEnabled() {
        return StreamerModeHandler.hideModeration();
    }
}
