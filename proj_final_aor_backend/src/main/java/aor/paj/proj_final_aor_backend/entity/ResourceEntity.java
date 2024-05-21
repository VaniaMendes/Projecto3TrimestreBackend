package aor.paj.proj_final_aor_backend.entity;

import aor.paj.proj_final_aor_backend.util.enums.ResourceType;
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
@NamedQuery(name = "Resource.findAllResources", query = "SELECT r FROM ResourceEntity r")
@NamedQuery(name = "Resource.findAllResourcesOrderedDESC", query = "SELECT r FROM ResourceEntity r ORDER BY r.createdAt DESC")
@NamedQuery(name = "Resource.findResourceById", query = "SELECT r FROM ResourceEntity r WHERE r.id = :id")
@NamedQuery(name = "Resource.findResourceByName", query = "SELECT r FROM ResourceEntity r WHERE r.name = :name")
@NamedQuery(name = "Resource.findResourcesByType", query = "SELECT r FROM ResourceEntity r WHERE r.type = :type")
@NamedQuery(name = "Resource.findResourcesByBrand", query = "SELECT r FROM ResourceEntity r WHERE r.brand = :brand")
@NamedQuery(name = "Resource.findResourcesBySupplier", query = "SELECT r FROM ResourceEntity r JOIN r.suppliers rs JOIN rs.supplier s WHERE s.name = :supplierName")
@NamedQuery(name = "Resource.findResourcesBySourceId", query = "SELECT r FROM ResourceEntity r WHERE r.sourceId = :sourceId")

//Combinations
@NamedQuery(name = "Resource.findResourcesByTypeAndBrand", query = "SELECT r FROM ResourceEntity r WHERE r.type = :type AND r.brand = :brand")
@NamedQuery(name = "Resource.findResourcesByTypeAndSupplier", query = "SELECT r FROM ResourceEntity r JOIN r.suppliers rs JOIN rs.supplier s WHERE r.type = :type AND s.name = :supplierName")
@NamedQuery(name = "Resource.findResourcesByBrandAndSupplier", query = "SELECT r FROM ResourceEntity r JOIN r.suppliers rs JOIN rs.supplier s WHERE r.brand = :brand AND s.name = :supplierName")
@NamedQuery(name = "Resource.findResourcesByTypeAndBrandAndSupplier", query = "SELECT r FROM ResourceEntity r JOIN r.suppliers rs JOIN rs.supplier s WHERE r.type = :type AND r.brand = :brand AND s.name = :supplierName")


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
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * The type of the resource.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    private ResourceType type;

    /**
     * The brand of the resource. It is a many-to-one relationship with the BrandEntity.
     */
    @Column(name = "brand", nullable = false, unique = false, updatable = true)
    private String brand;

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
     * The last update timestamp of the resource.
     */
    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    /**
     * The source ID of the resource.
     */
    @Column(name = "source_id", nullable = false)
    private String sourceId;


    @OneToMany(mappedBy = "resource")
    private Set<ResourceSupplierEntity> suppliers = new HashSet<>();

    /**
     * The projects that use the resource. It is a one-to-many relationship with the ProjectResource.
     */
    @OneToMany(mappedBy = "resource")
    private Set<ProjectResourceEntity> projects = new HashSet<>();

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
    public ResourceType getType() {
        return type;
    }

    /**
     * Sets the type of the resource.
     * @param type the new type of the resource
     */
    public void setType(ResourceType type) {
        this.type = type;
    }

    /**
     * Returns the brand of the resource.
     * @return brand of the resource
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the brand of the resource.
     * @param brand the new brand of the resource
     */
    public void setBrand(String brand) {
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


    public Set<ResourceSupplierEntity> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<ResourceSupplierEntity> suppliers) {
        this.suppliers = suppliers;
    }

    public void addSupplier(SupplierEntity supplier) {
        ResourceSupplierEntity resourceSupplier = new ResourceSupplierEntity();
        resourceSupplier.setResource(this);
        resourceSupplier.setSupplier(supplier);

        this.suppliers.add(resourceSupplier);
    }

    /**
     * Returns the projects that use the resource.
     * This is a getter method for retrieving the projects associated with the resource.
     * It is a part of the one-to-many relationship between ResourceEntity and ProjectResource.
     *
     * @return a Set of ProjectResource objects that are associated with the resource
     */
    public Set<ProjectResourceEntity> getProjects() {
        return projects;
    }

    /**
     * Sets the projects that use the resource.
     * This is a setter method for defining the projects associated with the resource.
     * It is a part of the one-to-many relationship between ResourceEntity and ProjectResource.
     *
     * @param projects a Set of ProjectResource objects to be associated with the resource
     */
    public void setProjects(Set<ProjectResourceEntity> projects) {
        this.projects = projects;
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
}