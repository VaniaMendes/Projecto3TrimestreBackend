package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="messages")
public class MessageEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;


    @Column(name = "readStatus", nullable = false, unique = false, updatable = true)
    private boolean readStatus;

    @Column(name = "sendTimestamp", nullable = false, unique = false, updatable = true)
    private LocalDateTime sendTimestamp;

    @Column(name = "readTimestamp", nullable = false, unique = false, updatable = true)
    private LocalDateTime readTimestamp;

    @Column(name = "content", nullable = false, unique = false, updatable = true)
    private String content;

    @ManyToOne
    @JoinColumn(name="sender_id", nullable = false, unique = false, updatable = true)
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name="receiver_id", nullable = false, unique = false, updatable = true)
    private UserEntity receiver;


}
