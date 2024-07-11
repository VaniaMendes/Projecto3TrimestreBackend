package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.SkillDao;
import aor.paj.proj_final_aor_backend.dao.UserDao;
import aor.paj.proj_final_aor_backend.dao.UserSkillDao;
import aor.paj.proj_final_aor_backend.dto.Skill;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import aor.paj.proj_final_aor_backend.entity.UserSkillEntity;
import aor.paj.proj_final_aor_backend.util.enums.SkillType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SkillBeanTest {

    @InjectMocks
    SkillBean skillBean;

    @Mock
    SkillDao skillDao;

    @Mock
    UserBean userBean;
    @Mock
    UserDao userDao;
    @Mock
    UserSkillDao userSkillDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void softDeleteSkill_withValidUserAndSkill_softDeletesSkill() {
        long userId = 1L;
        long skillId = 1L;
        when(userDao.findUserById(userId)).thenReturn(new UserEntity());
        when(skillDao.find(skillId)).thenReturn(new SkillEntity());
        when(userSkillDao.findUserSkillByUserAndSkill(userId, skillId)).thenReturn(new UserSkillEntity());

        boolean result = skillBean.softDeleteSkill(userId, skillId);

        assertTrue(result);
        verify(userSkillDao, times(1)).updateUserSkill(any(UserSkillEntity.class));
    }

    @Test
    void softDeleteSkill_withNonExistingUser_returnsFalse() {
        long userId = 1L;
        long skillId = 1L;
        when(userDao.findUserById(userId)).thenReturn(null);

        boolean result = skillBean.softDeleteSkill(userId, skillId);

        assertFalse(result);
        verify(userSkillDao, never()).updateUserSkill(any(UserSkillEntity.class));
    }

    @Test
    void softDeleteSkill_withNonExistingSkill_returnsFalse() {
        long userId = 1L;
        long skillId = 1L;
        when(userDao.findUserById(userId)).thenReturn(new UserEntity());
        when(skillDao.find(skillId)).thenReturn(null);

        boolean result = skillBean.softDeleteSkill(userId, skillId);

        assertFalse(result);
        verify(userSkillDao, never()).updateUserSkill(any(UserSkillEntity.class));
    }

    @Test
    void softDeleteSkill_withNonExistingUserSkill_returnsFalse() {
        long userId = 1L;
        long skillId = 1L;
        when(userDao.findUserById(userId)).thenReturn(new UserEntity());
        when(skillDao.find(skillId)).thenReturn(new SkillEntity());
        when(userSkillDao.findUserSkillByUserAndSkill(userId, skillId)).thenReturn(null);

        boolean result = skillBean.softDeleteSkill(userId, skillId);

        assertFalse(result);
        verify(userSkillDao, never()).updateUserSkill(any(UserSkillEntity.class));
    }

    @Test
    void softDeleteSkill_withDatabaseError_returnsFalse() {
        long userId = 1L;
        long skillId = 1L;
        when(userDao.findUserById(userId)).thenReturn(new UserEntity());
        when(skillDao.find(skillId)).thenReturn(new SkillEntity());
        when(userSkillDao.findUserSkillByUserAndSkill(userId, skillId)).thenReturn(new UserSkillEntity());
        doThrow(RuntimeException.class).when(userSkillDao).updateUserSkill(any(UserSkillEntity.class));

        boolean result = skillBean.softDeleteSkill(userId, skillId);

        assertFalse(result);
    }


    @Test
    void verifySkillExists_withExistingUserSkill_returnsTrue() {
        long userId = 1L;
        long skillId = 1L;
        when(userSkillDao.findUserSkillByUserAndSkill(userId, skillId)).thenReturn(new UserSkillEntity());

        boolean result = skillBean.verifySkillExists(userId, skillId);

        assertTrue(result);
    }

    @Test
    void verifySkillExists_withNonExistingUserSkill_returnsFalse() {
        long userId = 1L;
        long skillId = 1L;
        when(userSkillDao.findUserSkillByUserAndSkill(userId, skillId)).thenReturn(null);

        boolean result = skillBean.verifySkillExists(userId, skillId);

        assertFalse(result);
    }


    @Test
    void verifySkillIsInactive_withInactiveSkill_returnsTrue() {
        long userId = 1L;
        long skillId = 1L;
        UserSkillEntity userSkillEntity = new UserSkillEntity();
        userSkillEntity.setActive(false);
        when(userSkillDao.findUserSkillByUserAndSkill(userId, skillId)).thenReturn(userSkillEntity);

        boolean result = skillBean.verifyskillIsInative(userId, skillId);

        assertTrue(result);
    }

    @Test
    void verifySkillIsInactive_withActiveSkill_returnsFalse() {
        long userId = 1L;
        long skillId = 1L;
        UserSkillEntity userSkillEntity = new UserSkillEntity();
        userSkillEntity.setActive(true);
        when(userSkillDao.findUserSkillByUserAndSkill(userId, skillId)).thenReturn(userSkillEntity);

        boolean result = skillBean.verifyskillIsInative(userId, skillId);

        assertFalse(result);
    }

    @Test
    void verifySkillIsInactive_withNonExistingUserSkill_returnsFalse() {
        long userId = 1L;
        long skillId = 1L;
        when(userSkillDao.findUserSkillByUserAndSkill(userId, skillId)).thenReturn(null);

        boolean result = skillBean.verifyskillIsInative(userId, skillId);

        assertFalse(result);
    }

    @Test
    void associateSkillToUser_withValidUserAndSkill_associatesSkill() {
        long userId = 1L;
        long skillId = 1L;
        when(userDao.findUserById(userId)).thenReturn(new UserEntity());
        when(skillDao.find(skillId)).thenReturn(new SkillEntity());
        when(userSkillDao.findUserSkillByUserAndSkill(userId, skillId)).thenReturn(null);

        boolean result = skillBean.associateSkillToUser(userId, skillId);

        assertTrue(result);
        verify(userSkillDao, times(1)).persist(any(UserSkillEntity.class));
    }

    @Test
    void associateSkillToUser_withNonExistingUser_returnsFalse() {
        long userId = 1L;
        long skillId = 1L;
        when(userDao.findUserById(userId)).thenReturn(null);

        boolean result = skillBean.associateSkillToUser(userId, skillId);

        assertFalse(result);
        verify(userSkillDao, never()).persist(any(UserSkillEntity.class));
    }

    @Test
    void associateSkillToUser_withNonExistingSkill_returnsFalse() {
        long userId = 1L;
        long skillId = 1L;
        when(userDao.findUserById(userId)).thenReturn(new UserEntity());
        when(skillDao.find(skillId)).thenReturn(null);

        boolean result = skillBean.associateSkillToUser(userId, skillId);

        assertFalse(result);
        verify(userSkillDao, never()).persist(any(UserSkillEntity.class));
    }



    @Test
    void associateSkillToUser_withInactiveUserSkill_activatesSkill() {
        long userId = 1L;
        long skillId = 1L;
        UserSkillEntity userSkillEntity = new UserSkillEntity();
        userSkillEntity.setActive(false);
        when(userDao.findUserById(userId)).thenReturn(new UserEntity());
        when(skillDao.find(skillId)).thenReturn(new SkillEntity());
        when(userSkillDao.findUserSkillByUserAndSkill(userId, skillId)).thenReturn(userSkillEntity);

        boolean result = skillBean.associateSkillToUser(userId, skillId);

        assertTrue(result);
        assertTrue(userSkillEntity.isActive());
        verify(userSkillDao, times(1)).updateUserSkill(any(UserSkillEntity.class));
    }


    @Test
    void getSkillsByUserId_withExistingUser_returnsSkills() {
        long userId = 1L;
        List<SkillEntity> skillEntities = new ArrayList<>();
        skillEntities.add(new SkillEntity());
        when(userSkillDao.findAllSkillsForUser(userId)).thenReturn(skillEntities);

        List<Skill> result = skillBean.getSkillsByUserId(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getSkillsByUserId_withNonExistingUser_returnsNull() {
        long userId = 1L;
        when(userSkillDao.findAllSkillsForUser(userId)).thenReturn(null);

        List<Skill> result = skillBean.getSkillsByUserId(userId);

        assertNull(result);
    }

    @Test
    void getSkillsByUserId_withUserHavingNoSkills_returnsEmptyList() {
        long userId = 1L;
        when(userSkillDao.findAllSkillsForUser(userId)).thenReturn(new ArrayList<>());

        List<Skill> result = skillBean.getSkillsByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
