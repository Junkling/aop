package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class AspectV6Advice {

    /**Around 는 반드시 proceed()를 통해 타겟의 메서드를 호출해야한다.
     그렇지 않으면 다음 코드가 실행되지 않는 오류가 발생한다.
     **/

    @Around("hello.aop.order.aop.Pointcuts.allOrderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            //@Before
            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            //@AfterReturning
            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            //@AfterThrowing
            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
            throw e;
        } finally {
            //@After
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
        }
    }

    @Before("hello.aop.order.aop.Pointcuts.allOrderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[before] {}", joinPoint.getSignature());
    }
    @AfterReturning(value="hello.aop.order.aop.Pointcuts.allOrderAndService()",returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("[return] {}", joinPoint.getSignature(),result);
    }
    @AfterThrowing(value="hello.aop.order.aop.Pointcuts.allOrderAndService()",throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[ex] {}", joinPoint.getSignature(),ex);
    }
    @After("hello.aop.order.aop.Pointcuts.allOrderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature());
    }
}
