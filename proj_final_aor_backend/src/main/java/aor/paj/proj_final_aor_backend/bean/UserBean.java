package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.*;
import aor.paj.proj_final_aor_backend.dto.Lab;
import aor.paj.proj_final_aor_backend.dto.MessageInfoUser;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.dto.UserInfoInProject;
import aor.paj.proj_final_aor_backend.entity.*;
import aor.paj.proj_final_aor_backend.util.enums.UserType;
import aor.paj.proj_final_aor_backend.util.EmailServiceHelper;
import aor.paj.proj_final_aor_backend.util.enums.UserTypeInProject;
import aor.paj.proj_final_aor_backend.util.enums.Workplace;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class is responsible for the business logic of the User entity.
 * It is responsible for the registration of a new user, the verification of the password and email, the encryption of the password and the sending of the confirmation email.
 */
@Stateless
public class UserBean implements Serializable {
    /**
     * Logger for the UserBean class.
     */
    private static final Logger logger = LogManager.getLogger(UserBean.class);

    /**
     * UserDao instance to interact with the database.
     */
    @EJB
    UserDao userDao;

    /**
     * SessionDao instance to interact with the database.
     */
    @EJB
    SessionDao sessionDao;
    /**
     * EmailServiceHelper instance to send emails.
     */
    @Inject
    EmailServiceHelper emailService;
    /**
     * AuthenticationDao instance to interact with the database.
     */
    @EJB
    AuthenticationDao authenticationDao;
    /**
     * LabDao instance to interact with the database.
     */
    @EJB
    LabDao labDao;
    /**
     * UserInterestDao instance to interact with the database.
     */
    @EJB
    UserInterestDao interestDao;

    /**
     * SettingsBean instance to interact with the database.
     */
    @EJB
    SettingsBean settingsBean;

    /**
     * Default constructor for the UserBean class.
     */
    public UserBean() {
    }


    /**
     * This method is responsible by find one user by token
     *
     * @param token the token of the user
     * @return the user
     */
    public User getUserByToken(String token) {
        // Initialize a User object to null.
        User u = null;

        // Check if the provided token is not null.
        if (token != null) {
            // Use the sessionDao to find the user ID associated with the token.
            long id = sessionDao.findUserIDbyToken(token);

            // Check if a valid user ID was found (i.e., id is not -1).
            if (id != -1) {
                // Retrieve the UserEntity object corresponding to the user ID from userDao.
                UserEntity userEntity = userDao.findUserById(id);

                // Check if the UserEntity object is not null.
                if (userEntity != null) {
                    // Convert the UserEntity object to a User DTO (Data Transfer Object).
                    u = convertUserEntityToDto(userEntity);
                }
            }
        }

        // Return the User object, which will be null if any of the checks failed.
        return u;
    }


    /**
     * This method is responsible by find one user by id.
     *
     * @param id the id of the user.
     * @return the user.
     */
    public User getUserById(long id) {
        //Retrieve the user from the database
        UserEntity userEntity = userDao.findUserById(id);
        //Check if the user exists
        if (userEntity != null) {
            //If exists convert the user entity to a user dto
            return convertUserEntityToDto(userEntity);
        }
        //If the user does not exist return null
        return null;
    }

    /**
     * This method is responsible by find one user by email
     *
     * @param email the email of the user
     * @return the user
     */
    public User getUserByEmail(String email) {
        // Initialize a User object to null.
        User u = null;
        // Check if the provided email is not null.
        if (email != null) {
            // Use the userDao to find the user associated with the email.
            UserEntity userEntity = userDao.findUserByEmail(email);

            // Check if the UserEntity object is not null.
            if (userEntity != null) {
                // Convert the UserEntity object to a User DTO (Data Transfer Object).
                u = convertUserEntityToDto(userEntity);
            }

        }
        // Return the User object, which will be null if any of the checks failed.
        return u;
    }


