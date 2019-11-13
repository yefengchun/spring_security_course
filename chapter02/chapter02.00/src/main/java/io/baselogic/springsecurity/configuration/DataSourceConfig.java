package io.baselogic.springsecurity.configuration;

import io.baselogic.springsecurity.dao.EventRowMapper;
import io.baselogic.springsecurity.dao.UserRowMapper;
import io.baselogic.springsecurity.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.h2.server.web.WebServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.SQLException;

/**
 * Database Configuration
 */
@Configuration
@EnableTransactionManagement
@Slf4j
public class DataSourceConfig {


    //-------------------------------------------------------------------------

    @Bean
    public UserRowMapper userRowMapper(){
        return new UserRowMapper("users.");
    }

    @Bean
    public UserRowMapper ownerRowMapper(){
        return new UserRowMapper("owner_");
    }

    @Bean
    public UserRowMapper attendeeRowMapper(){
        return new UserRowMapper("attendee_");
    }

    @Bean
    public EventRowMapper eventRowMapper(){
        return new EventRowMapper(ownerRowMapper(),
                                                           attendeeRowMapper());
    }

    @Bean
    public String EVENT_QUERY(){
         return "select e.id, e.summary, e.description, e.event_date, " +
                "owner.id as owner_id, owner.email as owner_email, owner.password as owner_password, owner.first_name as owner_first_name, owner.last_name as owner_last_name, " +
                "attendee.id as attendee_id, attendee.email as attendee_email, attendee.password as attendee_password, attendee.first_name as attendee_first_name, attendee.last_name as attendee_last_name " +
                "from events as e, users as owner, users as attendee " +
                "where e.owner = owner.id and e.attendee = attendee.id";
    }

    @Bean
    public String USER_QUERY(){
        return "SELECT id, email, password, first_name, last_name FROM users WHERE ";
    }



//    @Bean
//    public MethodValidationPostProcessor methodValidationPostProcessor() {
//        return new MethodValidationPostProcessor();
//    }

    //-------------------------------------------------------------------------


    /**
     * Access the H2 Console:
     * http://localhost:8080/admin/h2/
     * @return
     */
    @Bean
    public ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/admin/h2/*");
        return registrationBean;

    }

} // The End...