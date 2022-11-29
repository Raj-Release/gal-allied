package com.shaic.reimbursement.investigationmaster;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.event.GWizardEvent;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardInitEvent;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.shaic.arch.CrmFlaggedComponents;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizard;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigationPresenter;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigationWizard;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorView;
import com.shaic.reimbursement.assigninvesigation.ConfirmationViewImpl;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class InvestigationMasterWizardViewImpl extends AbstractMVPView implements InvestigationMasterWizardView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private Panel mainPanel;

	@Inject
	private IWizard wizard;

	@Inject
	private InvestigationMasterDTO investigationMasterDTO;

	@Inject
	private Instance<InvestigationMasterUI> investigationMasterUIInstance;

	private InvestigationMasterUI investigationMasterUIObj;

	private FieldGroup binder;

	private PreauthDTO preauthDTO = null;

	private TextArea hospitalAddress;

	private TextArea proposerAddress;

	private void initBinder() {
		//wizard.getFinishButton().setCaption("Submit");
		this.binder = new FieldGroup();
		BeanItem<InvestigationMasterDTO> item = new BeanItem<InvestigationMasterDTO>(investigationMasterDTO);
		this.binder.setItemDataSource(item);
	}

	public void initView(InvestigationMasterDTO investigationMasterDTO) {
		try{
			this.wizard = new IWizard();
			this.investigationMasterDTO = investigationMasterDTO;
			initBinder();
			this.preauthDTO = new PreauthDTO();

			investigationMasterUIObj = investigationMasterUIInstance.get();
			investigationMasterUIObj.init(this.investigationMasterDTO,wizard);
			//wizard.addStep(investigationMasterUIObj, "Investigation Master");

			
			VerticalLayout viewLayout = new VerticalLayout(investigationMasterUIObj);
			viewLayout.setSpacing(true);
			Panel mainPanel = new Panel();
			mainPanel.setContent(viewLayout);
			mainPanel.setCaption("Investigator Master");
			mainPanel.setSizeFull();
			mainPanel.setHeight("700px");
			setCompositionRoot(mainPanel);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}



	@Override
	public void resetView() {
		// TODO Auto-generated method stub
	}

	@Override
	public void activeStepChanged(WizardStepActivationEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void wizardCompleted(WizardCompletedEvent event) {

	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {

/*		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
				"No", "Yes", new ConfirmDialog.Listener() {

			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			public void onClose(ConfirmDialog dialog) {
				//				assignInvestigatorDto = new AssignInvestigatorDto();
				if (!dialog.isConfirmed()) {

					wizard.releaseHumanTask();
					fireViewEvent(MenuItemBean.SHOW_ASSIGN_INVESTIGATION, null);

				} else {
					//                    dialog.close();
				}
			}
		});

		dialog.setStyleName(Reindeer.WINDOW_BLACK);*/
	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void wizardSave(GWizardEvent event) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("static-access")
	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label("<b style = 'color: black;'>Details Saved successfully !!!</b>", ContentMode.HTML);

		Button homeButton = new Button("Home Page");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);

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
				fireViewEvent(MenuItemBean.INVESTIGATION_MASTER, null);

			}
		});
	}

}

