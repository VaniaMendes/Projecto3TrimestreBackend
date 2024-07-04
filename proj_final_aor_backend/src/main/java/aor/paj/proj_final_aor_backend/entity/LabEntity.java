package aor.paj.proj_final_aor_backend.entity;

import aor.paj.proj_final_aor_backend.util.enums.Workplace;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

/**
 * This class represents a LabEntity in the system.
 * It contains various properties related to a lab and their getter and setter methods.
 */
@Entity
@Table(name = "lab")

@NamedQuery(name = "Lab.findAllLabs", query = " SELECT l FROM LabEntity l")
@NamedQuery(name = "Lab.findLabById", query = " SELECT l FROM LabEntity l WHERE l.id = :id")
@NamedQuery(name = "Lab.findLabByName", query = " SELECT l FROM LabEntity l WHERE l.name = :name")
@NamedQuery(name = "Lab.findLabByUserId", query = " SELECT l FROM LabEntity l JOIN l.users u WHERE u.id = :userId")
public class LabEntity implements Serializable {

    // Unique identifier for serialization
    private static final long serialVersionUID = 1L;

    // Unique identifier for the lab
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Integer id;

    // Name of the lab
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true, updatable = false)
    private Workplace name;

    // Set of projects associated with the lab
    @OneToMany(mappedBy = "lab")
    private Set<ProjectEntity> projects;

    // Set of users associated with the lab
    @OneToMany(mappedBy = "lab")
    private Set<UserEntity> users;

    /**
     * Default constructor for the LabEntity class.
     */
    public LabEntity() {
    }

    /**
     * Getter for the unique identifier of the lab.
     * @return id of the lab.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Setter for the unique identifier of the lab.
     * @param id the new id of the lab.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter for the name of the lab.
     * @return name of the lab.
     */
    public Workplace getName() {
        return name;
    }

    /**
     * Setter for the name of the lab.
     * @param name the new name of the lab.
     */
    public void setName(Workplace name) {
        this.name = name;
    }

    /**
     * Getter for the projects associated with the lab.
     * @return projects associated with the lab.
     */
    public Set<ProjectEntity> getProjects() {
        return projects;
    }

    /**
     * Setter for the projects associated with the lab.
     * @param projects the new projects associated with the lab.
     */
    public void setProjects(Set<ProjectEntity> projects) {
        this.projects = projects;
    }

    /**
     * Getter for the users associated with the lab.
     * @return users associated with the lab.
     */
    public Set<UserEntity> getUsers() {
        return users;
    }

    /**
     * Setter for the users associated with the lab.
     * @param users the new users associated with the lab.
     */
    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }
}