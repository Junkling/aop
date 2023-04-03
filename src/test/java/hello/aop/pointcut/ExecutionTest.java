package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ExecutionTest {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method hellMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        hellMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod() {
        log.info("helloMethod = {}", hellMethod);
    }

    @Test
    void exactMatch() {
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void allMatch() {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void nameMatch1() {
        pointcut.setExpression("execution(* hello(..))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void nameMatch2() {
        pointcut.setExpression("execution(* he*(..))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void nameMatch3() {
        pointcut.setExpression("execution(* *llo(..))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void nameMatch4() {
        pointcut.setExpression("execution(* *el*(..))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void packageMatch1() {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void packageMatch2() {
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void packageFalse() {
        pointcut.setExpression("execution(* hello.aop.*.*(..))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isFalse();
    }
    @Test
    void packageMatchSubPackage1() {
        pointcut.setExpression("execution(* hello.aop.member..*.*(..))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void packageMatchSubPackage2() {
        pointcut.setExpression("execution(* hello.aop..*.*(..))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void typeExactMatch() {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void typeSuperTypeMatch() {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void typeMatchInternal() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        Method internal = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internal, MemberServiceImpl.class)).isFalse();
    }
    @Test
    void argsMatch() {
        pointcut.setExpression("execution(* *(String))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void argsMatchNoArgs() {
        pointcut.setExpression("execution(* *())");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isFalse();
    }
    @Test
    void argsMatchAllType() {
        pointcut.setExpression("execution(* *(*))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
    //모든 타입의 여러 파라미터
    @Test
    void argsMatchAllTypeAndMulti() {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
    //시작 파라미터 타입, 그후 모든 타입 허용
    //(String,..)-> 처음만 String 이면 됨
    //(String,*)-> 두개의 파라미터 중 첫 파라미터가 String 이어야함
    @Test
    void argsMatchAll() {
        pointcut.setExpression("execution(* *(String,..))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
}
