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
    public void createAllLabs_createsNewLabs_whenNoneExist() {
        int workplaceCount = Workplace.values().length;

        for (Workplace workplace : Workplace.values()) {
            when(labDao.findLabByName(workplace.name())).thenReturn(null);
        }

        labBean.createAllLabs();

        for (Workplace workplace : Workplace.values()) {
            verify(labDao, times(1)).findLabByName(workplace.name());
        }
        verify(labDao, times(workplaceCount)).persist(any());
    }

    @Test
    public void createAllLabs_doesNotCreateNewLabs_whenTheyAlreadyExist() {
        for (Workplace workplace : Workplace.values()) {
            LabEntity existingLabEntity = new LabEntity();
            existingLabEntity.setName(Workplace.valueOf(workplace.name()));
            when(labDao.findLabByName(workplace.name())).thenReturn(existingLabEntity);
        }

        labBean.createAllLabs();

        for (Workplace workplace : Workplace.values()) {
            verify(labDao, times(1)).findLabByName(workplace.name());
            verify(labDao, never()).persist(any());
        }
    }
}