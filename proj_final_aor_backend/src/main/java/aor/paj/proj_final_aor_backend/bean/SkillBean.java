package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.SkillDao;
import aor.paj.proj_final_aor_backend.dao.UserDao;
import aor.paj.proj_final_aor_backend.dao.UserSkillDao;
import aor.paj.proj_final_aor_backend.dto.Skill;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import aor.paj.proj_final_aor_backend.entity.UserSkillEntity;
import aor.paj.proj_final_aor_backend.util.enums.SkillType;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.Set;

@Stateless
public class SkillBean implements Serializable {

    // Logger for this class
    private static Logger logger = LogManager.getLogger(ResourceBean.class);
    @EJB
    SkillDao skillDao;
    @EJB
    UserDao userDao;

    @EJB
    UserSkillDao userSkillDao;

    public SkillBean() {
    }

    public boolean createNewSkill(Skill skill) {
        if (skill.getName() == null || skill.getType().equals(" ")) {
            logger.error("Skill name is null or empty.");
            return false;
        }
        SkillEntity skillEntity = new SkillEntity();
        skillEntity.setName(skill.getName());
        skillEntity.setType(skill.getType());
        skillDao.createSkill(skillEntity);
        return true;
    }

    public boolean associateSkillToUser(long userId, long skillId) {
        // Fetch the user and skill from the database
        UserEntity user = userDao.findUserById(userId);
        SkillEntity skill = skillDao.find(skillId);

        if (user == null || skill == null) {
            // Either the user or the skill does not exist
            return false;
        }

        // Associate the skill to the user
        UserSkillEntity userSkill = new UserSkillEntity();
        userSkill.setUser(user);
        userSkill.setSkill(skill);
        userSkill.setActive(true);

        userSkillDao.persist(userSkill);

        return true;
    }


    public boolean softDeleteSkill(long userId, long skillId) {
        // Fetch the user and skill from the database
        UserEntity user = userDao.findUserById(userId);
        SkillEntity skill = skillDao.find(skillId);


        if (user == null || skill == null) {
            // Either the user or the skill does not exist
            System.out.println("User or skill does not exist");
            return false;
        }
        UserSkillEntity userSkill = userSkillDao.findUserSkillByUserAndSkill(userId, skillId);
        if (userSkill == null) {
            System.out.println("userSkill entity does not exist");
            return false;
        }
        userSkill.setActive(false); // Soft delete the skill
        userSkillDao.updateUserSkill(userSkill);
        return true;
    }
}
