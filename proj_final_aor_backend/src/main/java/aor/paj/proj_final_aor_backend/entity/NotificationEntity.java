package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="notifications")
public class NotificationEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;


    @Column(name = "sender_id", nullable = false, unique = false, updatable = false)
    private long sender_id;

    @Column(name = "receiver_id", nullable = false, unique = false, updatable = false)
    private long receiver_id;

    @Column(name = "readStatus", nullable = false, unique = false, updatable = true)
    private boolean readStatus;

    @Column(name = "sendTimestamp", nullable = false, unique = false, updatable = true)
    private LocalDateTime sendTimestamp;

    @Column(name = "readTimestamp", nullable = false, unique = false, updatable = true)
    private LocalDateTime readTimestamp;

    @Column(name = "type", nullable = false, unique = false, updatable = true)
    private String type;


    public NotificationEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public LocalDateTime getSendTimestamp() {
        return sendTimestamp;
    }

    public void setSendTimestamp(LocalDateTime sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    public LocalDateTime getReadTimestamp() {
        return readTimestamp;
    }

    public void setReadTimestamp(LocalDateTime readTimestamp) {
        this.readTimestamp = readTimestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSender_id() {
        return sender_id;
    }

    public void setSender_id(long sender_id) {
        this.sender_id = sender_id;
    }

    public long getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(long receiver_id) {
        this.receiver_id = receiver_id;
    }
}
