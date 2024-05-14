package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * The ResourceEntity class represents a resource entity in the database.
 * It is annotated as an Entity, meaning it is a JPA entity.
 * The Table annotation specifies the details of the table that will be used to create the table in the database.
 * It implements Serializable, which means that an object of this class can be converted to a sequence of bytes and that sequence can be reverted back into an object.
 */
@Entity
@Table(name = "resource")
public class ResourceEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The ID of the resource. It is the primary key of the resource table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    /**
     * The name of the resource. It is a unique field.
     */
    @Column(name = "name", nullable = false, unique = true, updatable = false)
    private String name;

    /**
     * The description of the resource.
     */
    @Column(name = "description", nullable = true, unique = false, updatable = true)
    private String description;

    /**
     * The type of the resource.
     */
    @Column(name = "type", nullable = false, unique = false, updatable = false)
    private int type;

    /**
     * The brand of the resource. It is a many-to-one relationship with the BrandEntity.
     */
    @ManyToOne
    @JoinColumn(name = "brand", referencedColumnName = "name")
    private BrandEntity brand;

    /**
     * The observation of the resource.
     */
    @Column(name = "observation", nullable = true, unique = false, updatable = true)
    private String observation;

    /**
     * The photo of the resource.
     */
    @Column(name = "photo", nullable = true, unique = false, updatable = true)
    private String photo;

    /**
     * The creation timestamp of the resource. It is automatically generated.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * The source ID of the resource.
     */
    @Column(name = "source_id", nullable = false, unique = false, updatable = true)
    private String sourceId;

    /**
     * The quantity of the resource.
     */
    @Column(name = "quantity", nullable = false, unique = false, updatable = true)
    private Integer quantity;

    /**
     * The suppliers of the resource. It is a many-to-many relationship with the SupplierEntity.
     */
    @ManyToMany
    @JoinTable(
            name = "resource_supplier",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "supplier_id"))
    private Set<SupplierEntity> suppliers = new HashSet<>();

    /**
     * The projects that use the resource. It is a one-to-many relationship with the ProjectResource.
     */
    @OneToMany(mappedBy = "resource")
    private Set<ProjectResource> projects = new HashSet<>();

    /**
     * Default constructor.
     */
    public ResourceEntity() {
    }

    /**
     * Returns the ID of the resource.
     * @return id of the resource
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the resource.
     * @param id the new ID of the resource
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name of the resource.
     * @return name of the resource
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the resource.
     * @param name the new name of the resource
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of the resource.
     * @return description of the resource
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the resource.
     * @param description the new description of the resource
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the type of the resource.
     * @return type of the resource
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the type of the resource.
     * @param type the new type of the resource
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Returns the brand of the resource.
     * @return brand of the resource
     */
    public BrandEntity getBrand() {
        return brand;
    }

    /**
     * Sets the brand of the resource.
     * @param brand the new brand of the resource
     */
    public void setBrand(BrandEntity brand) {
        this.brand = brand;
    }

    /**
     * Returns the observation of the resource.
     * @return observation of the resource
     */
    public String getObservation() {
        return observation;
    }

    /**
     * Sets the observation of the resource.
     * @param observation the new observation of the resource
     */
    public void setObservation(String observation) {
        this.observation = observation;
    }

    /**
     * Returns the photo of the resource.
     * @return photo of the resource
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Sets the photo of the resource.
     * @param photo the new photo of the resource
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * Returns the creation timestamp of the resource.
     * @return creation timestamp of the resource
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of the resource.
     * @param createdAt the new creation timestamp of the resource
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the source ID of the resource.
     * @return source ID of the resource
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * Sets the source ID of the resource.
     * @param sourceId the new source ID of the resource
     */
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * Returns the quantity of the resource.
     * @return quantity of the resource
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the resource.
     * @param quantity the new quantity of the resource
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns the suppliers of the resource.
     * @return suppliers of the resource
     */
    public Set<SupplierEntity> getSuppliers() {
        return suppliers;
    }

    /**
     * Sets the suppliers of the resource.
     * @param suppliers the new suppliers of the resource
     */
    public void setSuppliers(Set<SupplierEntity> suppliers) {
        this.suppliers = suppliers;
    }
}