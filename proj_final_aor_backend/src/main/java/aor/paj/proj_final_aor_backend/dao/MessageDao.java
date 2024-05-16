package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.MessageEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import jakarta.ejb.Stateless;

import java.util.List;

/**
 * MessageDao is a DAO class that provides methods to access the MessageEntity table in the database.
 * It extends the AbstractDao class and provides methods to find messages between users and messages grouped by sender.

 */
@Stateless
public class MessageDao extends AbstractDao<MessageEntity>{
    private static final long serialVersionUID = 1L;

    public MessageDao() {
        super(MessageEntity.class);
    }


    /**
     * Method to find a message between two users
     * @param user1 the first user.
     * @param user2 the second user.
     * @return a list of messages between the two users, or null if no messages are found.
     */
    public List<MessageEntity> findMessagesBetweenUsers(UserEntity user1, UserEntity user2) {
        try {
            List<MessageEntity> messageEntities = em.createNamedQuery("Message.findMessagesBetweenUsers").setParameter("user1", user1.getId()).setParameter("user2", user2.getId()).getResultList();
            return messageEntities;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Method to find messages grouped by sender.
     * @param receiver the receiver of the messages.
     * @return  a list of messages grouped by sender, or null if no messages are found.
     */
    public List<MessageEntity> findMessagesGroupedBySender (UserEntity receiver) {
        try {
            List<MessageEntity> messageEntities = em.createNamedQuery("Message.findMessagesGroupedBySender").setParameter("receiver", receiver).getResultList();
            return messageEntities;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method to create a new message.
     * It uses the entity manager to persist the message in the database.
     * @param message the message object to create.
     */
    public void createMessage(MessageEntity message) {
        em.persist(message);
    }


}
