package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;

/**
 * This class represents a session.
 */
@XmlRootElement
public class Session {

    /**
     * The id of the session.
     */
    @XmlElement
    private long id;

    /**
     * The initial time of the session.
     */
    @XmlElement
    private LocalDateTime initSession;

    /**
     * The end time of the session.
     */
    @XmlElement
    private LocalDateTime endSession;

    /**
     * The token of the session.
     */
    @XmlElement
    private String token;


    /**
     * Default constructor.
     */
    public Session() {
    }


    /**
     * Returns the ID of the session.
     * @return the ID of the session.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID of the session.
     * @param id the ID of the session.
     */

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the initial time of the session.
     * @return the initial time of the session.
     */
    public LocalDateTime getInitSession() {
        return initSession;
    }

    /**
     * Sets the initial time of the session.
     * @param initSession the initial time of the session.
     */

    public void setInitSession(LocalDateTime initSession) {
        this.initSession = initSession;
    }

    /**
     * Returns the end time of the session.
     * @return the end time of the session.
     */

    public LocalDateTime getEndSession() {
        return endSession;
    }

    /**
     * Sets the end time of the session.
     * @param endSession the end time of the session.
     */
    public void setEndSession(LocalDateTime endSession) {
        this.endSession = endSession;
    }


    /**
     * Returns the token of the session.
     * @return the token of the session.
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token of the session.
     * @param token the token of the session.
     */
    public void setToken(String token) {
        this.token = token;
    }
}
