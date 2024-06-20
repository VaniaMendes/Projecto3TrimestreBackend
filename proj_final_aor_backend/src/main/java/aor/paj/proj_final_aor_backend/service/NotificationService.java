package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.NotificationBean;
import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.dao.SessionDao;
import aor.paj.proj_final_aor_backend.dto.Notification;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;

@Path("/notifications")
public class NotificationService {

    private static final Logger logger = LogManager.getLogger(NotificationService.class);

    @EJB
    NotificationBean notificationBean;
    @EJB
    UserBean userBean;


    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getNotifications(@HeaderParam("token") String token, @PathParam("userId") long userId,
                                     @QueryParam("page") @DefaultValue("1") int page , @Context HttpServletRequest request){
        String ip = request.getRemoteAddr();
        logger.debug("Received request to get notifications for user with id: " + userId);
        try {
            List<Notification> notifications = notificationBean.getNotificationsByUserId(token, userId, page, 6);

            if(notifications != null) {
                logger.info("IP Adress " + ip + "Notifications retrieved successfully by the user with the id" + userId);
                return Response.status(Response.Status.OK).entity(notifications).build();
            } else {
                logger.error("IP Adress " + ip + " Error retrieving notifications");
                return Response.status(Response.Status.BAD_REQUEST).entity("Error retrieving notifications").build();
            }
        } catch (Exception e) {
            logger.error("IP Adress " + ip + "Error retrieving notifications: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

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

    @GET
    @Path("/unread")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUnreadNotifications(@HeaderParam("token") String token){
        logger.info("Received request to get unread notifications");
        try {
            List<Notification> notifications = notificationBean.getUnreadNotifications(token);

            if(notifications != null) {
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
            int totalCount = notificationBean.getNumberofNotification(user.getId());
            int pageCount = (int) Math.ceil((double) totalCount / 6); // 6 notifications per page
            logger.info("IP Address " + ip + " Page count of notifications retrieved successfully: " + pageCount);
            return Response.status(Response.Status.OK).entity(pageCount).build();
        } catch (Exception e) {
            logger.error("IP Address " + ip + " Error retrieving page count of notifications: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


}

