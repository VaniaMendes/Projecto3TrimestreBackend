package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import aor.paj.proj_final_aor_backend.entity.UserSkillEntity;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class UserSkillDao extends AbstractDao<UserSkillEntity>{

    private static final long serialVersionUID = 1L;

    public UserSkillDao() {
        super(UserSkillEntity.class);
    }

    public List<UserEntity> findAllUsersWithSkill(long skillId) {
        try {
            return em.createNamedQuery("UserSkillEntity.findAllUsersWithSkill").setParameter("skill", skillId).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<SkillEntity> findAllSkillsForUser(long userId) {
        try {
            return em.createNamedQuery("UserSkillEntity.findAllSkillsForUser").setParameter("user", userId).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public void createUserSkill(UserSkillEntity userSkill) {
        persist(userSkill);
    }

}
