package io.github.codeutilities.util.chat;

import io.github.codeutilities.features.streamermode.message.MessageType;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

public class MessageGrabberTask {

    public int messages;
    public Consumer<List<Text>> consumer;
    public boolean silent;
    public MessageType filter;

    public MessageGrabberTask(int messages, Consumer<List<Text>> consumer, boolean silent, MessageType filter) {
        this.messages = messages;
        this.consumer = consumer;
        this.silent = silent;
        this.filter = filter;
    }

}
