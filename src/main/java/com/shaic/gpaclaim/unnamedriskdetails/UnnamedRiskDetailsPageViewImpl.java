package com.shaic.gpaclaim.unnamedriskdetails;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class UnnamedRiskDetailsPageViewImpl extends AbstractMVPView implements UnnamedRiskDetailsPageView{

		
	@Inject 
	private Instance<UnnamedRiskDetailsPage> unnamedRiskDetailsPageInstance;

	private UnnamedRiskDetailsPage unnamedRiskDetailsPage;
	
	private UnnamedRiskDetailsPageDTO bean;
	
	private GWizard wizard;

	@PostConstruct
	public void initView() {
		
	}
	
	@Override
	public void resetView() {
		
		
	}


	@Override
	public void init(UnnamedRiskDetailsPageDTO bean) {
			
		addStyleName("view");
		setSizeFull();
		unnamedRiskDetailsPage = unnamedRiskDetailsPageInstance.get();
		unnamedRiskDetailsPage.init(bean);
		VerticalLayout mainPanel = new VerticalLayout(unnamedRiskDetailsPage);
		setCompositionRoot(mainPanel);
	}

	
	private void showSuccessMessage(String msg) {
		Label label = new Label(msg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		
	}
	
	@Override
	public void init(BeanItemContainer<SelectValue> category) {

		unnamedRiskDetailsPage.setDropDownValues(category);

	}

	@Override
	public void buildSuccessLayout() {
		// TODO Auto-generated method stub
		unnamedRiskDetailsPage.buildSuccessLayout();
	}

}
