package com.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScopedValuesDemo {
    private static final Logger logger = LoggerFactory.getLogger(ScopedValuesDemo.class);

    // Traditional ThreadLocal approach
    private static final ThreadLocal<RequestContext> threadLocalContext = new ThreadLocal<>();

    // Modern ScopedValue approach
    private static final ScopedValue<RequestContext> scopedContext = ScopedValue.newInstance();

    public static void main(String[] args) {
        // Demo with multiple threads
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Run multiple requests to demonstrate the difference
            for (int i = 0; i < 3; i++) {
                final int requestNum = i;
                executor.submit(() -> runWithThreadLocal(requestNum));
                executor.submit(() -> runWithScopedValue(requestNum));
            }

            // Give time for all tasks to complete
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Executes a task using ThreadLocal to manage request context.
     * 
     * This method demonstrates the traditional approach of using ThreadLocal
     * to pass context information through the execution of a request. It
     * creates a new RequestContext, sets it in the ThreadLocal, simulates
     * some work, and then cleans up the ThreadLocal to prevent memory leaks.
     *
     * @param requestNum An integer representing the request number, used to
     *                   generate unique user and request IDs.
     */
    private static void runWithThreadLocal(int requestNum) {
        String userId = "user-" + requestNum;
        String requestId = "req-" + ThreadLocalRandom.current().nextInt(1000);
        RequestContext context = new RequestContext(userId, requestId);

        try {
            // Set the context
            threadLocalContext.set(context);

            // Simulate some work
            logger.info("[ThreadLocal] Starting work with context: {}", threadLocalContext.get());
            simulateWork();
            logger.info("[ThreadLocal] Finished work with context: {}", threadLocalContext.get());

            // Simulate nested method call
            nestedMethodWithThreadLocal();
        } finally {
            // Clean up is mandatory with ThreadLocal
            threadLocalContext.remove();
        }
    }

    /**
     * Executes a task using ScopedValue to manage request context.
     * 
     * This method demonstrates the modern approach of using ScopedValue
     * to pass context information through the execution of a request. It
     * creates a new RequestContext, sets it in the ScopedValue, simulates
     * some work, and then automatically cleans up the context.
     *
     * @param requestNum An integer representing the request number, used to
     *                   generate unique user and request IDs.
     */
    private static void runWithScopedValue(int requestNum) {
        String userId = "user-" + requestNum;
        String requestId = "req-" + ThreadLocalRandom.current().nextInt(1000);
        RequestContext context = new RequestContext(userId, requestId);

        // Run with scoped value - cleanup is automatic
        ScopedValue.where(scopedContext, context).run(() -> {
            logger.info("[ScopedValue] Starting work with context: {}", scopedContext.get());
            simulateWork();
            logger.info("[ScopedValue] Finished work with context: {}", scopedContext.get());

            // Simulate nested method call
            nestedMethodWithScopedValue();
        });
    }

    private static void nestedMethodWithThreadLocal() {
        logger.info("[ThreadLocal] In nested method with context: {}", threadLocalContext.get());
    }

    private static void nestedMethodWithScopedValue() {
        logger.info("[ScopedValue] In nested method with context: {}", scopedContext.get());
    }

    private static void simulateWork() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
