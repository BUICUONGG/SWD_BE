package swd.fpt.exegroupingmanagement.config;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import swd.fpt.exegroupingmanagement.constant.PredefinedRole;
import swd.fpt.exegroupingmanagement.entity.RoleEntity;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import swd.fpt.exegroupingmanagement.enums.UserStatus;
import swd.fpt.exegroupingmanagement.repository.PermissionRepository;
import swd.fpt.exegroupingmanagement.repository.RoleRepository;
import swd.fpt.exegroupingmanagement.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    UserRepository userRepository;
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            if (roleRepository.findByRoleName(PredefinedRole.ROLE_ADMIN).isEmpty()) {
                RoleEntity adminRole = RoleEntity.builder()
                        .roleName(PredefinedRole.ROLE_ADMIN)
//                        .permissions(new HashSet<>(permissions.values()))
                        .build();
                roleRepository.save(adminRole);
                UserEntity adminAccount = UserEntity.builder()
                            .email("admin@admin.com")
                        .fullName("Admin User")
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(2000, 1, 1))
                        .avatarUrl("https://vi.wikipedia.org/wiki/Cristiano_Ronaldo")
                        .roles(Set.of(adminRole))
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
                        .roles(Set.of(studentRole))
                        .passwordHash(passwordEncoder.encode("student"))
                        .build();
                userRepository.save(studentAccount);
            }

            if (roleRepository.findByRoleName(PredefinedRole.ROLE_MENTOR).isEmpty()) {
                RoleEntity mentorRole = RoleEntity.builder()
                        .roleName(PredefinedRole.ROLE_MENTOR)
//                        .permissions(mentorPermissions)
                        .build();
                roleRepository.save(mentorRole);
                UserEntity mentorAccount = UserEntity.builder()
                        .email("mentor@mentor.com")
                        .fullName("Mentor user")
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(2000, 1, 1))
                        .avatarUrl("https://example.com/mentor-avatar.png")
                        .roles(Set.of(mentorRole))
                        .passwordHash(passwordEncoder.encode("mentor"))
                        .build();
                userRepository.save(mentorAccount);
            }
        };
    }
}
