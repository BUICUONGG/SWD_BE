package swd.fpt.exegroupingmanagement.service;

import java.util.List;

import swd.fpt.exegroupingmanagement.dto.request.UserRequest;
import swd.fpt.exegroupingmanagement.dto.response.UserResponse;
import swd.fpt.exegroupingmanagement.entity.UserEntity;

public interface UserService {
    UserEntity getUserById(Long id);

    UserResponse getMyInform();

    List<UserResponse> getAllUsers();

    void restoreUser(Long id);

    UserResponse getUserResponseById(Long id);

    UserEntity getUserByEmail(String email);

    UserEntity getActiveUser(String email);

    void deleteUser(Long id);
    
    // Search method
    List<UserResponse> searchUsers(String keyword);
    
    // Create user
    UserResponse createUser(UserRequest request);
}
