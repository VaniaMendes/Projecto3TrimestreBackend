package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.MessageEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import jakarta.ejb.Stateless;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The MessageDao class provides data access operations for the MessageEntity.
 * It extends the AbstractDao class, inheriting common data access operations.
 * This class is marked as Stateless, meaning it does not hold any conversational state.
 */
@Stateless
public class MessageDao extends AbstractDao<MessageEntity> {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     * Initializes the superclass with MessageEntity class type
     */
    public MessageDao() {
        super(MessageEntity.class);
    }

    /**
     * Finds messages between two users.
     *
     * @param user1 The first user.
     * @param user2 The second user.
     * @param page  The page number for pagination.
     * @param size  The size of the page for pagination.
     * @return A list of messages between the two users, or null if no messages are found.
     */
    public List<MessageEntity> findMessagesBetweenUsers(UserEntity user1, UserEntity user2, int page, int size) {
        try {
            List<MessageEntity> messageEntities = em.createNamedQuery("Message.findMessagesBetweenUsers")
                    .setParameter("user1", user1.getId())
                    .setParameter("user2", user2.getId())
                    .setFirstResult(page * size)
                    .setMaxResults(size)
                    .getResultList();
            return messageEntities;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Finds the total number of messages between two users.
     *
     * @param user1 The first user.
     * @param user2 The second user.
     * @return The total number of messages between the two users.
     */
    public int findTotalMessagesBetweenTwoUsers(UserEntity user1, UserEntity user2) {
        try {
            return em.createNamedQuery("Message.findTotalMessagesBetweenTwoUsers", Long.class)
                    .setParameter("user1", user1.getId())
                    .setParameter("user2", user2.getId())
                    .getSingleResult().intValue();
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * Finds users with whom the specified user has exchanged messages.
     *
     * @param userId The ID of the user.
     * @return A list of users with whom the specified user has exchanged messages.
     */
    public List<UserEntity> findUsersWithExchangedMessages(long userId) {
        try {
            return em.createNamedQuery("Message.findUsersWithExchangedMessages", UserEntity.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    /**
     * Finds messages by project ID.
     *
     * @param projectId The ID of the project.
     * @return A list of messages for the specified project, or null if no messages are found.
     */
    public List<MessageEntity> findMessagesByProject(long projectId) {
        try {
            List<MessageEntity> messageEntities = em.createNamedQuery("Message.findMessagesByProject").setParameter("projectId", projectId).getResultList();
            return messageEntities;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Finds a message by its ID.
     *
     * @param id The ID of the message.
     * @return The found message, or null if no message with the given ID exists.
     */
    public MessageEntity findMessageById(long id) {
        return em.find(MessageEntity.class, id);
    }

    /**
     * Updates an existing message in the database.
     * If the message does not exist, it will not be created.
     *
     * @param message The message to update.
     */
    public void updateMessage(MessageEntity message) {
        em.merge(message);
    }

    /**
     * Method to create a new message.
     * It uses the entity manager to persist the message in the database.
     *
     * @param message the message object to create.
     */
    public void createMessage(MessageEntity message) {
        em.persist(message);
    }


}
