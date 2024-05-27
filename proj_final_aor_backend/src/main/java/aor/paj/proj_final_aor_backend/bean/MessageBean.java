package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.*;
import aor.paj.proj_final_aor_backend.dto.Message;
import aor.paj.proj_final_aor_backend.entity.MessageEntity;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import aor.paj.proj_final_aor_backend.entity.UserProjectEntity;
import com.mysql.cj.Messages;
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
    @EJB
    ProjectDao projectDao;
    @EJB
    UserProjectDao userProjectDao;


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
     * @return Returns true if the message was successfully sent, false otherwise.
     */
    public boolean sendMessage(String token, String content, long id) {
        // Find the user by token
        UserEntity user = sessionDao.findUserByToken(token);
        // Check if the user exists
        if (user == null) {
            logger.error("User not found");
            return false;
        }

        // Find the receiver by id
        UserEntity receiver = userDao.findUserById(id);
        // Check if the receiver exists
        if (receiver == null) {
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


    /**
     * This method is used to get all the messages between two users.
     * It is used to display the messages in the chat between two users.
     * @param token The token of the user who is requesting the messages. This is used to authenticate the user.
     * @param id The id of the user with whom the messages are to be retrieved.
     * @return Returns a list of messages between the two users.
     */
    public List<Message> getMessagesBetweenTwoUsers(String token, long id) {

        // Find the user by token
        UserEntity user1 = sessionDao.findUserByToken(token);
        // Find the user by id
        UserEntity user2 = userDao.findUserById(id);

        // Check if the users exist
        if (user1 == null || user2 == null) {
            logger.error("User not found");
            return null;
        }

        // Get the messages between the two users
        List<MessageEntity> messageEntities = messageDao.findMessagesBetweenUsers(user1, user2);

        // Convert the message entities to message DTOs
        List<Message> messages = new ArrayList<>();
        if (messageEntities != null && !messageEntities.isEmpty()) {
            for (MessageEntity messageEntity : messageEntities) {
                Message message = convertMessageToDto(messageEntity);
                messages.add(message);
            }
        }

        return messages;

    }


    /**
     * This method is used to send a message to a chat group in a project.
     * @param token The token of the user who is sending the message. This is used to authenticate the user.
     * @param project_id The id of the project in which the message is to be sent.
     * @param content The content of the message that is to be sent.
     * @return Returns true if the message was successfully sent, false otherwise.
     */
    public boolean sendMessageToChatGroup(String token, long project_id, String content) {

        //Check if the user exists
        UserEntity sender = sessionDao.findUserByToken(token);

        if (sender == null) {
            logger.debug("User not found");
            return false;
        }

        //Check if the project exists
        ProjectEntity project = projectDao.findProjectById(project_id);
        System.out.println(project_id);
        if (project == null) {
            logger.debug("Project not found");
            return false;
        }

        //Check if the user is in the project
        UserProjectEntity userProject = userProjectDao.findUserInProject(project_id, sender.getId());
        if (userProject == null) {
            logger.debug("User not in project");
            return false;
        }

        // Create the message
        MessageEntity message = new MessageEntity();
        message.setContent(content);
        message.setSender(sender);
        message.setReceiverGroup(userProject);
        message.setSendTimestamp(LocalDateTime.now());
        message.setReadStatus(false);
        message.setReadTimestamp(null);
        // Save the message in the database
        messageDao.createMessage(message);
        return true;

    }


    /**
     * This method is used to get all the messages in the chat group of a project.
     * @param token The token of the user who is requesting the messages. This is used to authenticate the user.
     * @param project_id The id of the project in which the messages are to be retrieved.
     * @return Returns a list of messages in the chat group of the project.
     */
    public List<Message> getMessagesChatPtoject(String token, long project_id) {
        UserEntity user = sessionDao.findUserByToken(token);
        if (user == null) {
            logger.debug("User not found");
            return null;
        }
        ProjectEntity project = projectDao.findProjectById(project_id);
        if (project == null) {
            logger.debug("Project not found");
            return null;
        }

        //Check if the user is in the project
        UserProjectEntity userProject = userProjectDao.findUserInProject(project_id, user.getId());
        if (userProject == null) {
            logger.debug("User not in project");
            return null;
        }

        //Get the messages in the chat group of the project
        List<MessageEntity> messageEntities = messageDao.findMessagesByProject(project_id);
        List<Message> messages = new ArrayList<>();
        //Convert the message entities to message DTOs
        if (messageEntities != null && !messageEntities.isEmpty()) {
            for (MessageEntity messageEntity : messageEntities) {
                Message message = convertMessageChatGroupToDTO(messageEntity);
                messages.add(message);
            }
        }
        return messages;

    }


    /**
     * This method is used to convert a message entity to a message DTO.
     * @param messageEntity The message entity that is to be converted.
     * @return Returns the message DTO that is created.
     */
    public Message convertMessageToDto(MessageEntity messageEntity) {
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

    /**
     * This method is used to convert a message entity to a message DTO for a chat group.
     * @param messageEntity The message entity that is to be converted.
     * @return Returns the message DTO that is created.
     */
    public Message convertMessageChatGroupToDTO(MessageEntity messageEntity) {
        Message messageDto = new Message();
        messageDto.setId(messageEntity.getId());
        messageDto.setContent(messageEntity.getContent());
        messageDto.setSender(userBean.convertUserToDTOForMessage(messageEntity.getSender()));
        messageDto.setSendTimestamp(messageEntity.getSendTimestamp());
        messageDto.setReadStatus(messageEntity.isReadStatus());
        messageDto.setReadTimestamp(messageEntity.getReadTimestamp());
        return messageDto;

    }
}