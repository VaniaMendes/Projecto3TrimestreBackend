package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;

/**
 * This class represents a text event.
 */

@XmlRootElement
public abstract class TextEvent {

    /**
     * The id of the text event.
     */
    @XmlElement
    private long id;

    /**
     * The read status of the text event.
     */
    @XmlElement
    private boolean readStatus;

    /**
     * The time when the text event was sent.
     */
    @XmlElement
    private LocalDateTime sendTimestamp;

    /**
     * The time when the text event was read.
     */
    @XmlElement
    private LocalDateTime readTimestamp;

    /**
     * The id of the sender of the text event.
     */
    @XmlElement
    private long sender_id;

    /**
     * The id of the receiver of the text event.
     */
    @XmlElement
    private long receiver_id;


    /**
     * Default constructor.
     */
    public TextEvent() {
    }

    /**
     * Returns the ID of the text event.
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
     * Returns the read status of the text event.
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
     * Returns the time when the text event was sent.
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
     * Returns the time when the text event was read.
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
     * Returns the ID of the sender of the text event.
     * @return the ID of the sender of the text event.
     */

    public long getSender_id() {
        return sender_id;
    }

    /**
     * Sets the ID of the sender of the text event.
     * @param sender_id the ID of the sender of the text event.
     */
    public void setSender_id(long sender_id) {
        this.sender_id = sender_id;
    }

    /**
     * Returns the ID of the receiver of the text event.
     * @return the ID of the receiver of the text event.
     */
    public long getReceiver_id() {
        return receiver_id;
    }

    /**
     * Sets the ID of the receiver of the text event.
     * @param receiver_id the ID of the receiver of the text event.
     */
    public void setReceiver_id(long receiver_id) {
        this.receiver_id = receiver_id;
    }
}
