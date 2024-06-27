package aor.paj.proj_final_aor_backend.dto;


import aor.paj.proj_final_aor_backend.util.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * This class represents a notification, which extends a TextEvent.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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
    private long relatedIDEntity;


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

    public long getRelatedIDEntity() {
        return relatedIDEntity;
    }

    public void setRelatedIDEntity(long relatedIDEntity) {
        this.relatedIDEntity = relatedIDEntity;
    }
}
