package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.bean.UserProjectBean;
import aor.paj.proj_final_aor_backend.dto.Login;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.dto.UserRegistration;
import aor.paj.proj_final_aor_backend.dto.*;
import aor.paj.proj_final_aor_backend.entity.LabEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Path("/users")
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);

    @EJB
    UserBean userBean;

    @EJB
    UserProjectBean userProjectBean;

    @Context
    private HttpServletRequest request;


    /**
     * This method is used to register a new user.
     * It first tries to register the user using the provided email, password, and confirmation password.
     * If the registration is successful, it logs the success and returns a response with status CREATED and a success message.
     * If the registration is not successful, it returns a response with status BAD_REQUEST and an error message.
     * If an exception occurs during the process, it logs the error and returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param user The UserRegistration object containing the email, password, and confirmation password of the user to be registered.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing a success or error message.
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response registerUser(UserRegistration user, @Context HttpServletRequest request) {

        try {
            boolean isRegistered = userBean.registerUser(user.getEmail(), user.getPassword(), user.getConfirmPassword());

            System.out.println(isRegistered);
            if (isRegistered) {
                logger.info("IP Address: " + request.getRemoteAddr() + " - User registered: " + user.getEmail() + " at " + System.currentTimeMillis());
                return Response.status(Response.Status.CREATED).entity("User registered").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();
            }
        } catch (Exception e) {
            logger.error("IP Address: " + request.getRemoteAddr() + " - Failed to register user: " + user.getEmail() + " at " + System.currentTimeMillis() + " - " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to register user").build();
        }

    }


    /**
     * This method is used to confirm a user's account.
     * It first retrieves the user's first name for logging purposes.
     * Then it attempts to confirm the user using the provided user object, token confirmation, and lab.
     * If the confirmation is successful, it logs the success and returns a response with status OK and a success message.
     * If the confirmation is not successful, it logs the failure and returns a response with status BAD_REQUEST and an error message.
     * If an exception occurs during the process, it logs the error and returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param tokenConfirmation The token confirmation string.
     * @param lab The lab string.
     * @param user The User object containing the user's information.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing a success or error message.
     */
    @PUT
    @Path("/confirm-account")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmUser(@HeaderParam("tokenConfirmation") String tokenConfirmation, @QueryParam("lab") String lab, User user, @Context HttpServletRequest request) {

        try {
            System.out.println(user.getFirstName());

            boolean isConfirmed = userBean.confirmUser(user, tokenConfirmation, lab);

            if (isConfirmed) {
                logger.info("IP Address: " + request.getRemoteAddr() + " - User confirmed: " + user.getEmail() + " at " + LocalDateTime.now());
                return Response.status(Response.Status.OK).entity("User confirmed").build();
            } else {
                logger.warn("IP Address: " + request.getRemoteAddr() + " - Failed to confirm user: " + user.getEmail() + " at " + LocalDateTime.now());
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();
            }
        } catch (Exception e) {
            logger.error("IP Address: " + request.getRemoteAddr() + " - Failed to confirm user: " + user.getEmail() + " at " + LocalDateTime.now()+ " - " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to confirm user").build();
        }
    }

    /**
     * This method is used to log in a user.
     * It first retrieves the user by the provided email.
     * If the user is not found, it logs the error and returns a response with status BAD_REQUEST and an error message.
     * If the user is found, it attempts to log in the user using the provided email and password.
     * If the login is successful, it logs the success and returns a response with status OK and the user's token.
     * If the login is not successful, it logs the error and returns a response with status BAD_REQUEST and an error message.
     * If an exception occurs during the process, it logs the error and returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param user The Login object containing the email and password of the user trying to log in.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the user's token or an error message.
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response loginUser(Login user, @Context HttpServletRequest request) {

        try {
           User userlogged = userBean.getUserByEmail(user.getEmail());
           if(userlogged == null){
               logger.warn("IP Adress " + request.getRemoteAddr() + " - User not found: " + user.getEmail() + " at " + LocalDateTime.now());
               return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();

           }

           String token = userBean.loginUser(user.getEmail(), user.getPassword(), request);
           if(token == null){
               logger.warn("IP Adress " + request.getRemoteAddr() + " - User failed to login: " + user.getEmail() + " at " + LocalDateTime.now());
               return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();
           }
           logger.info("IP Address: " + request.getRemoteAddr() + " - User logged in with successfully: " + user.getEmail() + " at " + LocalDateTime.now());
              return Response.status(Response.Status.OK).entity(token).build();
        } catch (Exception e) {
            logger.error("IP Address: " + request.getRemoteAddr() + " - Failed to login user: " + user.getEmail() + " at " + LocalDateTime.now() + " - " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to login user").build();
        }

    }

    /**
     * This method is used to log out a user.
     * It first retrieves the user by the provided token.
     * If the user is not found, it logs the error and returns a response with status BAD_REQUEST and an error message.
     * If the user is found, it attempts to log out the user using the provided token.
     * If the logout is successful, it logs the success and returns a response with status OK and a success message.
     * If the logout is not successful, it returns a response with status BAD_REQUEST and an error message.
     * If an exception occurs during the process, it logs the error and returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param token The token of the user trying to log out.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing a success or error message.
     */
    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response logoutUser(@HeaderParam("token") String token, @Context HttpServletRequest request) {

        try {

            User user = userBean.getUserByToken(token);

            if (user == null) {

                logger.warn("IP Address: " + request.getRemoteAddr() + " - User not found: " + token + " at " + LocalDateTime.now());
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();
            }
                boolean isLoggedOut = userBean.logoutUser(token);

                if (isLoggedOut) {
                    logger.info("IP Address: " + request.getRemoteAddr() + " - User logged out: " + token + " at " + LocalDateTime.now());
                    return Response.status(Response.Status.OK).entity("User logged out").build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();
                }

        } catch (Exception e) {
            logger.error("IP Address: " + request.getRemoteAddr() + " - Failed to logout user: " + token + " at " + LocalDateTime.now() + " - " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to logout user").build();
        }

    }

    /**
     * This method is used to recover a user's password.
     * It first attempts to recover the user's password using the provided email.
     * If the password is recovered, it logs the success and returns a response with status OK and a success message.
     * If the password is not recovered, it returns a response with status BAD_REQUEST and an error message.
     * If an exception occurs during the process, it logs the error and returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param email The email of the user trying to recover the password.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing a success or error message.
     */
    @POST
    @Path("/recovery-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response recoveryPassword(@HeaderParam("email") String email, @Context HttpServletRequest request) {

        try {
            boolean isRecovered = userBean.recoveryPassword(email);

            if (isRecovered) {
                logger.info("IP Address: " + request.getRemoteAddr() + " - User request a recovered password for the email: " + email + " at " + LocalDateTime.now());
                return Response.status(Response.Status.OK).entity("User recovered password").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();
            }
        } catch (Exception e) {
            logger.error("IP Address: " + request.getRemoteAddr() + " - Failed to recover password: " + email + " at " + LocalDateTime.now() + " - " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to recover password").build();
        }

    }

    /**
     * This method is used to change a user's password.
     * It first attempts to change the user's password using the provided reset password token, password, and confirmation password.
     * If the password is changed, it logs the success and returns a response with status OK and a success message.
     * If the password is not changed, it returns a response with status BAD_REQUEST and an error message.
     * If an exception occurs during the process, it logs the error and returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param resetPassToken The reset password token string.
     * @param password The new password string.
     * @param confirmPassword The confirmation password string.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing a success or error message.
     */
    @PUT
    @Path("/change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response changePassword(@HeaderParam("resetPassToken") String resetPassToken, @QueryParam("password") String password, @QueryParam("confirmPassword") String confirmPassword, @Context HttpServletRequest request) {

        if(resetPassToken == null || password == null || confirmPassword == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();
        }
        try {
            boolean isChanged = userBean.changePassword(resetPassToken, password, confirmPassword);
            System.out.println(isChanged);

            if (isChanged) {
                logger.info("IP Address: " + request.getRemoteAddr() + " - User changed password: " + resetPassToken + " at " + LocalDateTime.now());
                return Response.status(Response.Status.OK).entity("User changed password").build();
            } else {
                logger.warn("IP Address: " + request.getRemoteAddr() + " - Failed to change password: " + resetPassToken + " at " + LocalDateTime.now());
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();
            }
        } catch (Exception e) {
            logger.error("IP Address: " + request.getRemoteAddr() + " - Failed to change password: " + resetPassToken + " at " + LocalDateTime.now() + " - " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to change password").build();
        }

    }

    /**
     * This method is used to update a user's information.
     * It first attempts to update the user's information using the provided user object and user ID.
     * If the user is updated, it logs the success and returns a response with status OK and a success message.
     * If the user is not updated, it returns a response with status BAD_REQUEST and an error message.
     * If an exception occurs during the process, it logs the error and returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param token The token of the user trying to update the information.
     * @param userId The ID of the user trying to update the information.
     * @param user The User object containing the user's information.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing a success or error message.
     */
    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateUser(@HeaderParam("token") String token, @PathParam("userId") int userId, User user, @Context HttpServletRequest request) {

        try {
            //Authentication and authorization
            User userlogged = userBean.getUserByToken(token);
            if(userlogged == null || userlogged.getId() != userId){
                logger.warn("IP Adress " + request.getRemoteAddr() + " - User not found: " + token + " at " + LocalDateTime.now());
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();

            }

            //Validation of the user and update
            boolean isUpdated = userBean.updateUser(user, userId);
            System.out.println(isUpdated);
            if(isUpdated){
                logger.info("IP Address: " + request.getRemoteAddr() + " - User updated with successfully: " + user.getEmail() + " at " + LocalDateTime.now());
                return Response.status(Response.Status.OK).entity("User updated").build();
            }else{
                logger.warn("IP Adress " + request.getRemoteAddr() + " - User failed to update: " + user.getEmail() + " at " + LocalDateTime.now());
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();
            }
        } catch (Exception e) {
            logger.error("IP Address: " + request.getRemoteAddr() + " - Failed to update user: " + user.getEmail() + " at " + LocalDateTime.now() + " - " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update user").build();
        }

    }


    /**
     * This method is used to update the visibility of a user's profile.
     * It first authenticates and authorizes the user using the provided token and user ID.
     * If the user is not found or unauthorized, it logs the error and returns a response with status BAD_REQUEST and an error message.
     * If the user is found and authorized, it attempts to update the visibility of the user's profile.
     * If the visibility is updated, it logs the success and returns a response with status OK and a success message.
     * If the visibility is not updated, it logs the error and returns a response with status BAD_REQUEST and an error message.
     * If an exception occurs during the process, it logs the error and returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param token The token of the user trying to update the visibility.
     * @param userId The ID of the user trying to update the visibility.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing a success or error message.
     */
    @PUT
    @Path("/{userId}/visibility")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateVisibility(@HeaderParam("token") String token, @PathParam("userId") int userId, @Context HttpServletRequest request) {

        try {
            // Authentication and authorization
            User userLogged = userBean.getUserByToken(token);
            if (userLogged == null || userLogged.getId() != userId) {
                logger.warn("IP Address " + request.getRemoteAddr() + " - User not found: " + token + " at " + LocalDateTime.now());
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();
            }

            // Update visibility
            boolean isUpdated = userBean.updateVisibility(userId, !userLogged.isVisibilityState());
            if (isUpdated) {
                logger.info("IP Address: " + request.getRemoteAddr() + " - Visibility updated successfully for user: " + userId + " at " + LocalDateTime.now());
                return Response.status(Response.Status.OK).entity("Visibility updated").build();
            } else {
                logger.warn("IP Address " + request.getRemoteAddr() + " - Failed to update visibility for user: " + userId + " at " + LocalDateTime.now());
                return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update visibility").build();
            }
        } catch (Exception e) {
            logger.error("IP Address: " + request.getRemoteAddr() + " - Failed to update visibility for user: " + userId + " at " + LocalDateTime.now() + " - " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update visibility").build();
        }
    }


    /**
     * This method is used to update the biography of a user.
     * It first authenticates and authorizes the user using the provided token and user ID.
     * If the user is not found or unauthorized, it logs the error and returns a response with status BAD_REQUEST and an error message.
     * If the user is found and authorized, it attempts to update the biography of the user.
     * If the biography is updated, it logs the success and returns a response with status OK and a success message.
     * If the biography is not updated, it logs the error and returns a response with status BAD_REQUEST and an error message.
     * If an exception occurs during the process, it logs the error and returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param token The token of the user trying to update the biography.
     * @param userId The ID of the user trying to update the biography.
     * @param user The User object containing the user's biography.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing a success or error message.
     */
    @PUT
    @Path("/{userId}/biography")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateBiography(@HeaderParam("token") String token, @PathParam("userId") int userId, User user, @Context HttpServletRequest request) {

        try {
            // Authentication and authorization
            User userLogged = userBean.getUserByToken(token);
            if (userLogged == null || userLogged.getId() != userId) {
                logger.warn("IP Address " + request.getRemoteAddr() + " - User not found: " + token + " at " + LocalDateTime.now());
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();
            }

            // Update biography
            boolean isUpdated = userBean.updateBiography(user.getBiography(), userId);
            if (isUpdated) {
                logger.info("IP Address: " + request.getRemoteAddr() + " - Biography updated successfully for user: " + userId + " at " + LocalDateTime.now());
                return Response.status(Response.Status.OK).entity("Biography updated").build();
            } else {
                logger.warn("IP Address " + request.getRemoteAddr() + " - Failed to update biography for user: " + userId + " at " + LocalDateTime.now());
                return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update biography").build();
            }
        } catch (Exception e) {
            logger.error("IP Address: " + request.getRemoteAddr() + " - Failed to update biography for user: " + userId + " at " + LocalDateTime.now() + " - " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update biography").build();
        }
    }

    /**
     * This method is used to retrieve a user's information.
     * It first authenticates the user using the provided token.
     * If the user is not found, it logs the error and returns a response with status BAD_REQUEST and an error message.
     * If the user is found, it logs the success and returns a response with status OK and the user's information.
     * If an exception occurs during the process, it logs the error and returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param token The token of the user trying to retrieve the information.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the user's information or an error message.
     */
    @GET
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response getUser(@HeaderParam("token") String token, @Context HttpServletRequest request) {

        try {
                   User userlogged = userBean.getUserByToken(token);
            if(userlogged == null){
                logger.warn("IP Adress " + request.getRemoteAddr() + " - User not found: " + token + " at " + LocalDateTime.now());
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();

            }

            logger.info("IP Address: " + request.getRemoteAddr() + " - User retrieved with successfully: " + userlogged.getEmail() + " at " + LocalDateTime.now());
            return Response.status(Response.Status.OK).entity(userlogged).build();
        } catch (Exception e) {
            logger.error("IP Address: " + request.getRemoteAddr() + " - Failed to retrieve user at " + LocalDateTime.now() + " - " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to retrieve user").build();
        }

    }


    /**
     * This method is used to retrieve a user's information based on the provided user ID.
     * It first checks if the provided token is valid.
     * If the token is not valid, it logs a warning and returns a response with status UNAUTHORIZED and an error message.
     * If the token is valid, it retrieves the user's profile based on the provided user ID.
     * If the user's profile is not visible, it returns a response with status UNAUTHORIZED and an error message.
     * If the user's profile is visible, it logs the request and returns a response with status OK and the user's profile.
     *
     * @param token The token of the user trying to retrieve the information.
     * @param userId The ID of the user whose information is to be retrieved.
     * @return Response The response of the operation, containing the user's information or an error message.
     */
    @GET
    @Path("/profile/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInfoByTokenAndUserId(@HeaderParam("token") String token, @PathParam("userId") Long userId) {
        String ip = request.getRemoteAddr();

        // Check if the token is valid
        User user = userBean.getUserByToken(token);
        if(user == null ){
            logger.warn("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        // Check if the user profile is visible
        User userProfile = userBean.getUserById(userId);

        if(userProfile.isVisibilityState()) {
            logger.info("Received request to get user info by user id from IP: " + ip);
        }else{
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        // Return the user profile
        return Response.status(Response.Status.OK).entity(userProfile).build();
    }



    /**
     * This method is used to count the number of projects associated with a specific user.
     * It first logs the request to count projects by user id.
     * Then it retrieves the count of projects associated with the provided user id and state.
     * The state parameter is optional and can be used to filter projects by their state.
     * It returns a response with status OK and the count of projects.
     *
     * @param userId The id of the user whose projects are to be counted.
     * @param state The state of the projects to be counted. This parameter is optional.
     * @return Response The response of the operation, containing the count of projects.
     */
    @GET
    @Path("/{id}/projects/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countProjectsByUserId(@PathParam("id") long userId, @QueryParam("state") Integer state) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to count projects by user id from IP: " + ip);

        return Response.status(Response.Status.OK).entity(userProjectBean.countProjectsByUserId(userId, state)).build();
    }

    /**
     * This method is used to retrieve users by their first name, last name or nickname.
     * It first checks if the provided token is not null.
     * If the token is null, it returns a response with status BAD_REQUEST and an error message.
     * If the token is not null, it retrieves the user associated with the token.
     * If the user is not found, it returns a response with status UNAUTHORIZED and an error message.
     * If the user is found, it retrieves a list of users whose first name starts with the provided prefix.
     * If the list of users is not empty, it returns a response with status OK and the list of users.
     * If the list of users is empty, it returns a response with status OK and an empty list.
     *
     * @param token The token of the user trying to retrieve the users.
     * @param prefix The prefix of the first name of the users to be retrieved.
     * @return Response The response of the operation, containing the list of users or an error message.
     */
    @GET
    @Path("/filterByName")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUserByName(@HeaderParam("token") String token, @QueryParam("prefix") String prefix) {
        if (token != null) {
            User user = userBean.getUserByToken(token);
            if (user != null) {
                List<User> users = userBean.getUsersByFirstName(token,prefix);
                if (users != null && !users.isEmpty()) {
                    return Response.ok(users).build();
                } else {
                    // Retorna uma array vazia se nenhum usuário for encontrado para que no frontend nao dê erro ao imprimir a tabela
                    return Response.ok(Collections.emptyList()).build();
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing token").build();
        }
    }

    /**
     * This method is used to retrieve a user's information based on the provided user ID.
     * It first authenticates the user using the provided token.
     * If the user is not found or unauthorized, it logs the error and returns a response with status UNAUTHORIZED and an error message.
     * If the user is found and authorized, it retrieves the user's information based on the provided user ID.
     * If the user's information is found, it returns a response with status OK and the user's information.
     * If the user's information is not found, it returns a response with status NOT_FOUND and an error message.
     *
     * @param token The token of the user trying to retrieve the information.
     * @param userId The ID of the user whose information is to be retrieved.
     * @return Response The response of the operation, containing the user's information or an error message.
     */
    @GET
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@HeaderParam("token") String token, @PathParam("userId") long userId) {
        User user = userBean.getUserByToken(token);
        if (user != null) {
            User userById = userBean.getUserById(userId);
            if (userById != null) {
                return Response.ok(userById).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }


    /**
     * This method is used to retrieve the status of a specific project associated with a specific user.
     * It first checks if the provided token is valid.
     * If the token is not valid, it logs a warning and returns a response with status UNAUTHORIZED and an error message.
     * If the token is valid, it retrieves the status of the project associated with the provided user ID and project ID.
     * It logs the request and returns a response with status OK and the project status.
     *
     * @param token The token of the user trying to retrieve the project status.
     * @param userId The ID of the user associated with the project.
     * @param projectId The ID of the project whose status is to be retrieved.
     * @return Response The response of the operation, containing the project status or an error message.
     */
    @GET
    @Path("/profile/{userId}/project/{projectId}/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProjectStatus(@HeaderParam("token") String token,
                                         @PathParam("userId") Long userId,
                                         @PathParam("projectId") Long projectId) {
        String ip = request.getRemoteAddr();
        User user = userBean.getUserByToken(token);
        if(user == null ){
            logger.warn("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        logger.info("Received request to get user project status by user id and project id from IP: " + ip);

        return Response.status(Response.Status.OK).entity(userProjectBean.getUserProject(userId, projectId)).build();
    }
}

