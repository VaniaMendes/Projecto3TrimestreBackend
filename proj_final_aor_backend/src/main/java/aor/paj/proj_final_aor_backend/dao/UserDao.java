package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.List;

/**
 * UserDao is a DAO class that provides methods to access the UserEntity table in the database.
 * It extends the AbstractDao class and provides methods to find users.
 */
@Stateless
public class UserDao extends AbstractDao<UserEntity> {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor of the UserDao class.
     */
    public UserDao() {
        super(UserEntity.class);
    }



    /**
     * Method to find a user by their ID.
     * It uses a named query "User.findUserById" and sets the "id" parameter for the query.
     * @param id the ID of the user to find.
     * @return the user with the given ID, or null if no user is found.
     */
    public UserEntity findUserById(long id) {
        try {
            return (UserEntity) em.createNamedQuery("User.findUserById").setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Method to find a user by their email.
     * It uses a named query "User.findUserByEmail" and sets the "email" parameter for the query.
     * @param email the email of the user to find.
     * @return the user with the given email, or null if no user is found.
     */
    public UserEntity findUserByEmail(String email) {
        try {
            return (UserEntity) em.createNamedQuery("User.findUserByEmail").setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Method to find all active users.
     * It uses a named query "User.findAllAtiveUsers" to find all users with the activeState set to true.
     * @return a list of all active users, or null if no users are found.
     */
    public List<UserEntity> findAllAtiveUsers (){
        try{
            return em.createNamedQuery("User.findAllAtiveUsers", UserEntity.class).getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    public UserEntity findUserByNickname(String nickname){
        try{
            return (UserEntity) em.createNamedQuery("User.findUserByNickname").setParameter("nickname", nickname).getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    /**
     * Method to find a user by their username.
     * It uses a named query "User.findUserByUsername" and sets the "username" parameter for the query.
     * @param prefix the firstName or the lastName of the user to find.
     * @return a list of users with the given firstName or lastName, or null if no users are found.
     */
    public List<UserEntity> findUsersByFirstNameStartingWith(long userId, String prefix) {
        try {
            return em.createNamedQuery("User.findUserByNameStartingWith", UserEntity.class)
                    .setParameter("prefix", prefix + "%")
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }


    /**
     * Method to update a user.
     * If the update is successful, it returns true. Otherwise, it returns false.
     * @param userEntity The userEntity object to update.
     * @return true if the update is successful, false otherwise.
     */
    public boolean updateUser(UserEntity userEntity) {
        try {
            em.merge(userEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to create a new user.
     * It uses the EntityManager's persist method to insert the new userEntity into the database.
     *
     * @param userEntity The UserEntity object to be created.
     */
    public void createUser(UserEntity userEntity) {
        em.persist(userEntity);
    }

}
