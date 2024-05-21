package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.ResourceSupplierEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class ResourceSupplierDao extends AbstractDao<ResourceSupplierEntity>{

    private static final long serialVersionUID = 1L;

    public ResourceSupplierDao() {
        super(ResourceSupplierEntity.class);
    }

    public List<ResourceSupplierEntity> findAllResourcesFromSupplier(Long supplierId) {
        try {
            return em.createNamedQuery("ResourceSupplier.findAllResourcesFromSupplier", ResourceSupplierEntity.class)
                    .setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public List<ResourceSupplierEntity> findAllSuppliersForResource(Long resourceId) {
        try {
            return em.createNamedQuery("ResourceSupplier.findAllSuppliersForResource", ResourceSupplierEntity.class)
                    .setParameter("resourceId", resourceId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }
}
