package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * This class represents a message, which extends a TextEvent.
 */
@XmlRootElement
public class Message extends TextEvent{


    /**
     * The content of the message.

     */
    @XmlElement
    private String content;

    /**
     * The subject of the message.
     */
    @XmlElement
    private String subject;

    /**
     * Default constructor.

     */
    public Message() {
    }


    /**
     * Returns the content of the message.
     * @return  the content of the message.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the message.
     * @param content the content of the message.
     */

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
