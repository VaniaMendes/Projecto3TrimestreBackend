package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * The ResourceSupplierEntity class represents the "resource_supplier" table in the database.
 * It uses a composite key which is represented by the ResourceSupplierId class.
 */
@Entity
@Table(name = "resource_supplier")
@IdClass(ResourceSupplierId.class)

@NamedQuery(name = "ResourceSupplier.findAllResourcesFromSupplier", query = "SELECT rs FROM ResourceSupplierEntity rs WHERE rs.supplier.id = :supplierId")
@NamedQuery(name = "ResourceSupplier.findAllSuppliersForResource", query = "SELECT rs FROM ResourceSupplierEntity rs WHERE rs.resource.id = :resourceId")
public class ResourceSupplierEntity implements Serializable {

    /**
     * The resource field represents the resource part of the composite key.
     * It is a many-to-one relationship with the ResourceEntity class.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "resource_id", referencedColumnName = "id")
    private ResourceEntity resource;

    /**
     * The supplier field represents the supplier part of the composite key.
     * It is a many-to-one relationship with the SupplierEntity class.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private SupplierEntity supplier;

    /**
     * The activeStatus field represents the active status of the resource supplier.
     * It is a boolean value where true indicates that the resource supplier is active.
     */
    @Column(name = "active_status", nullable = false)
    private boolean activeStatus;

    /**
     * Default constructor for the ResourceSupplierEntity class.
     */
    public ResourceSupplierEntity() {
    }

    /**
     * Getter for the resource field.
     * @return the resource part of the composite key.
     */
    public ResourceEntity getResource() {
        return resource;
    }

    /**
     * Setter for the resource field.
     * @param resource the new resource part of the composite key.
     */
    public void setResource(ResourceEntity resource) {
        this.resource = resource;
    }

    /**
     * Getter for the supplier field.
     * @return the supplier part of the composite key.
     */
    public SupplierEntity getSupplier() {
        return supplier;
    }

    /**
     * Setter for the supplier field.
     * @param supplier the new supplier part of the composite key.
     */
    public void setSupplier(SupplierEntity supplier) {
        this.supplier = supplier;
    }

    /**
     * Getter for the activeStatus field.
     * @return the active status of the resource supplier.
     */
    public boolean isActiveStatus() {
        return activeStatus;
    }

    /**
     * Setter for the activeStatus field.
     * @param activeStatus the new active status of the resource supplier.
     */
    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

}