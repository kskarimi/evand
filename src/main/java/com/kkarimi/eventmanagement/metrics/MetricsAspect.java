package com.kkarimi.eventmanagement.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
class MetricsAspect {

    private final MeterRegistry meterRegistry;

    MetricsAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Around("@annotation(measuredOperation)")
    public Object measure(ProceedingJoinPoint joinPoint, MeasuredOperation measuredOperation) throws Throwable {
        Timer.Sample sample = null;
        if (!measuredOperation.timer().isBlank()) {
            sample = Timer.start(meterRegistry);
        }

        try {
            Object result = joinPoint.proceed();
            if (!measuredOperation.successCounter().isBlank()) {
                meterRegistry.counter(measuredOperation.successCounter()).increment();
            }
            if (sample != null) {
                sample.stop(meterRegistry.timer(measuredOperation.timer()));
            }
            return result;
        } catch (Throwable ex) {
            if (!measuredOperation.failureCounter().isBlank()) {
                meterRegistry.counter(measuredOperation.failureCounter()).increment();
            }
            if (sample != null) {
                sample.stop(meterRegistry.timer(measuredOperation.timer()));
            }
            throw ex;
        }
    }
}
