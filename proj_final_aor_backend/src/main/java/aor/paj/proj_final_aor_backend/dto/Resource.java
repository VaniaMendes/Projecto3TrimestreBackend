package aor.paj.proj_final_aor_backend.dto;

import aor.paj.proj_final_aor_backend.util.enums.ResourceType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Resource in the system.
 * It contains various properties related to a resource and their getter and setter methods.
 */
@XmlRootElement
public class Resource {
    // Unique identifier for the resource
    @XmlElement
    private long id;

    // Name of the resource
    @XmlElement
    private String name;

    // Description of the resource
    @XmlElement
    private String description;

    // Type of the resource
    @XmlElement
    private ResourceType type;

    // Brand of the resource
    @XmlElement
    private String brand;

    // Observations related to the resource
    @XmlElement
    private String observation;

    // Photo of the resource
    @XmlElement
    private String photo;

    // Date and time when the resource was created
    @XmlElement
    private LocalDateTime createdAt;

    // Date and time when the resource was last updated
    @XmlElement
    private LocalDateTime updatedAt;

    // Source ID of the resource
    @XmlElement
    private String sourceId;

    // Suppliers of the resource
    @XmlElement
    private List<Supplier> suppliers;

    // Quantity of the resource
    @XmlElement
    private int quantity;

    /**
     * Default constructor for the Resource class.
     */
    public Resource() {
    }

    // Getter and setter methods for all the properties


    /**
     * This method is used to get the unique identifier of the resource.
     * It returns a long value representing the ID of the resource.
     *
     * @return id of the resource.
     */
    public long getId() {
        return id;
    }

    /**
     * This method is used to set the unique identifier of the resource.
     * It takes a long value as a parameter and assigns it to the id variable.
     *
     * @param id The new id for the resource.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for the name of the resource.
     * @return name of the resource.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name of the resource.
     * @param name the new name of the resource.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the description of the resource.
     * @return description of the resource.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description of the resource.
     * @param description the new description of the resource.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This method is used to get the type of the resource.
     * It returns a ResourceType value representing the type of the resource.
     *
     * @return type of the resource.
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * This method is used to set the type of the resource.
     * It takes a ResourceType value as a parameter and assigns it to the type variable.
     *
     * @param type The new type for the resource.
     */
    public void setType(ResourceType type) {
        this.type = type;
    }

    /**
     * Getter for the brand of the resource.
     * @return brand of the resource.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Setter for the brand of the resource.
     * @param brand the new brand of the resource.
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Getter for the observations related to the resource.
     * @return observations related to the resource.
     */
    public String getObservation() {
        return observation;
    }

    /**
     * Setter for the observations related to the resource.
     * @param observation the new observations related to the resource.
     */
    public void setObservation(String observation) {
        this.observation = observation;
    }

    /**
     * Getter for the photo of the resource.
     * @return photo of the resource.
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Setter for the photo of the resource.
     * @param photo the new photo of the resource.
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * Getter for the creation date and time of the resource.
     * @return creation date and time of the resource.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Setter for the creation date and time of the resource.
     * @param createdAt the new creation date and time of the resource.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the last update timestamp of the resource.
     * This is a getter method for retrieving the last update timestamp of the resource.
     *
     * @return updatedAt, the last update timestamp of the resource
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update timestamp of the resource.
     * This is a setter method for defining the last update timestamp of the resource.
     *
     * @param updatedAt the new last update timestamp of the resource
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Getter for the source ID of the resource.
     * @return source ID of the resource.
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * Setter for the source ID of the resource.
     * @param sourceId the new source ID of the resource.
     */
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public void addSupplier(Supplier supplier) {
        if (this.suppliers == null) {
            this.suppliers = new ArrayList<>();
        }
        this.suppliers.add(supplier);
    }

    /**
     * Getter for the quantity of the resource.
     * @return quantity of the resource.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Setter for the quantity of the resource.
     * @param quantity the new quantity of the resource.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}