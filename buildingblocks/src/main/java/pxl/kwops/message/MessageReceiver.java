package pxl.kwops.message;

import pxl.kwops.domain.models.Message;

public interface MessageReceiver<T extends Message> {
    void receiveMessage(T message);

}
