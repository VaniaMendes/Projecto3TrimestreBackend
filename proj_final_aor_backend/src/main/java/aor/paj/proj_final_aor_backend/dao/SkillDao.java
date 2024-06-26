package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import jakarta.ejb.Stateless;

import java.util.List;

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

    public SkillEntity findSkillByName(String name) {
        try {
            return em.createNamedQuery("Skill.findSkillByName", SkillEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

  public void createSkill(SkillEntity skill) {
        em.persist(skill);
    }

    public List<SkillEntity> findAllSkills() {
        try {
            return em.createNamedQuery("Skill.findAllSkills", SkillEntity.class)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }


    public List<SkillEntity>findSkillsByUserId(long userId){
        try {
            return em.createNamedQuery("Skill.findSkillsByUserId", SkillEntity.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }






}
