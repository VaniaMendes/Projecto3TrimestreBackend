package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import aor.paj.proj_final_aor_backend.entity.UserSkillEntity;
import jakarta.ejb.Stateless;

import java.util.List;

/**
 * The UserSkillDao class provides data access operations for the UserSkillEntity.
 * It extends the AbstractDao class, inheriting common data access operations.
 * This class is marked as Stateless, meaning it does not hold any conversational state.
 */
@Stateless
public class UserSkillDao extends AbstractDao<UserSkillEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     * Initializes the superclass with UserSkillEntity class type
     */
    public UserSkillDao() {
        super(UserSkillEntity.class);
    }


    /**
     * Retrieves all SkillEntity instances associated with a user by their ID.
     *
     * @param userId The ID of the user.
     * @return A list of SkillEntity instances associated with the user, or null if an error occurs.
     */
    public List<SkillEntity> findAllSkillsForUser(long userId) {
        try {
            return em.createNamedQuery("UserSkillEntity.findAllSkillsForUser").setParameter("id", userId).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds a UserSkillEntity by its associated user's ID and skill's ID.
     *
     * @param userId  The ID of the user associated with the UserSkillEntity to find.
     * @param skillId The ID of the skill associated with the UserSkillEntity to find.
     * @return The found UserSkillEntity, or null if no entity associated with the given user ID and skill ID exists.
     */
    public UserSkillEntity findUserSkillByUserAndSkill(long userId, long skillId) {
        try {
            return (UserSkillEntity) em.createNamedQuery("UserSkillEntity.findUserSkill").setParameter("user", userId).setParameter("skill", skillId).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Creates a new UserSkillEntity in the database.
     *
     * @param userSkill The UserSkillEntity to create.
     */
    public void createUserSkill(UserSkillEntity userSkill) {
        persist(userSkill);
    }


    /**
     * Updates an existing UserSkillEntity in the database.
     * If the entity does not exist, it will not be created.
     *
     * @param userSkill The UserSkillEntity to update.
     */
    public void updateUserSkill(UserSkillEntity userSkill) {
        merge(userSkill);
    }

}
