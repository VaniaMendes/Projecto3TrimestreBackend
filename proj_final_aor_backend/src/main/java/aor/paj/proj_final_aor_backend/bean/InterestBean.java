package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.InterestDao;
import aor.paj.proj_final_aor_backend.dao.UserDao;
import aor.paj.proj_final_aor_backend.dao.UserInterestDao;
import aor.paj.proj_final_aor_backend.dto.Interest;
import aor.paj.proj_final_aor_backend.entity.*;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
public class InterestBean {
    private static final Logger logger = LogManager.getLogger(UserBean.class);


    @EJB
    InterestDao interestDao;
    @EJB
    UserDao userDao;

    @EJB
    UserInterestDao userInterestDao;


    public InterestBean() {
    }

    public boolean createNewInterest(Interest interest){
        if(interest.getName() == null ){
            logger.error("Interest name is null or empty.");
            return false;
        }
        InterestEntity interestEntity = new InterestEntity();
        interestEntity.setName(interest.getName());

        interestDao.createInterest(interestEntity);
        return true;
    }


    public boolean associateInterestToUser(long userId, long interestId){
        // Fetch the user and skill from the database
        UserEntity user = userDao.findUserById(userId);
        InterestEntity interest = interestDao.find(interestId);

        if (user == null || interest == null) {
            // Either the user or the skill does not exist
            return false;
        }

        if(verifyInterestExists(interestId)){
            return false;
        }
        // Associate the skill to the user
        UserInterestEntity userInterest = new UserInterestEntity();
        userInterest.setUser(user);
        userInterest.setInterest(interest);
        userInterest.setActive(true);

        userInterestDao.persist(userInterest);

        return true;
    }

    public boolean verifyInterestExists(Long interestId){
        InterestEntity interest = interestDao.find(interestId);
        return interest != null;
    }
    public boolean softDeleteInterest(Long userId, Long interestId){
        // Fetch the user and skill from the database
        UserEntity user = userDao.findUserById(userId);
        InterestEntity interest = interestDao.find(interestId);


        if (user == null || interest == null) {
            // Either the user or the skill does not exist
            System.out.println("User or interest does not exist");
            return false;
        }
        UserInterestEntity userInterest = userInterestDao.findUserInterestByUserAndSkill(userId, interestId);
        if (userInterest == null) {
            System.out.println("UserInterest entity does not exist");
            return false;
        }
        userInterest.setActive(false); // Soft delete the skill
        userInterestDao.updateUserInterest(userInterest);
        return true;
    }
}
