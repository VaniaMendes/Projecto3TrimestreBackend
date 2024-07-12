package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.ProjectDao;
import aor.paj.proj_final_aor_backend.dao.SessionDao;
import aor.paj.proj_final_aor_backend.dao.SkillDao;
import aor.paj.proj_final_aor_backend.dto.Lab;
import aor.paj.proj_final_aor_backend.dto.Project;
import aor.paj.proj_final_aor_backend.entity.LabEntity;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import aor.paj.proj_final_aor_backend.util.enums.ProjectActivityType;
import aor.paj.proj_final_aor_backend.util.enums.Workplace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectBeanTest {

    @InjectMocks
    private ProjectBean projectBean;

    @Mock
    private ProjectDao projectDao;

    @Mock
    private LabBean labBean;
    @Mock
    private UserProjectBean userProjectBean;
    @Mock
    private SessionDao sessionDao;
    @Mock
    private ActivityBean activityBean;
    @Mock
    private SkillDao skillDao;
    @Mock
    private ProjectSkillBean projectSkillBean;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }




    @Test
    void updateDescription_returnsFalseWhenProjectNotFound() {
        when(projectDao.findProjectById(1L)).thenReturn(null);

        boolean result = projectBean.updateDescription(1L, "New Description", "validToken");

        assertFalse(result);
    }

    @Test
    void updateDescription_returnsFalseWhenDescriptionIsNull() {
        ProjectEntity projectEntity = new ProjectEntity();
        when(projectDao.findProjectById(1L)).thenReturn(projectEntity);

        boolean result = projectBean.updateDescription(1L, null, "validToken");

        assertFalse(result);
    }

    @Test
    void updateDescription_returnsFalseWhenDescriptionIsEmpty() {
        ProjectEntity projectEntity = new ProjectEntity();
        when(projectDao.findProjectById(1L)).thenReturn(projectEntity);

        boolean result = projectBean.updateDescription(1L, "", "validToken");

        assertFalse(result);
    }



    @Test
    void getProjectById_returnsNull_whenProjectDoesNotExist() {
        Long projectId = 1L;
        when(projectDao.findProjectById(projectId)).thenReturn(null);

        Project result = projectBean.getProjectById(projectId);

        assertNull(result);
        verify(projectDao).findProjectById(projectId);
    }


    @Test
    void joinSkill_returnsFalse_whenProjectDoesNotExist() {
        Long projectId = 1L;
        Long skillId = 1L;

        when(projectDao.findProjectById(projectId)).thenReturn(null);

        boolean result = projectBean.joinSkill(projectId, skillId);

        assertFalse(result);
    }
    @Test
    void findProject_returnsProject_whenProjectExists() {
        Long projectId = 1L;
        ProjectEntity projectEntity = new ProjectEntity();
        when(projectDao.findProjectById(projectId)).thenReturn(projectEntity);

        ProjectEntity result = projectBean.findProject(projectId);

        assertNotNull(result);
        assertEquals(projectEntity, result);
        verify(projectDao).findProjectById(projectId);
    }

    @Test
    void findProject_returnsNull_whenProjectDoesNotExist() {
        Long projectId = 1L;
        when(projectDao.findProjectById(projectId)).thenReturn(null);

        ProjectEntity result = projectBean.findProject(projectId);

        assertNull(result);
        verify(projectDao).findProjectById(projectId);
    }

    @Test
    void countProjects_returnsAllProjectsCount_whenStateIsOne() {
        when(projectDao.countAllProjects()).thenReturn(5);

        Integer result = projectBean.countProjects(1);

        assertEquals(5, result);
        verify(projectDao).countAllProjects();
    }

    @Test
    void countProjects_returnsStateSpecificProjectsCount_whenStateIsNotOne() {
        when(projectDao.countProjectsByState(2)).thenReturn(3);

        Integer result = projectBean.countProjects(2);

        assertEquals(3, result);
        verify(projectDao).countProjectsByState(2);
    }



}