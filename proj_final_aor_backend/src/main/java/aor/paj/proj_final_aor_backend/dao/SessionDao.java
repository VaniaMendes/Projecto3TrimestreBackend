package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.SessionEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@Stateless
public class SessionDao extends AbstractDao<SessionEntity> {
    private static final long serialVersionUID = 1L;

    public SessionDao() {
        super(SessionEntity.class);
    }

    public long findUserIDbyToken(String token) {
        try {
            return (long) em.createNamedQuery("Session.findUserIDbyToken").setParameter("token", token).getSingleResult();
        } catch (Exception e) {
            return -1;
        }
    }


    public SessionEntity findSessionByUserId(long userId) {
        try {
            return (SessionEntity) em.createNamedQuery("Session.findSessionByUserId").setParameter("userId", userId).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public SessionEntity findSessionByToken(String token) {
        try {
            return (SessionEntity) em.createNamedQuery("Session.findSessionByToken").setParameter("token", token).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public UserEntity findUserByToken(String token) {
        try {
            return em.createNamedQuery("Session.findUserByToken", UserEntity.class)
                    .setParameter("token", token)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.out.println("Error fetching user by token: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public void update(SessionEntity session) {
        em.merge(session);
    }

    public void create(SessionEntity session) {
        em.persist(session);
    }
}
