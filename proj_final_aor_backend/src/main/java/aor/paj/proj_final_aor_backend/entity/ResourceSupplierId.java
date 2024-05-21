package aor.paj.proj_final_aor_backend.entity;

import java.io.Serializable;
import java.util.Objects;


/**
 * This class represents a composite key for a resource supplier in the system.
 * It implements Serializable interface to allow this class to be converted into a byte stream.
 */
public class ResourceSupplierId implements Serializable {

    // Supplier ID part of the composite key
    private Long supplier;

    // Resource ID part of the composite key
    private Long resource;

    /**
     * Returns the supplier ID.
     * @return A Long representing the supplier ID.
     */
    public Long getSupplier() {
        return supplier;
    }

    /**
     * Sets the supplier ID.
     * @param supplier A Long containing the supplier ID.
     */
    public void setSupplier(Long supplier) {
        this.supplier = supplier;
    }

    /**
     * Returns the resource ID.
     * @return A Long representing the resource ID.
     */
    public Long getResource() {
        return resource;
    }

    /**
     * Sets the resource ID.
     * @param resource A Long containing the resource ID.
     */
    public void setResource(Long resource) {
        this.resource = resource;
    }

    /**
     * Checks if the current object is equal to the specified object.
     * @param o The object to compare with.
     * @return A boolean indicating whether the current object is equal to the specified object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceSupplierId that = (ResourceSupplierId) o;
        return Objects.equals(getSupplier(), that.getSupplier()) && Objects.equals(getResource(), that.getResource());
    }

    /**
     * Returns a hash code value for the object.
     * @return An int representing the hash code value for the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getSupplier(), getResource());
    }
}
