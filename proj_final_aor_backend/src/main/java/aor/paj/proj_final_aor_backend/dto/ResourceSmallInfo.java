package aor.paj.proj_final_aor_backend.dto;

import aor.paj.proj_final_aor_backend.util.enums.ResourceType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResourceSmallInfo {

    @XmlElement
    private long id;

    // Name of the resource
    @XmlElement
    private String name;

    // Brand of the resource
    @XmlElement
    private String brand;

    // Quantity of the resource
    @XmlElement
    private String photo;

    @XmlElement
    private long projectsNumber;

    @XmlElement
    private ResourceType type;

    public ResourceSmallInfo() {
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public long getProjectsNumber() {
        return projectsNumber;
    }

    public void setProjectsNumber(long projectsNumber) {
        this.projectsNumber = projectsNumber;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }
}
