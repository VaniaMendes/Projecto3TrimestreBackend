package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * This class represents an interest.
 */
@XmlRootElement
public class Interest {

    /**
     * The id of the interest.
     */
    @XmlElement
    private long id;

    /**
     * The name of the interest.
     */
    @XmlElement
    private String name;

    /**
     * Default constructor of the Interest Class.
     */
    public Interest() {
    }

    /**
     * Returns the ID of the interest.
     * @return the ID of the interest.
     */
    public long getId() {
        return id;
    }


    /**
     * Sets the ID of the interest.
     * @param id the ID of the interest.
     */
    public void setId(long id) {
        this.id = id;
    }


    /**
     * Returns the name of the interest.
     * @return the name of the interest.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the interest.
     * @param name the name of the interest.
     */

    public void setName(String name) {
        this.name = name;
    }
}
