package com.fiap.pj.infra.util.security;

import com.fiap.pj.infra.security.UserDetailsImpl;
import org.springframework.messaging.Message;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.UUID;

public class SecurityContextUtils {

    private SecurityContextUtils() {
    }

    public static AbstractAuthenticationToken getAuthentication() {
        return (AbstractAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    public static UUID getUsuarioId() {
        if (Objects.isNull(getAuthentication())) {
            return null;
        }

        if (getAuthentication().getPrincipal() instanceof UserDetailsImpl userDetails) {
            return userDetails.getId();
        }

        if (getAuthentication().getPrincipal() instanceof AuthenticatedUser authenticatedUser) {
            return authenticatedUser.userId();
        }

        return null;
    }

    public static void configurarSecurityContext(Message message) {
        var userId = (String) message.getHeaders().get("userId");

        if (Objects.isNull(userId)) {
            return;
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        new AuthenticatedUser(UUID.fromString(userId)),
                        null,
                        null
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    public static void clear() {
        SecurityContextHolder.clearContext();
    }
}
