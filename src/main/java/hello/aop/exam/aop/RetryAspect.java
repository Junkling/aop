package hello.aop.exam.aop;

import hello.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public class RetryAspect {
//    @Around("@annotation(hello.aop.exam.annotation.Retry)")
    @Around("@annotation(retry)")
    public Object retry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        log.info("[retry] {} retry = {}", joinPoint.getSignature(), retry);
        int max = retry.value();
        Exception holder = null;
        for (int retryCount = 1; retryCount <= max; retryCount++) {
            try {
                log.info("[retry] try count={}/{}", retryCount, max);
                return joinPoint.proceed();
            } catch (Exception e) {
                log.info("[retry] try count++");
                holder = e;
            }
        }
        throw holder;
    }
}
