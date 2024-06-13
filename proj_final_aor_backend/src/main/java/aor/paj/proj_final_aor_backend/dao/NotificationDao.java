package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.NotificationEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NamedQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * This class represents a Data Access Object (DAO) for NotificationEntity.
 * It extends the AbstractDao class and provides methods for creating notifications.
 * It is annotated as Stateless, which means it does not hold any conversational state with the client.
 * Each method invocation is considered a separate transaction.
 */
@Stateless

public class NotificationDao extends AbstractDao<NotificationEntity>{
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     * Calls the parent constructor with the NotificationEntity class as the parameter.
     */
    public NotificationDao() {
        super(NotificationEntity.class);
    }



    /**
     * Method to create a new notification.
     * It uses the EntityManager's persist method to save the notification to the database.
     *
     * @param notification The NotificationEntity object to be saved.
     */
    public void create(NotificationEntity notification) {
        em.persist(notification);
    }

    public NotificationEntity findNotificationById(long id) {
        return em.find(NotificationEntity.class, id);
    }


    public List<NotificationEntity> findUnreadNotificationsByUserID(long id) {
        return em.createNamedQuery("Notification.findUnreadNotificationsByUserID", NotificationEntity.class)
                .setParameter("id", id)
                .getResultList();
    }
    public List<NotificationEntity> findNotificationsByUserID(long userId) {
        List<NotificationEntity> notifications = new ArrayList<>();

        try {
            NotificationEntity latestMessageReceived = em.createNamedQuery("Notification.findMessageReceivedByUserID", NotificationEntity.class)
                    .setParameter("userId", userId)
                    .setMaxResults(1)
                    .getSingleResult();
            if (latestMessageReceived != null) {
                notifications.add(latestMessageReceived);
            }
        } catch (Exception e) {
            // handle exception
        }

        try {
            List<NotificationEntity> newProjectNotifications = em.createNamedQuery("Notification.findNewProjectByUserID", NotificationEntity.class)
                    .setParameter("userId", userId)
                    .getResultList();
            notifications.addAll(newProjectNotifications);
        } catch (Exception e) {
            // handle exception
        }

        try {
            List<NotificationEntity> projectStatusNotifications = em.createNamedQuery("Notification.findProjectStatusByUserID", NotificationEntity.class)
                    .setParameter("userId", userId)
                    .getResultList();
            notifications.addAll(projectStatusNotifications);
        } catch (Exception e) {

        }

        notifications.sort((n1, n2) -> n2.getSendTimestamp().compareTo(n1.getSendTimestamp()));
        return notifications;
    }


}
