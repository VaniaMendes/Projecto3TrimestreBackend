package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This class represents a MessageEntity in the system.
 * It contains various properties related to a message and their getter and setter methods.
 */
@Entity
@Table(name="messages")

//Querys for the MessageEntity class
@NamedQuery(name = "Message.findMessagesBetweenUsers", query = "SELECT m FROM MessageEntity m WHERE (m.sender.id = :user1 AND m.receiver.id = :user2) OR (m.sender.id = :user1 AND m.receiver.id = :user2)")
@NamedQuery(name= "Message.findMessagesGroupedBySender", query = "SELECT m FROM MessageEntity m WHERE m.receiver.id = :receiver ORDER BY m.sendTimestamp DESC")
public class MessageEntity implements Serializable {

    /**
     * Unique identifier for message
     */
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;


    /**
     * Read status of the message
     */
    @Column(name = "readStatus", nullable = false, unique = false, updatable = true)
    private boolean readStatus;

    /**
     * Send timestamp of the message
     */
    @Column(name = "sendTimestamp", nullable = false, unique = false, updatable = true)
    private LocalDateTime sendTimestamp;

    /**
     * Read timestamp of the message
     */
    @Column(name = "readTimestamp", nullable = false, unique = false, updatable = true)
    private LocalDateTime readTimestamp;

    /**
     * Content of the message
     */
    @Column(name = "content", nullable = false, unique = false, updatable = true)
    private String content;

    /**
     * Sender of the message
     */
    @ManyToOne
    @JoinColumn(name="sender_id", nullable = false, unique = false, updatable = true)
    private UserEntity sender;

    /**
     * Single User Receiver of the message
     */
    @ManyToOne
    @JoinColumn(name="receiver_id", nullable = true)
    private UserEntity receiver;

    /**
     * Group Receiver of the message
     */
    @ManyToOne
    @JoinColumn(name="receiver_group_id", nullable = true)
    private UserProjectEntity receiverGroup;



    /**
     * Default constructor for the MessageEntity class.
     */

    public long getId() {
        return id;
    }

    /**
     * Setter for the unique identifier of the message.
     * @param id the new id of the message.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for the read status of the message.
     * @return read status of the message.
     */
    public boolean isReadStatus() {
        return readStatus;
    }

    /**
     * Setter for the read status of the message.
     * @param readStatus the new read status of the message.
     */
    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    /**
     * Getter for the send timestamp of the message.
     * @return send timestamp of the message.
     */
    public LocalDateTime getSendTimestamp() {
        return sendTimestamp;
    }

    /**
     * Setter for the send timestamp of the message.
     * @param sendTimestamp the new send timestamp of the message.
     */
    public void setSendTimestamp(LocalDateTime sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    /**
     * Getter for the read timestamp of the message.
     * @return read timestamp of the message.
     */
    public LocalDateTime getReadTimestamp() {
        return readTimestamp;
    }

    /**
     * Setter for the read timestamp of the message.
     * @param readTimestamp the new read timestamp of the message.
     */
    public void setReadTimestamp(LocalDateTime readTimestamp) {
        this.readTimestamp = readTimestamp;
    }

    /**
     * Getter for the content of the message.
     * @return content of the message.
     */

    public String getContent() {
        return content;
    }

    /**
     * Setter for the content of the message.
     * @param content the new content of the message.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Getter for the sender of the message.
     * @return sender of the message.
     */
    public UserEntity getSender() {
        return sender;
    }

    /**
     * Setter for the sender of the message.
     * @param sender the new sender of the message.
     */
    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    /**
     * Getter for the receiver of the message.
     * @return receiver of the message.
     */
    public UserEntity getReceiver() {
        return receiver;
    }

    /**
     * Setter for the receiver of the message.
     * @param receiver the new receiver of the message.
     */
    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    public UserProjectEntity getReceiverGroup() {
        return receiverGroup;
    }

    public void setReceiverGroup(UserProjectEntity receiverGroup) {
        this.receiverGroup = receiverGroup;
    }
}
