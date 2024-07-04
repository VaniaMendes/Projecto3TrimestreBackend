package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.AuthenticationEntity;
import aor.paj.proj_final_aor_backend.entity.MessageEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import jakarta.ejb.Stateless;

import java.util.List;

/**
 * The AuthenticationDao class provides data access operations for the AuthenticationEntity.
 * It extends the AbstractDao class, inheriting common data access operations.
 * This class is marked as Stateless, meaning it does not hold any conversational state.
 */
@Stateless
public class AuthenticationDao extends AbstractDao<AuthenticationEntity> {


    //Serial version UID for serialization and deserialization.
    private static final long serialVersionUID = 1L;


    /**
     * Default constructor.
     * Initializes the superclass with AuthenticationEntity class type
     */
    public AuthenticationDao() {
        super(AuthenticationEntity.class);
    }


    /**
     * Finds a UserEntity by its authentication token.
     *
     * @param authenticationToken The authentication token of the UserEntity to find.
     * @return The found UserEntity, or null if no entity with the given token exists.
     */
    public UserEntity findUserByAuthenticationToken(String authenticationToken) {
        try {
            UserEntity userEntity = (UserEntity) em.createNamedQuery("Authentication.findUserByToken").setParameter("authenticationToken", authenticationToken).getSingleResult();
            return userEntity;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds an AuthenticationEntity by its token confirmation.
     *
     * @param authenticationToken The token confirmation of the AuthenticationEntity to find.
     * @return The found AuthenticationEntity, or null if no entity with the given token confirmation exists.
     */
    public AuthenticationEntity findAuthenticationLineByTokenConfirmation(String authenticationToken) {
        try {
            AuthenticationEntity authentication = (AuthenticationEntity) em.createNamedQuery("Authentication.findByToken")
                    .setParameter("authenticationToken", authenticationToken)
                    .getSingleResult();
            return authentication;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Finds an AuthenticationEntity by its reset password token.
     *
     * @param resetPassToken The reset password token of the AuthenticationEntity to find.
     * @return The found AuthenticationEntity, or null if no entity with the given reset password token exists.
     */
    public AuthenticationEntity findAuthenticationLineByrestPassToken(String resetPassToken) {
        try {
            AuthenticationEntity authentication = (AuthenticationEntity) em.createNamedQuery("Authentication.findByresetPassToken")
                    .setParameter("resetPassToken", resetPassToken)
                    .getSingleResult();
            return authentication;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Finds a UserEntity by its reset password token.
     *
     * @param resetPassToken The reset password token of the UserEntity to find.
     * @return The found UserEntity, or null if no entity with the given reset password token exists.
     */
    public UserEntity findUserByresetPassToken(String resetPassToken) {
        try {
            UserEntity user = (UserEntity) em.createNamedQuery("Authentication.findUserByresetPassToken")
                    .setParameter("resetPassToken", resetPassToken)
                    .getSingleResult();
            return user;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Finds an AuthenticationEntity by its associated UserEntity.
     *
     * @param user The UserEntity associated with the AuthenticationEntity to find.
     * @return The found AuthenticationEntity, or null if no entity associated with the given UserEntity exists.
     */
    public AuthenticationEntity findAuthenticationByUser(UserEntity user) {
        try {
            AuthenticationEntity authentication = (AuthenticationEntity) em.createNamedQuery("Authentication.findByUser")
                    .setParameter("user", user)
                    .getSingleResult();
            return authentication;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Creates a new AuthenticationEntity in the database.
     *
     * @param authenticationEntity The AuthenticationEntity to create.
     */
    public void create(AuthenticationEntity authenticationEntity) {
        em.persist(authenticationEntity);
    }


    /**
     * Updates an existing AuthenticationEntity in the database.
     * If the entity does not exist, it will not be created.
     *
     * @param authenticationEntity The AuthenticationEntity to update.
     */
    public void update(AuthenticationEntity authenticationEntity) {
        em.merge(authenticationEntity);
    }

}
