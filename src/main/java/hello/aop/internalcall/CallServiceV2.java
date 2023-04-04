package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CallServiceV2 {
    //    private final ApplicationContext applicationContext;
    private final ObjectProvider<CallServiceV2> callServiceV2Provider;

    public CallServiceV2(ObjectProvider<CallServiceV2> callServiceV2Provider) {
        this.callServiceV2Provider = callServiceV2Provider;
    }
//
//    public CallServiceV2(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//    }


    public void external() {
        log.info("call external");
//        applicationContext.getBean(CallServiceV2.class).internal();     //내부 메서드 호출(this.internal())
        callServiceV2Provider.getObject().internal();
    }

    public void internal() {
        log.info("call internal");
    }
}
