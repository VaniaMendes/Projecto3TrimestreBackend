package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;
import jdk.jfr.Name;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This class represents a MessageEntity in the system.
 * It contains various properties related to a message and their getter and setter methods.
 */
@Entity
@Table(name="messages")

//Querys for the MessageEntity class
@NamedQuery(name = "Message.findMessagesBetweenUsers", query = "SELECT m FROM MessageEntity m WHERE (m.sender.id = :user1 AND m.receiver.id = :user2) OR (m.sender.id = :user2 AND m.receiver.id = :user1) ORDER BY m.sendTimestamp DESC")
@NamedQuery(name = "Message.findSentMessagesUsers", query = "SELECT m.receiver FROM MessageEntity m WHERE m.sender.id =:id ORDER BY m.sendTimestamp DESC")
@NamedQuery(name = "Message.findReceivedMessagesUsers", query = "SELECT m.sender FROM MessageEntity m WHERE m.receiver.id =:id ORDER BY m.sendTimestamp DESC")
@NamedQuery(name= "Message.findMessagesByProject", query = "SELECT m FROM MessageEntity m WHERE m.receiverGroup.project.id = :projectId ORDER BY m.sendTimestamp DESC")

@NamedQuery(name="Message.findTotalMessagesBetweenTwoUsers",  query = "SELECT COUNT(m) FROM MessageEntity m WHERE (m.sender.id = :user1 AND m.receiver.id = :user2) OR (m.sender.id = :user2 AND m.receiver.id = :user1)")

@NamedQuery(name = "Message.findUsersWithExchangedMessages", query =  "SELECT DISTINCT u " +
        "FROM UserEntity u " +
        "JOIN MessageEntity m ON u.id = m.sender.id OR u.id = m.receiver.id " +
        "WHERE (m.sender.id = :userId OR m.receiver.id = :userId) AND u.id != :userId " )

public class MessageEntity implements Serializable {

    /**
     * Unique identifier for message
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;


    /**
     * Read status of the message
     */
    @Column(name = "readStatus")
    private boolean readStatus;

    @Column(name = "subject")
    private String subject;

    /**
     * Send timestamp of the message
     */
    @Column(name = "sendTimestamp", nullable = false)
    private LocalDateTime sendTimestamp;

    /**
     * Read timestamp of the message
     */
    @Column(name = "readTimestamp")
    private LocalDateTime readTimestamp;

    /**
     * Content of the message
     */
    @Column(name = "content", nullable = false, length = 1000)
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
    @JoinColumn(name="receiver_id")
    private UserEntity receiver;

    /**
     * Group Receiver of the message
     */
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="receiver_project_id", referencedColumnName="project_id")
    })
    private UserProjectEntity receiverGroup;


    /**
     * Default constructor for the MessageEntity class.
     */
    public MessageEntity() {}

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
