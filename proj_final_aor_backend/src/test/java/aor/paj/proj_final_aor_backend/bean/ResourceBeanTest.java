package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.ResourceDao;
import aor.paj.proj_final_aor_backend.dao.SupplierDao;
import aor.paj.proj_final_aor_backend.dto.Resource;
import aor.paj.proj_final_aor_backend.dto.Supplier;
import aor.paj.proj_final_aor_backend.entity.ResourceEntity;
import aor.paj.proj_final_aor_backend.entity.SupplierEntity;
import aor.paj.proj_final_aor_backend.util.enums.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResourceBeanTest {

    @Mock
    private ResourceDao resourceDao;

    @Mock
    private SupplierDao supplierDao;

    @InjectMocks
    private SupplierBean supplierBean;

    @InjectMocks
    private ResourceBean resourceBean;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createResourceReturnsNullWhenResourceIsInvalid() {
        Resource resource = new Resource();
        resource.setName(null);

        ResourceEntity result = resourceBean.createResource(resource);

        assertNull(result);
    }




}