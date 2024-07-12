package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.NotificationDao;
import aor.paj.proj_final_aor_backend.dao.SessionDao;
import aor.paj.proj_final_aor_backend.dao.UserDao;
import aor.paj.proj_final_aor_backend.dto.MessageInfoUser;
import aor.paj.proj_final_aor_backend.dto.Notification;

import aor.paj.proj_final_aor_backend.entity.NotificationEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import aor.paj.proj_final_aor_backend.util.enums.NotificationType;
import aor.paj.proj_final_aor_backend.websocket.Notifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.xml.bind.SchemaOutputResolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for handling the business logic for the Notification entity.
 * It is responsible for sending notifications to users, marking notifications as read,
 * and getting notifications by user id.
 */
@Stateless
public class NotificationBean implements Serializable {

    /**
     * Logger for the MessageBean class.
     */
    private static final Logger logger = LogManager.getLogger(NotificationBean.class);
    /**
     * ObjectMapper instance to serialize and deserialize objects.
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * Object instance to interact with the database.
     */
    @EJB
    NotificationDao notificationDao;
    @EJB
    UserBean userBean;
    @EJB
    SessionDao sessionDao;
    @EJB
    UserProjectBean userProjectBean;
    @EJB
    UserDao userDao;
    @EJB
    Notifier notifier;


    /**
     * Default constructor for the NotificationBean class.
     */
    public NotificationBean() {
    }


    /**
     * Method to send a notification to a specific user.
     *
     * @param token  The token of the user sending the notification.
     * @param userId The id of the user receiving the notification.
     * @param type   The type of the notification.
     * @return True if the notification was sent successfully, false otherwise.
     */
    public boolean sendNotificationToOneUser(String token, long userId, NotificationType type) {
        // Find the user sending the notification
        UserEntity sender = sessionDao.findUserByToken(token);

        // Check if the user exists
        if (sender == null) {
            logger.error("No user found with token: " + token);
            return false;
        }

        // Find the user receiving the notification
        UserEntity user = userDao.findUserById(userId);
        if (user == null) {
            logger.error("No user found with id: " + userId);
            return false;
        }

        //Create the notification
        Notification notification = new Notification();
        notification.setReadStatus(false);
        notification.setSendTimestamp(LocalDateTime.now());
        notification.setSender(userBean.convertUserToDTOForMessage(sender));
        notification.setType(NotificationType.valueOf(type.toString()));
        notification.setReceiver(userBean.convertUserToDTOForMessage(user));


        NotificationEntity notificationEntity = convertDtoTOEntity(notification, sender);
        //Persist the notification in the database
        notificationDao.create(notificationEntity);

        //Add the notification to the user's list of notifications
        user.getNotifications().add(notificationEntity);
        notificationEntity.getUsers().add(user);

        //Update the user in the database
        userDao.updateUser(user);


        notification.setId(notificationEntity.getId());


        //Send the notification to the user
        try {
            String jsonMsg = mapper.writeValueAsString(notification);
            notifier.sendNotificationToUser(jsonMsg);
            logger.debug("Notification sent to user with id: " + user.getId());
        } catch (Exception e) {
            logger.error("Erro ao serializar a mensagem: " + e.getMessage());
        }

        return true;
    }

    /**
     * Method to send a notification to all users.
     *
     * @param token The token of the user sending the notification.
     * @param type  The type of the notification.
     * @return A list of notifications that were sent.
     * @throws Exception If no user is found with the given token, if there are no users to send the notification to, or if no type is specified for the notification.
     */

    public boolean sendNotificationToAllUsers(String token, NotificationType type, long projectId) {

        // Find the user sending the notification
        UserEntity sender = sessionDao.findUserByToken(token);

        // Check if the user exists
        if (sender == null) {
            logger.error("No user found with token: " + token);
            return false;
        }

        // Find all users
        List<UserEntity> users = userBean.getAllUsers();
        if (users == null || users.isEmpty()) {
            logger.error("No users to send notification to");

            return false;
        }
        // Check if a type is specified for the notification
        if (type == null || type.toString().isEmpty()) {
            logger.error("No type specified for notification");
            return false;
        }

        // Create a list to store the notifications that were sent
        List<Notification> sentNotifications = new ArrayList<>();
        for (UserEntity user : users) {
            if (user.getId() != sender.getId()) {
            try {


                    logger.debug("Sending notification to user with id: " + user.getId());
                    Notification notification = new Notification();
                    notification.setReadStatus(false);
                    notification.setSendTimestamp(LocalDateTime.now());
                    notification.setSender(userBean.convertUserToDTOForMessage(sender));
                    notification.setType(type);
                    notification.setReceiver(userBean.convertUserToDTOForMessage(user));
                    notification.setRelatedIDEntity(projectId);


                    //Create the notification
                    NotificationEntity notificationEntity = convertDtoTOEntity(notification, sender);
                    //Persist the notification in the database
                    notificationDao.create(notificationEntity);

                    user.getNotifications().add(notificationEntity);
                    notificationEntity.getUsers().add(user);

                    userDao.updateUser(user);

                    sentNotifications.add(notification);
                    notification.setId(notificationEntity.getId());
                    //Send the notification to the user
                    try {
                        String jsonMsg = mapper.writeValueAsString(notification);
                        notifier.sendNotificationToAllExceptTheSender(jsonMsg);
                        logger.debug("Notification sent to user with id: " + user.getId());
                    } catch (Exception e) {
                        logger.error("Erro ao serializar a mensagem: " + e.getMessage());
                    }



            } catch (Exception e) {
                logger.error("Error sending notification to user with id: " + user.getId(), e);
            }
            }
        }

        return true;
    }


