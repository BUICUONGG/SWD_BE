package swd.fpt.exegroupingmanagement.service;

import swd.fpt.exegroupingmanagement.dto.response.UserResponse;
import swd.fpt.exegroupingmanagement.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity getUserById(Long id);

    UserResponse getMyInform();

    List<UserResponse> getAllUsers();

    void restoreUser(Long id);

    UserEntity getUserByEmail(String email);

    UserEntity getActiveUser(String email);

    void deleteUser(Long id);
}
