package com.Jakima_Estate.controllers;

import com.Jakima_Estate.dtos.ResponseDTO;
import com.Jakima_Estate.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin management endpoints")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/roles")
    @Operation(summary = "Get all roles")
    public ResponseDTO getAllRoles() {
        return adminService.getAllRoles();
    }

    @PostMapping("/roles")
    @Operation(summary = "Create a new role")
    public ResponseDTO createRole(@RequestParam String roleName) {
        return adminService.createRole(roleName);
    }

    @DeleteMapping("/roles/{roleId}")
    @Operation(summary = "Delete a role")
    public ResponseDTO deleteRole(@PathVariable Long roleId) {
        return adminService.deleteRole(roleId);
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users")
    public ResponseDTO getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/users/{userId}/roles")
    @Operation(summary = "Get user roles")
    public ResponseDTO getUserRoles(@PathVariable Long userId) {
        return adminService.getUserRoles(userId);
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get user with roles")
    public ResponseDTO getUserWithRoles(@PathVariable Long userId) {
        return adminService.getUserWithRoles(userId);
    }

    @PostMapping("/users/{userId}/roles/{roleName}")
    @Operation(summary = "Assign role to user")
    public ResponseDTO assignRoleToUser(
            @PathVariable Long userId,
            @PathVariable String roleName) {
        return adminService.assignRoleToUser(userId, roleName);
    }

    @DeleteMapping("/users/{userId}/roles/{roleName}")
    @Operation(summary = "Remove role from user")
    public ResponseDTO removeRoleFromUser(
            @PathVariable Long userId,
            @PathVariable String roleName) {
        return adminService.removeRoleFromUser(userId, roleName);
    }

    @PatchMapping("/users/{userId}/toggle-status")
    @Operation(summary = "Enable/disable user account")
    public ResponseDTO toggleUserStatus(@PathVariable Long userId) {
        return adminService.toggleUserStatus(userId);
    }
}