package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.SkillDao;
import aor.paj.proj_final_aor_backend.dto.Skill;
import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import aor.paj.proj_final_aor_backend.util.enums.SkillType;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

@Stateless
public class SkillBean implements Serializable {

    // Logger for this class
    private static Logger logger = LogManager.getLogger(ResourceBean.class);
    @EJB
    SkillDao skillDao;

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




}
