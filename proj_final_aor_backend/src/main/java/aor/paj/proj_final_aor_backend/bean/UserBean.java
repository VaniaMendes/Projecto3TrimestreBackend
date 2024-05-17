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
import org.mindrot.jbcrypt.BCrypt;

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

    public boolean registerUser( String email, String password, String confirmPassword) {

        //Verifying if the email already exists
        UserEntity existingUser = userDao.findUserByEmail(email);
        if (existingUser != null) {
            logger.warn("Email already exists");
            return false;
        }
        //Verifying if the password is valid
        if (!isPasswordValid(password, confirmPassword)) {
            logger.warn("Password is not valid");
            return false;
        }
        //Verifying if the email is valid
        if(!isValidemail(email)) {
            logger.warn("Email is not valid");
            return false;
        }

        //Creating a new user
        UserEntity newUser = new UserEntity();
        newUser.setEmail(email);
        //Encrypting the password
         String encryptedPassword = encryptPassword(password);
         newUser.setPassword(encryptedPassword);
        newUser.setActiveState(false);
        newUser.setVisibilityState(false);
        newUser.setUserType(UserType.AUTHENTICATED_USER);

        ///Save the new user in the database
        userDao.createUser(newUser);
       return true;
    }


    public boolean isValidemail(String email){
        //Verifying if the email is valid
        if(email == null ){
            return false;
        }
        if(!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")){
            return false;
        }
        return true;
    }



    public boolean isPasswordValid(String password, String confirmPassword){

        //Verifying if the password and the confirm password are the same
        if(!password.equals(confirmPassword)){
            return false;
        }
        //Verifying if the password has at least 8 characters
        if(password.length() < 8 ){
            return false;
        }
        // Verifying if the password has at least one uppercase letter, one lowercase letter, one number and one special character
        if(!password.matches(".*[A-Z].*")){
            return false;
        }
        if(!password.matches(".*[a-z].*")){
            return false;
        }
        if(!password.matches(".*[0-9].*")){
            return false;
        }
        if(!password.matches(".*[!@#$%^&*].*")){
            return false;
        }

        return true;
    }

    public String encryptPassword(String password) {
        String salt = BCrypt.gensalt(12);
        String hashedPassword = BCrypt.hashpw(password, salt);
        return hashedPassword;
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
