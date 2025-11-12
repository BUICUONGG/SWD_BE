package swd.fpt.exegroupingmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "checklog_api", schema = "exegrouping")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class ChecklogApi extends BaseEntity {
    @Id
    @Column(name = "checklog_api_id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    @Lob
    @Column(name = "reason")
    String reason;

    @Column(name = "date")
    Instant date;
}