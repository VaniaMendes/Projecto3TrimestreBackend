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
    public List<NotificationEntity> findNotificationsByUserID(long userId, int page, int size) {
        List<NotificationEntity> notifications = new ArrayList<>();

        try {
            // Combine todas as notificações em uma única lista antes da paginação
            List<NotificationEntity> allNotifications = new ArrayList<>();

            // Obter notificações de mensagem recebida
            allNotifications.addAll(em.createNamedQuery("Notification.findMessageReceivedByUserID", NotificationEntity.class)
                    .setParameter("userId", userId)
                    .getResultList());

            // Obter notificações de novos projetos
            allNotifications.addAll(em.createNamedQuery("Notification.findNewProjectByUserID", NotificationEntity.class)
                    .setParameter("userId", userId)
                    .getResultList());

            // Obter notificações de status do projeto
            allNotifications.addAll(em.createNamedQuery("Notification.findProjectStatusByUserID", NotificationEntity.class)
                    .setParameter("userId", userId)
                    .getResultList());

            // Ordenar as notificações pela data de envio
            allNotifications.sort((n1, n2) -> n2.getSendTimestamp().compareTo(n1.getSendTimestamp()));

            // Aplicar paginação manualmente na lista combinada
            int fromIndex = page * size;
            int toIndex = Math.min(fromIndex + size, allNotifications.size());

            if (fromIndex < toIndex) {
                notifications = allNotifications.subList(fromIndex, toIndex);
            }
        } catch (Exception e) {
            // handle exception
            e.printStackTrace();
        }

        return notifications;
    }





    public int numberOfnotificationsByUserID(long userId) {
        try {
            Long count = em.createNamedQuery("Notification.numberOfnotificationsByUserID", Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return count.intValue();
        } catch (Exception e) {

            throw new RuntimeException("Error counting notifications", e);
        }
    }


    public List<NotificationEntity>findNotificationsByUserIDandType(long userId, String type) {
        return em.createNamedQuery("Notification.findNotificationsByUserIDandType", NotificationEntity.class)
                .setParameter("userId", userId)
                .setParameter("type", type)
                .getResultList();
    }
}
