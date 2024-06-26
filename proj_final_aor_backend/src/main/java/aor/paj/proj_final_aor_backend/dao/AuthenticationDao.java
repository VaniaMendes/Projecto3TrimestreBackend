package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.AuthenticationEntity;
import aor.paj.proj_final_aor_backend.entity.MessageEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class AuthenticationDao extends AbstractDao<AuthenticationEntity> {

    private static final long serialVersionUID = 1L;
    public AuthenticationDao() {
        super(AuthenticationEntity.class);
    }


    public UserEntity findUserByAuthenticationToken(String authenticationToken) {
        try {
            UserEntity userEntity = (UserEntity) em.createNamedQuery("Authentication.findUserByToken").setParameter("authenticationToken", authenticationToken).getSingleResult();
            return userEntity;
        } catch (Exception e) {
            return null;
        }
    }
    public AuthenticationEntity findAuthenticationLineByTokenConfirmation(String authenticationToken) {
        try {
            AuthenticationEntity authentication = (AuthenticationEntity) em.createNamedQuery("Authentication.findByToken")
                    .setParameter("authenticationToken", authenticationToken)
                    .getSingleResult();
            return authentication;
        } catch (Exception e) {
            return null;
        }
    }

    public AuthenticationEntity findAuthenticationLineByrestPassToken(String resetPassToken) {
        try {
            AuthenticationEntity authentication = (AuthenticationEntity) em.createNamedQuery("Authentication.findByresetPassToken")
                    .setParameter("resetPassToken", resetPassToken)
                    .getSingleResult();
            return authentication;
        } catch (Exception e) {
            return null;
        }
    }

    public UserEntity findUserByresetPassToken(String resetPassToken) {
        try {
            UserEntity user = (UserEntity) em.createNamedQuery("Authentication.findUserByresetPassToken")
                    .setParameter("resetPassToken", resetPassToken)
                    .getSingleResult();
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public AuthenticationEntity findAuthenticationByUser(UserEntity user) {
        try {
            AuthenticationEntity authentication = (AuthenticationEntity) em.createNamedQuery("Authentication.findByUser")
                    .setParameter("user", user)
                    .getSingleResult();
            return authentication;
        } catch (Exception e) {
            return null;
        }
    }


   public void create(AuthenticationEntity authenticationEntity) {
        em.persist(authenticationEntity);
    }

    public void update(AuthenticationEntity authenticationEntity) {
        em.merge(authenticationEntity);
    }

}
