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

           String token = userBean.loginUser(user.getEmail(), user.getPassword());
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


    @GET
    @Path("/{id}/projects/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countProjectsByUserId(@PathParam("id") long userId, @QueryParam("state") Integer state) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to count projects by user id from IP: " + ip);

        return Response.status(Response.Status.OK).entity(userProjectBean.countProjectsByUserId(userId, state)).build();
    }

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

