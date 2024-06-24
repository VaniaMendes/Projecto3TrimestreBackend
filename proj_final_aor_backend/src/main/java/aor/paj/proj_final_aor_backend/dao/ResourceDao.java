package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.ResourceEntity;
import aor.paj.proj_final_aor_backend.util.enums.ResourceType;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

/**
 * Stateless session bean that provides CRUD operations for ResourceEntity.
 */
@Stateless
public class ResourceDao extends AbstractDao<ResourceEntity>{

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public ResourceDao() {
        super(ResourceEntity.class);
    }

    /**
     * Retrieves all resources from the database.
     * @return a list of all resources, or an empty list if no resources are found.
     */
    public List<ResourceEntity> findAllResources() {
        try {
            return em.createNamedQuery("Resource.findAllResources", ResourceEntity.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all resources from the database, ordered in descending order.
     * @return a list of all resources, or an empty list if no resources are found.
     */
    public List<ResourceEntity> findAllResourcesOrderedDESC() {
        try {
            return em.createNamedQuery("Resource.findAllResourcesOrderedDESC", ResourceEntity.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a resource by its ID.
     * @param id the ID of the resource.
     * @return the resource with the given ID, or null if no such resource is found.
     */
    public ResourceEntity findResourceById(Long id) {
        try {
            return (ResourceEntity) em.createNamedQuery("Resource.findResourceById").setParameter("id", id)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Retrieves a resource by its name.
     * @param name the name of the resource.
     * @return the resource with the given name, or null if no such resource is found.
     */
    public ResourceEntity findResourceByName(String name) {
        try {
            return (ResourceEntity) em.createNamedQuery("Resource.findResourceByName").setParameter("name", name)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Retrieves resources by their type.
     * @param type the type of the resources.
     * @return a list of resources with the given type, or an empty list if no such resources are found.
     */
    public List<ResourceEntity> findResourcesByType(ResourceType type) {
        try {
            return em.createNamedQuery("Resource.findResourcesByType", ResourceEntity.class).setParameter("type", type)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their brand.
     * @param brand the brand of the resources.
     * @return a list of resources with the given brand, or an empty list if no such resources are found.
     */
    public List<ResourceEntity> findResourcesByBrand(String brand) {
        try {
            return em.createNamedQuery("Resource.findResourcesByBrand", ResourceEntity.class).setParameter("brand", brand)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their supplier.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given supplier, or an empty list if no such resources are found.
     */
    public List<ResourceEntity> findResourcesBySupplier(long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesBySupplier", ResourceEntity.class).setParameter("supplierId", supplierId)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a resource by its source ID.
     * @param sourceId the source ID of the resource.
     * @return the resource with the given source ID, or null if no such resource is found.
     */
    public List<ResourceEntity> findResourcesBySourceId(String sourceId) {
        try {
            return em.createNamedQuery("Resource.findResourcesBySourceId", ResourceEntity.class).setParameter("sourceId", sourceId)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type and brand.
     * @param type the type of the resources.
     * @param brand the brand of the resources.
     * @return a list of resources with the given type and brand, or an empty list if no such resources are found.
     */
    public List<ResourceEntity> findResourcesByTypeAndBrand(ResourceType type, String brand) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndBrand", ResourceEntity.class).setParameter("type", type)
                    .setParameter("brand", brand).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type and supplier.
     * @param type the type of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given type and supplier, or an empty list if no such resources are found.
     */
    public List<ResourceEntity> findResourcesByTypeAndSupplier(ResourceType type, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndSupplier", ResourceEntity.class).setParameter("type", type)
                    .setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their brand and supplier.
     * @param brand the brand of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given brand and supplier, or an empty list if no such resources are found.
     */
    public List<ResourceEntity> findResourcesByBrandAndSupplier(String brand, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByBrandAndSupplier", ResourceEntity.class).setParameter("brand", brand)
                    .setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type, brand, and supplier.
     * @param type the type of the resources.
     * @param brand the brand of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given type, brand, and supplier, or an empty list if no such resources are found.
     */
    public List<ResourceEntity> findResourcesByTypeAndBrandAndSupplier(ResourceType type, String brand, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndBrandAndSupplier", ResourceEntity.class).setParameter("type", type)
                    .setParameter("brand", brand).setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public List<String> findAllBrands() {
        try {
            return em.createNamedQuery("Resource.findAllBrands").getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

}
