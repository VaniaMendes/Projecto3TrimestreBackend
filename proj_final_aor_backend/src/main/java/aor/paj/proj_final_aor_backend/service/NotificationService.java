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


    @POST
    @Path("/project/{projectId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response sendNotificationToProject(@HeaderParam("token") String token, @PathParam("projectId") long projectId,
                                              Notification notification, @Context HttpServletRequest request){

        User user = userBean.getUSerByToken(token);
        String ip = request.getRemoteAddr();

        if(user == null) {
            logger.error("No user found with token: " + token);
            return Response.status(Response.Status.BAD_REQUEST).entity("No user found with token: " + token).build();
        }
        logger.info("IP Adress: " + ip + "Received request to send a new notification to project " + projectId);
        try {
            boolean sent = notificationBean.sendNotificationToProjectUsers(token, projectId, notification.getType().toString());

            if(sent) {
                logger.info("IP Adress: "+ ip + "Notification sent successfully" + " to members of project with id " + projectId +
                        " at " + LocalDateTime.now());
                return Response.status(Response.Status.OK).entity("Notification sent successfully").build();
            } else {
                logger.error("Error sending notification");
                return Response.status(Response.Status.BAD_REQUEST).entity("Error sending notification").build();
            }
        } catch (Exception e) {
            logger.error("Error sending notification: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getNotifications(@HeaderParam("token") String token, @PathParam("userId") long userId){
        logger.info("Received request to get notifications for user with id: " + userId);
        try {
            List<Notification> notifications = notificationBean.getNotificationsByUserId(token, userId);

            if(notifications != null) {
                logger.info("Notifications retrieved successfully");
                return Response.status(Response.Status.OK).entity(notifications).build();
            } else {
                logger.error("Error retrieving notifications");
                return Response.status(Response.Status.BAD_REQUEST).entity("Error retrieving notifications").build();
            }
        } catch (Exception e) {
            logger.error("Error retrieving notifications: " + e.getMessage());
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

}
