package aor.paj.proj_final_aor_backend.dto;

import aor.paj.proj_final_aor_backend.util.enums.UserTypeInProject;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserProject {

    @XmlElement
    private Long projectId;

    @XmlElement
    private Long userId;

    @XmlElement
    private UserTypeInProject userType;

    @XmlElement
    private boolean approved;

    @XmlElement
    private boolean exited;

    public UserProject() {
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserTypeInProject getUserType() {
        return userType;
    }

    public void setUserType(UserTypeInProject userType) {
        this.userType = userType;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isExited() {
        return exited;
    }

    public void setExited(boolean exited) {
        this.exited = exited;
    }
}
