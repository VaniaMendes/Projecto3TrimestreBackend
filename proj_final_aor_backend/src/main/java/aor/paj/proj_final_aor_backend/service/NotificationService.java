package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.NotificationBean;
import aor.paj.proj_final_aor_backend.dto.Notification;
import jakarta.ejb.EJB;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
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

    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response sendNotification(@HeaderParam("token") String token, Notification notification){
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
}
