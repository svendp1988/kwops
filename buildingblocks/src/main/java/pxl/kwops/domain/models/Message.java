package pxl.kwops.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;


public abstract class Message {
    private final UUID id;
    private final LocalDateTime timestamp;

    public Message() {
        this.id = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
