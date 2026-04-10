package com.Jakima_Estate.services;

import com.Jakima_Estate.dtos.ResponseDTO;
import com.Jakima_Estate.models.Role;
import com.Jakima_Estate.models.User;
import com.Jakima_Estate.repos.RoleRepository;
import com.Jakima_Estate.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public ResponseDTO getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return ResponseDTO.success("All roles retrieved", roles);
    }

    @Transactional
    public ResponseDTO createRole(String roleName) {
        if (roleRepository.existsByName(roleName)) {
            return ResponseDTO.error("Role already exists");
        }

        Role role = new Role();
        role.setName(roleName.toUpperCase());
        roleRepository.save(role);

        return ResponseDTO.success("Role created successfully", role);
    }

    @Transactional
    public ResponseDTO deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null) {
            return ResponseDTO.error("Role not found");
        }

        if (role.getName().equals("USER") || role.getName().equals("ADMIN")) {
            return ResponseDTO.error("Cannot delete default roles: USER or ADMIN");
        }

        roleRepository.deleteById(roleId);
        return ResponseDTO.success("Role deleted successfully", null);
    }

    public ResponseDTO getUserRoles(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseDTO.error("User not found");
        }

        List<String> roles = userRepository.getUserRoles(userId);
        return ResponseDTO.success("User roles retrieved", roles);
    }

    @Transactional
    public ResponseDTO assignRoleToUser(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseDTO.error("User not found");
        }

        Role role = roleRepository.findByName(roleName.toUpperCase()).orElse(null);
        if (role == null) {
            return ResponseDTO.error("Role not found");
        }

        List<String> currentRoles = userRepository.getUserRoles(userId);
        if (currentRoles.contains(roleName.toUpperCase())) {
            return ResponseDTO.error("User already has this role");
        }

        userRepository.addRoleToUser(userId, role.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("userEmail", user.getEmail());
        response.put("assignedRole", roleName);
        response.put("allRoles", userRepository.getUserRoles(userId));

        return ResponseDTO.success("Role assigned successfully", response);
    }

    @Transactional
    public ResponseDTO removeRoleFromUser(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseDTO.error("User not found");
        }

        Role role = roleRepository.findByName(roleName.toUpperCase()).orElse(null);
        if (role == null) {
            return ResponseDTO.error("Role not found");
        }

        List<String> currentRoles = userRepository.getUserRoles(userId);
        if (!currentRoles.contains(roleName.toUpperCase())) {
            return ResponseDTO.error("User does not have this role");
        }

        if (roleName.toUpperCase().equals("USER") && currentRoles.size() == 1) {
            return ResponseDTO.error("User must have at least one role");
        }

        userRepository.removeRoleFromUser(userId, role.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("userEmail", user.getEmail());
        response.put("removedRole", roleName);
        response.put("remainingRoles", userRepository.getUserRoles(userId));

        return ResponseDTO.success("Role removed successfully", response);
    }

    public ResponseDTO getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseDTO.success("All users retrieved", users);
    }

    public ResponseDTO getUserWithRoles(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseDTO.error("User not found");
        }

        List<String> roles = userRepository.getUserRoles(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("fullName", user.getFullName());
        response.put("email", user.getEmail());
        response.put("phone", user.getPhone());
        response.put("isActive", user.getIsActive());
        response.put("roles", roles);
        response.put("createdAt", user.getCreatedAt());

        return ResponseDTO.success("User details retrieved", response);
    }

    @Transactional
    public ResponseDTO toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseDTO.error("User not found");
        }

        user.setIsActive(!user.getIsActive());
        userRepository.save(user);

        return ResponseDTO.success("User status updated", Map.of(
                "userId", userId,
                "isActive", user.getIsActive()
        ));
    }
}