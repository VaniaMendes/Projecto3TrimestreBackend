package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.InterestEntity;
import aor.paj.proj_final_aor_backend.entity.UserInterestEntity;
import jakarta.ejb.Stateless;

import java.util.List;

/**
 * The UserInterestDao class provides data access operations for the UserInterestEntity.
 * It extends the AbstractDao class, inheriting common data access operations.
 * This class is marked as Stateless, meaning it does not hold any conversational state.
 */
@Stateless
public class UserInterestDao extends AbstractDao<UserInterestEntity>{

    private static final long serialVersionUID = 1L;


    /**
     * Default constructor.
     * Initializes the superclass with UserInterestEntity class type
     */
    public UserInterestDao() {
        super(UserInterestEntity.class);
    }

    /**
     * Creates a new UserInterestEntity in the database.
     * @param userInterest The UserInterestEntity to create.
     */
    public void createUserInterest(UserInterestEntity userInterest) {
        persist(userInterest);
    }

    /**
     * Updates an existing UserInterestEntity in the database.
     * If the entity does not exist, it will not be created.
     * @param userInterest The UserInterestEntity to update.
     */
    public void updateUserInterest(UserInterestEntity userInterest) {
        merge(userInterest);
    }

    /**
     * Finds a UserInterestEntity by its associated user's ID and interest's ID.
     * @param userId The ID of the user associated with the UserInterestEntity to find.
     * @param interestId The ID of the interest associated with the UserInterestEntity to find.
     * @return The found UserInterestEntity, or null if no entity associated with the given user ID and interest ID exists.
     */
    public UserInterestEntity findUserInterestByUserAndInterest(long userId, long interestId) {
        try {
            return (UserInterestEntity) em.createNamedQuery("UserInterestEntity.findUserInterest").setParameter("user", userId).setParameter("interest", interestId).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves all InterestEntity instances associated with a user by their ID.
     * @param userId The ID of the user.
     * @return A list of InterestEntity instances associated with the user, or null if an error occurs.
     */
    public List<InterestEntity> getAllInterestsByUserId(long userId) {
        try {
            return em.createNamedQuery("UserInterestEntity.getAllInterestsByUserId").setParameter("user", userId).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
