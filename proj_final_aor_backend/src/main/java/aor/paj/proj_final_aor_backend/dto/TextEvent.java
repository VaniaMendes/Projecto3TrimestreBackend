package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;

/**
 * This class represents a text event.
 * It has an id, read status, send timestamp, read timestamp, sender id, and receiver id.
 */

@XmlRootElement
public class  TextEvent {

    @XmlElement
    private long id;

    @XmlElement
    private boolean readStatus;

    @XmlElement
    private LocalDateTime sendTimestamp;


    @XmlElement
    private LocalDateTime readTimestamp;


    @XmlElement
    private User sender;


    @XmlElement
    private User receiver;


    /**
     * Default constructor of the class.
     */
    public TextEvent() {
    }

    /**
     * Gets the ID of the text event.
     * @return the ID of the text event.
     */

    public long getId() {
        return id;
    }


    /**
     * Sets the ID of the text event.
     * @param id the ID of the text event.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the read status of the text event.
     * @return the read status of the text event.
     */

    public boolean isReadStatus() {
        return readStatus;
    }

    /**
     * Sets the read status of the text event.
     * @param readStatus the read status of the text event.
     */
    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    /**
     * Gets the time when the text event was sent.
     * @return the time when the text event was sent.
     */
    public LocalDateTime getSendTimestamp() {
        return sendTimestamp;
    }

    /**
     * Sets the time when the text event was sent.
     * @param sendTimestamp the time when the text event was sent.
     */

    public void setSendTimestamp(LocalDateTime sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    /**
     * Gets the time when the text event was read.
     * @return the time when the text event was read.
     */

    public LocalDateTime getReadTimestamp() {
        return readTimestamp;
    }

    /**
     * Sets the time when the text event was read.
     * @param readTimestamp the time when the text event was read.
     */
    public void setReadTimestamp(LocalDateTime readTimestamp) {
        this.readTimestamp = readTimestamp;
    }

    /**
     * Gets the ID of the sender of the text event.
     * @return the ID of the sender of the text event.
     */

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