    /**
     * This method is responsible by register a new user.
     * It verifies if the email already exists, if the password is valid and if the email is valid.
     *
     * @param email           the email of the user.
     * @param password        the password of the user.
     * @param confirmPassword the confirmation of the password. It should match the password parameter.
     * @return A boolean value indicating whether the registration was successful.Returns false if the email already exists, the password is not valid, or the email is not valid. Returns true if the user was successfully registered.
     */
    public boolean registerUser(String email, String password, String confirmPassword) {

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
        if (!isValidemail(email)) {
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

    /**
     * This method is responsible for logging in a user.
     *
     * @param email    The email of the user trying to login. It should be a valid email.
     * @param password The password of the user trying to login. It should be a valid password.
     * @return A string value representing the token of the user. Returns null if the email does not exist, the user is not active, or the password is incorrect.
     */
    public String loginUser(String email, String password, HttpServletRequest request) {
        //Retrieve the user associated with the provided email
        UserEntity user = userDao.findUserByEmail(email);
        //Check if the user exists and is active
        if (user != null && user.isActiveState()) {

            //Library oh Crypt to check if the password is correct
            //If the password is correct returns true
            if (BCrypt.checkpw(password, user.getPassword())) {
                String token = UUID.randomUUID().toString();
                //Create a new session
                SessionEntity sessionEntity = new SessionEntity();
                sessionEntity.setUser(user);
                sessionEntity.setToken(token);
                sessionEntity.setInitSession(LocalDateTime.now());
                sessionDao.create(sessionEntity);

                // Return the token session
                return token;
            }
        }
        //Return null if the user does not exist, the user is not active, or the password is incorrect
        return null;
    }


    /**
     * This method is responsible for confirming a user´s account.
     *
     * @param user  The user to be confirmed. It should have the first name, last name, lab, and visibility state fields filled.
     * @param token The confirmation token of the user. It should be a valid token.
     * @return A boolean value indicating whether the confirmation was successful. Returns false if the required fields are not present, the nickname already exists, the lab is not valid, or the token is not valid. Returns true if the user was successfully confirmed.
     */
    public boolean confirmUser(User user, String token, String lab) {

        // Check if the required fields are present
        if (user.getFirstName() == null || user.getLastName() == null) {
            return false;
        }
        //Check if the nickname already exists
        if (!user.getNickname().isEmpty()) {
            if (nicknameExists(user.getNickname())) {
                return false;
            }
        }

        //Find the lab by name
        LabEntity labEntity = labDao.findLabByName(lab);

        //Check if the lab is valid
        if (labEntity == null) {
            System.out.println(labEntity.getName());
            return false;
        }
        //Check if the token is valid and belongs to the user
        UserEntity userConfirm = authenticationDao.findUserByAuthenticationToken(token);

        //Check if the user exists
        if (userConfirm == null) {
            return false;
        }

        //Update the user´s fields
        userConfirm.setActiveState(true);
        userConfirm.setFirstName(user.getFirstName());
        userConfirm.setLastName(user.getLastName());
        userConfirm.setLab(labEntity);
        userConfirm.setVisibilityState(user.isVisibilityState());

        //Check if the biography, photo and nickname are present
        //If they are present, update the user´s fields
        if (user.getBiography() != null) {
            userConfirm.setBiography(user.getBiography());
        }

        if (user.getNickname() != null) {
            userConfirm.setNickname(user.getNickname());
        }
        if (user.getPhoto() != null) {
            userConfirm.setPhoto(user.getPhoto());
        }

        //Update the user in the database
        userDao.updateUser(userConfirm);

        //Update the authentication in the database
        AuthenticationEntity authentication = authenticationDao.findAuthenticationLineByTokenConfirmation(token);
        authentication.setAuthState(true);
        authentication.setAuthenticationToken(null);
        authenticationDao.update(authentication);

        return true;
    }

    /**
     * This method is responsible for changing a user´s password.
     *
     * @param resetPassToken  The reset password token of the user. It should be a valid token that was previously sent to the user's email.
     * @param password        The new password of the user. It should be a valid password according to the system's password policy.
     * @param confirmPassword The confirmation of the new password. It should match the password parameter.
     * @return A boolean value indicating whether the password change was successful. Returns false if the reset password token is not valid, the password is not valid, or the confirmation password does not match the password. Returns true if the password was successfully changed.
     */
    public boolean changePassword(String resetPassToken, String password, String confirmPassword) {
        //Retrieve the authentication entity associated with the provided resetPassToken
        AuthenticationEntity authenticationEntity = authenticationDao.findAuthenticationLineByrestPassToken(resetPassToken);
        System.out.println(authenticationEntity.getResetPassToken());

        // Check if the authentication entity exists
        if (authenticationEntity != null) {
            //Retrieve the user associated with the provided resetPassToken
            UserEntity user = authenticationDao.findUserByresetPassToken(resetPassToken);

            //Check if the provided password is valid and matches the confirmation password
            if (isPasswordValid(password, confirmPassword)) {
                //Encrypt the new password
                String encryptedPassword = encryptPassword(password);
                //Set the new password for the user
                user.setPassword(encryptedPassword);
                //Update the user in the database
                userDao.updateUser(user);
                //Nullify the resetPassToken in the authentication table
                authenticationEntity.setResetPassToken(null);
                //Update the authentication in the database
                authenticationDao.update(authenticationEntity);
                return true;
            }
        }
        return false;
    }

    /**
     * This method is responsible for initiating the password recovery process for a user.
     *
     * @param email The email of the user who wants to recover their password. It should be a valid email that exists in the system.
     * @return A boolean value indicating whether the password recovery process was successfully initiated. Returns false if the email does not exist or the user is not active. Returns true if the password recovery process was successfully initiated.
     */
    public boolean recoveryPassword(String email) {
        //Retrive the user associated with the provided email
        UserEntity user = userDao.findUserByEmail(email);
        //Check if the user exists and is active
        if (user != null && user.isActiveState()) {
            //Generate a reset password token
            String resetPassToken = UUID.randomUUID().toString();
            //Retrieve the aithentication entity associated with the user
            AuthenticationEntity authenticationEntity = authenticationDao.findAuthenticationByUser(user);
            //Set the reset password token in the authentication entity
            authenticationEntity.setResetPassToken(resetPassToken);
            //Update the authentication in the database
            authenticationDao.update(authenticationEntity);
            //Send the confirmation email to the user
            sendConfirmationEmailToRecoveryPassword("proj_final_aor@outlook.com", resetPassToken);
            return true;
        }
        return false;
    }

    /**
     * This method is responsible for sending a confirmation email to a user who wants to recover their password.
     *
     * @param to             The email of the user who wants to recover their password. It should be a valid email that exists in the system.
     * @param resetPassToken The reset password token of the user. It should be a valid token that was previously sent to the user's email.
     */
    public void sendConfirmationEmailToRecoveryPassword(String to, String resetPassToken) {
        //Sending the confirmation email
        String subject = "Password Recovery ";

        // Construct the email body with HTML for a professional tone and formatting
        StringBuilder body = new StringBuilder();
        body.append("<html>");
        body.append("<body>");
        body.append("<h1>Welcome to Innovation Lab Management!</h1>");
        body.append("<p>Dear User,</p>");
        body.append("<p>You requested to recover your password. Please click the link below to recover your password:</p>");
        body.append("<p><a href=\"https://localhost:3000/change-password?token=" + resetPassToken + "\">Recover Password</a></p>");
        body.append("<p>If you did not request to recover your password, please ignore this email.</p>");
        body.append("<p>Best regards,<br>Critical Software</p>");
        body.append("</body>");
        body.append("</html>");

        //Send the email
        emailService.sendEmail(to, subject, body.toString());
        logger.info("Sending confirmation email to " + to + " with token " + resetPassToken);
    }

    /**
     * This method is responsible for logging out a user.
     *
     * @param token The session token of the user trying to log out. It should be a valid token that was previously assigned to the user upon login.
     * @return A boolean value indicating whether the logout was successful. Returns false if the session token does not exist. Returns true if the user was successfully logged out.
     */
    public boolean logoutUser(String token) {
        //Retrieve the session associated with the provided token
        SessionEntity session = sessionDao.findSessionByToken(token);
        //Check if the session exists
        if (session != null) {
            //Set the end session time to the current time
            session.setEndSession(LocalDateTime.now());
            //Nullify the session token in the database
            session.setToken(null);
            sessionDao.update(session);
            return true;
        }
        return false;
    }

    /**
     * This method is responsible for verifying if a nickname already exists in the system.
     *
     * @param nickname The nickname to verify. It should be a valid nickname.
     * @return A boolean value indicating whether the nickname already exists. Returns true if the nickname already exists. Returns false if the nickname does not exist.
     */
    public boolean nicknameExists(String nickname) {
        //Retrieve the user associated with the provided nickname
        UserEntity user = userDao.findUserByNickname(nickname);
        //Check if the user exists
        if (user != null) {
            //If the user exists, the nickname already exists, return true
            return true;
        }
        //If the user does not exist, the nickname does not exist, return false
        return false;
    }

    /**
     * This method is responsible for sending a confirmation email to a user.
     *
     * @param to    The email of the user to send the confirmation email. It should be a valid email.
     * @param token The confirmation token of the user. It should be a valid token.
     */
    public void sendConfirmationEmail(String to, String token) {
        //Sending the confirmation email
        logger.info("Sending confirmation email to " + to + " with token " + token);
        String subject = "Account Confirmation ";


        // Construct the email body with HTML
        StringBuilder body = new StringBuilder();
        body.append("<html>");
        body.append("<body>");
        body.append("<h1>Welcome to Innovation Lab Management!</h1>");
        body.append("<p>Dear User,</p>");
        body.append("<p>Thank you for registering with us. We're excited to have you on board.</p>");
        body.append("<p>To complete your registration, please click the link below to confirm your account:</p>");
        body.append("<p><a href=\"https://localhost:3000/confirm-account?token=" + token + "\">Confirm Account</a></p>");
        body.append("<p>If you did not register for our service, please ignore this email.</p>");
        body.append("<p>Best regards,<br>Critical Software</p>");
        body.append("</body>");
        body.append("</html>");

        //Send the email
        emailService.sendEmail(to, subject, body.toString());

    }

    /**
     * This method is responsible for verifying if an email is valid.
     *
     * @param email The email to verify. It should be a valid email.
     * @return A boolean value indicating whether the email is valid. Returns true if the email is valid. Returns false if the email is not valid.
     */
    public boolean isValidemail(String email) {
        //Verifying if the email exists
        if (email == null) {
            return false;
        }
        //Verifying if the email is valid
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            return false;
        }
        return true;
    }


    /**
     * This method is responsible for verifying if a password is valid.
     * It verifies if the password and the confirmPassword are the same, if the password has at least 8 characters, if the password has at least one uppercase letter, one lowercase letter, one number, and one special character.
     *
     * @param password        The password to verify. It should be a valid password.
     * @param confirmPassword The confirmation of the password. It should match the password parameter.
     * @return
     */
    public boolean isPasswordValid(String password, String confirmPassword) {

        //Verifying if the password and the confirmPassword are the same
        if (!password.equals(confirmPassword)) {
            return false;
        }
        //Verifying if the password has at least 8 characters
        if (password.length() < 8) {
            return false;
        }
        // Verifying if the password has at least one uppercase letter, one lowercase letter, one number and one special character
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        if (!password.matches(".*[0-9].*")) {
            return false;
        }
        if (!password.matches(".*[!@#$%^&*].*")) {
            return false;
        }

        return true;
    }

    /**
     * This method is responsible for encrypting a password.
     * It uses the BCrypt library to encrypt the password. The encrypted password is a string value.
     *
     * @param password The password to encrypt. It should be a valid password.
     * @return A string value representing the encrypted password.
     */
    public String encryptPassword(String password) {
        if(password == null){
            throw new NullPointerException("Password cannot be null");
        }
        // Generate a salt with a log2 value of 12
        String salt = BCrypt.gensalt(12);
        String hashedPassword = BCrypt.hashpw(password, salt);
        //Return the encrypted password
        return hashedPassword;
    }

    //Methods for update user´s fields

    /**
     * This method is responsible for updating a user´s fields.
     * It verifies if the user exists, if the fields are valid, and if the nickname already exists.
     *
     * @param user   The user to update. It should be a valid user.
     * @param userId The id of the user to update. It should be a valid id.
     * @return A boolean value indicating whether the update was successful. Returns false if the user does not exist, the nickname already exists, or the fields are not valid. Returns true if the user was successfully updated.
     */
    public boolean updateUser(User user, long userId) {
        //Retrieve the user from the database
        UserEntity userEntity = userDao.findUserById(userId);
        //Check if the user exists
        if (userEntity == null) {
            logger.warn("User not found with ID: " + user.getId());
            return false;
        }

        //Check if the first name is not null
        if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
            logger.info("Updating first name to: " + user.getFirstName());
            userEntity.setFirstName(user.getFirstName());
        }

        //Check if the last name is not null
        if (user.getLastName() != null && !user.getLastName().isEmpty()) {
            logger.info("Updating last name to: " + user.getLastName());
            userEntity.setLastName(user.getLastName());
        }
        // Check if the lab name is not null
        if (user.getLab() != null && user.getLab().getName() != null) {
            // Find the lab entity by name
            LabEntity labEntity = labDao.findLabByName(String.valueOf(user.getLab().getName()));
            if (labEntity == null) {
                logger.warn("Lab not found with name: " + user.getLab().getName());
                return false;
            }
            // Assign the lab entity to the user
            userEntity.setLab(labEntity);
        }

        //Check if the photo field is not null
        if (user.getPhoto() != null) {
            userEntity.setPhoto(user.getPhoto());
        }
        //Check if the nickname is not null and if is a valid nickname
        if (user.getNickname() != null && !user.getNickname().isEmpty()) {
            if (nicknameExists(user.getNickname())) {
                logger.warn("Nickname already exists: " + user.getNickname());
                return false;
            } else {
                userEntity.setNickname(user.getNickname());
            }
        }

        try {
            //Update the user in the database
            userDao.updateUser(userEntity);
            logger.info("User updated successfully: " + user.getId());
            return true;
        } catch (Exception e) {
            logger.error("Failed to update user: " + user.getId(), e);
            return false;
        }
    }

