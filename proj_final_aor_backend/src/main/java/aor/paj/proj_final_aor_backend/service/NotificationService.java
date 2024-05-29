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
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response sendNotification(@HeaderParam("token") String token, Notification notification, @Context HttpServletRequest request){
        logger.info("Received request to send a new notification");
        try {
            boolean sent = notificationBean.sendNotificationToAllUsers(token, notification.getType());

       if(sent) {
                logger.info("Notification sent successfully");
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
            boolean sent = notificationBean.sendNotificationToProjectUsers(token, projectId, notification.getType());

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

}
