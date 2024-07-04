package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import jakarta.ejb.Stateless;

import java.util.List;


/**
 * The SkillDao class provides data access operations for the SkillEntity.
 * It extends the AbstractDao class, inheriting common data access operations.
 * This class is marked as Stateless, meaning it does not hold any conversational state.
 */
@Stateless
public class SkillDao extends AbstractDao<SkillEntity> {
    private static final long serialVersionUID = 1L;


    /**
     * Default constructor.
     * Initializes the superclass with SkillEntity class type
     */
    public SkillDao() {
        super(SkillEntity.class);
    }

    /**
     * Finds a SkillEntity by its ID.
     *
     * @param id The ID of the SkillEntity to find.
     * @return The found SkillEntity, or null if no entity with the given ID exists.
     */
    public SkillEntity findSkillById(Long id) {
        try {
            return em.createNamedQuery("Skill.findSkillById", SkillEntity.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Finds a SkillEntity by its name.
     *
     * @param name The name of the SkillEntity to find.
     * @return The found SkillEntity, or null if no entity with the given name exists.
     */
    public SkillEntity findSkillByName(String name) {
        try {
            return em.createNamedQuery("Skill.findSkillByName", SkillEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Creates a new SkillEntity in the database.
     *
     * @param skill The SkillEntity to create.
     */
    public void createSkill(SkillEntity skill) {
        em.persist(skill);
    }


    /**
     * Retrieves all SkillEntity instances from the database.
     *
     * @return A list of all SkillEntity instances, or null if an error occurs.
     */
    public List<SkillEntity> findAllSkills() {
        try {
            return em.createNamedQuery("Skill.findAllSkills", SkillEntity.class)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Finds SkillEntity instances by the associated user's ID.
     *
     * @param userId The ID of the user associated with the SkillEntity instances to find.
     * @return A list of SkillEntity instances associated with the user, or null if an error occurs.
     */
    public List<SkillEntity> findSkillsByUserId(long userId) {
        try {
            return em.createNamedQuery("Skill.findSkillsByUserId", SkillEntity.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }


}
