package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.NotificationDao;
import aor.paj.proj_final_aor_backend.dao.SessionDao;
import aor.paj.proj_final_aor_backend.dao.UserDao;
import aor.paj.proj_final_aor_backend.dto.Notification;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.dto.UserInfoInProject;
import aor.paj.proj_final_aor_backend.entity.NotificationEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
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


    /**
     * Default constructor for the NotificationBean class.
     */
    public NotificationBean() {
    }

    /**
     * Method to send a notification to all users.
     * @param token The token of the user sending the notification.
     * @param type The type of the notification.
     * @return A list of notifications that were sent.
     * @throws Exception If no user is found with the given token, if there are no users to send the notification to, or if no type is specified for the notification.
     */

    public boolean sendNotificationToAllUsers(String token, String type) {

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
        if(type == null || type.isEmpty()) {
            logger.error("No type specified for notification");
            return false;
        }

        List<Notification> sentNotifications = new ArrayList<>();
        for (UserEntity user : users) {
            try {
                logger.debug("Sending notification to user with id: " + user.getId());
                Notification notification = new Notification();
                notification.setReadStatus(false);
                notification.setSendTimestamp(LocalDateTime.now());
                notification.setSender(userBean.convertUserToDTOForMessage(sender));
                notification.setType(type);
                notification.setReceiver(userBean.convertUserToDTOForMessage(user));
                notificationDao.create(convertDtoTOEntity(notification, sender));

                NotificationEntity notificationEntity = convertDtoTOEntity(notification, sender);
                notificationDao.create(notificationEntity); // Save the NotificationEntity first

                user.getNotifications().add(notificationEntity);
                notificationEntity.getUsers().add(user);

                userDao.updateUser(user); // This will update the intermediate table

                sentNotifications.add(notification);
            } catch (Exception e) {
                logger.error("Error sending notification to user with id: " + user.getId(), e);
            }
        }

        return true;
    }

    public boolean sendNotificationToProjectUsers(String token, long project_id, String type) {
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
                notification.setType(type);

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

    public NotificationEntity convertDtoTOEntity(Notification notification, UserEntity sender) {
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setReadStatus(notification.isReadStatus());
        notificationEntity.setSendTimestamp(notification.getSendTimestamp());
        notificationEntity.setType(notification.getType());
        notificationEntity.setSender_id(userBean.convertUserToDTOForMessage(sender).getId());
        return notificationEntity;
    }


}