package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.LabEntity;
import aor.paj.proj_final_aor_backend.util.enums.Workplace;
import jakarta.ejb.Stateless;

import java.util.List;

/**
 * The LabDao class provides data access operations for the LabEntity.
 * It extends the AbstractDao class, inheriting common data access operations.
 * This class is marked as Stateless, meaning it does not hold any conversational state.
 */
@Stateless
public class LabDao extends AbstractDao<LabEntity> {

    // Default serial version UID for serialization.
    private static final long serialVersionUID = 1L;


    /**
     * Default constructor.
     * Initializes the superclass with LabEntity class type
     */
    public LabDao() {
        super(LabEntity.class);
    }

    /**
     * Retrieves all LabEntity instances from the database.
     *
     * @return A list of all LabEntity instances, or null if an error occurs.
     */
    public List<LabEntity> findAllLabs() {
        try {
            List<LabEntity> labEntities = em.createNamedQuery("Lab.findAllLabs").getResultList();
            return labEntities;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Finds a LabEntity by its associated user's ID.
     *
     * @param userId The ID of the user associated with the LabEntity to find.
     * @return The found LabEntity, or null if no entity associated with the given user ID exists.
     */
    public LabEntity findLabByUserId(Long userId) {
        try {
            LabEntity labEntity = (LabEntity) em.createNamedQuery("Lab.findLabByUserId").setParameter("userId", userId).getSingleResult();
            return labEntity;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Finds a LabEntity by its name.
     *
     * @param name The name of the LabEntity to find.
     * @return The found LabEntity, or null if no entity with the given name exists.
     */
    public LabEntity findLabByName(String name) {
        try {
            Workplace workplace = Workplace.valueOf(name.toUpperCase());
            LabEntity labEntity = (LabEntity) em.createNamedQuery("Lab.findLabByName")
                    .setParameter("name", workplace)
                    .getSingleResult();

            return labEntity;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }


}
