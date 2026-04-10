package com.Jakima_Estate.services;

import com.Jakima_Estate.config.JwtUtils;
import com.Jakima_Estate.config.UserDetailsImpl;
import com.Jakima_Estate.dtos.AuthResponseDTO;
import com.Jakima_Estate.dtos.LoginDTO;
import com.Jakima_Estate.dtos.RegisterDTO;
import com.Jakima_Estate.dtos.ResponseDTO;
import com.Jakima_Estate.models.Role;
import com.Jakima_Estate.models.User;
import com.Jakima_Estate.repos.RoleRepository;
import com.Jakima_Estate.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public ResponseDTO authenticateUser(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        AuthResponseDTO response = new AuthResponseDTO(
                jwt,
                "Bearer",
                userDetails.getId(),
                userDetails.getFullName(),
                userDetails.getEmail(),
                role);

        return ResponseDTO.success("Login successful", response);
    }

    public ResponseDTO registerUser(RegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            return ResponseDTO.error("Email is already taken");
        }

        User user = new User();
        user.setFullName(registerDTO.getFullName());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setIsActive(true);

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role not found. Please run the application first to create roles."));

        List<Role> roles = new ArrayList<>();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return ResponseDTO.success("User registered successfully", null);
    }
}