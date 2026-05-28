package com.ev.ready.admin.auth.dto;

import java.util.List;

public record AdminAuthResponse(
        boolean authenticated,
        String username,
        List<String> roles,
        String message
) {
}
