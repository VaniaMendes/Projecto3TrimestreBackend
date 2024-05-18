package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.SessionEntity;
import jakarta.ejb.Stateless;

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

    public void update(SessionEntity session) {
        em.merge(session);
    }

    public void create(SessionEntity session) {
        em.persist(session);
    }
}
