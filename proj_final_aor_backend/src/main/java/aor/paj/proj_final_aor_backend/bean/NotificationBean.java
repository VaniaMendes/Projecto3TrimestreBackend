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

@Stateless
public class NotificationBean implements Serializable {

    /**
     * Logger for the MessageBean class.
     */
    private static final Logger logger = LogManager.getLogger(NotificationBean.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.registerModule(new JavaTimeModule());
    }

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
     * @param token The token of the user sending the notification.
     * @param userId The id of the user receiving the notification.
     * @param type The type of the notification.
     * @return True if the notification was sent successfully, false otherwise.
     */
    public boolean sendNotificationToOneUser(String token, long userId, NotificationType type){
        UserEntity sender = sessionDao.findUserByToken(token);

        if(sender == null) {
            logger.error("No user found with token: " + token);
            return false;
        }

        UserEntity user = userDao.findUserById(userId);
        if(user == null) {
            logger.error("No user found with id: " + userId);
            return false;
        }

        Notification notification = new Notification();
        notification.setReadStatus(false);
        notification.setSendTimestamp(LocalDateTime.now());
        notification.setSender(userBean.convertUserToDTOForMessage(sender));
        notification.setType(NotificationType.valueOf(type.toString()));
        notification.setReceiver(userBean.convertUserToDTOForMessage(user));

        NotificationEntity notificationEntity = convertDtoTOEntity(notification, sender);
        notificationDao.create(notificationEntity);

        user.getNotifications().add(notificationEntity);
        notificationEntity.getUsers().add(user);

        userDao.updateUser(user);
        System.out.println("id of notification" + notification.getId());
        notification.setId(notificationEntity.getId());


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
     * @param token The token of the user sending the notification.
     * @param type The type of the notification.
     * @return A list of notifications that were sent.
     * @throws Exception If no user is found with the given token, if there are no users to send the notification to, or if no type is specified for the notification.
     */

    public boolean sendNotificationToAllUsers(String token, NotificationType type, long projectId) {

        UserEntity sender = sessionDao.findUserByToken(token);

        if(sender == null) {
            logger.error("No user found with token: " + token);
            return false;
        }

        List<UserEntity> users = userBean.getAllUsers();
        if(users == null || users.isEmpty()) {
            logger.error("No users to send notification to");

            return false;
        }
        if(type == null || type.toString().isEmpty()) {
            logger.error("No type specified for notification");
            return false;
        }

        List<Notification> sentNotifications = new ArrayList<>();
        for (UserEntity user : users) {
            try {

                if(user.getId() != sender.getId() ){
                    logger.debug("Sending notification to user with id: " + user.getId());
                    Notification notification = new Notification();
                    notification.setReadStatus(false);
                    notification.setSendTimestamp(LocalDateTime.now());
                    notification.setSender(userBean.convertUserToDTOForMessage(sender));
                    notification.setType(type);
                    notification.setReceiver(userBean.convertUserToDTOForMessage(user));
                    notification.setRelatedIDEntity(projectId);



                    NotificationEntity notificationEntity = convertDtoTOEntity(notification, sender);
                    notificationDao.create(notificationEntity);

                    user.getNotifications().add(notificationEntity);
                    notificationEntity.getUsers().add(user);

                    userDao.updateUser(user);

                    sentNotifications.add(notification);
                    notification.setId(notificationEntity.getId());
                    try {
                        String jsonMsg = mapper.writeValueAsString(notification);
                        notifier.sendNotificationToAllExceptTheSender(jsonMsg);
                        logger.debug("Notification sent to user with id: " + user.getId());
                    } catch (Exception e) {
                        logger.error("Erro ao serializar a mensagem: " + e.getMessage());
                    }


                }
            } catch (Exception e) {
                logger.error("Error sending notification to user with id: " + user.getId(), e);
            }
        }

        return true;
    }



    public boolean sendNotificationToProjectUsers(String token, long project_id, String type, long projectID) {
        UserEntity sender = sessionDao.findUserByToken(token);

        if(sender == null) {
            logger.error("No user found with token: " + token);
            return false;
        }

        List<UserEntity> usersProject = userProjectBean.getActiveUsersInProject(project_id);
        System.out.println(usersProject.size());

        if(usersProject == null || usersProject.isEmpty()) {
            logger.error("No users to send notification to");

            return false;
        }
        if(type == null || type.isEmpty()) {
            logger.error("No type specified for notification");
            return false;
        }

        List<Notification> sentNotifications = new ArrayList<>();

        for (UserEntity user : usersProject) {
            System.out.println(user.getEmail());
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

        return true;
    }

    public long getUnreadNotifications(String token) {
        UserEntity user = sessionDao.findUserByToken(token);

        if (user == null) {
            logger.error("No user found with token: " + token);
            return 0;
        }

        long notifications = notificationDao.findUnreadNotificationsByUserID(user.getId());
        System.out.println(notifications);

        if (notifications == 0) {
            logger.error("No unread notifications found for user with id: " + user.getId());
            return 0;
        }


        return notifications;
    }

    public List<Notification> getNotificationsByUserId(String token, long userId, int page, int size) {
        UserEntity user = sessionDao.findUserByToken(token);

        if(user == null) {
            logger.error("No user found with token: " + token);
            return null;
        }

        List<NotificationEntity> notifications = notificationDao.findNotificationsByUserID(userId, page, size);

        if(notifications == null || notifications.isEmpty()) {
            logger.error("No notifications found for user with id: " + userId);
            return null;
        }

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

    public boolean markNotificationAsRead(String token, long notificationId){
        UserEntity user = sessionDao.findUserByToken(token);

        if(user == null) {
            logger.error("No user found with token: " + token);
            return false;
        }

        NotificationEntity notification = notificationDao.findNotificationById(notificationId);

        if(notification == null) {
            logger.error("No notification found with id: " + notificationId);
            return false;
        }

        notification.setReadStatus(true);
        notification.setReadTimestamp(LocalDateTime.now());
        notificationDao.merge(notification);


        String typeOfNotification =  NotificationType.MESSAGE_RECEIVED.toString();
        String typeOfNotification2 =  NotificationType.MESSAGE_PROJECT.toString();
        if(notification.getType().equals(typeOfNotification) || notification.getType().equals(typeOfNotification2)){
            List<NotificationEntity> notificationEntityList = notificationDao.findNotificationsByUserIDandType(user.getId(), typeOfNotification );

            for(NotificationEntity notificationEntity : notificationEntityList){
                notificationEntity.setReadStatus(true);
                notificationEntity.setReadTimestamp(LocalDateTime.now());
                notificationDao.merge(notificationEntity);
            }
        }
        return true;
    }

    public long getNumberofNotification(long userId) {
        return notificationDao.numberOfnotificationsByUserID(userId);
    }

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
