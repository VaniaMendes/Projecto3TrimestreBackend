package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.MessageBean;
import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.dto.Message;
import aor.paj.proj_final_aor_backend.dto.MessageInfoUser;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.bean.UserBean;
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

@Path("/messages")
public class MessageService {
    private static final Logger logger = LogManager.getLogger(MessageService.class);

    @EJB
    MessageBean messageBean;

    @EJB
    UserBean userBean;


    /**
     * This method is used to send a new message from one user to another.
     * It first retrieves the user by the provided token and logs the IP address of the request.
     * If the user is not found, it returns a response with status UNAUTHORIZED.
     * If the user is found, it checks if the receiver exists and if the message content is not empty.
     * If the receiver does not exist or the message content is empty, it returns a response with status BAD_REQUEST.
     * If the receiver exists and the message content is not empty, it attempts to send the message.
     * If the message is sent successfully, it logs the success and returns a response with status CREATED.
     * If the message sending fails, it logs the failure and returns a response with status BAD_REQUEST.
     *
     * @param token The token of the user trying to send the message.
     * @param message The message to be sent.
     * @param request The HTTP request.
     * @return Response The response of the operation.
     */
    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendNewMessage(@HeaderParam("token") String token, Message message, @Context HttpServletRequest request) {

        // Get the user by token
        User user = userBean.getUserByToken(token);
        String ip = request.getRemoteAddr();
        logger.info("Received request to send a new message from IP: " + ip);

        if (user == null) {
            logger.error("User not found");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }

        System.out.println(message.getReceiver().getId());
        // Check if the receiver exists
        User receiver = userBean.getUserById(message.getReceiver().getId());

        if (message.getContent().isEmpty() || receiver == null) {
            logger.error("Receiver not found");
            return Response.status(Response.Status.BAD_REQUEST).entity("Receiver not found").build();
        }
        // Send the message
        boolean sent = messageBean.sendMessage(token, message);
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

    /**
     * This method is used to retrieve all messages between two users.
     * It first retrieves the users by the provided token and user id and logs the IP address of the request.
     * If either of the users is not found, it returns a response with status UNAUTHORIZED.
     * If both users are found, it retrieves all messages between them from the database.
     * If no messages are found, it returns a response with status NOT_FOUND.
     * If messages are found, it returns a response with status OK and the list of all messages between the two users.
     *
     * @param token The token of the user trying to retrieve the messages.
     * @param user_id The id of the other user with whom the messages are exchanged.
     * @param page The page number for pagination. Default value is 0.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the list of all messages between the two users.
     */
    @GET
    @Path("/{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getMessagesBetweenTwoUsers(@HeaderParam("token") String token,
                                               @PathParam("user_id") long user_id,
                                               @QueryParam("page") @DefaultValue("0") int page,
                                               @Context HttpServletRequest request) {
        //Get the user by token
        User user1 = userBean.getUserByToken(token);
        User user2 = userBean.getUserById(user_id);
        String ip = request.getRemoteAddr();
        logger.info("Received request to get messages between two users from IP: " + ip);
        if (user1 == null || user2 == null) {
            logger.error("IP Adress " + ip + "User not found");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }
        List<Message> messages = messageBean.getMessagesBetweenTwoUsers(token, user_id, page, 4);
        if (messages == null || messages.isEmpty()) {
            logger.error("IP Adress " + ip + "Messages not found");
            return Response.status(Response.Status.NOT_FOUND).entity("Messages not found").build();
        }
        return Response.status(Response.Status.OK).entity(messages).build();
    }


    /**
     * This method is used to send a new message to a chat group associated with a specific project.
     * It first retrieves the user by the provided token and logs the IP address of the request.
     * If the user is not found, it returns a response with status UNAUTHORIZED.
     * If the user is found, it attempts to send the message to the chat group of the specified project.
     * If the message is sent successfully, it logs the success and returns a response with status CREATED.
     * If the message sending fails, it logs the failure and returns a response with status BAD_REQUEST.
     *
     * @param token The token of the user trying to send the message.
     * @param project_id The id of the project whose chat group the message is to be sent to.
     * @param message The message to be sent.
     * @param request The HTTP request.
     * @return Response The response of the operation.
     */
    @POST
    @Path("/send/{project_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response sendMessageToChat(@HeaderParam("token") String token, @PathParam("project_id") long project_id,
                                      Message message, @Context HttpServletRequest request) {
        // Get the user by token
        User user = userBean.getUserByToken(token);
        String ip = request.getRemoteAddr();
        logger.info("Received request to send a message to a chat group from IP: " + ip);
        if (user == null) {
            logger.error("User not found");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }
        // Send the message
        boolean sent = messageBean.sendMessageToChatGroup(token, project_id, message.getContent());
        System.out.println(sent);
        if (sent) {
            logger.info("IP Adress: " + ip + "Message was sent sucessfully for project with the id: " + project_id + " by user with the id: " + user.getId() + " at " + LocalDateTime.now());
            return Response.status(Response.Status.CREATED).entity("Message sent successfully").build();
        } else {
            logger.error("IP Adress: " + ip + "Message not sent for project with the id: " + project_id + " by user with the id: " + user.getId() + " at " + LocalDateTime.now());
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to send message").build();
        }
    }

    /**
     * This method is used to retrieve all messages associated with a specific project.
     * It first retrieves the user by the provided token and logs the IP address of the request.
     * If the user is not found, it returns a response with status UNAUTHORIZED.
     * If the user is found, it retrieves all messages associated with the specified project from the database.
     * If no messages are found, it returns a response with status NOT_FOUND.
     * If messages are found, it logs the success and returns a response with status OK and the list of all messages associated with the project.
     *
     * @param token The token of the user trying to retrieve the messages.
     * @param project_id The id of the project whose messages are to be retrieved.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the list of all messages associated with the project.
     */
    @GET
    @Path("/project/{project_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getMessagesByProject(@HeaderParam("token") String token, @PathParam("project_id") long project_id, @Context HttpServletRequest request) {
        // Get the user by token
        User user = userBean.getUserByToken(token);
        String ip = request.getRemoteAddr();
        logger.info("Received request to get messages by project from IP: " + ip);
        if (user == null) {
            logger.error("User not found");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }
        List<Message> messages = messageBean.getMessagesChatPtoject(token, project_id);
        if (messages == null || messages.isEmpty()) {
            logger.error("IP Adress " + ip + "Messages not found for project with the id: " + project_id + " requested by user " + user.getId() + " at " + LocalDateTime.now());
            return Response.status(Response.Status.NOT_FOUND).entity("Messages not found").build();
        }
        logger.info("IP Adress " + ip + "Messages found for project with the id: " + project_id + " requested by user " + user.getId() + " at " + LocalDateTime.now());
        return Response.status(Response.Status.OK).entity(messages).build();
    }


    //Get the list of users that the user logged sent or received messages
    /**
     * This method is used to retrieve all users with whom the authenticated user has exchanged messages.
     * It first logs the IP address of the request.
     * Then it checks if the user is authenticated by checking the token.
     * If the user is not authenticated, it returns a response with status UNAUTHORIZED.
     * If the user is authenticated, it retrieves all users with whom the authenticated user has exchanged messages from the database.
     * If no users are found, it returns a response with status UNAUTHORIZED.
     * If users are found, it returns a response with status OK and the list of all users with whom the authenticated user has exchanged messages.
     *
     * @param token The token of the user trying to retrieve the users.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the list of all users with whom the authenticated user has exchanged messages.
     */
    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getMessagedUsers(@HeaderParam("token") String token, @Context HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to get messaged users from IP: " + ip);
        List<MessageInfoUser> users = messageBean.getListOfUsersWithExchangeMessages(token);

        if (users == null) {
            logger.error("User not found");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }
        return Response.status(Response.Status.OK).entity(users).build();
    }

    /**
     * This method is used to retrieve all users with whom the authenticated user has exchanged messages.
     * It first logs the IP address of the request.
     * Then it checks if the user is authenticated by checking the token.
     * If the user is not authenticated, it returns a response with status UNAUTHORIZED.
     * If the user is authenticated, it retrieves all users with whom the authenticated user has exchanged messages from the database.
     * If no users are found, it returns a response with status UNAUTHORIZED.
     * If users are found, it returns a response with status OK and the list of all users with whom the authenticated user has exchanged messages.
     *
     * @param token The token of the user trying to retrieve the users.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the list of all users with whom the authenticated user has exchanged messages.
     */
    @GET
    @Path("/{user_id}/pageCount")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getPageCountBetweenTwoUsers(@HeaderParam("token") String token,
                                                @PathParam("user_id") long user_id, @Context HttpServletRequest request) {
        // Get the user by token
        User user1 = userBean.getUserByToken(token);
        User user2 = userBean.getUserById(user_id);
        String ip = request.getRemoteAddr();
        logger.info("Received request to get page count between two users from IP: " + ip);
        if (user1 == null || user2 == null) {
            logger.error("IP Adress " + ip + "User not found");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }
        int count = messageBean.getMessageCountBetweenTwoUsers(token, user_id);
        int pageCount = (int) Math.ceil((double) count / 4); // 4 messages per page, starting from page 1
        return Response.status(Response.Status.OK).entity(pageCount).build();
    }

    /**
     * This method is used to mark a specific message as read.
     * It first logs the request to mark the message as read.
     * Then it attempts to mark the message as read in the database.
     * If the operation is successful, it logs the success and returns a response with status OK and a success message.
     * If the operation fails, it logs the error and returns a response with status BAD_REQUEST and an error message.
     * If an exception occurs during the operation, it logs the exception message and returns a response with status BAD_REQUEST and the exception message.
     *
     * @param token The token of the user trying to mark the message as read.
     * @param messageId The id of the message to be marked as read.
     * @return Response The response of the operation.
     */
    @PUT
    @Path("/{messageId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response markMessageAsRead(@HeaderParam("token") String token, @PathParam("messageId") long messageId){
        logger.info("Received request to mark message with id: " + messageId + " as read");
        try {
            boolean updated = messageBean.markMessageAsRead(token, messageId);

            if(updated) {
                logger.info("Message marked as read successfully");
                return Response.status(Response.Status.OK).entity("Message marked as read successfully").build();
            } else {
                logger.error("Error marking message as read");
                return Response.status(Response.Status.BAD_REQUEST).entity("Error marking message as read").build();
            }
        } catch (Exception e) {
            logger.error("Error marking message as read: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}

