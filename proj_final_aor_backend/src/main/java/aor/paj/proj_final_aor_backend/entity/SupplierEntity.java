package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a SupplierEntity in the system.
 * It contains various properties related to a supplier and their getter and setter methods.
 */
@Entity
@Table(name = "supplier")
@NamedQuery(name = "Supplier.findAllSuppliers", query = "SELECT s FROM SupplierEntity s")
@NamedQuery(name = "Supplier.findSupplierById", query = "SELECT s FROM SupplierEntity s WHERE s.id = :id")
@NamedQuery(name = "Supplier.findSupplierByName", query = "SELECT s FROM SupplierEntity s WHERE s.name = :name")
public class SupplierEntity implements Serializable {

    // Unique identifier for serialization
    private static final long serialVersionUID = 1L;

    // Unique identifier for the supplier
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    // Name of the supplier
    @Column(name = "name", nullable = false, unique = true, updatable = true)
    private String name;

    // Contact information of the supplier
    @Column(name = "contact", nullable = false, unique = true, updatable = true)
    private String contact;

    // Set of resources associated with the supplier
    @OneToMany(mappedBy = "supplier")
    private Set<ResourceSupplierEntity> resources = new HashSet<>();

    /**
     * Default constructor for the SupplierEntity class.
     */
    public SupplierEntity() {
    }

    /**
     * Getter for the unique identifier of the supplier.
     * @return id of the supplier.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the unique identifier of the supplier.
     * @param id the new id of the supplier.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the name of the supplier.
     * @return name of the supplier.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name of the supplier.
     * @param name the new name of the supplier.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the contact information of the supplier.
     * @return contact information of the supplier.
     */
    public String getContact() {
        return contact;
    }

    /**
     * Setter for the contact information of the supplier.
     * @param contact the new contact information of the supplier.
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    public Set<ResourceSupplierEntity> getResources() {
        return resources;
    }

    public void setResources(Set<ResourceSupplierEntity> resources) {
        this.resources = resources;
    }
}