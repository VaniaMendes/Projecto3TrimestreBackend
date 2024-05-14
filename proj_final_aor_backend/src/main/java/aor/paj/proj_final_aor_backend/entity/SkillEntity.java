package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name="skill")
public class SkillEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;

    @Column(name = "name", nullable = false, unique = true, updatable = false)
    private String name;

    @Column(name = "type", nullable = false, unique = true, updatable = false)
    private String type;


    public SkillEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}