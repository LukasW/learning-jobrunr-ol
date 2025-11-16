package ch.css.learning.jobrunr.batch.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mail_sending")
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MailSending {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mail_sending_seq")
    @SequenceGenerator(name = "mail_sending_seq", sequenceName = "mail_sending_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String mailTemplateKey;

    @Column(name = "sent_date", nullable = false)
    private LocalDateTime sentDate;

    @ManyToOne
    private User user;

    @PrePersist
    protected void onCreate() {
        sentDate = LocalDateTime.now();
    }
}