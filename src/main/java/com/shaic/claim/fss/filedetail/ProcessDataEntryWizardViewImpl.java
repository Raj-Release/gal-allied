/**
 * 
 */
package com.shaic.claim.fss.filedetail;

import java.util.Map;

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

import com.shaic.arch.SHAConstants;
import com.shaic.claim.fss.searchfile.SearchDataEntryTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.IWizard;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;



/**
 * 
 *
 */
public class ProcessDataEntryWizardViewImpl extends AbstractMVPView implements ProcessDataEntryWizard {
	
	@Inject
	private Instance<ProcessDataEntryView> processFileDataEntryObj;
	
	private ProcessDataEntryView processFileDataEntry;
	
	private Panel mainPanel;
	
	private IWizard wizard;
	
	private FieldGroup binder;
	
	private SearchDataEntryTableDTO bean;
	
	private Window popup;
	
	private void initBinder() {
		wizard.getFinishButton().setCaption("Submit");
		this.binder = new FieldGroup();
		BeanItem<SearchDataEntryTableDTO> item = new BeanItem<SearchDataEntryTableDTO>(bean);
		this.binder.setItemDataSource(item);	
	}
	
	public void initView(SearchDataEntryTableDTO dto)
	{
		this.bean = dto;
		mainPanel = new Panel();
		//this.wizard = iWizard.get();
		this.wizard = new IWizard();
		initBinder();	
		processFileDataEntry = processFileDataEntryObj.get();
		processFileDataEntry.init(bean,wizard);
		wizard.addStep(this.processFileDataEntry,"File Storage Data Entry");
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);	
		/*PreauthIntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(this.bean.getClaimDTO().getNewIntimationDto(), this.bean.getClaimDTO(),  "Acknowledge Receipt of Documents");
		mainPanel.setFirstComponent(intimationDetailsCarousel);*/
		
	//	VerticalLayout wizardLayout1 = new VerticalLayout(commonButtonsLayout());
		
		
	/*	Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
		panel1.setHeight("50px");*/
		//VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
		
		
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setWidth("100%");
		hLayout.setHeight("2px");
		//VerticalLayout wizardLayout2 = new VerticalLayout(commonBtnLayout, wizard);
		VerticalLayout wizardLayout2 = new VerticalLayout(hLayout, wizard);
	//	wizardLayout2.setComponentAlignment(commonBtnLayout, Alignment.MIDDLE_RIGHT);
		wizardLayout2.setSpacing(true);
		//wizardLayout2.setHeight("50px");
		mainPanel.setContent(wizardLayout2);
		
		
		mainPanel.setSizeFull();
		//mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
		//mainPanel.setHeight("700px");
		
		setCompositionRoot(mainPanel);			
		}

	public void getErrorMessage(String eMsg){

			Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Alert");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
     }
	
	
	@PostConstruct
	public void initView() {
		addStyleName("view");
		
		setSizeFull();			
	}
	

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wizardSave(GWizardEvent event) {
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
		if(processFileDataEntry.onAdvance())
		{
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			processFileDataEntry.setTableValuesToDTO();
			fireViewEvent(ProcessDataEntryPresenter.SUBMIT_FILE_DETAILS, bean, userName);
		}
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		// TODO Auto-generated method stub
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
		        "No", "Yes", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
		                    // Confirmed to continue
		                	fireViewEvent(MenuItemBean.WAR_HOUSE, null);
		                } else {
		                    // User did not confirm
		                }
		            }
		        });
		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void buildSuccessLayout(String statusMsg) {
		/*String successMessage = "";
		if(null != rrcRequestNo)
		{
			 successMessage = "RRC RequestNo" + " " + rrcRequestNo + " Successfully submitted processing !!!";
		}
		else {
			 successMessage = "Failure occured while submitting RRC Request.Please contact administrator.!!!";
		}*/
		Label successLabel = null;
		
		if(statusMsg.equalsIgnoreCase(SHAConstants.SUCCESS_FLAG)){
			successLabel = new Label(
					"<b style = 'color: green;'>" + "Saved Successfully" + "</b>",
					ContentMode.HTML);
		}else if(statusMsg.equalsIgnoreCase(SHAConstants.FAILURE_FLAG)){
			successLabel = new Label(
					"<b style = 'color: red;'>" + "Failed to Save" + "</b>",
					ContentMode.HTML);
		}
		

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
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
				fireViewEvent(MenuItemBean.WAR_HOUSE, null);
			
			}
		});
		
		
	}

	@Override
	public void init(PreauthDTO bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onAdvance() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}
}
