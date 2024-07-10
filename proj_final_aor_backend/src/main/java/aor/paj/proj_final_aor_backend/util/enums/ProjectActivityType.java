package aor.paj.proj_final_aor_backend.util.enums;
/**
 * The NotificationType enum is used to define the different types of notifications that can be sent within the system.
 * Each enum value represents a different type of notification.
 *
 * MESSAGE_RECEIVED: This notification type is used when a message is received.
 * NEW_PROJECT: This notification type is used when a new project is created.
 * PROJECT_STATE_CHANGE: This notification type is used when there is a change in the state of a project.
 * MESSAGE_PROJECT: This notification type is used when a message is sent within a project.
 * NEW_MEMBER: This notification type is used when a new member joins a project.
 * MEMBER_EXIT: This notification type is used when a member exits a project.
 */
public enum ProjectActivityType {
    ADDED_MEMBER,
    REMOVED_MEMBER,
    EDIT_TASK_STATE,
    EDIT_PROJECT_DATA,
    EDIT_PROJECT_STATE,
    ADDED_CANDIDATE,
    UPDATED_MEMBER_ROLE,
    ADDED_RESOURCE,
    MEMBER_COMMENT
}
