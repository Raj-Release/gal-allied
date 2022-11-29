package com.shaic.claim.intimation;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.MVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.annotation.LabelProperties;

import com.shaic.arch.utils.Lang;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.navigator.View;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings("serial")
@ViewScoped
@CDIView(value = MenuItemBean.INDEX)
public class IndexPage extends AbstractMVPView implements MVPView ,View {
    @Inject
    private Lang lang;
    @Inject
    @LabelProperties(valueKey = "helpwindow-content", contentMode = ContentMode.HTML)
    private Label label;

    @PostConstruct
    public void init() {
        final VerticalLayout mainLayout = new VerticalLayout(label);
        setCompositionRoot(mainLayout);
        localize(null);
    }

    void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameters) {
        setCaption(lang.getText("helpwindow-caption"));
    }

}
