package io.github.codeutilities.features.streamermode.message.finalizers;

import io.github.codeutilities.features.streamermode.StreamerModeMessageCheck;
import io.github.codeutilities.features.streamermode.message.Message;
import io.github.codeutilities.features.streamermode.message.MessageCheck;
import io.github.codeutilities.features.streamermode.message.MessageFinalizer;
import io.github.codeutilities.features.streamermode.message.MessageType;
import io.github.codeutilities.features.streamermode.message.checks.DirectMessageCheck;

public class StreamerModeFinalizer extends MessageFinalizer {

    private static final String[] HIDE_DMS_EXEMPTIONS = new String[]{
            "Vattendroppen236",
            "Reasonless",
            "KabanFriends",
            "TechStreet",
            "tk2217",
            "BlazeMCworld"
    };

    @Override
    protected void receive(Message message) {
        MessageCheck check = message.getCheck();

        if (
                check instanceof StreamerModeMessageCheck
                && ((StreamerModeMessageCheck) check).streamerHideEnabled()
                && !matchesDirectMessageExemptions(message)
        ) {
            message.cancel();
        }
    }

    private static boolean matchesDirectMessageExemptions(Message message) {
        if (message.typeIs(MessageType.DIRECT_MESSAGE)) {
            String stripped = message.getStripped();

            for (String username : HIDE_DMS_EXEMPTIONS) {
                if (DirectMessageCheck.usernameMatches(message, username)) {
                    return true;
                }
            }
        }
        return false;
    }
}
