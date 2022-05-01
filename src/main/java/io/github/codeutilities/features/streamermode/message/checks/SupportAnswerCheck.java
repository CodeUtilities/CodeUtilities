package io.github.codeutilities.features.streamermode.message.checks;

import io.github.codeutilities.features.streamermode.StreamerModeHandler;
import io.github.codeutilities.features.streamermode.StreamerModeMessageCheck;
import io.github.codeutilities.features.streamermode.message.Message;
import io.github.codeutilities.features.streamermode.message.MessageCheck;
import io.github.codeutilities.features.streamermode.message.MessageType;

public class SupportAnswerCheck extends MessageCheck implements StreamerModeMessageCheck {

    private static final String SUPPORT_ANSWER_REGEX = "^.*\\n» \\w+ has answered \\w+'s question:\\n\\n.+\\n.*$";

    @Override
    public MessageType getType() {
        return MessageType.SUPPORT_ANSWER;
    }

    @Override
    public boolean check(Message message, String stripped) {
        return stripped.matches(SUPPORT_ANSWER_REGEX);
    }

    @Override
    public void onReceive(Message message) {

    }

    @Override
    public boolean streamerHideEnabled() {
        return StreamerModeHandler.hideSupport();
    }
}
