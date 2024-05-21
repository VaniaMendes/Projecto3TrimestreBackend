package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.ResourceDao;
import aor.paj.proj_final_aor_backend.dao.SupplierDao;
import aor.paj.proj_final_aor_backend.dto.Resource;
import aor.paj.proj_final_aor_backend.dto.Supplier;
import aor.paj.proj_final_aor_backend.entity.ResourceEntity;
import aor.paj.proj_final_aor_backend.entity.SupplierEntity;
import aor.paj.proj_final_aor_backend.util.enums.ResourceType;
import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResourceBeanTest {

    @Mock
    private ResourceDao resourceDao;

    @Mock
    private SupplierDao supplierDao;

    @Mock
    private Logger logger;

    @InjectMocks
    private SupplierBean supplierBean;

    @InjectMocks
    private ResourceBean resourceBean;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        supplierBean = new SupplierBean(supplierDao);
        resourceBean = new ResourceBean(resourceDao, supplierBean, logger);
    }

    @Test
    void createResourceReturnsNullWhenResourceIsInvalid() {
        Resource resource = new Resource();
        resource.setName(null);

        ResourceEntity result = resourceBean.createResource(resource);

        assertNull(result);
    }

    @Test
    void addSupplierToResourceReturnsNullWhenResourceDoesNotExist() {
        String resourceName = "resource1";
        String supplierName = "supplier1";

        when(resourceDao.findResourceByName(resourceName)).thenReturn(null);

        ResourceEntity result = resourceBean.addSupplierToResource(resourceName, supplierName);

        assertNull(result);
        verify(logger).error("Resource with name '" + resourceName + "' does not exist");
    }

    @Test
    void addSupplierToResourceReturnsNullWhenSupplierDoesNotExist() {
        String resourceName = "resource1";
        String supplierName = "supplier1";

        ResourceEntity resourceEntity = new ResourceEntity();

        when(resourceDao.findResourceByName(resourceName)).thenReturn(resourceEntity);
        when(supplierBean.findSupplierByName(supplierName)).thenReturn(null);

        ResourceEntity result = resourceBean.addSupplierToResource(resourceName, supplierName);

        assertNull(result);
        verify(logger).error("Supplier does not exist: " + supplierName);
    }

    @Test
    void getAllResourcesReturnsEmptyListWhenNoResourcesExist() {
        when(resourceDao.findAllResources()).thenReturn(Collections.emptyList());

        List<Resource> result = resourceBean.getAllResources();

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void getAllResourcesReturnsListOfResourcesWhenResourcesExist() {
        ResourceEntity resourceEntity1 = new ResourceEntity();
        resourceEntity1.setId(1L);
        ResourceEntity resourceEntity2 = new ResourceEntity();
        resourceEntity2.setId(2L);

        when(resourceDao.findAllResources()).thenReturn(Arrays.asList(resourceEntity1, resourceEntity2));

        List<Resource> result = resourceBean.getAllResources();

        // Assuming that convertToDTO() correctly transforms ResourceEntity to Resource,
        // the result should contain Resources with the same IDs as the ResourceEntities.
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }


}