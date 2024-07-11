package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.NotificationBean;
import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.dto.Notification;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Path("/notifications")
public class NotificationService {

    private static final Logger logger = LogManager.getLogger(NotificationService.class);

    @EJB
    NotificationBean notificationBean;
    @EJB
    UserBean userBean;


    /**
     * This method is used to retrieve all notifications for a specific user.
     * It first logs the IP address of the request.
     * Then it attempts to retrieve all notifications for the user with the provided user id from the database.
     * If the operation is successful and notifications are found, it logs the success and returns a response with status OK and the list of notifications.
     * If the operation is successful but no notifications are found, it logs the error and returns a response with status BAD_REQUEST and an error message.
     * If an exception occurs during the operation, it logs the exception message and returns a response with status INTERNAL_SERVER_ERROR and the exception message.
     *
     * @param token The token of the user trying to retrieve the notifications.
     * @param userId The id of the user whose notifications are to be retrieved.
     * @param page The page number for pagination. Default value is 1.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the list of all notifications for the user or an error message.
     */
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getNotifications(@HeaderParam("token") String token, @PathParam("userId") long userId,
                                     @QueryParam("page") @DefaultValue("1") int page, @Context HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        logger.debug("Received request to get notifications for user with id: " + userId);
        try {
            List<Notification> notifications = notificationBean.getNotificationsByUserId(token, userId, page, 6);

            if (notifications != null) {
                logger.info("IP Address " + ip + ": Notifications retrieved successfully for user with id " + userId);
                return Response.status(Response.Status.OK).entity(notifications).build();
            } else {
                logger.error("IP Address " + ip + ": Error retrieving notifications for user with id " + userId);
                return Response.status(Response.Status.BAD_REQUEST).entity("Error retrieving notifications").build();
            }
        } catch (Exception e) {
            logger.error("IP Address " + ip + ": Error retrieving notifications for user with id " + userId + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving notifications: " + e.getMessage()).build();
        }
    }

    /**
     * This method is used to mark a specific notification as read.
     * It first logs the request to mark the notification as read.
     * Then it attempts to mark the notification as read in the database.
     * If the operation is successful, it logs the success and returns a response with status OK and a success message.
     * If the operation fails, it logs the error and returns a response with status BAD_REQUEST and an error message.
     * If an exception occurs during the operation, it logs the exception message and returns a response with status BAD_REQUEST and the exception message.
     *
     * @param token The token of the user trying to mark the notification as read.
     * @param notificationId The id of the notification to be marked as read.
     * @return Response The response of the operation.
     */
    @PUT
    @Path("/{notificationId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response markNotificationAsRead(@HeaderParam("token") String token, @PathParam("notificationId") long notificationId){
        logger.info("Received request to mark notification with id: " + notificationId + " as read");
        try {
            boolean updated = notificationBean.markNotificationAsRead(token, notificationId);

            if(updated) {
                logger.info("Notification marked as read successfully");
                return Response.status(Response.Status.OK).entity("Notification marked as read successfully").build();
            } else {
                logger.error("Error marking notification as read");
                return Response.status(Response.Status.BAD_REQUEST).entity("Error marking notification as read").build();
            }
        } catch (Exception e) {
            logger.error("Error marking notification as read: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * This method is used to retrieve the count of unread notifications for a specific user.
     * It first logs the request to get unread notifications.
     * Then it attempts to retrieve the count of unread notifications for the user with the provided token from the database.
     * If the operation is successful and there are unread notifications, it logs the success and returns a response with status OK and the count of unread notifications.
     * If the operation is successful but there are no unread notifications, it logs the error and returns a response with status BAD_REQUEST and an error message.
     * If an exception occurs during the operation, it logs the exception message and returns a response with status BAD_REQUEST and the exception message.
     *
     * @param token The token of the user trying to retrieve the count of unread notifications.
     * @return Response The response of the operation, containing the count of unread notifications for the user or an error message.
     */
    @GET
    @Path("/unread")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUnreadNotifications(@HeaderParam("token") String token){
        logger.info("Received request to get unread notifications");
        try {
            long notifications = notificationBean.getUnreadNotifications(token);

            if(notifications !=0) {
                logger.info("Unread notifications retrieved successfully");
                return Response.status(Response.Status.OK).entity(notifications).build();
            } else {
                logger.error("Theres is no unread notifications");
                return Response.status(Response.Status.BAD_REQUEST).entity("Error retrieving unread notifications").build();
            }
        } catch (Exception e) {
            logger.error("Error retrieving unread notifications: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * This method is used to retrieve the total number of pages of notifications for a specific user.
     * It first logs the IP address of the request.
     * Then it retrieves the user associated with the provided token.
     * If the user is not found, it logs the error and returns a response with status UNAUTHORIZED and an error message.
     * If the user is found, it retrieves the total count of notifications for the user from the database.
     * It then calculates the total number of pages by dividing the total count by 6 (as there are 6 notifications per page) and rounding up to the nearest integer.
     * If the operation is successful, it logs the success and returns a response with status OK and the total number of pages.
     * If an exception occurs during the operation, it logs the exception message and returns a response with status BAD_REQUEST and the exception message.
     *
     * @param token The token of the user trying to retrieve the total number of pages of notifications.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the total number of pages of notifications for the user or an error message.
     */
    @GET
    @Path("/pageCount")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getPageCountOfNotifications(@HeaderParam("token") String token, @Context HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to get page count of notifications from IP: " + ip);

        User user = userBean.getUserByToken(token);
        if (user == null) {
            logger.error("IP Address " + ip + " User not found");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }

        try {
            long totalCount = notificationBean.getNumberofNotification(user.getId());
            System.out.println("Total count: " + totalCount);
            int pageCount = (int) Math.ceil((double) totalCount / 6); // 6 notifications per page
            logger.info("IP Address " + ip + " Page count of notifications retrieved successfully: " + pageCount);
            return Response.status(Response.Status.OK).entity(pageCount).build();
        } catch (Exception e) {
            logger.error("IP Address " + ip + " Error retrieving page count of notifications: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * This method is used to retrieve all unread notifications for a specific user.
     * It first logs the request to get unread notifications for the user with the provided user id.
     * Then it attempts to retrieve all unread notifications for the user with the provided token from the database.
     * If the operation is successful and unread notifications are found, it logs the success and returns a response with status OK and the list of unread notifications.
     * If the operation is successful but no unread notifications are found, it logs the error and returns a response with status BAD_REQUEST and an error message.
     * If an exception occurs during the operation, it logs the exception message and returns a response with status BAD_REQUEST and the exception message.
     *
     * @param token The token of the user trying to retrieve the unread notifications.
     * @param userId The id of the user whose unread notifications are to be retrieved.
     * @return Response The response of the operation, containing the list of all unread notifications for the user or an error message.
     */
    @GET
    @Path("/unread/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUnreadNotificationsByUserId(@HeaderParam("token") String token,
                                                   @PathParam("userId") long userId){
        logger.info("Received request to get unread notifications for user with id: " + userId);
        try {
            List<Notification> notifications = notificationBean.getUnreadNotificationList(token);

            if(notifications != null) {
                logger.info("Unread notifications retrieved successfully for user with id: " + userId);
                return Response.status(Response.Status.OK).entity(notifications).build();
            } else {
                logger.error("Theres is no unread notifications for user with id: " + userId);
                return Response.status(Response.Status.BAD_REQUEST).entity("Error retrieving unread notifications").build();
            }
        } catch (Exception e) {
            logger.error("Error retrieving unread notifications for user with id: " + userId + ": " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * This method is used to mark all notifications as open for a specific user.
     * It first logs the request to mark all notifications as open.
     * Then it attempts to mark all notifications as open in the database for the user with the provided token.
     * If the operation is successful, it logs the success and returns a response with status OK and a success message.
     * If the operation fails, it logs the error and returns a response with status BAD_REQUEST and an error message.
     * If an exception occurs during the operation, it logs the exception message and returns a response with status BAD_REQUEST and the exception message.
     *
     * @param token The token of the user trying to mark all notifications as open.
     * @return Response The response of the operation.
     */
    @PUT
    @Path("/markAllAsOpen")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response markAllNotificationsAsOpen(@HeaderParam("token") String token){
        logger.info("Received request to mark all notifications as open");
        try {
            boolean updated = notificationBean.markAllNotificationsAsOpen(token);

            if(updated) {
                logger.info("All notifications marked as open successfully");
                return Response.status(Response.Status.OK).entity("All notifications marked as open successfully").build();
            } else {
                logger.error("Error marking all notifications as open");
                return Response.status(Response.Status.BAD_REQUEST).entity("Error marking all notifications as open").build();
            }
        } catch (Exception e) {
            logger.error("Error marking all notifications as open: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * This method is used to retrieve the number of open notifications for a specific user.
     * It first logs the request to get open notifications.
     * Then it retrieves the user associated with the provided token.
     * If the user is not found, it logs the error and returns a response with status BAD_REQUEST and an error message.
     * If the user is found, it retrieves the number of open notifications for the user from the database.
     * If the operation is successful and there are open notifications, it logs the success and returns a response with status OK and the number of open notifications.
     * If the operation is successful but there are no open notifications, it logs the success and returns a response with status OK and the number of open notifications (which is 0).
     * If an exception occurs during the operation, it logs the exception message and returns a response with status BAD_REQUEST and the exception message.
     *
     * @param token The token of the user trying to retrieve the number of open notifications.
     * @return Response The response of the operation, containing the number of open notifications for the user or an error message.
     */
    @GET
    @Path("/open")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getOpenNotifications(@HeaderParam("token") String token){
        logger.info("Received request to get open notifications");
        try {
            // Find the user
            User user = userBean.getUserByToken(token);

            // Check if the user exists
            if (user == null) {
                logger.error("No user found with token: " + token);
                return Response.status(Response.Status.BAD_REQUEST).entity("No user found with token").build();
            }

            // Find the number of open notifications for the user
            long openNotifications = notificationBean.getOpenNotifications(token);

            if (openNotifications > 0) {
                logger.info("Open notifications retrieved successfully");
                return Response.status(Response.Status.OK).entity(openNotifications).build();
            } else {
                logger.info("No open notifications found for user with id: " + user.getId());
                return Response.status(Response.Status.OK).entity(openNotifications).build();
            }
        } catch (Exception e) {
            logger.error("Error retrieving open notifications: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity("Error retrieving open notifications").build();
        }
    }

}

