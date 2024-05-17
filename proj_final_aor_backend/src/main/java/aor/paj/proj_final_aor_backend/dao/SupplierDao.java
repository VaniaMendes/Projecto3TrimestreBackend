package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.SupplierEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

/**
 * Stateless session bean to handle database operations for Supplier entities.
 */
@Stateless
public class SupplierDao extends AbstractDao<SupplierEntity>{

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor initializing the superclass with SupplierEntity class type.
     */
    public SupplierDao() {
        super(SupplierEntity.class);
    }

    /**
     * Retrieves all suppliers from the database.
     *
     * @return A list of SupplierEntity objects. Returns an empty list if no suppliers are found.
     */
    public List<SupplierEntity> findAllSuppliers() {
        try {
            return em.createNamedQuery("Supplier.findAllSuppliers", SupplierEntity.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Finds a supplier by its ID.
     *
     * @param id The ID of the supplier to find.
     * @return The found SupplierEntity or null if the supplier does not exist.
     */
    public SupplierEntity findSupplierById(Long id) {
        try {
            return (SupplierEntity) em.createNamedQuery("Supplier.findSupplierById").setParameter("id", id)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Finds a supplier by its name.
     *
     * @param name The name of the supplier to find.
     * @return The found SupplierEntity or null if the supplier does not exist.
     */
    public SupplierEntity findSupplierByName(String name) {
        try {
            return (SupplierEntity) em.createNamedQuery("Supplier.findSupplierByName").setParameter("name", name)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }
}
