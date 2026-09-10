package com.gym.engagement.app.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("!execution(* com.gym.engagement.app.service.common.CredentialsGenerator.generatePassword(..))")
    public void excludeGeneratePassword() {
    }

    @Pointcut("execution(* com.gym.engagement.app.service..*(..))")
    public void serviceLayer() {
    }

    @Pointcut("(serviceLayer() " +
            "|| execution(* com.gym.engagement.app.dao..*(..)) " +
            "|| execution(* com.gym.engagement.app.facade..*(..)))")
    public void appLayers() {
    }

    @Around("appLayers() && excludeGeneratePassword()")
    public Object logTimeAndDebug(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        if (log.isDebugEnabled()) {
            log.debug("Entering {}.{}() with args: {}",
                    className, methodName, joinPoint.getArgs());
        }

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - startTime;

        if (log.isDebugEnabled()) {
            log.debug("Exiting {}.{}() with result: {} [Execution time: {} ms]",
                    className, methodName, result, duration);
        }

        return result;
    }

    @AfterReturning(pointcut = "serviceLayer() && excludeGeneratePassword()", returning = "result")
    public void logSuccessfulBusinessEvent(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String action = determineBusinessAction(methodName);

        if (action != null) {
            String className = joinPoint.getSignature().getDeclaringTypeName();
            String domainEntity = extractDomainEntity(className);
            String contextDetails = extractContextDetails(joinPoint, result);

            log.info("BUSINESS EVENT | Action: [{}] | Domain: [{}] | Context: [{}]",
                    action, domainEntity, contextDetails);
        }
    }

    @AfterThrowing(pointcut = "appLayers() && excludeGeneratePassword()", throwing = "ex")
    public void logSystemException(JoinPoint joinPoint, Throwable ex) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        log.warn("Exception in {}.{}() with message = '{}'", className, methodName, ex.getMessage(), ex);
    }

    private String determineBusinessAction(String methodName) {
        if (methodName.startsWith("save") || methodName.startsWith("create")) {
            return "CREATE";
        }

        if (methodName.startsWith("update")) {
            return "UPDATE";
        }

        if (methodName.startsWith("delete")) {
            return "DELETE";
        }

        if (methodName.startsWith("find") || methodName.startsWith("get")) {
            return "READ";
        }

        return null;
    }

    private String extractContextDetails(JoinPoint joinPoint, Object result) {
        Object[] args = joinPoint.getArgs();

        if (args != null && args.length > 0 && args[0] instanceof Number) {
            return "ID=" + args[0];
        }

        if (result != null) {
            return result.toString();
        }

        return "No additional details";
    }

    private String extractDomainEntity(String className) {
        String simpleName = className.substring(className.lastIndexOf('.') + 1);

        return simpleName.replace("ServiceImpl", "").replace("Service", "");
    }
}