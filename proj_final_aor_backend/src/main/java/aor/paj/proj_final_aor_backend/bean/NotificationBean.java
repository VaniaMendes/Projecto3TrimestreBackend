package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.NotificationDao;
import aor.paj.proj_final_aor_backend.dao.SessionDao;
import aor.paj.proj_final_aor_backend.dto.Notification;
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
        // assuming you have a method to convert MessageInfoUser to UserEntity
        notificationEntity.setSender_id(userBean.convertUserToDTOForMessage(sender).getId());
        return notificationEntity;
    }


}
