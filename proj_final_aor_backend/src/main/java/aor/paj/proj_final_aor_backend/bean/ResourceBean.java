package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.ResourceDao;
import aor.paj.proj_final_aor_backend.dao.ResourceSupplierDao;
import aor.paj.proj_final_aor_backend.dto.Resource;
import aor.paj.proj_final_aor_backend.dto.Supplier;
import aor.paj.proj_final_aor_backend.entity.ResourceEntity;
import aor.paj.proj_final_aor_backend.entity.ResourceSupplierEntity;
import aor.paj.proj_final_aor_backend.entity.SupplierEntity;
import aor.paj.proj_final_aor_backend.util.enums.ResourceType;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The ResourceBean class provides methods for creating and managing resources.
 * It is a stateless EJB and implements Serializable for distributed computing.
 */
@Stateless
public class ResourceBean implements Serializable {

    // Logger for this class
    private static Logger logger = LogManager.getLogger(ResourceBean.class);

    // Injected ResourceDao EJB
    @EJB
    private ResourceDao resourceDao;

    @EJB
    private ResourceSupplierDao resourceSupplierDao;

    // Injected SupplierBean EJB
    @EJB
    private SupplierBean supplierBean;

    /**
     * Default constructor.
     */
    public ResourceBean() {
    }

    /**
     * Constructor that allows injection of a ResourceDao.
     *
     * @param resourceDao The ResourceDao to be used by this bean.
     */
    public ResourceBean (ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }

    public ResourceBean(ResourceDao resourceDao, SupplierBean supplierBean, Logger logger) {
        this.resourceDao = resourceDao;
        this.supplierBean = supplierBean;
        this.logger = logger;
    }

    /**
     * This method is used to create a new resource.
     * It first validates the resource, then converts it to a ResourceEntity and adds any suppliers.
     * If the resource is valid and all suppliers exist, it is persisted in the database.
     *
     * @param resource The Resource object to be created.
     * @return The created ResourceEntity, or null if the resource is invalid or a supplier does not exist.
     */
    public ResourceEntity createResource(Resource resource) {

        // Validate the resource
        if (isInvalidResource(resource)) {
            return null;
        }

        // Convert the resource to a ResourceEntity
        ResourceEntity resourceEntity = convertToEntity(resource);

        // Add any suppliers to the ResourceEntity
        for (Supplier supplier : resource.getSuppliers()) {
            SupplierEntity supplierEntity = supplierBean.findSupplierByName(supplier.getName());
            if (supplierEntity != null) {
                persistResourceSupplierConnection(resourceEntity, supplierEntity);
            } else {
                logger.error("Supplier does not exist: " + supplier.getName());
                return null;
            }
        }

        // Persist the ResourceEntity in the database
        resourceDao.persist(resourceEntity);
        return resourceEntity;
    }


