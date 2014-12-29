package it.unitn.introsde.spring;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import it.unitn.introsde.ServiceConfiguration;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.beans.PropertyVetoException;

/**
 * Spring configuration:
 * - datasource
 * - hibernate c3p0
 * - transactional manager
 * - REST Template
 * - JSR-303 Bean Validation
 */
@Configuration
@ComponentScan("it.unitn.introsde")
@EnableWebMvc
@EnableTransactionManagement
public class ApplicationContext {

    @Bean(name = "dataSource")
    public ComboPooledDataSource getDataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("org.h2.Driver");
        dataSource.setJdbcUrl("jdbc:h2:mem:" + ServiceConfiguration.SCHEMA + ":;INIT=CREATE SCHEMA IF NOT EXISTS " + ServiceConfiguration.SCHEMA);
        dataSource.setUser("sa");
        dataSource.setPassword("sa");
        return dataSource;
    }

    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(ComboPooledDataSource dataSource) {
        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
        sessionBuilder.scanPackages("it.unitn.introsde.persistence.entity");
        sessionBuilder.setProperty("hibernate.show_sql", "true");
        sessionBuilder.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        sessionBuilder.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        return sessionBuilder.buildSessionFactory();
    }

    @Autowired
    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

    @Bean(name = "restTemplate")
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean(name = "validatorFactory")
    public ValidatorFactory getValidatorFactory() {
        return Validation.buildDefaultValidatorFactory();
    }
}
