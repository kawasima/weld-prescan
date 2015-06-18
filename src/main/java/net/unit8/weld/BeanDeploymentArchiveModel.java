package net.unit8.weld;

import org.jboss.weld.bootstrap.spi.BeanDiscoveryMode;

import javax.xml.bind.annotation.XmlElementWrapper;
import java.net.URL;
import java.util.Collection;
import java.util.List;

/**
 * @author kawasima
 */
public class BeanDeploymentArchiveModel {
    private String id;

    private List<StringMetadata> enabledAlternatives;
    private List<StringMetadata> enabledAlternativeStereotypes;
    private List<StringMetadata> enabledDecorators;
    private List<StringMetadata> enabledInterceptors;

    private URL url;
    private BeanDiscoveryMode discoveryMode;
    private String version;

    private Collection<String> beanClasses;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElementWrapper
    public List<StringMetadata> getEnabledAlternatives() {
        return enabledAlternatives;
    }

    public void setEnabledAlternatives(List<StringMetadata> enabledAlternatives) {
        this.enabledAlternatives = enabledAlternatives;
    }

    @XmlElementWrapper
    public List<StringMetadata> getEnabledAlternativeStereotypes() {
        return enabledAlternativeStereotypes;
    }

    public void setEnabledAlternativeStereotypes(List<StringMetadata> enabledAlternativeStereotypes) {
        this.enabledAlternativeStereotypes = enabledAlternativeStereotypes;
    }

    @XmlElementWrapper
    public List<StringMetadata> getEnabledDecorators() {
        return enabledDecorators;
    }

    public void setEnabledDecorators(List<StringMetadata> enabledDecorators) {
        this.enabledDecorators = enabledDecorators;
    }

    @XmlElementWrapper
    public List<StringMetadata> getEnabledInterceptors() {
        return enabledInterceptors;
    }

    public void setEnabledInterceptors(List<StringMetadata> enabledInterceptors) {
        this.enabledInterceptors = enabledInterceptors;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public BeanDiscoveryMode getDiscoveryMode() {
        return discoveryMode;
    }

    public void setDiscoveryMode(BeanDiscoveryMode discoveryMode) {
        this.discoveryMode = discoveryMode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XmlElementWrapper
    public Collection<String> getBeanClasses() {
        return beanClasses;
    }

    public void setBeanClasses(Collection<String> beanClasses) {
        this.beanClasses = beanClasses;
    }
}
