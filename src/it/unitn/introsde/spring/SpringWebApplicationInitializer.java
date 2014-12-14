package it.unitn.introsde.spring;

import it.unitn.introsde.persistence.dao.MeasureDao;
import it.unitn.introsde.persistence.dao.PersonDao;
import it.unitn.introsde.persistence.entity.Person;
import it.unitn.introsde.util.RandomUtil;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Initialize Spring-WEBMVC for RESTFul calls
 */
public class SpringWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(ApplicationContext.class);
        context.setServletContext(servletContext);
        context.refresh();

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("SpringDispatcher", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        initData(context, 100);
    }

    /**
     * Init database with random fatty girls.
     * <p/>
     * Database consist of two tables
     * - person - generates given magicNumber of people
     * - health_history - generates quarter of magicNumber of random measurement history for each person
     *
     * @param context     - spring application context
     * @param magicNumber - number of people to generate
     */
    private void initData(AnnotationConfigWebApplicationContext context, int magicNumber) {
        PersonDao personDao = context.getBean(PersonDao.class);
        MeasureDao measureDao = context.getBean(MeasureDao.class);
        for (int i = 0; i < magicNumber; i++) {
            Person person = personDao.save(RandomUtil.getPerson());
            for (int j = 0; j < magicNumber / 4; j++) {
                measureDao.save(RandomUtil.getMeasure(person));
            }
        }
    }
}