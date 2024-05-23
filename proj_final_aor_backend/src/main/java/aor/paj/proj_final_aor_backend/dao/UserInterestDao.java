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

    public void updateUserInterest(UserInterestEntity userInterest) {
        merge(userInterest);
    }

    public UserInterestEntity findUserInterestByUserAndInterest(long userId, long interestId) {
        try {
            return (UserInterestEntity) em.createNamedQuery("UserInterestEntity.findUserInterest").setParameter("user", userId).setParameter("interest", interestId).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
