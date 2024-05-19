package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.ResourceDao;
import aor.paj.proj_final_aor_backend.dto.Resource;
import aor.paj.proj_final_aor_backend.dto.Supplier;
import aor.paj.proj_final_aor_backend.entity.ResourceEntity;
import aor.paj.proj_final_aor_backend.entity.SupplierEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class ResourceBean implements Serializable {

    private static final Logger logger = LogManager.getLogger(ResourceBean.class);

    @EJB
    private ResourceDao resourceDao;
    @EJB
    private SupplierBean supplierBean;

    public ResourceBean() {
    }

    public ResourceBean (ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }

    public ResourceEntity createResource(Resource resource) {

        if (isInvalidResource(resource)) {
            return null;
        }

        ResourceEntity resourceEntity = convertToEntity(resource);

        for (Supplier supplier : resource.getSuppliers()) {
            SupplierEntity supplierEntity = supplierBean.findSupplierByName(supplier.getName());
            if (supplierEntity != null) {
                resourceEntity.addSupplier(supplierEntity);
            } else {
                logger.error("Supplier does not exist: " + supplier.getName());
                return null;
            }
        }


        resourceDao.persist(resourceEntity);
        return resourceEntity;
    }

    /**
     * This method is used to validate a Resource object.
     * It checks if any of the required fields in the Resource object are null or invalid.
     *
     * @param resource The Resource object to be validated.
     * @return true if the Resource object is invalid, false otherwise.
     */
    private boolean isInvalidResource(Resource resource) {
        if (resource == null) {
            return true;
        }
        if (resource.getName() == null || resource.getName().isEmpty()) {
            return true;
        }
        if (resource.getDescription() == null || resource.getDescription().isEmpty()) {
            return true;
        }
        if (resource.getType() == null) {
            return true;
        }
        if (resource.getBrand() == null || resource.getBrand().isEmpty()) {
            return true;
        }
        if (resource.getSourceId() == null || resource.getSourceId().isEmpty()) {
            return true;
        }
        if (resource.getSuppliers() == null || resource.getSuppliers().isEmpty()) {
            return true;
        }
        for (Supplier supplier : resource.getSuppliers()) {
            if (supplier.getName() == null || supplier.getName().isEmpty()) {
                return true;
            }
        }

        return false;
    }


    public ResourceEntity convertToEntity(Resource resource) {
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setName(resource.getName());
        resourceEntity.setDescription(resource.getDescription());
        resourceEntity.setType(resource.getType());
        resourceEntity.setBrand(resource.getBrand());
        resourceEntity.setObservation(resource.getObservation());
        resourceEntity.setPhoto(resource.getPhoto());
        resourceEntity.setSourceId(resource.getSourceId());
        if (resource.getSuppliers() != null){
            for (Supplier supplier : resource.getSuppliers()) {
                SupplierEntity supplierEntity = supplierBean.findSupplierByName(supplier.getName());
                if (supplierEntity != null) {
                    resourceEntity.addSupplier(supplierEntity);
                }
            }
        }
        return resourceEntity;
    }


    public Resource convertToDTO(ResourceEntity resourceEntity) {
        Resource resource = new Resource();
        resource.setId(resourceEntity.getId());
        resource.setName(resourceEntity.getName());
        resource.setDescription(resourceEntity.getDescription());
        resource.setType(resourceEntity.getType());
        resource.setBrand(resourceEntity.getBrand());
        resource.setObservation(resourceEntity.getObservation());
        resource.setPhoto(resourceEntity.getPhoto());
        resource.setCreatedAt(resourceEntity.getCreatedAt());
        resource.setUpdatedAt(resourceEntity.getUpdatedAt());
        resource.setSourceId(resourceEntity.getSourceId());
        if (!resourceEntity.getSuppliers().isEmpty()){
            resource.setSuppliers(resourceEntity.getSuppliers().stream()
                    .map(supplierBean::convertToDTO)
                    .collect(Collectors.toList()));
        }
        return resource;
    }

}
