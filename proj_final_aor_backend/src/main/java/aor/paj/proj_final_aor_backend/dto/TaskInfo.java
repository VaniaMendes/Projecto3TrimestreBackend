package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TaskInfo {


    /**
     * Unique identifier for the task
     */
    @XmlElement
    private Long id;

    /**
     * Title of the task
     */

    @XmlElement
    private String title;

    /**
     * Default constructor.
     */
    public TaskInfo() {
    }

    /**
     * Returns the ID of the task.
     * @return id of the task
     */

    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the task.
     * @param id the new ID of the task
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the title of the task.
     * @return title of the task
     */

    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the task.
     * @param title the new title of the task
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
