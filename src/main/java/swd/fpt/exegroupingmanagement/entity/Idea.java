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
@Table(name = "idea", schema = "exegrouping")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Idea extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idea_id", nullable = false)
    Long id;

    @Size(max = 255)
    @Column(name = "name")
    String name;

    @Lob
    @Column(name = "description")
    String description;

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    Enrollment enrollment;

}