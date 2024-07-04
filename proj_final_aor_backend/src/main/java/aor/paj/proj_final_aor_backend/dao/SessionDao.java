package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.SessionEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * The SessionDao class provides data access operations for the SessionEntity.
 * It extends the AbstractDao class, inheriting common data access operations.
 * This class is marked as Stateless, meaning it does not hold any conversational state.
 */
@Stateless
public class SessionDao extends AbstractDao<SessionEntity> {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     * Initializes the superclass with SessionEntity class type
     */
    public SessionDao() {
        super(SessionEntity.class);
    }

    /**
     * Finds a user ID by its session token.
     *
     * @param token The session token of the user ID to find.
     * @return The found user ID, or -1 if no user with the given session token exists.
     */
    public long findUserIDbyToken(String token) {
        try {
            return (long) em.createNamedQuery("Session.findUserIDbyToken").setParameter("token", token).getSingleResult();
        } catch (Exception e) {
            return -1;
        }
    }


    /**
     * Finds a SessionEntity by its token.
     *
     * @param token The token of the SessionEntity to find.
     * @return The found SessionEntity, or null if no entity with the given token exists.
     */
    public SessionEntity findSessionByToken(String token) {
        try {
            return (SessionEntity) em.createNamedQuery("Session.findSessionByToken").setParameter("token", token).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Finds a UserEntity by its session token.
     *
     * @param token The session token of the UserEntity to find.
     * @return The found UserEntity, or null if no entity with the given session token exists.
     */
    @Transactional
    public UserEntity findUserByToken(String token) {
        try {
            return em.createNamedQuery("Session.findUserByToken", UserEntity.class)
                    .setParameter("token", token)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.out.println("Error fetching user by token: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Updates an existing SessionEntity in the database.
     * If the entity does not exist, it will not be created.
     *
     * @param session The SessionEntity to update.
     */
    public void update(SessionEntity session) {
        em.merge(session);
    }


    /**
     * Creates a new SessionEntity in the database.
     *
     * @param session The SessionEntity to create.
     */
    public void create(SessionEntity session) {
        em.persist(session);
    }
}
