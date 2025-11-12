package swd.fpt.exegroupingmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
@SuperBuilder
@Table(name = "system_config", schema = "exegrouping")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemConfig extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "system_config_id", nullable = false)
    Long id;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    String status;
}