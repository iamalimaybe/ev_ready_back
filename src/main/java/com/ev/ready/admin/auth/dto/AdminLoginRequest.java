package com.ev.ready.admin.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminLoginRequest(
        @NotBlank
        @Size(max = 120)
        String username,

        @NotBlank
        @Size(max = 200)
        String password
) {
}
