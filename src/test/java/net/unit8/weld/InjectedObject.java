package net.unit8.weld;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by kawasima on 15/06/18.
 */
@Dependent
public class InjectedObject {
    @Inject
    Logger logger;

    public void hello() {
        logger.log(Level.INFO, "Hello, world");
    }
}
