package aor.paj.proj_final_aor_backend.dto;

/**
 * This enum represents the type of a user.
 * <li> ADMIN: Represents an administrative user</li>
 * <li> AUTHENTICATED_USER: Represents a user who has authenticated</li>
 * <li>NOT_AUTHENTICATED_USER: Represents a user who has not authenticated. </li>
 */
public enum UserType {

    /**
     * Represents an administrative user.
     */
    ADMIN,
    /**
     * Represents a user who has authenticated.
     */
    AUTHENTICATED_USER,

    /**
     * Represents a user who has not authenticated.
     */
    NOT_AUTHENTICATED_USER
}
