package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.InterestDao;
import aor.paj.proj_final_aor_backend.dao.UserDao;
import aor.paj.proj_final_aor_backend.dao.UserInterestDao;
import aor.paj.proj_final_aor_backend.dto.Interest;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.entity.InterestEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import aor.paj.proj_final_aor_backend.entity.UserInterestEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class InterestBeanTest {
    @InjectMocks
    InterestBean interestBean;

    @Mock
    UserBean userBean;

    @Mock
    InterestDao interestDao;
    @Mock
    UserDao userDao;

    @Mock
    UserInterestDao userInterestDao;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void interestAlreadyExists_withExistingInterest_returnsTrue() {
        String interestName = "Existing Interest";
        when(interestDao.findInterestByName(interestName)).thenReturn(new InterestEntity());

        boolean result = interestBean.interestAlreadyExists(interestName);

        assertTrue(result);
    }

    @Test
    void interestAlreadyExists_withNonExistingInterest_returnsFalse() {
        String interestName = "NonExisting Interest";
        when(interestDao.findInterestByName(interestName)).thenReturn(null);

        boolean result = interestBean.interestAlreadyExists(interestName);

        assertFalse(result);
    }

    @Test
    void userAlreadyHasInterest_withExistingUserInterest_returnsTrue() {
        long userId = 1L;
        long interestId = 1L;
        when(userInterestDao.findUserInterestByUserAndInterest(userId, interestId)).thenReturn(new UserInterestEntity());

        boolean result = interestBean.userAlreadyHasInterest(userId, interestId);

        assertTrue(result);
    }

    @Test
    void userAlreadyHasInterest_withNonExistingUserInterest_returnsFalse() {
        long userId = 1L;
        long interestId = 1L;
        when(userInterestDao.findUserInterestByUserAndInterest(userId, interestId)).thenReturn(null);

        boolean result = interestBean.userAlreadyHasInterest(userId, interestId);

        assertFalse(result);
    }

    @Test
    void userHasInterestInactive_withInactiveUserInterest_returnsTrue() {
        long userId = 1L;
        long interestId = 1L;
        UserInterestEntity userInterestEntity = new UserInterestEntity();
        userInterestEntity.setActive(false);
        when(userInterestDao.findUserInterestByUserAndInterest(userId, interestId)).thenReturn(userInterestEntity);

        boolean result = interestBean.userHasInterestInactive(userId, interestId);

        assertTrue(result);
    }

    @Test
    void userHasInterestInactive_withActiveUserInterest_returnsFalse() {
        long userId = 1L;
        long interestId = 1L;
        UserInterestEntity userInterestEntity = new UserInterestEntity();
        userInterestEntity.setActive(true);
        when(userInterestDao.findUserInterestByUserAndInterest(userId, interestId)).thenReturn(userInterestEntity);

        boolean result = interestBean.userHasInterestInactive(userId, interestId);

        assertFalse(result);
    }

    @Test
    void userHasInterestInactive_withNonExistingUserInterest_returnsFalse() {
        long userId = 1L;
        long interestId = 1L;
        when(userInterestDao.findUserInterestByUserAndInterest(userId, interestId)).thenReturn(null);

        boolean result = interestBean.userHasInterestInactive(userId, interestId);

        assertFalse(result);
    }

    @Test
    void removeInterestfromUser_withExistingUserAndInterest_setsInterestInactive() {
        long userId = 1L;
        long interestId = 1L;
        UserInterestEntity userInterestEntity = new UserInterestEntity();
        userInterestEntity.setActive(true);
        when(userDao.findUserById(userId)).thenReturn(new UserEntity());
        when(interestDao.find(interestId)).thenReturn(new InterestEntity());
        when(userInterestDao.findUserInterestByUserAndInterest(userId, interestId)).thenReturn(userInterestEntity);

        boolean result = interestBean.removeInterestfromUser(userId, interestId);

        assertTrue(result);
        assertFalse(userInterestEntity.isActive());
    }

    @Test
    void removeInterestfromUser_withNonExistingUser_returnsFalse() {
        long userId = 1L;
        long interestId = 1L;
        when(userDao.findUserById(userId)).thenReturn(null);

        boolean result = interestBean.removeInterestfromUser(userId, interestId);

        assertFalse(result);
    }

    @Test
    void removeInterestfromUser_withNonExistingInterest_returnsFalse() {
        long userId = 1L;
        long interestId = 1L;
        when(userDao.findUserById(userId)).thenReturn(new UserEntity());
        when(interestDao.find(interestId)).thenReturn(null);

        boolean result = interestBean.removeInterestfromUser(userId, interestId);

        assertFalse(result);
    }

    @Test
    void removeInterestfromUser_withNonAssociatedInterest_returnsFalse() {
        long userId = 1L;
        long interestId = 1L;
        when(userDao.findUserById(userId)).thenReturn(new UserEntity());
        when(interestDao.find(interestId)).thenReturn(new InterestEntity());
        when(userInterestDao.findUserInterestByUserAndInterest(userId, interestId)).thenReturn(null);

        boolean result = interestBean.removeInterestfromUser(userId, interestId);

        assertFalse(result);
    }

}
