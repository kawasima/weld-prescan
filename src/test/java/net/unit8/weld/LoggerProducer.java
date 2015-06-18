package net.unit8.weld;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Named;
import java.util.logging.Logger;

/**
 * @author kawasima
 */
@Named
@Dependent
public class LoggerProducer {
    @Produces
    public Logger getLogger(InjectionPoint ip) {
        return Logger.getLogger(ip.getMember().getDeclaringClass().getName());
    }
}
