package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.SkillDao;
import aor.paj.proj_final_aor_backend.dto.Skill;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import aor.paj.proj_final_aor_backend.util.enums.SkillType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SkillBeanTest {

    @InjectMocks
    SkillBean skillBean;

    @Mock
    SkillDao skillDao;

    @Mock
    UserBean userBean;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testCreateNewSkill_skillExists() {
        Skill skill = new Skill();
        skill.setName("existingSkill");
        when(skillDao.findSkillByName(anyString())).thenReturn(new SkillEntity());
        assertFalse(skillBean.createNewSkill("token", skill));
    }

    @Test
    void testCreateNewSkill_success() {
        Skill skill = new Skill();
        skill.setName("newSkill");
        skill.setType(SkillType.CONHECIMENTO);
        when(skillDao.findSkillByName(anyString())).thenReturn(null);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        User user = userBean.convertUserEntityToDto(userEntity);
        when(userBean.getUserByToken("token")).thenReturn(user);

        // Check if user is not null before calling createNewSkill
        if (user != null) {
            assertTrue(skillBean.createNewSkill("token", skill));
        } else {
            fail("User is null");
        }
    }
}
