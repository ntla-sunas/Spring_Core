package slide1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "main.java")


public class AppConfig {
    @Bean
    public QuizMaster springQuizMaster() {
        return new SpringQuizMaster();
    }

    @Bean

    public QuizMasterService quizMasterService() {
        QuizMasterService service = new QuizMasterService();
        service.setQuizMaster(springQuizMaster());
        return service;
    }
}