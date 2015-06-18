package net.unit8.weld;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kawasima
 */
@XmlRootElement
public class BeanDeploymentArchivesModel {
    private List<BeanDeploymentArchiveModel> beanDeploymentArchiveModel = new ArrayList<>();

    public List<BeanDeploymentArchiveModel> getBeanDeploymentArchiveModel() {
        return beanDeploymentArchiveModel;
    }

    public void setBeanDeploymentArchiveModel(List<BeanDeploymentArchiveModel> beanDeploymentArchiveModel) {
        this.beanDeploymentArchiveModel = beanDeploymentArchiveModel;
    }

    public void addBeanDeploymentArchive(BeanDeploymentArchiveModel model) {
        beanDeploymentArchiveModel.add(model);
    }
}
