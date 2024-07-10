package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.AuthenticationDao;
import aor.paj.proj_final_aor_backend.dao.UserDao;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import aor.paj.proj_final_aor_backend.util.EmailServiceHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserBeanTest {

    @InjectMocks
    UserBean userBean;

    @Mock
    UserDao userDao;
    @Mock
    AuthenticationDao authenticationDao;
    @Mock
    EmailServiceHelper emailServiceHelper;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerUser_withValidInput_returnsTrue() {
        when(userDao.findUserByEmail(anyString())).thenReturn(null);

        boolean result = userBean.registerUser("taest@test.com", "Paassword1!", "Paassword1!");

        assertTrue(result);
        verify(userDao, times(1)).createUser(any(UserEntity.class));
    }

    @Test
    public void registerUser_withExistingEmail_returnsFalse() {
        when(userDao.findUserByEmail(anyString())).thenReturn(new UserEntity());

        //User with the same email already exists
        boolean result = userBean.registerUser("test@test.com", "Password1!", "Password1!");

        assertFalse(result);
    }

    @Test
    public void registerUser_withInvalidPassword_returnsFalse() {
        when(userDao.findUserByEmail(anyString())).thenReturn(null);

        boolean result = userBean.registerUser("test1@test.com", "password", "password");

        assertFalse(result);
    }

    @Test
    public void registerUser_withInvalidEmail_returnsFalse() {
        boolean result = userBean.registerUser("test", "Password1!", "Password1!");

        assertFalse(result);
    }

    @Test
    public void registerUser_withNonMatchingPasswords_returnsFalse() {
        when(userDao.findUserByEmail(anyString())).thenReturn(null);

        boolean result = userBean.registerUser("test@test.com", "Password1!", "Password2!");

        assertFalse(result);
    }
    @Test
    void passwordValidation_withValidPassword_returnsTrue() {
        boolean result = userBean.isPasswordValid("Password1!", "Password1!");
        assertTrue(result);
    }

    @Test
    void passwordValidation_withMismatchedPasswords_returnsFalse() {
        boolean result = userBean.isPasswordValid("Password1!", "Password2!");
        assertFalse(result);
    }

    @Test
    void passwordValidation_withShortPassword_returnsFalse() {
        boolean result = userBean.isPasswordValid("Pass1!", "Pass1!");
        assertFalse(result);
    }

    @Test
    void passwordValidation_withoutUppercase_returnsFalse() {
        boolean result = userBean.isPasswordValid("password1!", "password1!");
        assertFalse(result);
    }

    @Test
    void passwordValidation_withoutLowercase_returnsFalse() {
        boolean result = userBean.isPasswordValid("PASSWORD1!", "PASSWORD1!");
        assertFalse(result);
    }

    @Test
    void passwordValidation_withoutNumber_returnsFalse() {
        boolean result = userBean.isPasswordValid("Password!", "Password!");
        assertFalse(result);
    }

    @Test
    void passwordValidation_withoutSpecialCharacter_returnsFalse() {
        boolean result = userBean.isPasswordValid("Password1", "Password1");
        assertFalse(result);
    }


    @Test
    void encryptPassword_withValidPassword_returnsEncryptedPassword() {
        String password = "Password1!";
        String encryptedPassword = userBean.encryptPassword(password);

        assertTrue(BCrypt.checkpw(password, encryptedPassword));
    }

    @Test
    void encryptPassword_withEmptyPassword_returnsEncryptedPassword() {
        String password = "";
        String encryptedPassword = userBean.encryptPassword(password);

        assertTrue(BCrypt.checkpw(password, encryptedPassword));
    }

    @Test
    void encryptPassword_withNullPassword_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> userBean.encryptPassword(null));
    }


    @Test
    void updateVisibility_withExistingUser_updatesVisibility() {
        long userId = 1L;
        when(userDao.findUserById(userId)).thenReturn(new UserEntity());

        boolean result = userBean.updateVisibility(userId, true);

        assertTrue(result);
        verify(userDao, times(1)).updateUser(any(UserEntity.class));
    }

    @Test
    void updateVisibility_withNonExistingUser_returnsFalse() {
        long userId = 1L;
        when(userDao.findUserById(userId)).thenReturn(null);

        boolean result = userBean.updateVisibility(userId, true);

        assertFalse(result);
        verify(userDao, never()).updateUser(any(UserEntity.class));
    }

    @Test
    void updateVisibility_withDatabaseError_returnsFalse() {
        long userId = 1L;
        when(userDao.findUserById(userId)).thenReturn(new UserEntity());
        doThrow(RuntimeException.class).when(userDao).updateUser(any(UserEntity.class));

        boolean result = userBean.updateVisibility(userId, true);

        assertFalse(result);
    }


    @Test
    void updateBiography_withExistingUser_updatesBiography() {
        long userId = 1L;
        String newBiography = "New biography text";
        when(userDao.findUserById(userId)).thenReturn(new UserEntity());

        boolean result = userBean.updateBiography(newBiography, userId);

        assertTrue(result);
        verify(userDao, times(1)).updateUser(any(UserEntity.class));
    }

    @Test
    void updateBiography_withNonExistingUser_returnsFalse() {
        long userId = 1L;
        String newBiography = "New biography text";
        when(userDao.findUserById(userId)).thenReturn(null);

        boolean result = userBean.updateBiography(newBiography, userId);

        assertFalse(result);
        verify(userDao, never()).updateUser(any(UserEntity.class));
    }

    @Test
    void updateBiography_withDatabaseError_returnsFalse() {
        long userId = 1L;
        String newBiography = "New biography text";
        when(userDao.findUserById(userId)).thenReturn(new UserEntity());
        doThrow(RuntimeException.class).when(userDao).updateUser(any(UserEntity.class));

        boolean result = userBean.updateBiography(newBiography, userId);

        assertFalse(result);
    }

    @Test
    void updateBiography_withEmptyBiography_doesNotUpdateBiography() {
        long userId = 1L;
        String newBiography = "";
        UserEntity userEntity = new UserEntity();
        userEntity.setBiography("Old biography text");
        when(userDao.findUserById(userId)).thenReturn(userEntity);

        boolean result = userBean.updateBiography(newBiography, userId);

        assertTrue(result);
        assertEquals("Old biography text", userEntity.getBiography());
        verify(userDao, times(1)).updateUser(any(UserEntity.class));
    }

}
