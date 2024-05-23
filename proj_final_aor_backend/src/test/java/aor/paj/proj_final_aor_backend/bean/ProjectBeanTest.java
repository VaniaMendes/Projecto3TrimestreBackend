package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.ProjectDao;
import aor.paj.proj_final_aor_backend.dto.Lab;
import aor.paj.proj_final_aor_backend.dto.Project;
import aor.paj.proj_final_aor_backend.entity.LabEntity;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


}