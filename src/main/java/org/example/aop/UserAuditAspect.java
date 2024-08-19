package org.example.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
public class UserAuditAspect {
    @Around("execution(* org.example.repository..*(..))")
    public Object auditMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result;
        try {
            System.out.println("**************************");
            log.info("Method {} is starting", joinPoint.getSignature().toShortString());
            result = joinPoint.proceed();
        } finally {
            long timeTaken = System.currentTimeMillis() - startTime;
            log.info("Method {} finished in {} ms", joinPoint.getSignature().toShortString(), timeTaken);
        }
        return result;
    }
}