    /**
     * Fetches all resources from the database.
     *
     * @return A list of all resources, converted to DTOs.
     */
    public List<Resource> getAllResources() {
        return resourceDao.findAllResources().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Fetches resources from the database that match the given type, brand, and supplier name.
     *
     * @param type The type of the resources to fetch.
     * @param brand The brand of the resources to fetch.
     * @param supplierName The name of the supplier of the resources to fetch.
     * @return A list of matching resources, converted to DTOs.
     */
    public List<Resource> findResourcesByTypeAndBrandAndSupplier(ResourceType type, String brand, String supplierName) {
        return resourceDao.findResourcesByTypeAndBrandAndSupplier(type, brand, supplierName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Fetches resources from the database that match the given type and brand.
     *
     * @param type The type of the resources to fetch.
     * @param brand The brand of the resources to fetch.
     * @return A list of matching resources, converted to DTOs.
     */
    public List<Resource> findResourcesByTypeAndBrand(ResourceType type, String brand) {
        return resourceDao.findResourcesByTypeAndBrand(type, brand).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Fetches resources from the database that match the given type and supplier name.
     *
     * @param type The type of the resources to fetch.
     * @param supplierName The name of the supplier of the resources to fetch.
     * @return A list of matching resources, converted to DTOs.
     */
    public List<Resource> findResourcesByTypeAndSupplier(ResourceType type, String supplierName) {
        return resourceDao.findResourcesByTypeAndSupplier(type, supplierName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Fetches resources from the database that match the given brand and supplier name.
     *
     * @param brand The brand of the resources to fetch.
     * @param supplierName The name of the supplier of the resources to fetch.
     * @return A list of matching resources, converted to DTOs.
     */
    public List<Resource> findResourcesByBrandAndSupplier(String brand, String supplierName) {
        return resourceDao.findResourcesByBrandAndSupplier(brand, supplierName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Fetches resources from the database that match the given type.
     *
     * @param type The type of the resources to fetch.
     * @return A list of matching resources, converted to DTOs.
     */
    public List<Resource> findResourcesByType(ResourceType type) {
        return resourceDao.findResourcesByType(type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Fetches resources from the database that match the given brand.
     *
     * @param brand The brand of the resources to fetch.
     * @return A list of matching resources, converted to DTOs.
     */
    public List<Resource> findResourcesByBrand(String brand) {
        return resourceDao.findResourcesByBrand(brand).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Fetches resources from the database that match the given supplier name.
     *
     * @param supplierName The name of the supplier of the resources to fetch.
     * @return A list of matching resources, converted to DTOs.
     */
    public List<Resource> findResourcesBySupplier(String supplierName) {
        return resourceDao.findResourcesBySupplier(supplierName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Fetches resources from the database that match the given source ID.
     *
     * @param sourceId The source ID of the resources to fetch.
     * @return A list of matching resources, converted to DTOs.
     */
    public List<Resource> findResourceBySourceId(String sourceId) {
        return resourceDao.findResourcesBySourceId(sourceId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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

    /**
     * Checks if a resource with the given name exists in the database.
     *
     * @param name The name of the resource.
     * @return true if the resource exists, false otherwise.
     */
    public boolean exists(String name) {
        return resourceDao.findResourceByName(name) != null;
    }


    /**
     * Updates the properties of a resource in the database.
     * The method first finds the resource in the database using the name from the provided parameters.
     * If the resource is found, it updates its properties with the non-null values from the parameters.
     * Finally, it persists the updated resource back to the database.
     *
     * @param name The name of the resource to be updated.
     * @param description The new description of the resource. If null, the description is not updated.
     * @param brand The new brand of the resource. If null, the brand is not updated.
     * @param observation The new observation of the resource. If null, the observation is not updated.
     * @param photo The new photo of the resource. If null, the photo is not updated.
     * @param sourceId The new sourceId of the resource. If null, the sourceId is not updated.
     * @return The updated ResourceEntity, or null if the resource does not exist.
     */
    public ResourceEntity updateResource(String name, String description, String brand, String observation, String photo, String sourceId) {
        ResourceEntity resourceEntity = resourceDao.findResourceByName(name);
        if (resourceEntity == null) {
            logger.error("Resource with name '" + name + "' does not exist");
            return null;
        }

        if (description != null) {
            resourceEntity.setDescription(description);
        }

        if (brand != null) {
            resourceEntity.setBrand(brand);
        }

        if (observation != null) {
            resourceEntity.setObservation(observation);
        }

        if (photo != null) {
            resourceEntity.setPhoto(photo);
        }

        if (sourceId != null) {
            resourceEntity.setSourceId(sourceId);
        }

        resourceEntity.setUpdatedAt(LocalDateTime.now());
        resourceDao.merge(resourceEntity);
        return resourceEntity;
    }

    /**
     * Adds a supplier to a resource in the database.
     * The method first finds the resource and the supplier in the database using the name from the provided Resource object and the supplier name.
     * If both the resource and the supplier are found, it adds the supplier to the resource's suppliers set.
     * Finally, it persists the updated resource back to the database.
     *
     * @param resourceName The name of the Resource object to which the supplier should be added.
     * @param supplierName The name of the supplier to be added.
     * @return The updated ResourceEntity, or null if the resource or the supplier does not exist.
     */
    public ResourceEntity addSupplierToResource(String resourceName, String supplierName) {
        ResourceEntity resourceEntity = resourceDao.findResourceByName(resourceName);
        if (resourceEntity == null) {
            logger.error("Resource with name '" + resourceName + "' does not exist");
            return null;
        }

        SupplierEntity supplierEntity = supplierBean.findSupplierByName(supplierName);
        if (supplierEntity == null) {
            logger.error("Supplier does not exist: " + supplierName);
            return null;
        }

        persistResourceSupplierConnection(resourceEntity, supplierEntity);

        resourceEntity.setUpdatedAt(LocalDateTime.now());
        resourceDao.merge(resourceEntity);
        return resourceEntity;
    }

    /**
     * This method is used to create a new ResourceSupplierEntity object and persist it in the database.
     * It sets the provided ResourceEntity and SupplierEntity objects to the new ResourceSupplierEntity object,
     * sets the active status to true, and then persists the new ResourceSupplierEntity object in the database.
     *
     * @param resourceEntity The ResourceEntity object to be set in the new ResourceSupplierEntity object.
     * @param supplierEntity The SupplierEntity object to be set in the new ResourceSupplierEntity object.
     */
    private void persistResourceSupplierConnection(ResourceEntity resourceEntity, SupplierEntity supplierEntity) {
        ResourceSupplierEntity resourceSupplier = new ResourceSupplierEntity();
        resourceSupplier.setResource(resourceEntity);
        resourceSupplier.setSupplier(supplierEntity);
        resourceSupplier.setActiveStatus(true);

        resourceSupplierDao.persist(resourceSupplier);
    }


    /**
     * This method is used to convert a Resource object into a ResourceEntity object.
     * It copies the properties of the Resource object into a new ResourceEntity object.
     *
     * @param resource The Resource object to be converted.
     * @return The newly created ResourceEntity object.
     */
    private ResourceEntity convertToEntity(Resource resource) {
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setName(resource.getName());
        resourceEntity.setDescription(resource.getDescription());
        resourceEntity.setType(resource.getType());
        resourceEntity.setBrand(resource.getBrand());
        resourceEntity.setObservation(resource.getObservation());
        resourceEntity.setPhoto(resource.getPhoto());
        resourceEntity.setSourceId(resource.getSourceId());
        return resourceEntity;
    }

    /**
     * This method is used to convert a ResourceEntity object into a Resource object.
     * It copies the properties of the ResourceEntity object into a new Resource object.
     * If the ResourceEntity object has any suppliers, it also adds them to the Resource object.
     *
     * @param resourceEntity The ResourceEntity object to be converted.
     * @return The newly created Resource object.
     */
    private Resource convertToDTO(ResourceEntity resourceEntity) {
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
                    .map(ResourceSupplierEntity::getSupplier)
                    .map(supplierBean::convertToDTO)
                    .collect(Collectors.toList()));
        }
        return resource;
    }

}
