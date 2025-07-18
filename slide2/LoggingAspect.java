package slide2;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @After("execution(* slide2.MyService.m*(..))")
    public void logAndNotify() {
        System.out.println("[AOP] Logging & Notifying after m* method");
    }
}

