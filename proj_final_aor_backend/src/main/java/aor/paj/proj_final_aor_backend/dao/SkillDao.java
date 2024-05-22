package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import jakarta.ejb.Stateless;

@Stateless
public class SkillDao extends AbstractDao<SkillEntity>{
    private static final long serialVersionUID = 1L;

    public SkillDao() {
        super(SkillEntity.class);
    }

    public SkillEntity findSkillById(Long id) {
        try {
            return em.createNamedQuery("Skill.findSkillById", SkillEntity.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

  public void createSkill(SkillEntity skill) {
        em.persist(skill);
    }








}
