package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

/**
 * This class represents a ProjectResource in the system.
 * It contains various properties related to a project resource and their getter and setter methods.
 */
@Entity
@Table(name = "project_resource")
@IdClass(ProjectResourceId.class)
public class ProjectResourceEntity {

    // Project associated with the resource
    @Id
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private ProjectEntity project;

    // Resource associated with the project
    @Id
    @ManyToOne
    @JoinColumn(name = "resource_id", referencedColumnName = "id")
    private ResourceEntity resource;

    // Quantity of the resource
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * Default constructor for the ProjectResource class.
     */
    public ProjectResourceEntity() {
    }

    /**
     * Getter for the project associated with the resource.
     * @return project associated with the resource.
     */
    public ProjectEntity getProject() {
        return project;
    }

    /**
     * Setter for the project associated with the resource.
     * @param project the new project associated with the resource.
     */
    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    /**
     * Getter for the resource associated with the project.
     * @return resource associated with the project.
     */
    public ResourceEntity getResource() {
        return resource;
    }

    /**
     * Setter for the resource associated with the project.
     * @param resource the new resource associated with the project.
     */
    public void setResource(ResourceEntity resource) {
        this.resource = resource;
    }

    /**
     * Getter for the quantity of the resource.
     * @return quantity of the resource.
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Setter for the quantity of the resource.
     * @param quantity the new quantity of the resource.
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}