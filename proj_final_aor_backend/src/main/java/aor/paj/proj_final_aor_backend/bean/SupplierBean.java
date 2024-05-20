package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.SupplierDao;
import aor.paj.proj_final_aor_backend.dto.Supplier;
import aor.paj.proj_final_aor_backend.entity.SupplierEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class SupplierBean implements Serializable {

    private static final Logger logger = LogManager.getLogger(SupplierBean.class);

    @EJB
    private SupplierDao supplierDao;

    public SupplierBean() {
    }

    public SupplierBean (SupplierDao supplierDao) {
        this.supplierDao = supplierDao;
    }

    /**
     * Creates a new supplier in the database.
     * <p>
     * This method first checks if the given supplier is valid by calling {@code isInvalidSupplier}.
     * If the supplier is deemed invalid, the method returns null. Otherwise, it proceeds to convert
     * the supplier DTO to a {@link SupplierEntity} object, persists it in the database, and returns
     * the persisted entity.
     *
     * @param supplier The supplier DTO to be converted and persisted.
     * @return The persisted {@link SupplierEntity}, or null if the supplier is invalid.
     */
    public SupplierEntity createSupplier(Supplier supplier) {

        if (isInvalidSupplier(supplier)) {
            return null;
        }

        SupplierEntity supplierEntity = convertToEntity(supplier);

        supplierDao.persist(supplierEntity);
        return supplierEntity;
    }

    /**
     * Validates the given supplier.
     * <p>
     * Checks if the supplier's name and contact information are not null or empty. Additionally,
     * it verifies if a supplier with the same name does not already exist in the database. If any
     * of these conditions are met, the method logs an appropriate error and returns true, indicating
     * the supplier is invalid.
     *
     * @param supplier The supplier to validate.
     * @return true if the supplier is invalid, false otherwise.
     */
    private boolean isInvalidSupplier(Supplier supplier) {
        if (supplier.getName() == null || supplier.getName().isEmpty()) {
            logger.error("Supplier name is empty");
            return true;
        }

        if (supplier.getContact() == null || supplier.getContact().isEmpty()) {
            logger.error("Supplier contact is empty");
            return true;
        }

        if (supplierDao.findSupplierByName(supplier.getName()) != null) {
            logger.error("Supplier with name " + supplier.getName() + " already exists");
            return true;
        }

        return false;
    }

    /**
     * Checks if a supplier with the given name exists in the database.
     *
     * This method uses the {@code findSupplierByName} method of the {@code supplierDao} to retrieve a supplier entity
     * with the given name from the database. If a supplier entity with the given name is found, the method returns true.
     * If no such supplier entity is found, the method returns false.
     *
     * @param name The name of the supplier to check.
     * @return true if a supplier with the given name exists in the database, false otherwise.
     */
    public boolean exists(String name) {
        return supplierDao.findSupplierByName(name) != null;
    }

    /**
     * Retrieves a supplier from the database by its name.
     *
     * This method uses the {@code findSupplierByName} method of the {@code supplierDao} to retrieve a supplier entity
     * with the given name from the database. If a supplier entity with the given name is found, the method returns the entity.
     * If no such supplier entity is found, the method returns null.
     *
     * @param name The name of the supplier to retrieve.
     * @return The SupplierEntity with the given name, or null if no such supplier exists.
     */
    public SupplierEntity findSupplierByName(String name) {
        return supplierDao.findSupplierByName(name);
    }

    /**
     * Retrieves all suppliers from the database.
     * <p>
     * This method fetches all {@link SupplierEntity} objects from the database using the {@code findAllSuppliers} method
     * of the {@code supplierDao}. Each {@link SupplierEntity} is then converted to a {@link Supplier} DTO using the
     * {@code convertToDto} method. The resulting list of {@link Supplier} DTOs is returned.
     *
     * @return a list of {@link Supplier} DTOs representing all suppliers in the database.
     */
    public List<Supplier> getAllSuppliers() {
        List<SupplierEntity> supplierEntities = supplierDao.findAllSuppliers();
        List<Supplier> suppliers = new ArrayList<>();
        for (SupplierEntity supplierEntity : supplierEntities) {
            suppliers.add(convertToDTO(supplierEntity));
        }
        return suppliers;
    }

    /**
     * Updates the details of an existing supplier in the database.
     *
     * This method takes in the ID of the supplier to be updated, along with the new name and contact information.
     * It first retrieves the supplier entity from the database using the provided ID. If no such supplier exists,
     * it logs an error and returns null.
     *
     * If the supplier does exist, it checks if the new name and contact information are not null or empty.
     * If they are not, it updates the supplier entity's name and contact information with the new values.
     *
     * Finally, it calls the `merge` method on the `supplierDao` to persist the changes to the database, and
     * returns the updated supplier entity.
     *
     * @param id The ID of the supplier to be updated.
     * @param name The new name for the supplier. If null or empty, the supplier's name is not updated.
     * @param contact The new contact information for the supplier. If null or empty, the supplier's contact information is not updated.
     * @return The updated supplier entity, or null if no supplier with the provided ID exists.
     */
    public SupplierEntity updateSupplier(Long id, String name, String contact) {
        SupplierEntity supplierEntity = supplierDao.findSupplierById(id);
        if (supplierEntity == null) {
            logger.error("Supplier with id " + id + " does not exist");
            return null;
        }

        if (name != null && !name.isEmpty()) {
            supplierEntity.setName(name);
        }

        if (contact != null && !contact.isEmpty()) {
            supplierEntity.setContact(contact);
        }

        supplierDao.merge(supplierEntity);
        return supplierEntity;
    }

    /**
     * Converts a {@link Supplier} DTO to a {@link SupplierEntity}.
     * <p>
     * This method takes a supplier DTO as input and creates a new {@link SupplierEntity} instance.
     * It then sets the name and contact information from the DTO to the entity.
     *
     * @param supplier The supplier DTO to be converted.
     * @return The newly created {@link SupplierEntity} with values from the DTO.
     */
    public SupplierEntity convertToEntity(Supplier supplier) {
        SupplierEntity supplierEntity = new SupplierEntity();
        supplierEntity.setName(supplier.getName());
        supplierEntity.setContact(supplier.getContact());
        return supplierEntity;
    }

    /**
     * Converts a {@link SupplierEntity} to a {@link Supplier} DTO.
     * <p>
     * This method takes a supplier entity as input and creates a new {@link Supplier} DTO instance.
     * It then sets the ID, name, and contact information from the entity to the DTO.
     *
     * @param supplierEntity The supplier entity to be converted.
     * @return The newly created {@link Supplier} DTO with values from the entity.
     */
    public Supplier convertToDTO(SupplierEntity supplierEntity) {
        Supplier supplier = new Supplier();
        supplier.setId(supplierEntity.getId());
        supplier.setName(supplierEntity.getName());
        supplier.setContact(supplierEntity.getContact());
        return supplier;
    }
}
