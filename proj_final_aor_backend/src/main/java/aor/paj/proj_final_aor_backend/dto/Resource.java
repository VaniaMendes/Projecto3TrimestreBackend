package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;

import java.time.LocalDateTime;

/**
 * This class represents a Resource in the system.
 * It contains various properties related to a resource and their getter and setter methods.
 */
public class Resource {
    // Unique identifier for the resource
    @XmlElement
    private int id;

    // Name of the resource
    @XmlElement
    private String name;

    // Description of the resource
    @XmlElement
    private String description;

    // Type of the resource
    @XmlElement
    private int type;

    // Constants representing different types of resources
    @XmlElement
    public static final int MATERIAL = 10;
    @XmlElement
    public static final int DIGITAL = 20;

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

    // Source ID of the resource
    @XmlElement
    private String sourceId;

    // Name of the supplier of the resource
    @XmlElement
    private String supplierName;

    // Contact of the supplier of the resource
    @XmlElement
    private String supplierContact;

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
     * Getter for the unique identifier of the resource.
     * @return id of the resource.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the unique identifier of the resource.
     * @param id the new id of the resource.
     */
    public void setId(int id) {
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
     * Getter for the type of the resource.
     * @return type of the resource.
     */
    public int getType() {
        return type;
    }

    /**
     * Setter for the type of the resource.
     * @param type the new type of the resource.
     */
    public void setType(int type) {
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

    /**
     * Getter for the name of the supplier of the resource.
     * @return name of the supplier of the resource.
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * Setter for the name of the supplier of the resource.
     * @param supplierName the new name of the supplier of the resource.
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    /**
     * Getter for the contact of the supplier of the resource.
     * @return contact of the supplier of the resource.
     */
    public String getSupplierContact() {
        return supplierContact;
    }

    /**
     * Setter for the contact of the supplier of the resource.
     * @param supplierContact the new contact of the supplier of the resource.
     */
    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
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