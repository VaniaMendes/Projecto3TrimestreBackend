package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.SessionDao;
import aor.paj.proj_final_aor_backend.dao.UserDao;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import aor.paj.proj_final_aor_backend.util.enums.UserType;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

@Stateless
public class UserBean implements Serializable {
    private static final Logger logger = LogManager.getLogger(UserBean.class);

    @EJB
    UserDao userDao;

    @EJB
    SessionDao sessionDao;
    public UserBean() {
    }

    public User getUSerByToken(String token) {
        User u = null;
        if (token != null){
            long id = sessionDao.findUserIDbyToken(token);
            if(id != -1) {
                UserEntity userEntity = userDao.findUserById(id);

                if(userEntity != null) {
                    u = convertUserEntityToDto(userEntity);
                }
            }
    }
        return u;
    }

    public User registerUser( String email, String password, String confirmPassword){

        User user = null;
        //Verifying if the email already exists
        UserEntity existingUser = userDao.findUserByEmail(email);
        if(existingUser != null){
            logger.warn("Email already exists");
        }
        //Verifying if the password is valid
        if(isPasswordValid(password, confirmPassword)){
            logger.warn("Password is not valid");
        }

        //Creating a new user
        UserEntity newUser = new UserEntity();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setActiveState(false);
        newUser.setVisibilityState(false);
        newUser.setUserType(UserType.AUTHENTICATED_USER);

        ///Save the new user in the database
        userDao.createUser(newUser);

        //Convert the user entity to a user dto
        return convertUserEntityToDto(newUser);
    }


    public boolean isPasswordValid(String password, String confirmPassword){
        boolean passwordIsValid = false;

        //Verifying if the password and the confirm password are the same
        if(password.equals(confirmPassword)){
            passwordIsValid = false;
        }
        //Verifying if the password has at least 8 characters
        if(password.length() < 8 ){
            passwordIsValid = false;
        }
        // Verifying if the password has at least one uppercase letter, one lowercase letter, one number and one special character
        if(password.matches(".*[A-Z].*")){
            passwordIsValid = false;
        }
        if(password.matches(".*[a-z].*")){
            passwordIsValid = false;
        }
        if(password.matches(".*[0-9].*")){
            passwordIsValid = false;
        }
        if(password.matches(".*[!@#$%^&*].*")){
            passwordIsValid = false;
        }

        return passwordIsValid;
    }

    public UserEntity convertUserDtoToEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setActiveState(user.isActiveState());
        userEntity.setUserType(user.getUserType());
        userEntity.setBiography(user.getBiography());
        userEntity.setFirstName(user.getFirstName());
        return userEntity;
    }

    public User convertUserEntityToDto(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setEmail(userEntity.getEmail());
        user.setPassword(userEntity.getPassword());
        user.setActiveState(userEntity.isActiveState());
        user.setUserType(userEntity.getUserType());
        user.setBiography(userEntity.getBiography());
        user.setFirstName(userEntity.getFirstName());
        return user;
    }


}
