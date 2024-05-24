package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.MessageBean;
import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.dao.SessionDao;
import aor.paj.proj_final_aor_backend.dto.Message;
import aor.paj.proj_final_aor_backend.dto.User;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Path("/messages")
public class MessageService {
    private static final Logger logger = LogManager.getLogger(MessageService.class);

    @EJB
    MessageBean messageBean;

    @EJB
    UserBean userBean;


    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response sendNewMessage(@HeaderParam("token") String token, Message message, @Context HttpServletRequest request){

        // Get the user by token
        User user = userBean.getUSerByToken(token);
        String ip = request.getRemoteAddr();
        logger.info("Received request to send a new message from IP: " + ip);

        if(user == null) {
            logger.error("User not found");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }

        // Check if the receiver exists
        User receiver = userBean.getUserById(message.getReceiver().getId());

        if(message.getContent().isEmpty() || receiver== null){
            logger.error("Receiver not found");
            return Response.status(Response.Status.BAD_REQUEST).entity("Receiver not found").build();
        }
        // Send the message
        boolean sent = messageBean.sendMessage(token, message.getContent(), message.getReceiver().getId());
        System.out.println("Message sent: " + sent);

        // Check if the message was sent successfully
        if (sent) {
            logger.info("Message sent successfully");
            return Response.status(Response.Status.CREATED).entity("Message sent successfully").build();
        } else {
            logger.error("Failed to send message");
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to send message").build();
        }
    }

    @GET
    @Path("/{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getMessagesBetweenTwoUsers(@HeaderParam("token") String token, @PathParam("user_id") long user_id, @Context HttpServletRequest request ){
        //Get the user by token
        User user1 = userBean.getUSerByToken(token);
        User user2 = userBean.getUserById(user_id);
        String ip = request.getRemoteAddr();
        logger.info("Received request to get messages between two users from IP: " + ip);
        if(user1 == null || user2 == null){
            logger.error("IP Adress " + ip + "User not found");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }
        List<Message> messages = messageBean.getMessagesBetweenTwoUsers(token, user_id);
        if(messages == null || messages.isEmpty()){
            logger.error("IP Adress " + ip + "Messages not found");
            return Response.status(Response.Status.NOT_FOUND).entity("Messages not found").build();
        }
        return Response.status(Response.Status.OK).entity(messages).build();
    }

}
