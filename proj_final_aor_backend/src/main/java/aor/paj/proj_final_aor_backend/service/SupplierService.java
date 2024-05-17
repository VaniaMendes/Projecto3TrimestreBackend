package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.SupplierBean;
import aor.paj.proj_final_aor_backend.dto.Supplier;
import aor.paj.proj_final_aor_backend.entity.SupplierEntity;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerSupplier(Supplier supplier) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to register supplier from IP: " + ip);

        // Create the supplier
        SupplierEntity createdSupplier = supplierBean.createSupplier(supplier);

        // Check if the supplier was created successfully
        if (createdSupplier != null) {
            logger.info("Supplier registered successfully: " + supplier.getName());
            return Response.status(Response.Status.CREATED).entity("Supplier registered successfully").build();
        } else {
            logger.error("Failed to register supplier: " + supplier.getName());
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to register supplier").build();
        }
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSuppliers() {
        String ip = request.getRemoteAddr();
        logger.info("Received request to retrieve all suppliers from IP: " + ip);

        // Retrieve all suppliers
        return Response.ok(supplierBean.getAllSuppliers()).build();
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSupplier(@QueryParam("id") Long id, @QueryParam("name") String name, @QueryParam("contact") String contact) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to update supplier from IP: " + ip);

        // Update the supplier
        SupplierEntity updatedSupplier = supplierBean.updateSupplier(id, name, contact);

        // Check if the supplier was updated successfully
        if (updatedSupplier != null) {
            logger.info("Supplier updated successfully: " + updatedSupplier.getName());
            return Response.status(Response.Status.OK).entity("Supplier updated successfully").build();
        } else {
            logger.error("Failed to update supplier: " + name);
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update supplier").build();
        }
    }


}
