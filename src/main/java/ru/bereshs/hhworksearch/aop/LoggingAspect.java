package ru.bereshs.hhworksearch.aop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Around("@annotation(Loggable)")
    public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long duration = (System.currentTimeMillis() - startTime);
        String methodName = proceedingJoinPoint.getTarget().getClass().getName() + "." + proceedingJoinPoint.getSignature().getName();

        if (result instanceof List<?>) {
            log.info("{}: method completed at {} ms and returned list with size {} elements", methodName, duration, ((List<?>) result).size());
        } else if (result instanceof String) {
            log.info("{}: method completed at {} ms and returned string with content {}", methodName, duration, result);
        }

        return result;
    }

    @AfterThrowing(pointcut = "@annotation(Loggable)", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        String methodName = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
        log.info("{}: method exception {}", methodName, exception.getMessage());
    }
}
