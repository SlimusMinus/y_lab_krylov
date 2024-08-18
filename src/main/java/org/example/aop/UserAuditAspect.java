package org.example.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Slf4j
public class UserAuditAspect {
    @Pointcut("execution(* org.example.repository.CarStorage.getAll()")
    public void greeting() {}

    @Before("greeting()")
    public void beforeAdvice() {
        System.out.print("Привет ");
    }
}
