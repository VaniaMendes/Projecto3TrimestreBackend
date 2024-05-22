package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.UserSkillEntity;
import jakarta.ejb.Stateless;

@Stateless
public class UserSkillDao extends AbstractDao<UserSkillEntity>{

    private static final long serialVersionUID = 1L;

    public UserSkillDao() {
        super(UserSkillEntity.class);
    }

    public void createUserSkill(UserSkillEntity userSkill) {
        persist(userSkill);
    }

}
