package aor.paj.proj_final_aor_backend.dto;

import aor.paj.proj_final_aor_backend.util.enums.NotificationType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * This class represents a notification, which extends a TextEvent.
 */
@XmlRootElement
public class Notification extends TextEvent{

    /**
     * The type of the notification.
     */
    @XmlElement
    private NotificationType type;


    /**
     * The name of the related entity.
     */
    @XmlElement
    private String relatedEntityName;


    /**
     * Default constructor.
     */
    public Notification() {
    }

    /**
     * Returns the type of the notification.
     * @return the type of the notification.
     */
    public NotificationType getType() {
        return type;
    }

    /**
     * Sets the type of the notification.
     * @param type the type of the notification.
     */
    public void setType(NotificationType type) {
        this.type = type;
    }

    /**
     * Returns the name of the related entity.
     * @return the name of the related entity.
     */
    public String getRelatedEntityName() {
        return relatedEntityName;
    }

    /**
     * Sets the name of the related entity.
     * @param relatedEntityName the name of the related entity.
     */
    public void setRelatedEntityName(String relatedEntityName) {
        this.relatedEntityName = relatedEntityName;
    }
}
