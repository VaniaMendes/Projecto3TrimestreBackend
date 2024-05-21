package aor.paj.proj_final_aor_backend.dto;

import aor.paj.proj_final_aor_backend.util.enums.SkillType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private SkillType type;


    /**
     * Default constructor.
     */
    public Skill() {
        super();
    }

    /**
     * Returns the type of the skill.
     * @return the type of the skill.
     */
    public SkillType getType() {
        return type;
    }

    /**
     * Sets the type of the skill.
     * @param type the type of the skill.
     */
    public void setType(SkillType type) {
        this.type = type;
    }
}
