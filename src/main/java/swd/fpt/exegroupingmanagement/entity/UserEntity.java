package swd.fpt.exegroupingmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import org.hibernate.annotations.Nationalized;
import swd.fpt.exegroupingmanagement.enums.Gender;
import swd.fpt.exegroupingmanagement.enums.Provider;
import swd.fpt.exegroupingmanagement.enums.UserStatus;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long userId;

    @Column(name = "email", unique = true, length = 50)
    String email;

    @Column(name = "password_hash", length = 255)
    String passwordHash;

    @Column(name = "full_name", nullable = false, length = 50)
    @Nationalized
    String fullName;

    @Column(name = "avatar_url")
    String avatarUrl;

    @Column(name = "providerId")
    String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    @Builder.Default
    Provider provider = Provider.LOCAL;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status")
    UserStatus status = UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name="gender")
    @Builder.Default
    Gender gender = Gender.FEMALE;

    @Column(name="dob")
    LocalDate dob;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    RoleEntity role;

    @ManyToOne
    @JoinColumn(name = "major_id")
    MajorEntity major;
}
