package net.unit8.weld;

import org.jboss.weld.environment.se.WeldContainer;
import org.junit.Test;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import java.util.Set;

/**
 * @author kawasima
 */
public class PrescannerTest {
    @Test
    public void test() {
        WeldContainer container = new PrescannedWeld()
                .setDeploymentStream(new Prescanner().scan()).initialize();

        BeanManager bm = container.getBeanManager();
        Set<Bean<?>> beans = bm.getBeans(InjectedObject.class);
        Bean<?> bean = bm.resolve(beans);
        CreationalContext<?> ctx = bm.createCreationalContext(bean);
        InjectedObject obj = (InjectedObject) bm.getReference(bean, bean.getBeanClass(), ctx);
        obj.hello();
    }

    @Test
    public void deployFromFile() {
        WeldContainer container = new PrescannedWeld()
                .setDeploymentStream(getClass().getResourceAsStream("/weld-deployment.xml")).initialize();

        BeanManager bm = container.getBeanManager();
        Set<Bean<?>> beans = bm.getBeans(InjectedObject.class);
        Bean<?> bean = bm.resolve(beans);
        CreationalContext<?> ctx = bm.createCreationalContext(bean);
        InjectedObject obj = (InjectedObject) bm.getReference(bean, bean.getBeanClass(), ctx);
        obj.hello();
    }
}
