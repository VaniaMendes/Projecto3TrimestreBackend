package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.InterestEntity;
import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import jakarta.ejb.Stateless;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class InterestDao extends AbstractDao<InterestEntity> {
    private static final long serialVersionUID = 1L;

    public InterestDao() {
        super(InterestEntity.class);
    }

    public void createInterest(InterestEntity interest) {
        em.persist(interest);
    }

  public InterestEntity findInterestByName(String name) {
        try {
            return (InterestEntity) em.createNamedQuery("InterestEntity.findInterestByName").setParameter("name", name).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


    public List<InterestEntity> getAllInterests() {
        try {
            return em.createNamedQuery("InterestEntity.findAllInterests", InterestEntity.class).getResultList();
        } catch (Exception e) {
            return null;
        }
    }




}
