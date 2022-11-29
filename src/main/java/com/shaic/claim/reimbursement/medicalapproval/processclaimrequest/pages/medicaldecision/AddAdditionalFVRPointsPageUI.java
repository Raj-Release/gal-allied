package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.fvrdetails.view.FVRTriggerPtsTable;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class AddAdditionalFVRPointsPageUI extends ViewComponent{

	@Inject
	private Instance<FVRTriggerPtsTable> triggerPtsTable;
	
	private FVRTriggerPtsTable triggerPtsTableObj;
	private Button btnSubmit;	
	private Button btnClose;
	private PreauthDTO bean;
	private Window popup;
	
	
	@Inject
	private ViewDetails viewDetails;
	
	private Button viewFVRDetails;
	
	@EJB
	private ViewFVRService viewFVRService;
	
	public void init(PreauthDTO preauthDto,Window popup){
		this.bean = preauthDto;
		this.popup = popup;
		triggerPtsTableObj = triggerPtsTable.get();		
		triggerPtsTableObj.init();
		triggerPtsTableObj.getIsDisableFields(Boolean.TRUE);
		if(bean.getPreauthMedicalDecisionDetails().getFvrAdditionalTriggerPtsList() != null && !bean.getPreauthMedicalDecisionDetails().getFvrAdditionalTriggerPtsList().isEmpty()){
			triggerPtsTableObj.setTableList(bean.getPreauthMedicalDecisionDetails().getFvrAdditionalTriggerPtsList());
		}
		
		viewFVRDetails = new Button("View FVR Details");
		viewFVRDetails.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				viewDetails.getFVRDetails(bean.getNewIntimationDTO()
						.getIntimationId(), false);
			}
		});
		
		
		btnSubmit = new Button("Submit");
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		
		btnClose = new Button("Cancel");
		btnClose.addStyleName(ValoTheme.BUTTON_DANGER);
		btnClose.setWidth("-1px");
		btnClose.setHeight("-10px");
		
		FormLayout tableFormLayout = new FormLayout();
		tableFormLayout.addComponent(triggerPtsTableObj);
		tableFormLayout.setSpacing(true);
		
		HorizontalLayout buttonHor = new HorizontalLayout(btnSubmit,btnClose);
		buttonHor.setSpacing(true);
		
		VerticalLayout mainVertical = new VerticalLayout(tableFormLayout,buttonHor);
		mainVertical.setSpacing(true);
		mainVertical.setComponentAlignment(buttonHor, Alignment.BOTTOM_CENTER);
		
		HorizontalLayout mainHorizontalLayout = new HorizontalLayout(mainVertical,viewFVRDetails);
		mainHorizontalLayout.setSpacing(true);
		addListener();
		setCompositionRoot(mainHorizontalLayout);
	}
	
		public void addListener(){
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(null != bean.getClaimKey()){
					FieldVisitRequest fieldVisitRequest = viewFVRService.getFVRByClaimKey(bean.getClaimKey());				
					if (fieldVisitRequest.getKey() != null) {
						bean.getPreauthMedicalDecisionDetails().setFvrAdditionalTriggerPtsList(triggerPtsTableObj.getValues());	
						viewFVRService.saveAdditionalTriggerPoints(fieldVisitRequest,bean.getPreauthMedicalDecisionDetails().getFvrAdditionalTriggerPtsList(),bean);
						
					}
				}
				popup.close();
				/*Collection<Window> windows = getUI().getCurrent().getWindows();
				for (Window window : windows) {
					window.close();
				}*/
			}
	});
		
		btnClose.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				popup.close();
				List<ViewFVRDTO> tableList = triggerPtsTableObj.getValues();
				if(null != tableList && !tableList.isEmpty()){
					for (ViewFVRDTO viewFVRDTO : tableList) {
						viewFVRDTO.setRemarks(null != viewFVRDTO.getExistingTriggerPoint() ?viewFVRDTO.getExistingTriggerPoint() : viewFVRDTO.getRemarks());
					}
				}
			}
		});
		
		
	}
	
	
	public Boolean showErrorPopUp(String msg) {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + msg + "</b>",
				ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox( msg + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
			}
		});
		return true;
	}
}
