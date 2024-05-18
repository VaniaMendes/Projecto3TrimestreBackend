package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.LabEntity;
import jakarta.ejb.Stateless;

@Stateless
public class LabDao extends AbstractDao<LabEntity>{

    private static final long serialVersionUID = 1L;

    public LabDao() {
        super(LabEntity.class);
    }

  public LabEntity findLabById(Integer id) {
        try {
            LabEntity labEntity = (LabEntity) em.createNamedQuery("Lab.findLabById").setParameter("id", id).getSingleResult();
            return labEntity;
        } catch (Exception e) {
            return null;
        }
    }

    public LabEntity findLabByName(String name) {
        try {
            LabEntity labEntity = (LabEntity) em.createNamedQuery("Lab.findLabByName").setParameter("name", name).getSingleResult();
            return labEntity;
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
