package net.unit8.weld;

import com.google.common.collect.ImmutableSet;
import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.bootstrap.api.CDI11Bootstrap;
import org.jboss.weld.bootstrap.api.TypeDiscoveryConfiguration;
import org.jboss.weld.bootstrap.spi.BeansXml;
import org.jboss.weld.bootstrap.spi.Deployment;
import org.jboss.weld.bootstrap.spi.Metadata;
import org.jboss.weld.environment.deployment.WeldBeanDeploymentArchive;
import org.jboss.weld.environment.deployment.WeldDeployment;
import org.jboss.weld.environment.deployment.WeldResourceLoader;
import org.jboss.weld.environment.deployment.discovery.DiscoveryStrategy;
import org.jboss.weld.environment.deployment.discovery.DiscoveryStrategyFactory;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.contexts.ThreadScoped;
import org.jboss.weld.metadata.BeansXmlImpl;
import org.jboss.weld.metadata.MetadataImpl;
import org.jboss.weld.resources.spi.ClassFileServices;
import org.jboss.weld.resources.spi.ResourceLoader;

import javax.enterprise.inject.spi.Extension;
import javax.xml.bind.JAXB;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author kawasima
 */
public class PrescannedWeld extends Weld {
    InputStream deploymentStream;
    private Set<Metadata<Extension>> extensions;

    private List<Metadata<String>> convert(List<StringMetadata> stringMetadataList) {
        List<Metadata<String>> metadataList = new ArrayList<>();

        for (StringMetadata stringMetadata : stringMetadataList) {
            Metadata<String> metadata = new MetadataImpl<String>(stringMetadata.getValue(), stringMetadata.getLocation());
            metadataList.add(metadata);

        }
        return metadataList;

    }
    private Set<WeldBeanDeploymentArchive> loadArchives() {
        BeanDeploymentArchivesModel archivesModel = JAXB.unmarshal(deploymentStream, BeanDeploymentArchivesModel.class);
        Set<WeldBeanDeploymentArchive> archives = new HashSet<>();
        for (BeanDeploymentArchiveModel model : archivesModel.getBeanDeploymentArchiveModel()) {
            BeansXml beansXml = new BeansXmlImpl(
                    convert(model.getEnabledAlternatives()),
                    convert(model.getEnabledAlternativeStereotypes()),
                    convert(model.getEnabledDecorators()),
                    convert(model.getEnabledInterceptors()),
                    null,
                    model.getUrl(),
                    model.getDiscoveryMode(),
                    model.getVersion()
            );
            WeldBeanDeploymentArchive archive = new WeldBeanDeploymentArchive(
                    model.getId(),
                    model.getBeanClasses(),
                    beansXml);
            archives.add(archive);
        }
        return archives;
    }

    @Override
    protected Deployment createDeployment(ResourceLoader resourceLoader, CDI11Bootstrap bootstrap) {
        final Iterable<Metadata<Extension>> loadedExtensions = loadExtensions(WeldResourceLoader.getClassLoader(), bootstrap);
        final TypeDiscoveryConfiguration typeDiscoveryConfiguration = bootstrap.startExtensions(loadedExtensions);

        Deployment deployment=null;
        DiscoveryStrategy strategy = DiscoveryStrategyFactory.create(
                resourceLoader,
                bootstrap,
                ImmutableSet.<Class<? extends Annotation>>builder().addAll(typeDiscoveryConfiguration.getKnownBeanDefiningAnnotations())
                        // Add ThreadScoped manually as Weld SE doesn't support implicit bean archives without beans.xml
                        .add(ThreadScoped.class).build());
        Set<WeldBeanDeploymentArchive> discoveredArchives = loadArchives();

        /*
        String isolation = AccessController.doPrivileged(new GetSystemPropertyAction(ARCHIVE_ISOLATION_SYSTEM_PROPERTY));

        if (isolation != null && Boolean.valueOf(isolation).equals(Boolean.FALSE)) {
            WeldBeanDeploymentArchive archive = WeldBeanDeploymentArchive.merge(bootstrap, discoveredArchives);
            deployment = new WeldDeployment(resourceLoader, bootstrap, Collections.singleton(archive), loadedExtensions);
            CommonLogger.LOG.archiveIsolationDisabled();
        } else {
            deployment=  new WeldDeployment(resourceLoader, bootstrap, discoveredArchives, loadedExtensions);
            CommonLogger.LOG.archiveIsolationEnabled();
        }
        */
        deployment=  new WeldDeployment(resourceLoader, bootstrap, discoveredArchives, loadedExtensions);

        if(strategy.getClassFileServices() != null) {
            deployment.getServices().add(ClassFileServices.class, strategy.getClassFileServices());
        }

        return deployment;
    }

    private Iterable<Metadata<Extension>> loadExtensions(ClassLoader classLoader, Bootstrap bootstrap) {
        Iterable<Metadata<Extension>> iter = bootstrap.loadExtensions(classLoader);
        if (extensions != null) {
            Set<Metadata<Extension>> set = new HashSet<Metadata<Extension>>(extensions);
            for (Metadata<Extension> ext : iter) {
                set.add(ext);
            }
            return set;
        } else {
            return iter;
        }
    }

    public PrescannedWeld setDeploymentStream(InputStream is) {
        this.deploymentStream = is;
        return this;
    }
}
