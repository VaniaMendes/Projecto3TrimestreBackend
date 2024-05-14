package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a BrandEntity in the system.
 * It contains various properties related to a brand and their getter and setter methods.
 */
@Entity
@Table(name = "brand")
public class BrandEntity implements Serializable {

    // Unique identifier for serialization
    private static final long serialVersionUID = 1L;

    // Unique identifier for the brand
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    // Name of the brand
    @Column(name = "name", nullable = false, unique = true, updatable = false)
    private String name;

    // Set of resources associated with the brand
    @OneToMany(mappedBy = "brand", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ResourceEntity> resources = new HashSet<>();

    /**
     * Default constructor for the BrandEntity class.
     */
    public BrandEntity() {
    }

    /**
     * Getter for the unique identifier of the brand.
     * @return id of the brand.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the unique identifier of the brand.
     * @param id the new id of the brand.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the name of the brand.
     * @return name of the brand.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name of the brand.
     * @param name the new name of the brand.
     */
    public void setName(String name) {
        this.name = name;
    }
}