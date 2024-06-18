package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.SkillDao;
import aor.paj.proj_final_aor_backend.dao.UserDao;
import aor.paj.proj_final_aor_backend.dao.UserSkillDao;
import aor.paj.proj_final_aor_backend.dto.Skill;
import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import aor.paj.proj_final_aor_backend.entity.UserSkillEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.Serializable;
import java.util.List;


/**
 * This class is responsible for handling the business logic for the Skill entity.
 * It is responsible for creating, deleting and associating skills to users.
 */
@Stateless
public class SkillBean implements Serializable {

    /**
     * Logger for the SkillBean class.
     */
    private static Logger logger = LogManager.getLogger(ResourceBean.class);
    /**
     * SkillDao instance to interact with the database.
     */
    @EJB
    SkillDao skillDao;
    /**
     * UserDao instance to interact with the database.
     */
    @EJB
    UserDao userDao;

    /**
     * UserSkillDao instance to interact with the database.
     */
    @EJB
    UserSkillDao userSkillDao;

    @EJB
    UserBean userBean;

    /**
     * Default constructor for the SkillBean class.
     */
    public SkillBean() {
    }

    /**
     * This method returns a list of all the skills in the database.
     * @return List of all the skills in the database.
     */

    public List<SkillEntity> getSkills() {
        return skillDao.findAllSkills();
    }

    public List<SkillEntity>getSkillsByUserId(long userId){
        return skillDao.findSkillsByUserId(userId);
    }

    /**
     * This method creates a new skill in the database.
     * @param skill Skill object to be created.
     * @return True if the skill was created successfully, false otherwise.
     */
    public boolean createNewSkill(String token, Skill skill) {
        // Check if the skill name is null or empty
        if (skill.getName().isEmpty()) {
            logger.error("Skill name is null or empty.");
            return false;
        }
        // Check if the skill already exists
        if (skillDao.findSkillByName(skill.getName()) != null) {
            logger.error("Skill already exists.");
            return false;
        }
        // Create the skill
        SkillEntity skillEntity = new SkillEntity();

        String name = skill.getName();
        String[] words = name.split(" ");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
        }
        String formattedName = String.join(" ", words);
        skillEntity.setName(formattedName);

        skillEntity.setType(skill.getType());
        // Persist the skill in the database
        skillDao.createSkill(skillEntity);
        associateSkillToUser(userBean.getUSerByToken(token).getId(), skillEntity.getId());
        return true;
    }

    /**
     * This method associates a skill to a user.
     * It creates a new UserSkillEntity object and persists it in the database.
     * @param userId The id of the user to associate the skill to.
     * @param skillId The id of the skill to associate to the user.
     * @return True if the skill was associated successfully, false otherwise.
     */
    public boolean associateSkillToUser(long userId, long skillId) {
        // Fetch the user and skill from the database
        UserEntity user = userDao.findUserById(userId);
        SkillEntity skill = skillDao.find(skillId);
        System.out.println(user.getId());
        System.out.println(skill.getId());

        // Check if the user or the skill does not exist
        if (user == null || skill == null) {
            return false;
        }
        // Check if the user already has the skill associated
        if(verifySkillExists(userId, skillId)){
            return false;
        }

        // Associate the skill to the user
        UserSkillEntity userSkill = new UserSkillEntity();
        userSkill.setUser(user);
        userSkill.setSkill(skill);
        userSkill.setActive(true);
        // Persist the userSkill in the database
        userSkillDao.persist(userSkill);

        return true;
    }


    /**
     * This method soft deletes a skill from a user.
     * It sets the active field of the UserSkillEntity object to false.
     * @param userId The id of the user to soft delete the skill from.
     * @param skillId The id of the skill to soft delete.
     * @return True if the skill was soft deleted successfully, false otherwise.
     */
    public boolean softDeleteSkill(long userId, long skillId) {
        // Fetch the user and skill from the database
        UserEntity user = userDao.findUserById(userId);
        SkillEntity skill = skillDao.find(skillId);

        // Check if the user or the skill does not exist
        if (user == null || skill == null) {
            System.out.println("User or skill does not exist");
            return false;
        }
        // Fetch the userSkill from the database
        UserSkillEntity userSkill = userSkillDao.findUserSkillByUserAndSkill(userId, skillId);

        // Check if the userSkill does not exist
        if (userSkill == null) {
            System.out.println("userSkill entity does not exist");
            return false;
        }
        // Soft delete the skill
        userSkill.setActive(false);
        // Update the userSkill in the database
        userSkillDao.updateUserSkill(userSkill);
        return true;
    }



    public Skill convertToDTO(SkillEntity skillEntity) {
        Skill skill = new Skill();
        skill.setId(skillEntity.getId());
        skill.setName(skillEntity.getName());
        skill.setType(skillEntity.getType());
        return skill;
    }

    /**
     * This method verifies if a skill already exists in the database.
     * @param skillId The id of the skill to verify.
     * @return True if the skill exists, false otherwise.
     */
    public boolean verifySkillExists(Long userId, Long skillId){
        // Fetch the skill from the database
        UserSkillEntity skill = userSkillDao.findUserSkillByUserAndSkill(userId, skillId);
        return skill != null;
    }
}
