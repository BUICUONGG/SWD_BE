package swd.fpt.exegroupingmanagement.config;


import java.time.LocalDate;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import swd.fpt.exegroupingmanagement.constant.PredefinedRole;
import swd.fpt.exegroupingmanagement.entity.RoleEntity;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import swd.fpt.exegroupingmanagement.enums.UserStatus;
import swd.fpt.exegroupingmanagement.repository.RoleRepository;
import swd.fpt.exegroupingmanagement.repository.UserRepository;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            if (roleRepository.findByRoleName(PredefinedRole.ROLE_ADMIN).isEmpty()) {
                RoleEntity adminRole = RoleEntity.builder()
                        .roleName(PredefinedRole.ROLE_ADMIN)
                        .build();
                roleRepository.save(adminRole);
                UserEntity adminAccount = UserEntity.builder()
                            .email("admin@admin.com")
                        .fullName("Admin User")
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(2000, 1, 1))
                        .avatarUrl("https://vi.wikipedia.org/wiki/Cristiano_Ronaldo")
                        .role(adminRole)
                        .passwordHash(passwordEncoder.encode("admin"))
                        .build();
                userRepository.save(adminAccount);
            }

            if (roleRepository.findByRoleName(PredefinedRole.ROLE_STUDENT).isEmpty()) {

                RoleEntity studentRole = RoleEntity.builder()
                        .roleName(PredefinedRole.ROLE_STUDENT)
//                        .permissions(studentPermissions)
                        .build();
                roleRepository.save(studentRole);
                UserEntity studentAccount = UserEntity.builder()
                        .email("student@student.com")
                        .fullName("Student User")
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(2000, 1, 1))
                        .avatarUrl("https://example.com/student-avatar.png")
                        .role(studentRole)
                        .passwordHash(passwordEncoder.encode("student"))
                        .build();
                userRepository.save(studentAccount);
            }

            if (roleRepository.findByRoleName(PredefinedRole.ROLE_MENTOR).isEmpty()) {
                RoleEntity mentorRole = RoleEntity.builder()
                        .roleName(PredefinedRole.ROLE_MENTOR)
                        .build();
                roleRepository.save(mentorRole);
                UserEntity mentorAccount = UserEntity.builder()
                        .email("mentor@mentor.com")
                        .fullName("Mentor user")
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(2000, 1, 1))
                        .avatarUrl("https://example.com/mentor-avatar.png")
                        .role(mentorRole)
                        .passwordHash(passwordEncoder.encode("mentor"))
                        .build();
                userRepository.save(mentorAccount);
            }
        };
    }
}