    /**
     * This method is responsible for updating the visibility of a user profile.
     *
     * @param userId     The id of the user to update. It should be a valid id.
     * @param visibility The visibility state of the user profile. It should be a valid visibility state.
     * @return A boolean value indicating whether the update was successful. Returns false if the user does not exist. Returns true if the user visibility was successfully updated.
     */
    public boolean updateVisibility(long userId, boolean visibility) {
        if(userId == 0){
            throw new IllegalArgumentException("User ID cannot be 0");
        }
        //Retrieve the user from the database
        UserEntity userEntity = userDao.findUserById(userId);
        //Check if the user exists
        if (userEntity == null) {
            logger.warn("User not found with ID: " + userId);
            return false;
        }
        //Update the visibility state of the user
        userEntity.setVisibilityState(visibility);
        try {
            //Update the user in the database
            userDao.updateUser(userEntity);
            logger.debug("User visibility updated successfully: " + userId);
            return true;
        } catch (Exception e) {
            logger.debug("Failed to update user visibility: " + userId, e);
            return false;
        }
    }


    /**
     * This method is responsible for updating the user´s biography.
     *
     * @param biography The biography of the user. It should be a valid biography.
     * @param userId    The id of the user to update. It should be a valid id.
     * @return A boolean value indicating whether the update was successful.
     * Returns false if the user does not exist or the biography is not valid.
     * Returns true if the user biography was successfully updated.
     */
    public boolean updateBiography(String biography, long userId) {
        //Retrieve the user from the database
        UserEntity userEntity = userDao.findUserById(userId);

        if(userId == 0){
            throw new IllegalArgumentException("User ID cannot be 0");
        }
        //Check if the user exists
        if (userEntity == null) {
            logger.warn("User not found with ID: " + userId);
            return false;
        }
        //Check if the biography is not null
        if (!biography.isEmpty()) {
            logger.info("Updating biography to: " + userEntity.getBiography());

            userEntity.setBiography(biography);
        }
        try {
            //Update the user in the database
            userDao.updateUser(userEntity);
            logger.info("User updated successfully: " + userEntity.getId());
            return true;
        } catch (Exception e) {
            logger.error("Failed to update user: " + userId, e);
            return false;
        }
    }


