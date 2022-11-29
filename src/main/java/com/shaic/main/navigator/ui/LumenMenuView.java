package com.shaic.main.navigator.ui;

import org.vaadin.addon.cdimvp.MVPView;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.create.LumenPolicySearchResultTableDTO;
import com.shaic.claim.lumen.create.LumenRequestDTO;
import com.shaic.claim.lumen.create.LumenSearchResultTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface LumenMenuView extends MVPView{
	void setViewG(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree);
	void renderLumenInitiateRequestPage(Class<? extends GMVPView> viewClass,LumenSearchResultTableDTO resultDTO);
	void renderLevelOneWizardPage(Class<? extends GMVPView> viewClass, LumenRequestDTO resultDTO);
	void renderCoordinatorWizardPage(Class<? extends GMVPView> viewClass, LumenRequestDTO resultDTO);
	void renderLevelTwoWizardPage(Class<? extends GMVPView> viewClass, LumenRequestDTO resultDTO);
	void renderMISQueryWizardPage(Class<? extends GMVPView> viewClass, LumenRequestDTO lumenRequestDTO);
	void renderInitiatorWizardPage(Class<? extends GMVPView> viewClass, LumenRequestDTO resultDTO);
	void renderPolicyLumenInitiateRequestPage(Class<? extends GMVPView> viewClass,	LumenPolicySearchResultTableDTO resultDTO);
	void setLumenStatusReportView(Class<? extends GMVPView> viewClass, BeanItemContainer<SelectValue> cpuCodeContainer, BeanItemContainer<SelectValue> lumenStatusContainer, BeanItemContainer<SelectValue> clmTypeContainer);

}
