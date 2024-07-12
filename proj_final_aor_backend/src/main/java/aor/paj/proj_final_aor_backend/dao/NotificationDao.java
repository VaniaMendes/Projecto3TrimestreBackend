package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.NotificationEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NamedQuery;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * This class represents a Data Access Object (DAO) for NotificationEntity.
 * It extends the AbstractDao class and provides methods for creating notifications.
 * It is annotated as Stateless, which means it does not hold any conversational state with the client.
 */
@Stateless

public class NotificationDao extends AbstractDao<NotificationEntity> {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     * Initializes the superclass with NotificationEntity class type
     */
    public NotificationDao() {
        super(NotificationEntity.class);
    }


    /**
     * Creates a new NotificationEntity in the database.
     *
     * @param notification The NotificationEntity to create.
     */
    public void create(NotificationEntity notification) {
        em.persist(notification);
    }


    /**
     * Finds a NotificationEntity by its ID.
     *
     * @param id The ID of the NotificationEntity to find.
     * @return The found NotificationEntity, or null if no entity with the given ID exists.
     */
    public NotificationEntity findNotificationById(long id) {
        return em.find(NotificationEntity.class, id);
    }




    /**
     * Finds the total number of unread notifications for a user by their ID.
     *
     * @param userId The ID of the user.
     * @return The total number of unread notifications for the user.
     */
    public long findUnreadNotificationsByUserID(long userId) {
        try {
            // Count all notifications of type  MESSAGE_RECEIVED
            Long countNonMessageReceived = em.createNamedQuery("Notification.countUnreadForTypeMessage", Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();

            // Count all notifications of MESSAGE_PROJECT
            Long countNonMessageReceivedProject = em.createNamedQuery("Notification.countUnreadForTypeMessageProject", Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();

            // Count all notifications except MESSAGE_RECEIVED and MESSAGE_PROJECT
            Long countAllNotificationsExceptMessage = em.createNamedQuery("Notification.countUnreadNotifications", Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();

            // Sum the results and return the total count
            return countNonMessageReceived.intValue() + countNonMessageReceivedProject.intValue() + countAllNotificationsExceptMessage.intValue();
        } catch (Exception e) {
            throw new RuntimeException("Error counting notifications", e);
        }
    }




    public List<NotificationEntity> findNotificationsListForUser(long userId) {
        // Initialize an empty list to hold the notifications to be returned
        List<NotificationEntity> notifications = new ArrayList<>();

        try {
            // Initialize a list to combine all notifications before pagination
            List<NotificationEntity> allNotifications = new ArrayList<>();

            // Get notifications of type MESSAGE_RECEIVED
            allNotifications.addAll(em.createNamedQuery("Notification.findLatestMessageReceivedByUserID", NotificationEntity.class)
                    .setParameter("userId", userId)
                    .getResultList());

            // Get notifications of type MESSAGE_PROJECT
            allNotifications.addAll(em.createNamedQuery("Notification.findLatestMessageReceivedByProject", NotificationEntity.class)
                    .setParameter("userId", userId)
                    .getResultList());

            // Get notifications of all types except MESSAGE_RECEIVED and MESSAGE_PROJECT
            allNotifications.addAll(em.createNamedQuery("Notification.findAllNotificationsExceptMessageReceived", NotificationEntity.class)
                    .setParameter("userId", userId)
                    .getResultList());

            // Sort the notifications by their send timestamp in descending order
            allNotifications.sort((n1, n2) -> n2.getSendTimestamp().compareTo(n1.getSendTimestamp()));
            // Assign allNotifications to notifications to be returned
            notifications = allNotifications;


        } catch (Exception e) {
            // handle exception
            e.printStackTrace();
        }

        // Return the list of notifications
        return notifications;
    }


    /**
     * Finds notifications for a user by their ID.
     *
     * @param userId The ID of the user.
     * @param page   The page number for pagination.
     * @param size   The size of the page for pagination.
     * @return A list of notifications for the user, or null if no notifications are found.
     */
    public List<NotificationEntity> findNotificationsByUserID(long userId, int page, int size) {
        // Initialize an empty list to hold the notifications to be returned
        List<NotificationEntity> notifications = new ArrayList<>();

        try {
            // Initialize a list to combine all notifications before pagination
            List<NotificationEntity> allNotifications = new ArrayList<>();

            // Get notifications of type MESSAGE_RECEIVED
            allNotifications.addAll(em.createNamedQuery("Notification.findLatestMessageReceivedByUserID", NotificationEntity.class)
                    .setParameter("userId", userId)
                    .getResultList());

            // Get notifications of type MESSAGE_PROJECT
            allNotifications.addAll(em.createNamedQuery("Notification.findLatestMessageReceivedByProject", NotificationEntity.class)
                    .setParameter("userId", userId)
                    .getResultList());

            // Get notifications of all types except MESSAGE_RECEIVED and MESSAGE_PROJECT
            allNotifications.addAll(em.createNamedQuery("Notification.findAllNotificationsExceptMessageReceived", NotificationEntity.class)
                    .setParameter("userId", userId)
                    .getResultList());

            // Sort the notifications by their send timestamp in descending order
            allNotifications.sort((n1, n2) -> n2.getSendTimestamp().compareTo(n1.getSendTimestamp()));

            // Adjust indices to ensure pagination starts at page 1
            int fromIndex = (page - 1) * size;
            int toIndex = Math.min(fromIndex + size, allNotifications.size());
            // Check if fromIndex is less than toIndex to avoid IndexOutOfBoundsException
            if (fromIndex < toIndex) {
                notifications = allNotifications.subList(fromIndex, toIndex);
            }
        } catch (Exception e) {
            // handle exception
            e.printStackTrace();
        }

        // Return the list of notifications
        return notifications;
    }

    /**
     * Finds the total number of notifications for a user by their ID.
     *
     * @param userId The ID of the user.
     * @return The total number of notifications for the user.
     */
    public long numberOfnotificationsByUserID(long userId) {
        try {
            //Count all notifications of type MESSAGE_RECEIVED
            Long countNotificationMessage = em.createNamedQuery("Notification.countLatestMessageReceivedByUserID", Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();

            // Count all notifications of type MESSAGE_PROJECT
            Long countNotificationsMessageProject = em.createNamedQuery("Notification.countUnreadForTypeMessageProject", Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();


            // Count all notifications except MESSAGE_RECEIVED and MESSAGE_PROJECT
            Long countAllNotificationsExceptMessage = em.createNamedQuery("Notification.countAllNotificationsExceptMessageReceived", Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();

            // Sum the results and return the total count
            return countNotificationMessage.longValue() + countAllNotificationsExceptMessage.longValue() + countNotificationsMessageProject.longValue();
        } catch (Exception e) {
            throw new RuntimeException("Error counting notifications", e);
        }
    }


    /**
     * Finds notifications for a user by their ID and notification type.
     *
     * @param userId The ID of the user.
     * @param type   The type of the notifications to find.
     * @return A list of notifications for the user of the specified type, or null if no notifications are found.
     */
    public List<NotificationEntity> findNotificationsByUserIDandType(long userId, String type) {
        return em.createNamedQuery("Notification.findNotificationsByUserIDandType", NotificationEntity.class)
                .setParameter("userId", userId)
                .setParameter("type", type)
                .getResultList();
    }
}
