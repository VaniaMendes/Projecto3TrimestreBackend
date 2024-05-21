package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.dto.Lab;
import aor.paj.proj_final_aor_backend.dto.Login;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.dto.UserRegistration;
import aor.paj.proj_final_aor_backend.entity.LabEntity;
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

@Path("/users")
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);

    @EJB
    UserBean userBean;


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
    @Path("/confirm")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response confirmUser(@QueryParam("tokenConfirmation") String tokenConfirmation, User user, @Context HttpServletRequest request) {

        try {
            boolean isConfirmed = userBean.confirmUser(user, tokenConfirmation);
            System.out.println(isConfirmed);

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

            User user = userBean.getUSerByToken(token);

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
    public Response recoveryPassword(@QueryParam("email") String email, @Context HttpServletRequest request) {

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


}
