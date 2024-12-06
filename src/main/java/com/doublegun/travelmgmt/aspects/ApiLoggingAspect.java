package com.doublegun.travelmgmt.aspects;

import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApiLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ApiLoggingAspect.class);

    // Pointcut that matches all methods in controllers
    @Pointcut("execution(public * com.doublegun.travelmgmt.controllers..*(..))")
    public void apiMethods() {
        // This is just a placeholder method for the pointcut expression
    }

    // Before the method is executed
    @Before("apiMethods()")
    public void logBefore() {
        logger.info("API Request received");
    }

    // After the method is executed, regardless of success or failure
    @After("apiMethods()")
    public void logAfter() {
        logger.info("API Request completed");
    }

    // After the method successfully completes
    @AfterReturning(value = "apiMethods()", returning = "result")
    public void logAfterReturning(Object result) {
        logger.info("API Response: {}", result);
    }

    // After the method throws an exception
    @AfterThrowing(value = "apiMethods()", throwing = "exception")
    public void logAfterThrowing(Exception exception) {
        logger.error("API Exception: {}", exception.getMessage());
    }
}