    /**
     * Method to send a notification to all users in a project.
     *
     * @param token      The token of the user sending the notification.
     * @param project_id The id of the project.
     * @param type       The type of the notification.
     * @param projectID  The id of the project.
     * @return True if the notification was sent successfully, false otherwise.
     */
    public boolean sendNotificationToProjectUsers(String token, long project_id, String type, long projectID) {
        // Find the user sending the notification
        UserEntity sender = sessionDao.findUserByToken(token);

        // Check if the user exists
        if (sender == null) {
            logger.error("No user found with token: " + token);
            return false;
        }

        // Find all users in the project
        List<UserEntity> usersProject = userProjectBean.getActiveUsersInProject(project_id);

        // Check if there are users in the project
        if (usersProject == null || usersProject.isEmpty()) {
            logger.error("No users to send notification to");

            return false;
        }
        // Check if a type is specified for the notification
        if (type == null || type.isEmpty()) {
            logger.error("No type specified for notification");
            return false;
        }

        // Create a list to store the notifications that were sent
        List<Notification> sentNotifications = new ArrayList<>();

        // Send the notification to each user in the project
        for (UserEntity user : usersProject) {
            System.out.println(user.getEmail());
            if(user.getId() != sender.getId()) {
                try {

                    logger.debug("Sending notification to user with id: " + user.getId());
                    Notification notification = new Notification();
                    notification.setReadStatus(false);
                    notification.setSendTimestamp(LocalDateTime.now());
                    notification.setSender(userBean.convertUserToDTOForMessage(sender));
                    notification.setType(NotificationType.valueOf(type));
                    notification.setRelatedIDEntity(projectID);

                    NotificationEntity notificationEntity = convertDtoTOEntity(notification, sender);
                    notificationDao.create(notificationEntity);

                    user.getNotifications().add(notificationEntity);
                    notificationEntity.getUsers().add(user);

                    userDao.updateUser(user);

                    sentNotifications.add(notification);

                } catch (Exception e) {
                    logger.error("Error sending notification to user with id: " + user.getId(), e);
                }
            }
        }

        return true;
    }

    /**
     * Method to get the number of unread notifications for a user.
     *
     * @param token The token of the user sending the notification.
     * @return The number of unread notifications for the user.
     */
    public long getUnreadNotifications(String token) {
        // Find the user
        UserEntity user = sessionDao.findUserByToken(token);

        // Check if the user exists
        if (user == null) {
            logger.error("No user found with token: " + token);
            return 0;
        }

        // Find the number of unread notifications for the user
        long notifications = notificationDao.findUnreadNotificationsByUserID(user.getId());

        // Check if there are unread notifications
        if (notifications == 0) {
            logger.error("No unread notifications found for user with id: " + user.getId());
            return 0;
        }

        // Return the number of unread notifications
        return notifications;
    }


   public List<Notification> getUnreadNotificationList(String token){
        // Find the user
        UserEntity user = sessionDao.findUserByToken(token);

        // Check if the user exists
        if (user == null) {
            logger.error("No user found with token: " + token);
            return null;
        }

        // Find the notifications for the user
        List<NotificationEntity> notifications = notificationDao.findNotificationsListForUser(user.getId());

        // Check if there are notifications
        if (notifications == null || notifications.isEmpty()) {
            logger.error("No notifications found for user with id: " + user.getId());
            return null;
        }

        // Convert the notifications to DTOs
        List<Notification> notificationList = new ArrayList<>();

        for (NotificationEntity notificationEntity : notifications) {

            Notification notification = new Notification();
            notification.setId(notificationEntity.getId());
            notification.setReadStatus(notificationEntity.isReadStatus());
            notification.setSendTimestamp(notificationEntity.getSendTimestamp());
            notification.setType(NotificationType.valueOf(notificationEntity.getType().toString()));
            notification.setRelatedIDEntity(notificationEntity.getRelatedEntityId());
            UserEntity senderEntity = new UserEntity();
            senderEntity.setId(notificationEntity.getSender_id());
            senderEntity.setFirstName(userDao.findUserById(notificationEntity.getSender_id()).getFirstName());
            senderEntity.setLastName(userDao.findUserById(notificationEntity.getSender_id()).getLastName());
            senderEntity.setPhoto(userDao.findUserById(notificationEntity.getSender_id()).getPhoto());


            MessageInfoUser senderDto = userBean.convertUserToDTOForMessage(senderEntity);

            notification.setSender(senderDto);
            notificationList.add(notification);
        }

        return notificationList;
   }