    /**
     * This method is responsible for create a user ADMIN when the system starts.
     */
    public void createAdminUser() {
        // Check if the admin user already exists
        UserEntity admin = userDao.findUserByEmail("admin@example.com");
        // If the admin user does not exist, create it
        if (admin == null) {
            // Create the admin user
            UserEntity newAdmin = new UserEntity();
            newAdmin.setEmail("admin@example.com");
            newAdmin.setPassword(encryptPassword("Password1234%"));
            newAdmin.setUserType(UserType.ADMIN);
            newAdmin.setActiveState(true);
            LabEntity lab = new LabEntity();
            lab.setId(1);
            newAdmin.setLab(lab);
            // Persist the admin user in the database
            userDao.persist(newAdmin);
        }
    }

    /**
     * This method is responsible for finding all users by first name, last name or nickname.
     *
     * @param token
     * @param prefix
     * @return
     */
    public List<User> getUsersByFirstName(String token, String prefix) {

        // Find the user associated with the provided token
        UserEntity userRequested = sessionDao.findUserByToken(token);

        List<User> users = new ArrayList<>();
        // Find all users by first name, last name or nickname
        List<UserEntity> userEntities = userDao.findUsersByFirstNameStartingWith(userRequested.getId(), prefix);

        // Convert the UserEntity objects to User DTOs
        if (userEntities != null) {
            for (UserEntity userEntity : userEntities) {
                User user = convertUserEntityToDto(userEntity);
                users.add(user);

            }
        }

        // Return the list of User DTOs
        return users;
    }


