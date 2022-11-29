package com.shaic.claim.misc.updatesublimit.wizard;

import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.misc.updatesublimit.SearchUpdateSublimitTableDTO;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billingprocess.FinancialProcessPagePresenter;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class UpdateSublimitWizardViewImpl extends AbstractMVPView implements UpdateSublimitWizard{
	
	@Inject
	private Instance<UpdateSublimitWizardPage> updateSublimitWizard;
	
	private UpdateSublimitWizardPage updateSublimitWizardPage;
	
	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;
	
	@Inject
	private ViewDetails viewDetails;
	
	private PreauthDTO bean;


	@Override
	public void init(PreauthDTO preauthDTO) {
		
		this.bean = preauthDTO;
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(this.bean,  "Update Sublimit");
		
		updateSublimitWizardPage = updateSublimitWizard.get();
		updateSublimitWizardPage.init(preauthDTO);
		fireViewEvent(UpdateSublimitPresenter.BILLING_PROCESS_SET_UP_REFERENCE_FOR_SUBLIMIT, bean);
		
		viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(),bean.getKey(), ViewLevels.PREAUTH_MEDICAL,"Update Sublimit");
		
		VerticalLayout mainVertical = new VerticalLayout(intimationDetailsCarousel,viewDetails,updateSublimitWizardPage);
		
		mainVertical.setSpacing(true);
		
		setCompositionRoot(mainVertical);
	}
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'>Sublimit updated successfully.</b>",
				ContentMode.HTML);

		Button homeButton = new Button("Home Page");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				fireViewEvent(MenuItemBean.UPDATE_SUBLIMIT, null);

			}
		});
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		updateSublimitWizardPage.setupReferences(referenceData);
	}

	@Override
	public void getValuesForMedicalDecisionTable(
			DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		updateSublimitWizardPage.setAppropriateValuesToDTOFromProcedure(dto, medicalDecisionTableValues);
	}

	@Override
	public void editSublimitValues(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		updateSublimitWizardPage.editSublimitValuesForMedicalDescionTable(dto,medicalDecisionTableValues);
	}


}
