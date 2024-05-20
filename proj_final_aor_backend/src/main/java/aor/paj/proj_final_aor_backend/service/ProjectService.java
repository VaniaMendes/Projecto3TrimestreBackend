package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.ProjectBean;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/projects")
public class ProjectService {

    private static final Logger logger = LogManager.getLogger(ProjectService.class);

    @EJB
    private ProjectBean projectBean;

    @Context
    private HttpServletRequest request;



}
