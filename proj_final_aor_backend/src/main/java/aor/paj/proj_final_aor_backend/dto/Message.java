package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * This class represents a message, which extends a TextEvent.
 */
@XmlRootElement
public class Message extends TextEvent {


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
     * The id of the project.
     */
    @XmlElement
    private long projectId;

    /**
     * Default constructor.
     */
    public Message() {
    }


    /**
     * Returns the content of the message.
     *
     * @return the content of the message.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the message.
     *
     * @param content the content of the message.
     */

    public void setContent(String content) {
        this.content = content;
    }


    /**
     * Returns the subject of the message.
     *
     * @return the subject of the message.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject of the message.
     *
     * @param subject the subject of the message.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Returns the id of the project.
     *
     * @return the id of the project.
     */

    public long getProjectId() {
        return projectId;
    }

    /**
     * Sets the id of the project.
     *
     * @param projectId the id of the project.
     */
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
}
