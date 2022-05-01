package io.github.codeutilities.features.streamermode.message.finalizers;

import io.github.codeutilities.features.streamermode.message.Message;
import io.github.codeutilities.features.streamermode.message.MessageFinalizer;
import io.github.codeutilities.util.chat.MessageGrabber;

public class MessageGrabberFinalizer extends MessageFinalizer {

    @Override
    protected void receive(Message message) {
        if(MessageGrabber.isActive()) {
            MessageGrabber.supply(message);
        }
    }
}
