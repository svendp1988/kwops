package pxl.kwops.message;

import pxl.kwops.domain.models.Message;

public interface MessageSender<T extends Message> {
    void sendMessage(T message);

}
