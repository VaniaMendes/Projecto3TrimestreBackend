package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.LabEntity;
import aor.paj.proj_final_aor_backend.util.enums.Workplace;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class LabDao extends AbstractDao<LabEntity>{

    private static final long serialVersionUID = 1L;

    public LabDao() {
        super(LabEntity.class);
    }

    public List<LabEntity> findAllLabs() {
        try {
            List<LabEntity> labEntities = em.createNamedQuery("Lab.findAllLabs").getResultList();
            return labEntities;
        } catch (Exception e) {
            return null;
        }
    }


    public LabEntity findLabById(Integer id) {
        try {
            LabEntity labEntity = (LabEntity) em.createNamedQuery("Lab.findLabById").setParameter("id", id).getSingleResult();
            return labEntity;
        } catch (Exception e) {
            return null;
        }
    }


    public LabEntity findLabByUserId(Long userId) {
        try {
            LabEntity labEntity = (LabEntity) em.createNamedQuery("Lab.findLabByUserId").setParameter("userId", userId).getSingleResult();
            return labEntity;
        } catch (Exception e) {
            return null;
        }
    }


    public LabEntity findLabByName(String name) {
        try {
            Workplace workplace = Workplace.valueOf(name.toUpperCase());
            LabEntity labEntity = (LabEntity) em.createNamedQuery("Lab.findLabByName")
                    .setParameter("name", workplace)
                    .getSingleResult();

            return labEntity;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }



    public void createLab(LabEntity lab) {
        em.persist(lab);
    }

    public void updateLab(LabEntity lab) {
        em.merge(lab);
    }

    public void deleteLab(LabEntity lab) {
        em.remove(em.contains(lab) ? lab : em.merge(lab));
    }
}
