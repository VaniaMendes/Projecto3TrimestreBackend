package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.SupplierDao;
import aor.paj.proj_final_aor_backend.dto.Supplier;
import aor.paj.proj_final_aor_backend.entity.SupplierEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupplierBeanTest {

    @InjectMocks
    private SupplierBean supplierBean;

    @Mock
    private SupplierDao supplierDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSupplierSuccessfully() {
        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setContact("Test Contact");
        SupplierEntity expectedEntity = new SupplierEntity();
        expectedEntity.setName(supplier.getName());
        expectedEntity.setContact(supplier.getContact());

        when(supplierDao.findSupplierByName(supplier.getName())).thenReturn(null);
        doNothing().when(supplierDao).persist(any(SupplierEntity.class));

        SupplierEntity resultEntity = supplierBean.createSupplier(supplier);

        assertNotNull(resultEntity);
        assertEquals(expectedEntity.getName(), resultEntity.getName());
        assertEquals(expectedEntity.getContact(), resultEntity.getContact());
    }

    @Test
    void createSupplierWithEmptyNameReturnsNull() {
        Supplier supplier = new Supplier();
        supplier.setName("");
        supplier.setContact("Test Contact");

        SupplierEntity resultEntity = supplierBean.createSupplier(supplier);

        assertNull(resultEntity);
    }

    @Test
    void createSupplierWithNullNameReturnsNull() {
        Supplier supplier = new Supplier();
        supplier.setName(null);
        supplier.setContact("Test Contact");

        SupplierEntity resultEntity = supplierBean.createSupplier(supplier);

        assertNull(resultEntity);
    }

    @Test
    void createSupplierWithExistingNameReturnsNull() {
        Supplier supplier = new Supplier();
        supplier.setName("Existing Supplier");
        supplier.setContact("Test Contact");
        SupplierEntity existingEntity = new SupplierEntity();

        when(supplierDao.findSupplierByName(supplier.getName())).thenReturn(existingEntity);

        SupplierEntity resultEntity = supplierBean.createSupplier(supplier);

        assertNull(resultEntity);
    }

    @Test
    void createSupplierWithEmptyContactReturnsNull() {
        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setContact("");

        SupplierEntity resultEntity = supplierBean.createSupplier(supplier);

        assertNull(resultEntity);
    }

    @Test
    void createSupplierWithNullContactReturnsNull() {
        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setContact(null);

        SupplierEntity resultEntity = supplierBean.createSupplier(supplier);

        assertNull(resultEntity);
    }
}