package io.github.codeutilities.features.streamermode.message.checks;

import io.github.codeutilities.features.streamermode.StreamerModeHandler;
import io.github.codeutilities.features.streamermode.StreamerModeMessageCheck;
import io.github.codeutilities.features.streamermode.message.Message;
import io.github.codeutilities.features.streamermode.message.MessageCheck;
import io.github.codeutilities.features.streamermode.message.MessageType;

public class DirectMessageCheck extends MessageCheck implements StreamerModeMessageCheck {

    private static final String DIRECT_MESSAGE_REGEX = "^\\[(\\w{3,16}) → You] .+$";

    @Override
    public MessageType getType() {
        return MessageType.DIRECT_MESSAGE;
    }

    @Override
    public boolean check(Message message, String stripped) {
        return stripped.matches(DIRECT_MESSAGE_REGEX);
    }

    @Override
    public void onReceive(Message message) {

    }

    @Override
    public boolean streamerHideEnabled() {
        return StreamerModeHandler.hideDMs();
    }

    public static boolean usernameMatches(Message message, String username) {
        return message.getStripped().matches("^\\["+ username +" → You] .+$");
    }
}
