package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
@Slf4j
public class WithinTest {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method hellMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        hellMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void withinExact() {
        pointcut.setExpression("within(hello.aop.member.MemberServiceImpl)");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void withinStar() {
        pointcut.setExpression("within(hello.aop.member.Member*)");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void withinSubPackage() {
        pointcut.setExpression("within(hello.aop.member..*)");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }

    //within은 부모타입과 자식타입은 매칭되지 않음
    @Test
    void withinSubTypeFalse() {
        pointcut.setExpression("within(hello.aop.member.MemberService)");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isFalse();
    }
    //execution 은 부모타입과 자식타입이 매칭 가능함
    @Test
    void executionSuperTypeTrue() {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        assertThat(pointcut.matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }

}
