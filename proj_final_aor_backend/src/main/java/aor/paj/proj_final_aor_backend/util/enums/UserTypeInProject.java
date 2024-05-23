package aor.paj.proj_final_aor_backend.util.enums;

/**
 * Enum representing the different types of users in a project.
 *
 * <p>These are the possible roles a user can have in a project:</p>
 * <ul>
 *   <li>CANDIDATE: A user who is a potential member of the project.</li>
 *   <li>COLLABORATOR: A user who is a current member of the project.</li>
 *   <li>MANAGER: A user who has managerial responsibilities in the project.</li>
 *   <li>CREATOR: A user who is the creator of the project.</li>
 *   <li>EXITED: A user who was a member of the project but has since left.</li>
 * </ul>
 */
public enum UserTypeInProject {

    /**
     * Represents a user who is a potential member of the project.
     */
    CANDIDATE,

    /**
     * Represents a user who is a current member of the project.
     */
    COLLABORATOR,

    /**
     * Represents a user who has managerial responsibilities in the project.
     */
    MANAGER,

    /**
     * Represents a user who is the creator of the project.
     */
    CREATOR,

    /**
     * Represents a user who was a member of the project but has since left.
     */
    EXITED
}