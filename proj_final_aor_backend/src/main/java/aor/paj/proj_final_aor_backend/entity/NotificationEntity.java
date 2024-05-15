package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This class represents a NotificationEntity in the system.
 * It contains various properties related to a notification and their getter and setter methods.
 */
@Entity
@Table(name="notifications")
public class NotificationEntity implements Serializable {

    /**
     * Unique identifier for notification
     */
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;


    /**
     * Sender of the notification
     */
    @Column(name = "sender_id", nullable = false, unique = false, updatable = false)
    private long sender_id;

    /**
     * Receiver of the notification
     */
    @Column(name = "receiver_id", nullable = false, unique = false, updatable = false)
    private long receiver_id;

    /**
     * Read status of the notification
     */
    @Column(name = "readStatus", nullable = false, unique = false, updatable = true)
    private boolean readStatus;

    /**
     * Send timestamp of the notification
     */
    @Column(name = "sendTimestamp", nullable = false, unique = false, updatable = true)
    private LocalDateTime sendTimestamp;

    /**
     * Read timestamp of the notification
     */
    @Column(name = "readTimestamp", nullable = false, unique = false, updatable = true)
    private LocalDateTime readTimestamp;

    /**
     * Type of the notification
     */

    @Column(name = "type", nullable = false, unique = false, updatable = true)
    private String type;


    /**
     * Default constructor for the NotificationEntity class.
     */
    public NotificationEntity() {
    }

    /**
     * Getter for the unique identifier of the notification.
     * @return  id of the notification.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for the unique identifier of the notification.
     * @param id the new id of the notification.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for the read status of the notification.
     * @return read status of the notification.
     */
    public boolean isReadStatus() {
        return readStatus;
    }

    /**
     * Setter for the read status of the notification.
     * @param readStatus the new read status of the notification.
     */
    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    /**
     * Getter for the send timestamp of the notification.
     * @return send timestamp of the notification.
     */
    public LocalDateTime getSendTimestamp() {
        return sendTimestamp;
    }

    /**
     * Setter for the send timestamp of the notification.
     * @param sendTimestamp the new send timestamp of the notification.
     */
    public void setSendTimestamp(LocalDateTime sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    /**
     * Getter for the read timestamp of the notification.
     * @return read timestamp of the notification.
     */
    public LocalDateTime getReadTimestamp() {
        return readTimestamp;
    }

    /**
     * Setter for the read timestamp of the notification.
     * @param readTimestamp the new read timestamp of the notification.
     */
    public void setReadTimestamp(LocalDateTime readTimestamp) {
        this.readTimestamp = readTimestamp;
    }

    /**
     * Getter for the type of the notification.
     * @return type of the notification.
     */

    public String getType() {
        return type;
    }

    /**
     * Setter for the type of the notification.
     * @param type the new type of the notification.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for the sender of the notification.
     * @return sender of the notification.
     */
    public long getSender_id() {
        return sender_id;
    }

    /**
     * Setter for the sender of the notification.
     * @param sender_id the new sender of the notification.
     */
    public void setSender_id(long sender_id) {
        this.sender_id = sender_id;
    }

    /**
     * Getter for the receiver of the notification.
     * @return receiver of the notification.
     */
    public long getReceiver_id() {
        return receiver_id;
    }

    /**
     * Setter for the receiver of the notification.
     * @param receiver_id the new receiver of the notification.
     */
    public void setReceiver_id(long receiver_id) {
        this.receiver_id = receiver_id;
    }
}
