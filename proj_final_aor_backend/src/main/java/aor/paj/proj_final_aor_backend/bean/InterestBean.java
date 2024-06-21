package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.InterestDao;
import aor.paj.proj_final_aor_backend.dao.UserDao;
import aor.paj.proj_final_aor_backend.dao.UserInterestDao;
import aor.paj.proj_final_aor_backend.dto.Interest;
import aor.paj.proj_final_aor_backend.entity.*;
import aor.paj.proj_final_aor_backend.bean.UserBean;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * This class is responsible for handling the business logic for the Interest entity.
 * It is responsible for creating, deleting and associating interests to users.
 * It is also responsible for checking if an interest already exists and if a user already has an interest.
 */
@Stateless
public class InterestBean {
    /**
     * Logger for the InterestBean class.
     */
    private static final Logger logger = LogManager.getLogger(UserBean.class);


    /**
     * InterestDao instance to interact with the database.
     */
    @EJB
    InterestDao interestDao;
    /**
     * UserDao instance to interact with the database.
     */
    @EJB
    UserDao userDao;

    /**
     * UserInterestDao instance to interact with the database.
     */
    @EJB
    UserInterestDao userInterestDao;

    @EJB
    UserBean userBean;

    /**
     * Default constructor for the InterestBean class.
     */
    public InterestBean() {
    }

    /**
     * This method creates a new interest in the database.
     * @param interest The interest to be created.
     * @return True if the interest was created successfully, false otherwise.
     */

    public boolean createNewInterest(String token, Interest interest){
        // Check if the interest name is null or empty
        if(interest.getName().isEmpty() ){
            logger.error("Interest name is null or empty.");
            return false;
        }

        // Check if the interest already exists
       if(interestAlreadyExists(interest.getName())){
           return false;
       }

       // Create the interest
        InterestEntity interestEntity = new InterestEntity();

        String name = interest.getName();
        String[] words = name.split(" ");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
        }
        String formattedName = String.join(" ", words);
        interestEntity.setName(formattedName);

        // Persist the interest in the database
        interestDao.createInterest(interestEntity);
        associateInterestToUser(userBean.getUserByToken(token).getId(), interestEntity.getId());
        return true;
    }


    /**
     * This method associates an interest to a user in the database.
     * It checks if the user and the interest exist and if the user already has the interest associated.
     * @param userId The id of the user to associate the interest to.
     * @param interestId The id of the interest to associate to the user.
     * @return True if the interest was associated successfully, false otherwise.
     */
    public boolean associateInterestToUser(long userId, long interestId){
        // Fetch the user and skill from the database
        UserEntity user = userDao.findUserById(userId);
        InterestEntity interest = interestDao.find(interestId);

        // Check if the user and the interest exist
        if (user == null || interest == null) {
            return false;
        }

        if(userHasInterestInactive(userId,interestId)){
            UserInterestEntity userInterest = userInterestDao.findUserInterestByUserAndInterest(userId, interestId);
            userInterest.setActive(true);
            userInterestDao.updateUserInterest(userInterest);
            return true;
        }

        // Check if the user already has the interest associated
        if(userAlreadyHasInterest(userId,interestId)){
            return false;
        }


        // Associate the interest to the user
        UserInterestEntity userInterest = new UserInterestEntity();
        userInterest.setUser(user);
        userInterest.setInterest(interest);
        userInterest.setActive(true);
        // Persist the userInterest in the database
        userInterestDao.persist(userInterest);
        return true;
    }

    /**
     * This method checks if an interest already exists in the database.
     * @param name The name of the interest to check.
     * @return True if the interest already exists, false otherwise.
     */
    public boolean interestAlreadyExists(String name){
        InterestEntity interest = interestDao.findInterestByName(name);
        return interest != null;

    }

    /**
     * This method checks if a user already has an interest associated.
     * @param userId The id of the user to check.
     * @param interestId The id of the interest to check.
     * @return True if the user already has the interest associated, false otherwise.
     */
    public boolean userAlreadyHasInterest(long userId, long interestId){
        UserInterestEntity interest = userInterestDao.findUserInterestByUserAndInterest(userId, interestId);
        if (interest == null) {
            return false;
        }
        return true;
    }

    public boolean userHasInterestInactive(long userId, long interestId){
        UserInterestEntity interest = userInterestDao.findUserInterestByUserAndInterest(userId, interestId);
        if (interest == null) {
            return false;
        }
        return !interest.isActive();
    }

    /**
     * This method puts the interest from a user in the database to inactive.
     * @param userId The id of the user to remove the interest from.
     * @param interestId The id of the interest to remove from the user.
     * @return True if the interest was removed successfully, false otherwise.
     */
    public boolean removeInterestfromUser(long userId, long interestId){
        // Fetch the user and skill from the database
        UserEntity user = userDao.findUserById(userId);
        InterestEntity interest = interestDao.find(interestId);

        // Check if the user or the interest does not exist
        if (user == null || interest == null) {
            // Either the user or the skill does not exist
            logger.warn("User or interest does not exist");
            return false;
        }

        // Check if the user already has the interest associated
        UserInterestEntity userInterest = userInterestDao.findUserInterestByUserAndInterest(userId, interestId);
        if (userInterest == null) {
            logger.warn("UserInterest entity does not exist");
            return false;
        }
        // Soft delete the interest
        userInterest.setActive(false);
        userInterestDao.updateUserInterest(userInterest);
        return true;
    }

    /**
     * This method gets all the interests from the database.
     * @return List<InterestEntity> - The list of all interests in the database.
     */
    public List<InterestEntity> getAllInterests(){
        return interestDao.getAllInterests();
    }



    public List<InterestEntity> getAllInterestsByUser(long userId){
        return userInterestDao.getAllInterestsByUserId(userId);
    }

}
