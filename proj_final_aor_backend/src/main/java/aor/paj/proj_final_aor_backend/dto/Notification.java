package aor.paj.proj_final_aor_backend.dto;

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
    private String type;


    /**
     * Default constructor.
     */
    public Notification() {
    }

    /**
     * Returns the type of the notification.
     * @return the type of the notification.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the notification.
     * @param type the type of the notification.
     */
    public void setType(String type) {
        this.type = type;
    }
}
