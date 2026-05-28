package com.ev.ready.admin.auth.controller;

import com.ev.ready.admin.auth.dto.AdminAuthResponse;
import com.ev.ready.admin.auth.dto.AdminLoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/admin/auth")
public class AdminAuthController {

    private final AuthenticationManager authenticationManager;
    private final HttpSessionSecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public AdminAuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public AdminAuthResponse login(
            @Valid @RequestBody AdminLoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.username(),
                    request.password()
            ));
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid admin credentials.");
        }

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        securityContextRepository.saveContext(securityContext, httpRequest, httpResponse);

        return response(authentication, "Admin login successful.");
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @GetMapping("/me")
    public AdminAuthResponse me(Authentication authentication) {
        return response(authentication, "Admin session active.");
    }

    private AdminAuthResponse response(Authentication authentication, String message) {
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new AdminAuthResponse(true, authentication.getName(), roles, message);
    }
}
