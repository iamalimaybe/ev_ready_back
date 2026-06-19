package com.ev.ready.ai.filter;

import com.ev.ready.ai.config.AiRecommendationRateLimitProperties;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AiRecommendationRateLimitFilter extends OncePerRequestFilter {

    private static final String TARGET_PATH = "/api/v1/ai/recommendations";

    private final AiRecommendationRateLimitProperties properties;
    private final Map<String, ClientBucket> buckets = new ConcurrentHashMap<>();

    public AiRecommendationRateLimitFilter(AiRecommendationRateLimitProperties properties) {
        this.properties = properties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !HttpMethod.POST.matches(request.getMethod())
                || !TARGET_PATH.equals(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        cleanupExpiredBuckets();

        String clientKey = resolveClientKey(request);
        ClientBucket clientBucket = buckets.computeIfAbsent(clientKey, ignored -> new ClientBucket(newBucket()));
        clientBucket.markSeen();

        ConsumptionProbe probe = clientBucket.bucket().tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            response.setHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            filterChain.doFilter(request, response);
            return;
        }

        long waitSeconds = Math.max(1, probe.getNanosToWaitForRefill() / 1_000_000_000L);

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json");
        response.setHeader("Retry-After", String.valueOf(waitSeconds));
        response.getWriter().write("""
                {"timestamp":"%s","status":429,"error":"Too Many Requests","message":"Too many AI recommendation requests. Please wait before trying again.","path":"%s"}\
                """.formatted(
                escapeJson(OffsetDateTime.now().toString()),
                escapeJson(request.getRequestURI())
        ));
    }

    private Bucket newBucket() {
        Bandwidth limit = Bandwidth.builder()
                .capacity(properties.maxRequests())
                .refillGreedy(properties.maxRequests(), properties.window())
                .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    private String resolveClientKey(HttpServletRequest request) {
        String remoteAddress = request.getRemoteAddr();

        if (isLocalProxy(remoteAddress)) {
            String cloudflareIp = request.getHeader("CF-Connecting-IP");

            if (hasText(cloudflareIp)) {
                return cloudflareIp.trim();
            }

            String forwardedFor = request.getHeader("X-Forwarded-For");

            if (hasText(forwardedFor)) {
                return forwardedFor.split(",")[0].trim();
            }

            String realIp = request.getHeader("X-Real-IP");

            if (hasText(realIp)) {
                return realIp.trim();
            }
        }

        return remoteAddress;
    }

    private boolean isLocalProxy(String remoteAddress) {
        return "127.0.0.1".equals(remoteAddress)
                || "0:0:0:0:0:0:0:1".equals(remoteAddress)
                || "::1".equals(remoteAddress);
    }

    private void cleanupExpiredBuckets() {
        Instant cutoff = Instant.now().minus(properties.entryTtl());
        Iterator<Map.Entry<String, ClientBucket>> iterator = buckets.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, ClientBucket> entry = iterator.next();

            if (entry.getValue().lastSeenAt().isBefore(cutoff)) {
                iterator.remove();
            }
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static final class ClientBucket {

        private final Bucket bucket;
        private volatile Instant lastSeenAt;

        private ClientBucket(Bucket bucket) {
            this.bucket = bucket;
            this.lastSeenAt = Instant.now();
        }

        private Bucket bucket() {
            return bucket;
        }

        private Instant lastSeenAt() {
            return lastSeenAt;
        }

        private void markSeen() {
            this.lastSeenAt = Instant.now();
        }
    }
}