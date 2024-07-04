package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.InterestEntity;
import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import jakarta.ejb.Stateless;

import java.util.ArrayList;
import java.util.List;

/**
 * The InterestDao class provides data access operations for the InterestEntity.
 * It extends the AbstractDao class, inheriting common data access operations.
 * This class is marked as Stateless, meaning it does not hold any conversational state.
 */
@Stateless
public class InterestDao extends AbstractDao<InterestEntity> {

    // Default serial version UID for serialization.
    private static final long serialVersionUID = 1L;


    /**
     * Default constructor.
     * Initializes the superclass with InterestEntity class type
     */
    public InterestDao() {
        super(InterestEntity.class);
    }


    /**
     * Creates a new InterestEntity in the database.
     *
     * @param interest The InterestEntity to create.
     */
    public void createInterest(InterestEntity interest) {
        em.persist(interest);
    }


    /**
     * Finds an InterestEntity by its name.
     *
     * @param name The name of the InterestEntity to find.
     * @return The found InterestEntity, or null if no entity with the given name exists.
     */
    public InterestEntity findInterestByName(String name) {
        try {
            return (InterestEntity) em.createNamedQuery("InterestEntity.findInterestByName").setParameter("name", name).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Retrieves all InterestEntity instances from the database.
     *
     * @return A list of all InterestEntity instances, or null if an error occurs.
     */
    public List<InterestEntity> getAllInterests() {
        try {
            return em.createNamedQuery("InterestEntity.findAllInterests", InterestEntity.class).getResultList();
        } catch (Exception e) {
            return null;
        }
    }


}
