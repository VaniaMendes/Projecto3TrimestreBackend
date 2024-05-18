package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.AuthenticationDao;
import aor.paj.proj_final_aor_backend.dao.LabDao;
import aor.paj.proj_final_aor_backend.dao.SessionDao;
import aor.paj.proj_final_aor_backend.dao.UserDao;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.entity.AuthenticationEntity;
import aor.paj.proj_final_aor_backend.entity.SessionEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import aor.paj.proj_final_aor_backend.util.enums.UserType;
import aor.paj.proj_final_aor_backend.util.EmailServiceHelper;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * This class is responsible for the business logic of the User entity.
 * It is responsible for the registration of a new user, the verification of the password and email, the encryption of the password and the sending of the confirmation email.
 */
@Stateless
public class UserBean implements Serializable {
    private static final Logger logger = LogManager.getLogger(UserBean.class);

    @EJB
    UserDao userDao;

    @EJB
    SessionDao sessionDao;
    @EJB
    EmailServiceHelper emailService;
    @EJB
    AuthenticationDao authenticationDao;
    @EJB
    LabDao labDao;

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

    public User getUserByEmail(String email) {
        User u = null;
        if (email != null){
                UserEntity userEntity = userDao.findUserByEmail(email);

                if(userEntity != null) {
                    u = convertUserEntityToDto(userEntity);
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
        newUser.setUserType(UserType.LOGGED_IN);

        ///Save the new user in the database
        userDao.createUser(newUser);

        //Generating a token for the confirmation email
        String tokenConfirmation = UUID.randomUUID().toString();

        //Save the token and userId in the authentication table
        AuthenticationEntity authenticationEntity = new AuthenticationEntity();
        authenticationEntity.setAuthenticationToken(tokenConfirmation);

        // Format LocalDateTime to String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);
        authenticationEntity.setRegisterDate(formattedDate);
        authenticationEntity.setUser(newUser);
        authenticationEntity.setAuthState(false);
        authenticationDao.create(authenticationEntity);

        sendConfirmationEmail("proj_final_aor@outlook.com", tokenConfirmation);
       return true;
    }

    public String loginUser(String email, String password) {
        UserEntity user = userDao.findUserByEmail(email);
        if (user != null && user.isActiveState()) {

            //Library oh Crypt to check if the password is correct
            //If the password is correct returns true
            if (BCrypt.checkpw(password, user.getPassword())) {
                String token = UUID.randomUUID().toString();
                SessionEntity session = new SessionEntity();
                session.setUser(user);
                session.setToken(token);
                session.setInitSession(LocalDateTime.now());
                sessionDao.create(session);
                return token;
            }
        }
        return null;
    }

    public boolean confirmUser(User user, String token) {

        // Check if the required fields are present
        if(user.getFirstName() == null || user.getLastName() == null ){
            return false;
        }

        /*
        //Check if the lab is valid
        if(lab == null){
            return false;
        }
        LabEntity  labEntity = labDao.findLabById(lab.getId());


         */
        //Check if the token is valid
        UserEntity userConfirm = authenticationDao.findUserByAuthenticationToken(token);

        if(userConfirm == null){
            return false;
        }

        userConfirm.setActiveState(true);
        userConfirm.setFirstName(user.getFirstName());
        userConfirm.setLastName(user.getLastName());
        //userConfirm.setLab(labEntity);
        userConfirm.setVisibilityState(user.isVisibilityState());

        if(user.getBiography() != null){
            userConfirm.setBiography(user.getBiography());
        }
        if(user.getPhoto() != null){
            userConfirm.setPhoto(user.getPhoto());
        }
        if(user.getNickname() != null){
            userConfirm.setNickname(user.getNickname());
        }

        userDao.updateUser(userConfirm);

        AuthenticationEntity authentication = authenticationDao.findAuthenticationLineByTokenConfirmation(token);
        authentication.setAuthState(true);
        authentication.setAuthenticationToken(null);
        authenticationDao.update(authentication);


        return true;
    }


    public void sendConfirmationEmail(String to, String token){
        //Sending the confirmation email
        logger.info("Sending confirmation email to " + to + " with token " + token);
        String subject = "Account Confirmation ";


        // Construct the email body with a professional tone and formatting
        StringBuilder body = new StringBuilder();
        body.append("Welcome to Innovation Lab Management!\n");
        body.append("\n");
        body.append("Dear User,\n");
        body.append("Thank you for registering with us. We're excited to have you on board.\n");
        body.append("To complete your registration, please click the link below to confirm your account:\n");
        body.append("http://localhost:3000/confirm?token=" + token + "\n");
        body.append("If you did not register for our service, please ignore this email.\n");
        body.append("\n");
        body.append("Best regards,\n");
        body.append("Critical Software\n");


        emailService.sendEmail(to, subject, body.toString());
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

        //Verifying if the password and the confirmPassword are the same
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
