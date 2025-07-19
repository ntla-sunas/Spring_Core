package slide3;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.security.PublicKey;
import java.util.List;
import java.util.UUID;

// ====== MAIN =====
public class Main {
    public static void main(String[ ] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService service = context.getBean(UserService.class);

        User user = new User(0, "lan anh", "lananh@example.com");
        service.register(user);

        List<User> users = service.getAll();
        users.forEach(u -> System.out.println(u.getId() + " - " + u.getName() + " - " + u.getEmail()));

        context.close();
    }
}

// ===== MODEL =====
class User {
    private int id;
    private String name;
    private String email;

    public User() {}
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() {return name; }
    public void setName(String name) {this.name = name; }
    public String getEmail() {return email; }
    public void setEmail(String email) {this.email = email; }

}

// ===== DAO =====
class UserDao {
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(User user) {
        String sql = "INSERT INTO users(name, email) VALUES (?, ?)";
        jdbcTemplate.update(sql, user.getName(), user.getEmail());
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User (
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email")
        ));
    }
}

// ===== SERVICE =====
class UserService {
    private UserDao userDao;

    public void setUserDao(UserDao dao) {
        this.userDao = dao;
    }

    public void register(User user) {
        userDao.save(user);
    }
    public List<User> getAll() {
        return userDao.findAll();
    }
}

// ===== AOP =====
@Aspect
class LoggingAspect {
    @After("execution(* UserDao.*(..))")
    public void logAfterDb() {
        System.out.println("[AOP] A DB method in UserDao was called");
    }
}

//  ===== CONFIGURATION =====
@Configuration
@EnableAspectJAutoProxy

class AppConfig {
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/spring_demo");

        ds.setUsername("root");
        ds.setPassword("123456");
        return ds;
    }

    @Bean public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean public UserDao userDao() {
        UserDao dao = new UserDao();
        dao.setJdbcTemplate(jdbcTemplate());
        return dao;
    }

    @Bean public UserService userService() {
        UserService service = new UserService();
        service.setUserDao(userDao());
        return service;
    }

    @Bean public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}