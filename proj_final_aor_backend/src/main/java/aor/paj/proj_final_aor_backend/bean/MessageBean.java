package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.*;
import aor.paj.proj_final_aor_backend.dto.Message;
import aor.paj.proj_final_aor_backend.dto.MessageInfoUser;
import aor.paj.proj_final_aor_backend.entity.MessageEntity;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import aor.paj.proj_final_aor_backend.entity.UserProjectEntity;
import aor.paj.proj_final_aor_backend.util.enums.NotificationType;
import aor.paj.proj_final_aor_backend.websocket.Notifier;
import aor.paj.proj_final_aor_backend.websocket.WebsocketMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static aor.paj.proj_final_aor_backend.util.enums.NotificationType.MESSAGE_RECEIVED;


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
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.registerModule(new JavaTimeModule());
    }

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
    @EJB
    NotificationBean notificationBean;
    @EJB
    Notifier notifier;

    @EJB
    WebsocketMessage websocketMessage;


    /**
     * Default constructor for the MessageBean class.
     */
    public MessageBean() {
    }


    /**
     * This method is used to send a message from one user to another.
     *
     * @param token   The token of the user who is sending the message. This is used to authenticate the user.
        * @param message The message that is to be sent.
     * @return Returns true if the message was successfully sent, false otherwise.
     */
    public boolean sendMessage(String token, Message message)  {
        // Find the user by token
        UserEntity user = sessionDao.findUserByToken(token);
        // Check if the user exists
        if (user == null) {
            logger.error("User not found");
            return false;
        }

        // Find the receiver by id
        UserEntity receiver = userDao.findUserById(message.getReceiver().getId());
        // Check if the receiver exists
        if (receiver == null || receiver.getId()==user.getId()) {
            logger.error("Receiver not found");
            return false;
        }
        // Create the message
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setContent(message.getContent());
        messageEntity.setSubject(message.getSubject());
        messageEntity.setSubject(message.getSubject());
        messageEntity.setSender(user);
        messageEntity.setReceiver(receiver);
        messageEntity.setSendTimestamp(LocalDateTime.now());
        messageEntity.setReadStatus(false);
        messageEntity.setReadTimestamp(null);
        // Save the message in the database
        messageDao.createMessage(messageEntity);

        notificationBean.sendNotificationToOneUser(token, message.getReceiver().getId(), MESSAGE_RECEIVED);




        try {
            String jsonMsg = mapper.writeValueAsString(convertMessageToDto(messageEntity));
            websocketMessage.sendMessageTOUser(jsonMsg);
            logger.debug("Notification sent to user with id: " + user.getId());
        } catch (Exception e) {
            logger.error("Erro ao serializar a mensagem: " + e.getMessage());
        }

        return true;
    }


    /**
     * This method is used to get all the messages between two users.
     * It is used to display the messages in the chat between two users.
     * @param token The token of the user who is requesting the messages. This is used to authenticate the user.
     * @param id The id of the user with whom the messages are to be retrieved.
     * @return Returns a list of messages between the two users.
     */
    public List<Message> getMessagesBetweenTwoUsers(String token, long id, int page, int size) {

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
        List<MessageEntity> messageEntities = messageDao.findMessagesBetweenUsers(user1, user2, page, size);

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

        cloneMessageEntities(project);
        // Create the message
        MessageEntity message = new MessageEntity();
        message.setContent(content);
        message.setSender(sender);
        message.setReceiverGroup(userProject);
        message.setSendTimestamp(LocalDateTime.now());
        message.setReadStatus(false);
        message.setReadTimestamp(null);


        // Add the new message to the userProject's messagesReceived
        userProject.addMessageReceived(message);

        // Save the message in the database
        messageDao.createMessage(message);
        userProjectDao.merge(userProject);

        String nameOfProject = project.getName();
        String type = String.valueOf(NotificationType.MESSAGE_PROJECT);
        notificationBean.sendNotificationToProjectUsers(token, project_id, type, nameOfProject );
        try {
            String jsonMsg = mapper.writeValueAsString(convertMessageChatGroupToDTO(message));
            websocketMessage.sendMessageToProject(jsonMsg, project_id);
            logger.debug("Message sent to project with id: " + project_id);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing the message: " + e.getMessage());
        }
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

    public int getMessageCountBetweenTwoUsers(String token, long id) {
        UserEntity user1 = sessionDao.findUserByToken(token);
        UserEntity user2 = userDao.findUserById(id);
        if (user1 == null || user2 == null) {
            logger.error("User not found");
            return 0;
        }
        return messageDao.findTotalMessagesBetweenTwoUsers(user1, user2);
    }

    public List<MessageInfoUser> getListOfUsersWithExchangeMessages(String token) {
        UserEntity user = sessionDao.findUserByToken(token);
        if (user == null) {
            logger.debug("User not found ffffffffffffffffffffffff");
            return null;
        }
        List<UserEntity> listOfUsers = messageDao.findUsersWithExchangedMessages(user.getId());
        if(listOfUsers.isEmpty()){
            logger.info("No users found with exchanged messages");
            return null;
        }
        System.out.println(listOfUsers.size());

        List<MessageInfoUser> users = new ArrayList<>();
        if (listOfUsers != null && !listOfUsers.isEmpty()) {
            for (UserEntity userEntity : listOfUsers) {
                if (userEntity != null) {
                    MessageInfoUser user1 = userBean.convertUserToDTOForMessage(userEntity);
                    users.add(user1);
                } else {
                    logger.warn("Null UserEntity found in listOfUsers");
                }
            }
        }
        return users;
    }

    public boolean markMessageAsRead(String token, long messageId) {
        UserEntity user = sessionDao.findUserByToken(token);
        System.out.println(user.getFirstName());
        if (user == null) {
            logger.error("User not found");
            return false;
        }
        MessageEntity message = messageDao.findMessageById(messageId);
        System.out.println(messageId);
        if (message == null) {
            logger.error("Message not found");
            return false;
        }
        if (message.getReceiver().getId() != user.getId()) {
            logger.error("User not receiver of message");
            return false;
        }
        message.setReadStatus(true);
        message.setReadTimestamp(LocalDateTime.now());
        messageDao.updateMessage(message);
        return true;
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
        messageDto.setSubject(messageEntity.getSubject());
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
        messageDto.setProjectId(messageEntity.getReceiverGroup().getProject().getId());
        return messageDto;

    }

    public void cloneMessageEntities(ProjectEntity project) {
        Set<UserProjectEntity> userProjects = project.getUserProjects();
        for (UserProjectEntity userProject : userProjects) {
            Set<MessageEntity> originalMessages = userProject.getMessagesReceived();
            Set<MessageEntity> clonedMessages = new HashSet<>(originalMessages);
            userProject.setMessagesReceived(clonedMessages);
        }
    }
}