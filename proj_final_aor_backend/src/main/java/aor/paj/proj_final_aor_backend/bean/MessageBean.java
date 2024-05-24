package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.MessageDao;
import aor.paj.proj_final_aor_backend.dao.SessionDao;
import aor.paj.proj_final_aor_backend.dao.UserDao;
import aor.paj.proj_final_aor_backend.dto.Message;
import aor.paj.proj_final_aor_backend.entity.MessageEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents a MessageBean in the system.
 * It contains various methods related to messages.
 */
@Stateless
public class MessageBean implements Serializable {

    /**
     * Logger for the MessageBean class.
     */
    private static final Logger logger = LogManager.getLogger(MessageBean.class);

    /**
     * Data access object for messages.
     */
    @EJB
    MessageDao messageDao;
    /**
     * Data access object for users.
     */
    @EJB
    UserDao userDao;
    /**
     * Data access object for sessions.
     */
    @EJB
    SessionDao sessionDao;

    @EJB
    UserBean userBean;


    /**
     * Default constructor for the MessageBean class.
     */
    public MessageBean() {
    }


    /**
     * This method is used to send a message from one user to another.
     *
     * @param token   The token of the user who is sending the message. This is used to authenticate the user.
     * @param content The content of the message that is to be sent.
     * @param id      The id of the user who will receive the message.
     * @return        Returns true if the message was successfully sent, false otherwise.
     */
    public boolean sendMessage(String token, String content, long id){
        // Find the user by token
        UserEntity user = sessionDao.findUserByToken(token);
        // Check if the user exists
        if(user == null){
            logger.error("User not found");
            return false;
        }

        // Find the receiver by id
        UserEntity receiver = userDao.findUserById(id);
        // Check if the receiver exists
        if(receiver == null){
            logger.error("Receiver not found");
            return false;
        }
        // Create the message
        MessageEntity message = new MessageEntity();
        message.setContent(content);
        message.setSender(user);
        message.setReceiver(receiver);
        message.setSendTimestamp(LocalDateTime.now());
        message.setReadStatus(false);
        message.setReadTimestamp(null);
        // Save the message in the database
        messageDao.createMessage(message);

        return true;
    }


    public List<Message> getMessagesBetweenTwoUsers(String token, long id){

        UserEntity user1 = sessionDao.findUserByToken(token);
        UserEntity user2 = userDao.findUserById(id);
        if(user1 == null || user2 == null){
            logger.error("User not found");
            return null;
        }

        List<MessageEntity> messageEntities = messageDao.findMessagesBetweenUsers(user1,user2);


   List<Message> messages = new ArrayList<>();
   if(messageEntities != null && !messageEntities.isEmpty()){
       for(MessageEntity messageEntity : messageEntities){
           Message message = convertMessageToDto(messageEntity);
           messages.add(message);
       }
   }

        return messages;

    }




public Message convertMessageToDto (MessageEntity messageEntity){
    Message messageDto = new Message();
    messageDto.setId(messageEntity.getId());
    messageDto.setContent(messageEntity.getContent());
    messageDto.setSender(userBean.convertUserToDTOForMessage(messageEntity.getSender()));
    messageDto.setReceiver(userBean.convertUserToDTOForMessage(messageEntity.getReceiver()));
    messageDto.setSendTimestamp(messageEntity.getSendTimestamp());
    messageDto.setReadStatus(messageEntity.isReadStatus());
    messageDto.setReadTimestamp(messageEntity.getReadTimestamp());
    return messageDto;
}

}