package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.LabDao;
import aor.paj.proj_final_aor_backend.dto.Lab;
import aor.paj.proj_final_aor_backend.entity.LabEntity;
import aor.paj.proj_final_aor_backend.util.enums.Workplace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LabBeanTest {

    @InjectMocks
    private LabBean labBean;

    @Mock
    private LabDao labDao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createAllLabs_doesNotPersistLabs_whenLabsAlreadyExist() {
        for (Workplace workplace : Workplace.values()) {
            when(labDao.findLabByName(workplace.name())).thenReturn(new LabEntity());
        }

        labBean.createAllLabs();

        for (Workplace workplace : Workplace.values()) {
            verify(labDao, times(1)).findLabByName(workplace.name());
            verify(labDao, never()).persist(any(LabEntity.class));
        }
    }

    @Test
    public void findLabByName_returnsLab_whenLabExists() {
        String labName = "COIMBRA";
        LabEntity existingLabEntity = new LabEntity();
        existingLabEntity.setName(Workplace.valueOf(labName));
        when(labDao.findLabByName(labName)).thenReturn(existingLabEntity);

        LabEntity result = labBean.findLabByName(labName);

        verify(labDao, times(1)).findLabByName(labName);
        assertNotNull(result);
        assertEquals(labName, result.getName().name());
    }

    @Test
    public void findLabByName_returnsNull_whenLabDoesNotExist() {
        String labName = "NON_EXISTENT";
        when(labDao.findLabByName(labName)).thenReturn(null);

        LabEntity result = labBean.findLabByName(labName);

        verify(labDao, times(1)).findLabByName(labName);
        assertNull(result);
    }

    @Test
    public void findLabByName_returnsNull_whenNameIsNull() {
        LabEntity result = labBean.findLabByName(null);

        verify(labDao, never()).findLabByName(anyString());
        assertNull(result);
    }

    @Test
    public void findLabByName_returnsNull_whenNameIsEmpty() {
        LabEntity result = labBean.findLabByName("");

        verify(labDao, never()).findLabByName(anyString());
        assertNull(result);
    }

}