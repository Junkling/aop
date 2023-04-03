package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
@Import(ParameterTest.ParameterAspect.class)

public class ParameterTest {
    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy = {}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ParameterAspect {
        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember() {
        }

        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0];
            log.info("[logArgs1]{} , arg={}", joinPoint.getSignature(), arg1);
            return joinPoint.proceed();
        }
        @Around("allMember()&&args(arg,..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[logArgs2]{} , arg={}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }
        @Before("allMember()&&args(arg,..)")
        public void logArgs3(String arg) throws Throwable {
            log.info("[logArgs3] arg={}", arg);
        }
        @Before("allMember()&&this(obj)")
        public void thisArgs(JoinPoint joinPoint,MemberService obj) throws Throwable {
            log.info("[this]{} arg={}", joinPoint.getSignature(),obj.getClass());
        }
        @Before("allMember()&&target(obj)")
        public void targetArgs(JoinPoint joinPoint,MemberService obj) throws Throwable {
            log.info("[target]{} arg={}", joinPoint.getSignature(),obj.getClass());
        }
        @Before("allMember()&&@target(annotation)")
        public void annotationArgs1(JoinPoint joinPoint, ClassAop annotation) throws Throwable {
            log.info("[@target]{} arg={}", joinPoint.getSignature(),annotation);
        }
        @Before("allMember()&&@within(annotation)")
        public void annotationArgs2(JoinPoint joinPoint, ClassAop annotation) throws Throwable {
            log.info("[@within]{} arg={}", joinPoint.getSignature(),annotation);
        }
        @Before("allMember()&&@annotation(annotation)")
        public void annotationArgs3(JoinPoint joinPoint, MethodAop annotation) throws Throwable {
            log.info("[@annotation]{} annotationValue={}", joinPoint.getSignature(), annotation.value());
        }
    }
}