    /**
     * This method is responsible for finding all users.
     *
     * @return
     */
    public List<UserEntity> getAllUsers() {
        return userDao.findAllActiveUsers();
    }

    /**
     * This method is responsible for finding a user by id.
     *
     * @param id The id of the user to find. It should be a valid id.
     * @return A UserEntity object representing the user found. Returns null if the user does not exist.
     */
    public UserEntity findUserById(long id) {
        return userDao.findUserById(id);
    }


    /**
     * This method is responsible for converting a UserDto object to a UserEntity object.
     *
     * @param user The UserDto object to convert. It should be a valid UserDto object.
     * @return A UserEntity object representing the converted UserDto object.
     */
    public UserEntity convertUserDtoToEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setActiveState(user.isActiveState());
        userEntity.setUserType(user.getUserType());
        userEntity.setBiography(user.getBiography());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLab(labDao.findLabByUserId(user.getId()));
        return userEntity;
    }

    /**
     * This method is responsible for converting a UserEntity object to a UserDto object.
     *
     * @param userEntity The UserEntity object to convert. It should be a valid UserEntity object.
     * @return A UserDto object representing the converted UserEntity object.
     */
    public User convertUserEntityToDto(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setEmail(userEntity.getEmail());
        user.setActiveState(userEntity.isActiveState());
        user.setUserType(userEntity.getUserType());
        user.setBiography(userEntity.getBiography());
        user.setFirstName(userEntity.getFirstName());
        user.setLastName(userEntity.getLastName());
        user.setNickname(userEntity.getNickname());
        user.setPhoto(userEntity.getPhoto());
        user.setVisibilityState(userEntity.isVisibilityState());

        Lab lab = new Lab();
        lab.setId(userEntity.getLab().getId());
        lab.setName(userEntity.getLab().getName());
        user.setLab(lab);

        return user;
    }

    /**
     * This method is responsible for converting a UserEntity object to a UserDto object.
     * Just send the minimal information for the user nedded in the message.
     *
     * @param userEntity The UserEntity object to convert. It should be a valid UserEntity object.
     * @return A UserDto object representing the converted UserEntity object.
     */
    public MessageInfoUser convertUserToDTOForMessage(UserEntity userEntity) {
        MessageInfoUser user = new MessageInfoUser();
        user.setId(userEntity.getId());
        user.setFirstName(userEntity.getFirstName());
        user.setLastName(userEntity.getLastName());
        user.setPhoto(userEntity.getPhoto());
        return user;
    }

    /**
     * This method is responsible for converting a UserEntity object to a UserInfoInProject object.
     *
     * @param userEntity        The UserEntity object to convert. It should be a valid UserEntity object.
     * @param userTypeInProject The user type in the project. It should be a valid user type in the project.
     * @return A UserInfoInProject object representing the converted UserEntity object.
     */
    public UserInfoInProject convertToDTOWithType(UserEntity userEntity, UserTypeInProject userTypeInProject) {
        UserInfoInProject userInfoInProject = new UserInfoInProject();
        userInfoInProject.setUserId(userEntity.getId());
        userInfoInProject.setFirstName(userEntity.getFirstName());
        userInfoInProject.setLastName(userEntity.getLastName());
        userInfoInProject.setPhoto(userEntity.getPhoto());
        userInfoInProject.setUserType(userTypeInProject);
        userInfoInProject.setVisibility(userEntity.isVisibilityState());
        return userInfoInProject;
    }

    /**
     * This method is responsible for converting a UserEntity object to a UserInfoInProject object.
     *
     * @param userEntity The UserEntity object to convert. It should be a valid UserEntity object.
     * @return A UserInfoInProject object representing the converted UserEntity object.
     */
    public UserInfoInProject convertToDTO(UserEntity userEntity) {
        UserInfoInProject userInfoInProject = new UserInfoInProject();
        userInfoInProject.setUserId(userEntity.getId());
        userInfoInProject.setFirstName(userEntity.getFirstName());
        userInfoInProject.setLastName(userEntity.getLastName());
        userInfoInProject.setPhoto(userEntity.getPhoto());
        return userInfoInProject;
    }


}
