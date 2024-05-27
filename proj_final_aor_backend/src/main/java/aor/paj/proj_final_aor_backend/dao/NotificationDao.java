package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.NotificationEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import jakarta.ejb.Stateless;

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
}