    /**
     * Method to get the notifications for a user.
     *
     * @param token  The token of the user sending the notification.
     * @param userId The id of the user.
     * @param page   The page number.
     * @param size   The number of notifications per page.
     * @return A list of notifications for the user.
     */
    public List<Notification> getNotificationsByUserId(String token, long userId, int page, int size) {
        // Find the user
        UserEntity user = sessionDao.findUserByToken(token);

        // Check if the user exists
        if (user == null) {
            logger.error("No user found with token: " + token);
            return null;
        }

        // Find the notifications for the user
        List<NotificationEntity> notifications = notificationDao.findNotificationsByUserID(userId, page, size);

        // Check if there are notifications
        if (notifications == null || notifications.isEmpty()) {
            logger.error("No notifications found for user with id: " + userId);
            return null;
        }

        // Convert the notifications to DTOs
        List<Notification> notificationList = new ArrayList<>();

        for (NotificationEntity notificationEntity : notifications) {

            Notification notification = new Notification();
            notification.setId(notificationEntity.getId());
            notification.setReadStatus(notificationEntity.isReadStatus());
            notification.setSendTimestamp(notificationEntity.getSendTimestamp());
            notification.setType(NotificationType.valueOf(notificationEntity.getType().toString()));
            notification.setRelatedIDEntity(notificationEntity.getRelatedEntityId());
            UserEntity senderEntity = new UserEntity();
            senderEntity.setId(notificationEntity.getSender_id());
            senderEntity.setFirstName(userDao.findUserById(notificationEntity.getSender_id()).getFirstName());
            senderEntity.setLastName(userDao.findUserById(notificationEntity.getSender_id()).getLastName());
            senderEntity.setPhoto(userDao.findUserById(notificationEntity.getSender_id()).getPhoto());


            MessageInfoUser senderDto = userBean.convertUserToDTOForMessage(senderEntity);

            notification.setSender(senderDto);
            notificationList.add(notification);
        }

        return notificationList;
    }

    /**
     * Method to mark a notification as read.
     *
     * @param token          The token of the user sending the notification.
     * @param notificationId The id of the notification.
     * @return True if the notification was marked as read successfully, false otherwise.
     */
    public boolean markNotificationAsRead(String token, long notificationId) {
        // Find the user
        UserEntity user = sessionDao.findUserByToken(token);

        // Check if the user exists
        if (user == null) {
            logger.error("No user found with token: " + token);
            return false;
        }

        // Find the notification
        NotificationEntity notification = notificationDao.findNotificationById(notificationId);

        // Check if the notification exists
        if (notification == null) {
            logger.error("No notification found with id: " + notificationId);
            return false;
        }

        // Mark the notification as read
        notification.setReadStatus(true);
        notification.setReadTimestamp(LocalDateTime.now());
        notificationDao.merge(notification);


        // Check if the notification is a message to mark all messages as read
        String typeOfNotification = NotificationType.MESSAGE_RECEIVED.toString();
        String typeOfNotification2 = NotificationType.MESSAGE_PROJECT.toString();

        if (notification.getType().equals(typeOfNotification) || notification.getType().equals(typeOfNotification2)) {
            // Fetch all notifications of both types
            List<NotificationEntity> notificationEntityList1 = notificationDao.findNotificationsByUserIDandType(user.getId(), typeOfNotification);
            List<NotificationEntity> notificationEntityList2 = notificationDao.findNotificationsByUserIDandType(user.getId(), typeOfNotification2);

            // Mark all notifications of both types as read
            for (NotificationEntity notificationEntity : notificationEntityList1) {
                notificationEntity.setReadStatus(true);
                notificationEntity.setReadTimestamp(LocalDateTime.now());
                notificationDao.merge(notificationEntity);
            }

            for (NotificationEntity notificationEntity : notificationEntityList2) {
                notificationEntity.setReadStatus(true);
                notificationEntity.setReadTimestamp(LocalDateTime.now());
                notificationDao.merge(notificationEntity);
            }
        }
        return true;
    }



    /**
     * Method to get the number of notifications for a user.
     *
     * @param userId The id of the user.
     * @return The number of notifications for the user.
     */
    public long getNumberofNotification(long userId) {
        return notificationDao.numberOfnotificationsByUserID(userId);
    }

    /**
     * Method to convert a notification DTO to an entity.
     *
     * @param notification The notification DTO.
     * @param sender       The sender of the notification.
     * @return The notification entity.
     */
    public NotificationEntity convertDtoTOEntity(Notification notification, UserEntity sender) {
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setReadTimestamp(notification.getReadTimestamp());
        notificationEntity.setId(notification.getId());
        notificationEntity.setReadStatus(notification.isReadStatus());
        notificationEntity.setSendTimestamp(notification.getSendTimestamp());
        notificationEntity.setType(notification.getType().toString());
        notificationEntity.setRelatedEntityId(notification.getRelatedIDEntity());
        notificationEntity.setSender_id(userBean.convertUserToDTOForMessage(sender).getId());
        return notificationEntity;
    }


}
