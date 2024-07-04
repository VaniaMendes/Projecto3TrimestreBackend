package aor.paj.proj_final_aor_backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a NotificationEntity in the system.
 * It contains various properties related to a notification and their getter and setter methods.
 */
@Entity
@Table(name="notifications")
@NamedQuery(name = "Notification.findLatestMessageReceivedByUserID", query =
        "SELECT n FROM NotificationEntity n " +
                "WHERE n.id IN (" +
                "   SELECT MAX(n2.id) " +
                "   FROM NotificationEntity n2 " +
                "   JOIN n2.users u " +
                "   WHERE u.id = :userId AND n2.type = 'MESSAGE_RECEIVED' " +
                "   GROUP BY n2.sender_id" +
                ") ORDER BY n.sendTimestamp DESC")

@NamedQuery(name = "Notification.findLatestMessageReceivedByProject", query =
        "SELECT n FROM NotificationEntity n " +
                "WHERE n.id IN (" +
                "   SELECT MAX(n2.id) " +
                "   FROM NotificationEntity n2 " +
                "   JOIN n2.users u " +
                "   WHERE u.id = :userId AND n2.type = 'MESSAGE_PROJECT' " +
                "   GROUP BY n2.sender_id" +
                ") ORDER BY n.sendTimestamp DESC")



@NamedQuery(name = "Notification.findAllNotificationsExceptMessageReceived", query = "SELECT n FROM NotificationEntity n JOIN n.users u WHERE u.id = :userId AND (n.type != 'MESSAGE_RECEIVED' AND n.type != 'MESSAGE_PROJECT')")

@NamedQuery(name = "Notification.findUnreadNotificationsByUserID", query = "SELECT COUNT(n) FROM NotificationEntity n JOIN n.users u WHERE u.id = :id AND n.readStatus = false")

@NamedQuery(name = "Notification.findNotificationsByUserIDandType", query = "SELECT n FROM NotificationEntity n JOIN n.users u WHERE u.id = :userId AND n.type = :type")

@NamedQuery(name="Notification.finAllNotificationsDiferentFromMessageReceived", query = "SELECT n FROM NotificationEntity n JOIN n.users u WHERE u.id = :userId AND n.type != 'MESSAGE_RECEIVED'")


@NamedQuery(name = "Notification.countLatestMessageReceivedByUserID", query =
        "SELECT COUNT(n) FROM NotificationEntity n " +
                "WHERE n.id IN (" +
                "   SELECT MAX(n2.id) " +
                "   FROM NotificationEntity n2 " +
                "   JOIN n2.users u " +
                "   WHERE u.id = :userId AND n2.type = 'MESSAGE_RECEIVED' " +
                "   GROUP BY n2.sender_id" +
                ")")

@NamedQuery(name = "Notification.countAllNotificationsExceptMessageReceived", query = "SELECT COUNT(n) FROM NotificationEntity n JOIN n.users u WHERE u.id = :userId AND (n.type != 'MESSAGE_RECEIVED' AND n.type != 'MESSAGE_PROJECT')")

@NamedQuery(name = "Notification.countUnreadForTypeMessage", query =
        "SELECT COUNT(n) FROM NotificationEntity n " +
                "WHERE n.id IN (" +
                "   SELECT MAX(n2.id) " +
                "   FROM NotificationEntity n2 " +
                "   JOIN n2.users u " +
                "   WHERE u.id = :userId AND n2.type = 'MESSAGE_RECEIVED' " +
                "AND n2.readStatus = false" +
                "   GROUP BY n2.sender_id" +
                ")")

@NamedQuery(name = "Notification.countUnreadForTypeMessageProject", query =
        "SELECT COUNT(n) FROM NotificationEntity n " +
                "WHERE n.id IN (" +
                "   SELECT MAX(n2.id) " +
                "   FROM NotificationEntity n2 " +
                "   JOIN n2.users u " +
                "   WHERE u.id = :userId AND n2.type = 'MESSAGE_PROJECT' " +
                "AND n2.readStatus = false" +
                "   GROUP BY n2.sender_id" +
                ")")



@NamedQuery(name = "Notification.countUnreadNotifications", query = "SELECT COUNT(n) FROM NotificationEntity n JOIN n.users u WHERE u.id = :userId AND n.readStatus=false AND (n.type != 'MESSAGE_RECEIVED' AND n.type != 'MESSAGE_PROJECT')")


public class NotificationEntity implements Serializable {

    /**
     * Unique identifier for notification
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;


    /**
     * Sender of the notification
     */
    @Column(name = "sender_id", nullable = false, unique = false, updatable = false)
    private long sender_id;


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
    @Column(name = "readTimestamp")
    private LocalDateTime readTimestamp;

    /**
     * Related entity id
     */
    @Column(name = "relatedEntityId")
    private long relatedEntityId;

    /**
     * Type of the notification
     */

    @Column(name = "type", nullable = false, unique = false, updatable = true)
    private String type;

    /**
     * Many to many relationship with the UserEntity class
     */

    @ManyToMany(mappedBy = "notifications")
    private Set<UserEntity> users = new HashSet<>();



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
     * Getter for the users of the notification.
     * @return users of the notification.
     */
    public Set<UserEntity> getUsers() {
        return users;
    }

    /**
     * Setter for the users of the notification.
     * @param users the new users of the notification.
     */
    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    /**
     * Getter for the related entity id.
     * @return related entity id.
     */
    public long getRelatedEntityId() {
        return relatedEntityId;
    }

    /**
     * Setter for the related entity id.
     * @param relatedEntityId the new related entity id.
     */
    public void setRelatedEntityId(long relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }
}
