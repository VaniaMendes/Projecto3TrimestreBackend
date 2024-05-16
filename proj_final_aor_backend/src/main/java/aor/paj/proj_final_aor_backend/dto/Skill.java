package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * This class represents a skill, which extends an interest.
 * It has an id, name, and type.
 */

@XmlRootElement
public class Skill extends Interest{

    /**
     * The type of the skill.
     */
    @XmlElement
    private String type;


    /**
     * Default constructor.
     */
    public Skill() {
    }

    /**
     * Returns the type of the skill.
     * @return the type of the skill.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the skill.
     * @param type the type of the skill.
     */
    public void setType(String type) {
        this.type = type;
    }
}
