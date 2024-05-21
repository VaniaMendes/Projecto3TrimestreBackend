package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import jakarta.ejb.Stateless;

@Stateless
public class SkillDao extends AbstractDao<SkillEntity>{
    private static final long serialVersionUID = 1L;

    public SkillDao() {
        super(SkillEntity.class);
    }

  public void createSkill(SkillEntity skill) {
        em.persist(skill);
    }








}
