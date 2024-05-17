package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.SupplierBean;
import aor.paj.proj_final_aor_backend.dto.Supplier;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/suppliers")
public class SupplierService {

    private static final Logger logger = LogManager.getLogger(SupplierService.class);

    @EJB
    SupplierBean supplierBean;

    @Context
    private HttpServletRequest request;

    @POST
    @Path("/register")
    public Response registerSupplier(Supplier supplier){
        String ip = request.getRemoteAddr();
        logger.info("Received request to register supplier from IP: " + ip);

        supplierBean.createSupplier(supplier);

        return Response.status(200).entity("Supplier registered successfully").build();
    }
}
