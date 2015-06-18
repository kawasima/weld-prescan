package net.unit8.weld;

import org.jboss.weld.bootstrap.api.CDI11Bootstrap;
import org.jboss.weld.bootstrap.spi.BeanDeploymentArchive;
import org.jboss.weld.bootstrap.spi.BeansXml;
import org.jboss.weld.bootstrap.spi.Deployment;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.resources.spi.ResourceLoader;

import javax.xml.bind.JAXB;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

/**
 * @author kawasima
 */
public class Prescanner {
    public InputStream scan() {
        final StringWriter sw = new StringWriter();

        new Weld() {
            @Override
            protected Deployment createDeployment(ResourceLoader resourceLoader, CDI11Bootstrap bootstrap) {
                Deployment deployment = super.createDeployment(resourceLoader, bootstrap);
                BeanDeploymentArchivesModel archivesModel = new BeanDeploymentArchivesModel();
                for (BeanDeploymentArchive bda : deployment.getBeanDeploymentArchives()) {
                    BeanDeploymentArchiveModel model = new BeanDeploymentArchiveModel();
                    model.setId(bda.getId());
                    BeansXml beansXml = bda.getBeansXml();
                    model.setDiscoveryMode(beansXml.getBeanDiscoveryMode());
                    model.setEnabledAlternatives(StringMetadata.from(beansXml.getEnabledAlternativeClasses()));
                    model.setEnabledAlternativeStereotypes(StringMetadata.from(beansXml.getEnabledAlternativeStereotypes()));
                    model.setEnabledDecorators(StringMetadata.from(beansXml.getEnabledDecorators()));
                    model.setEnabledInterceptors(StringMetadata.from(beansXml.getEnabledInterceptors()));
                    //model.setScanning(beansXml.getScanning());
                    if (beansXml.getUrl() != null)
                        model.setUrl(beansXml.getUrl());
                    model.setVersion(beansXml.getVersion());

                    model.setBeanClasses(bda.getBeanClasses());
                    archivesModel.addBeanDeploymentArchive(model);
                }
                JAXB.marshal(archivesModel, sw);

                return deployment;
            }
        }.initialize();

        try {
            return new ByteArrayInputStream(sw.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
