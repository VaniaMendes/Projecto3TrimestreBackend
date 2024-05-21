package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.SkillBean;
import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.dto.Skill;
import aor.paj.proj_final_aor_backend.dto.User;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/skills")
public class SkillService {

    private static final Logger logger = LogManager.getLogger(SkillService.class);

    @EJB
    private SkillBean skillBean;
    @EJB
    private UserBean userBean;
    @Context
    private HttpServletRequest request;


    @POST
    @Path("/new-skill")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSkill(@HeaderParam("token") String token, Skill skill, @Context HttpServletRequest request) {
        User user = userBean.getUSerByToken(token);

        if(user == null) {
            logger.error("User not found");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }
        // Create the skill
        boolean created = skillBean.createNewSkill(skill);

        // Check if the skill was created successfully
        if (created) {
            logger.info("IPAdress: " + request.getRemoteAddr() + " Skill created successfully: " + skill.getName() + "by user with ");
            return Response.status(Response.Status.CREATED).entity("Skill created successfully").build();
        } else {
            logger.error("Failed to create skill: " + skill.getName());
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create skill").build();
        }
    }



}
