package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.UserDao;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserBeanTest {

    @InjectMocks
    UserBean userBean;

    @Mock
    UserDao userDao;


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

}
