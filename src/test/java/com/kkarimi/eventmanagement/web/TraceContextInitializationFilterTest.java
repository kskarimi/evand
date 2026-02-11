package com.kkarimi.eventmanagement.web;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TraceContextInitializationFilterTest {

    @Test
    void shouldUseCurrentSpanAndSetTraceHeaders() throws ServletException, IOException {
        Tracer tracer = mock(Tracer.class);
        Span currentSpan = mock(Span.class);
        TraceContext traceContext = mock(TraceContext.class);

        when(tracer.currentSpan()).thenReturn(currentSpan);
        when(currentSpan.context()).thenReturn(traceContext);
        when(traceContext.traceId()).thenReturn("trace-1");
        when(traceContext.spanId()).thenReturn("span-1");

        TraceContextInitializationFilter filter = new TraceContextInitializationFilter(tracer);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(
                new MockHttpServletRequest("GET", "/api/events"),
                response,
                new MockFilterChain()
        );

        assertEquals("trace-1", response.getHeader("X-Trace-Id"));
        assertEquals("span-1", response.getHeader("X-Span-Id"));
        verify(tracer, never()).nextSpan();
    }

    @Test
    void shouldCreateSpanWhenNoCurrentSpanAndSetTraceHeaders() throws ServletException, IOException {
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        TraceContext traceContext = mock(TraceContext.class);
        Tracer.SpanInScope scope = mock(Tracer.SpanInScope.class);

        when(tracer.currentSpan()).thenReturn(null);
        when(tracer.nextSpan()).thenReturn(span);
        when(span.name(anyString())).thenReturn(span);
        when(span.start()).thenReturn(span);
        when(tracer.withSpan(span)).thenReturn(scope);
        when(span.context()).thenReturn(traceContext);
        when(traceContext.traceId()).thenReturn("trace-2");
        when(traceContext.spanId()).thenReturn("span-2");

        TraceContextInitializationFilter filter = new TraceContextInitializationFilter(tracer);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(
                new MockHttpServletRequest("POST", "/api/registrations"),
                response,
                new MockFilterChain()
        );

        assertEquals("trace-2", response.getHeader("X-Trace-Id"));
        assertEquals("span-2", response.getHeader("X-Span-Id"));
        verify(span).end();
        verify(scope).close();
    }

    @Test
    void shouldFilterAsyncAndErrorDispatches() {
        TraceContextInitializationFilter filter = new TraceContextInitializationFilter(mock(Tracer.class));
        assertFalse(filter.shouldNotFilterAsyncDispatch());
        assertFalse(filter.shouldNotFilterErrorDispatch());
    }
}
