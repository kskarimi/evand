package com.kkarimi.eventmanagement.web;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
class TraceContextInitializationFilter extends OncePerRequestFilter {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String SPAN_ID_HEADER = "X-Span-Id";

    private final Tracer tracer;

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            writeTracingHeaders(response, currentSpan);
            filterChain.doFilter(request, response);
            return;
        }

        Span span = tracer.nextSpan().name(request.getMethod() + " " + request.getRequestURI()).start();
        try (Tracer.SpanInScope ignored = tracer.withSpan(span)) {
            writeTracingHeaders(response, span);
            filterChain.doFilter(request, response);
        } finally {
            span.end();
        }
    }

    private void writeTracingHeaders(HttpServletResponse response, Span span) {
        TraceContext context = span.context();
        if (context == null) {
            return;
        }
        response.setHeader(TRACE_ID_HEADER, context.traceId());
        response.setHeader(SPAN_ID_HEADER, context.spanId());
    }
}
