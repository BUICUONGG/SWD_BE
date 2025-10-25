package swd.fpt.exegroupingmanagement.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.constant.PredefinedRole;
import swd.fpt.exegroupingmanagement.dto.response.UserResponse;
import swd.fpt.exegroupingmanagement.entity.RoleEntity;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import swd.fpt.exegroupingmanagement.enums.UserStatus;
import swd.fpt.exegroupingmanagement.exception.ErrorCode;
import swd.fpt.exegroupingmanagement.exception.exceptions.BusinessException;
import swd.fpt.exegroupingmanagement.exception.exceptions.ForbiddenException;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.exception.exceptions.UnauthorizedException;
import swd.fpt.exegroupingmanagement.mapper.UserMapper;
import swd.fpt.exegroupingmanagement.repository.UserRepository;
import swd.fpt.exegroupingmanagement.service.UserService;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    UserMapper userMapper;
    UserRepository userRepository;

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    @Override
    public UserResponse getMyInform() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = getUserByEmail(name);
        return userMapper.toEntityDTO(userEntity);
    }
    @Override
    public List<UserResponse> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll().stream().filter(userEntity -> !userEntity.isDeleted()).toList();
        return userMapper.toEntityDTO(userEntities);
    }
    @Override
    public void restoreUser(Long id) {
        UserEntity entity = getUserById(id);
        entity.setDeleted(false);
        userRepository.save(entity);
    }
    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));
    }
    @Override
    public UserEntity getActiveUser(String email){
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() ->
                new UnauthorizedException(ErrorCode.UNAUTHENTICATED.getMessage()));
        if(!userEntity.getStatus().equals(UserStatus.ACTIVE)){
            throw new ForbiddenException(ErrorCode.USER_LOCKED.getMessage());
        }
        if (userEntity.isDeleted()) {
            throw new ResourceNotFoundException(ErrorCode.USER_DELETED.getMessage());
        }
        return userEntity;
    }
    @Override
    public void deleteUser(Long id) {
        UserEntity user = getUserById(id);
        if (user.getRole() != null) {
            handleAdminUser(user.getRole());
        }
        user.setStatus(UserStatus.LOCKED);
        user.setDeleted(true);
        userRepository.save(user);
    }

    private void handleAdminUser(RoleEntity entity) {
        if (PredefinedRole.ROLE_ADMIN.equals(entity.getRoleName())) {
            throw new BusinessException("Tài khoản ADMIN không được tùy chỉnh!");
        }
    }
}
