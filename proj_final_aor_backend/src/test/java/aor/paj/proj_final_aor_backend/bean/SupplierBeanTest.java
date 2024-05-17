package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.SupplierDao;
import aor.paj.proj_final_aor_backend.dto.Supplier;
import aor.paj.proj_final_aor_backend.entity.SupplierEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Test
    void getAllSuppliersReturnsAllSuppliers() {
        SupplierEntity supplierEntity1 = new SupplierEntity();
        supplierEntity1.setId(1L);
        supplierEntity1.setName("Supplier 1");
        supplierEntity1.setContact("Contact 1");

        SupplierEntity supplierEntity2 = new SupplierEntity();
        supplierEntity2.setId(2L);
        supplierEntity2.setName("Supplier 2");
        supplierEntity2.setContact("Contact 2");

        List<SupplierEntity> supplierEntities = Arrays.asList(supplierEntity1, supplierEntity2);

        when(supplierDao.findAllSuppliers()).thenReturn(supplierEntities);

        List<Supplier> suppliers = supplierBean.getAllSuppliers();

        assertNotNull(suppliers);
        assertEquals(2, suppliers.size());
        assertEquals("Supplier 1", suppliers.get(0).getName());
        assertEquals("Contact 1", suppliers.get(0).getContact());
        assertEquals("Supplier 2", suppliers.get(1).getName());
        assertEquals("Contact 2", suppliers.get(1).getContact());
    }

    @Test
    void getAllSuppliersReturnsEmptyListWhenNoSuppliers() {
        when(supplierDao.findAllSuppliers()).thenReturn(new ArrayList<>());

        List<Supplier> suppliers = supplierBean.getAllSuppliers();

        assertNotNull(suppliers);
        assertTrue(suppliers.isEmpty());
    }

    @Test
    void updateSupplierSuccessfullyUpdatesNameAndContact() {
        Long id = 1L;
        String newName = "Updated Supplier";
        String newContact = "Updated Contact";
        SupplierEntity existingEntity = new SupplierEntity();
        existingEntity.setId(id);
        existingEntity.setName("Old Supplier");
        existingEntity.setContact("Old Contact");

        when(supplierDao.findSupplierById(id)).thenReturn(existingEntity);
        doAnswer(i -> i.getArguments()[0]).when(supplierDao).merge(any(SupplierEntity.class));

        SupplierEntity resultEntity = supplierBean.updateSupplier(id, newName, newContact);

        assertNotNull(resultEntity);
        assertEquals(newName, resultEntity.getName());
        assertEquals(newContact, resultEntity.getContact());
    }

    @Test
    void updateSupplierWithNonExistingIdReturnsNull() {
        Long id = 1L;
        String newName = "Updated Supplier";
        String newContact = "Updated Contact";

        when(supplierDao.findSupplierById(id)).thenReturn(null);

        SupplierEntity resultEntity = supplierBean.updateSupplier(id, newName, newContact);

        assertNull(resultEntity);
    }

    @Test
    void updateSupplierWithNullNameDoesNotUpdateName() {
        Long id = 1L;
        String oldName = "Old Supplier";
        String newContact = "Updated Contact";
        SupplierEntity existingEntity = new SupplierEntity();
        existingEntity.setId(id);
        existingEntity.setName(oldName);
        existingEntity.setContact("Old Contact");

        when(supplierDao.findSupplierById(id)).thenReturn(existingEntity);
        doAnswer(i -> i.getArguments()[0]).when(supplierDao).merge(any(SupplierEntity.class));

        SupplierEntity resultEntity = supplierBean.updateSupplier(id, null, newContact);

        assertNotNull(resultEntity);
        assertEquals(oldName, resultEntity.getName());
        assertEquals(newContact, resultEntity.getContact());
    }

    @Test
    void updateSupplierWithNullContactDoesNotUpdateContact() {
        Long id = 1L;
        String newName = "Updated Supplier";
        String oldContact = "Old Contact";
        SupplierEntity existingEntity = new SupplierEntity();
        existingEntity.setId(id);
        existingEntity.setName("Old Supplier");
        existingEntity.setContact(oldContact);

        when(supplierDao.findSupplierById(id)).thenReturn(existingEntity);
        doAnswer(i -> i.getArguments()[0]).when(supplierDao).merge(any(SupplierEntity.class));

        SupplierEntity resultEntity = supplierBean.updateSupplier(id, newName, null);

        assertNotNull(resultEntity);
        assertEquals(newName, resultEntity.getName());
        assertEquals(oldContact, resultEntity.getContact());
    }
}