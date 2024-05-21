package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.InterestEntity;
import jakarta.ejb.Stateless;

@Stateless
public class InterestDao extends AbstractDao<InterestEntity> {
    private static final long serialVersionUID = 1L;

    public InterestDao() {
        super(InterestEntity.class);
    }

    public void createInterest(InterestEntity interest) {
        em.persist(interest);
    }

}
