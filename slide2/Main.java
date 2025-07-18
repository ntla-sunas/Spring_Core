package slide2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        MyService service = context.getBean(MyService.class);
        service.m1(); // sẽ trigger aspect
        service.n1(); // sẽ không trigger aspect
    }
}
