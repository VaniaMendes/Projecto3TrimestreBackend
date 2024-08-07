package aor.paj.proj_final_aor_backend.dto;

import aor.paj.proj_final_aor_backend.util.enums.Workplace;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * This class represents a Lab in the project.
 * Each lab has an id and a name which is of type Workplace.
 */
@XmlRootElement
public class Lab {

    /**
     * The unique identifier for the lab.
     */
    @XmlElement
    private Integer id;

    /**
     * The name of the lab.
     */
    @XmlElement
    private Workplace name;

    /**
     * Default constructor for the Lab class.
     */
    public Lab() {
    }


    /**
     * Constructor for the Lab class.
     * @param name the name of the lab
     */
    public Lab(String name) {
        this.name = Workplace.valueOf(name);
    }

    /**
     * Returns the id of the lab.
     * @return the id of the lab
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the lab.
     * @param id the new id of the lab
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the lab.
     *
     * @return the name of the lab
     */
    public Workplace getName() {
        return name;
    }

    /**
     * Sets the name of the lab.
     * @param name the new name of the lab
     */
    public void setName(Workplace name) {
        this.name = name;
    }
}
