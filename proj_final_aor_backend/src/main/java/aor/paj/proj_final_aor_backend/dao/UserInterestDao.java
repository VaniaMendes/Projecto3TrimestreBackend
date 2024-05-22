package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.UserInterestEntity;
import jakarta.ejb.Stateless;

@Stateless
public class UserInterestDao extends AbstractDao<UserInterestEntity>{

    private static final long serialVersionUID = 1L;

    public UserInterestDao() {
        super(UserInterestEntity.class);
    }

    public void createUserInterest(UserInterestEntity userInterest) {
        persist(userInterest);
    }
}
