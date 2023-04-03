package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ArgsTest {
    Method hellMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        hellMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    private AspectJExpressionPointcut pointcut(String expression) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);
        return pointcut;
    }

    /**
     * 타입 판단 기준
     * execution : 메서드의 시그니처로 판단 (정적)
     * args : 런타임에 전달된 인수로 판단 (동적)
     */

    @Test
    void args() {
        assertThat(pointcut("args(String)").matches(hellMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(Object)").matches(hellMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args()").matches(hellMethod, MemberServiceImpl.class)).isFalse();
        assertThat(pointcut("args(..)").matches(hellMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(*)").matches(hellMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(String,..)").matches(hellMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsAndExecution() {
        //args
        assertThat(pointcut("args(String)").matches(hellMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(java.io.Serializable)").matches(hellMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(Object)").matches(hellMethod, MemberServiceImpl.class)).isTrue();

        //execution
        assertThat(pointcut("execution(* *(String))").matches(hellMethod, MemberServiceImpl.class)).isTrue();

        //false
        assertThat(pointcut("execution(* *(java.io.Serializable))").matches(hellMethod, MemberServiceImpl.class)).isFalse();
        assertThat(pointcut("execution(* *(Object))").matches(hellMethod, MemberServiceImpl.class)).isFalse();
    }
}
