package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.AuthenticationEntity;
import aor.paj.proj_final_aor_backend.entity.MessageEntity;
import jakarta.ejb.Stateless;

@Stateless
public class AuthenticationDao extends AbstractDao<AuthenticationEntity> {

    private static final long serialVersionUID = 1L;
    public AuthenticationDao() {
        super(AuthenticationEntity.class);
    }


   public void create(AuthenticationEntity authenticationEntity) {
        em.persist(authenticationEntity);
    }

    public void update(AuthenticationEntity authenticationEntity) {
        em.merge(authenticationEntity);
    }

}
