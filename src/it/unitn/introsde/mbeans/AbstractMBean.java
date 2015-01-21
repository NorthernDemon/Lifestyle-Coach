package it.unitn.introsde.mbeans;

import it.unitn.introsde.persistence.process.AbstractProcess;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Convenient class to abstract managed beans for message passing
 */
public abstract class AbstractMBean extends AbstractProcess {

    protected ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    protected Map<String, Object> sessionMap = externalContext.getSessionMap();

    protected static Date getDate(String[] arr) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(arr[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(arr[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(arr[2]));
        return calendar.getTime();
    }
}
