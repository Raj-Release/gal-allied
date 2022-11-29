package com.shaic.claim.fvrgrading.page;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.fvrgrading.search.SearchFvrReportGradingTableDto;
import com.shaic.claim.preauth.wizard.dto.FvrGradingDetailsDTO;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimTableDto;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.NewFVRGradingDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.NewMedicalDecisionFVRGrading;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.ConvertClaimDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class FvrReportGradingPage extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private FvrReportGradingPageDto bean;
	
	private BeanFieldGroup<FvrReportGradingPageDto> binder;
	
	private TextArea remarks;
	
	private Button convertbutton;
	
	private Button cancelBtn;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private VerticalLayout verticalLayout;
	
	private HorizontalLayout buttonHorLayout;
	
	private SearchFvrReportGradingTableDto searchFormDto;
	
	private VerticalLayout verticalMain;

	@Inject
	private Instance<NewMedicalDecisionFVRGrading> fvrGradingTableInstance;
	
	private NewMedicalDecisionFVRGrading fvrGradingTableObj;
	
	@PostConstruct
	public void initView(){
		
	}
	
	public void initView(FvrReportGradingPageDto bean, SearchFvrReportGradingTableDto searchFormDto)
	{
		this.bean=bean;
		this.searchFormDto=searchFormDto;
		this.binder = new BeanFieldGroup<FvrReportGradingPageDto>(FvrReportGradingPageDto.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		fvrGradingTableObj = fvrGradingTableInstance.get();
		fvrGradingTableObj.initView(bean, false);
		
		remarks=(TextArea) binder.buildAndBind("FVR Grading Remarks", "gradingRemarks", TextArea.class);
		remarks.setWidth("400px");
		remarks.setMaxLength(2000);
		
		showOrHideValidation(false);
		
		convertbutton=new Button("Submit");
		cancelBtn=new Button("Cancel");
		
		convertbutton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		convertbutton.setWidth("-1px");
		convertbutton.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		buttonHorLayout=new HorizontalLayout(convertbutton,cancelBtn);
		buttonHorLayout.setSpacing(true);
		
		verticalLayout=new VerticalLayout(fvrGradingTableObj,remarks,buttonHorLayout);
		verticalLayout.setSpacing(true);
		verticalLayout.setComponentAlignment(remarks, Alignment.MIDDLE_CENTER);
		verticalLayout.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
		verticalMain=new VerticalLayout(verticalLayout);
		verticalMain.setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
		verticalMain.setSizeFull();
		
		addListener();
		
		setCompositionRoot(verticalMain);
		
	}
	public void addListener(){
		cancelBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
				        "No", "Yes", new ConfirmDialog.Listener() {

				            public void onClose(ConfirmDialog dialog) {
				                if (!dialog.isConfirmed()) {
				                	fireViewEvent(MenuItemBean.FVR_GRADING, true);
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
		convertbutton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Boolean hasError=false;
				if(bean.getFvrGradingDTO() == null || bean.getFvrGradingDTO().isEmpty()){
					hasError = true;
				}
				if(validatePage(hasError)){
				fireViewEvent(FvrReportGradingPagePresenter.SUBMIT_FVR_GRADING,bean,searchFormDto);
				}
			}
			
		});
		
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	private boolean validatePage(Boolean hasError) {
		//Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		
//		if (!this.binder.isValid()) {
//
//			for (Field<?> field : this.binder.getFields()) {
//				ErrorMessage errMsg = ((AbstractField<?>) field)
//						.getErrorMessage();
//				if (errMsg != null) {
//					eMsg += errMsg.getFormattedHtmlMessage();
//				}
//				hasError = true;
//			}
//		}
		if(hasError){
			eMsg.append("Please Select Any SEGMENT in FVR Grading");
		}else{
			//New FVR GRADING SEG A,B&C
			List<FvrGradingDetailsDTO> fvrBGradingDTO = this.bean.getFvrGradingDTO();
			if(!fvrBGradingDTO.isEmpty()) {
				int i=0;
				for (FvrGradingDetailsDTO fvrGradingDetailsDTO : fvrBGradingDTO) {
					i++;
						if(fvrGradingDetailsDTO.getIsSegmentBNotEdit() != null && !fvrGradingDetailsDTO.getIsSegmentBNotEdit() && fvrGradingDetailsDTO.getIsSegmentANotEdit() != null && !fvrGradingDetailsDTO.getIsSegmentANotEdit()){
							List<NewFVRGradingDTO> fvrGradingDTO2 = fvrGradingDetailsDTO.getNewFvrGradingDTO();
							for (NewFVRGradingDTO fvrGradingDTO3 : fvrGradingDTO2) {
									if(fvrGradingDTO3.getSegment() != null && fvrGradingDTO3.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_B) && fvrGradingDTO3.getSelectFlag() == null) {
										hasError = true;
										eMsg.append("Please Select All SEGMENT A and B in FVR Grading "+i+". </br>");
										break;
									}else if(fvrGradingDTO3.getSegment() != null && fvrGradingDTO3.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_A) && fvrGradingDTO3.getCheckFlagA() == null){
										hasError = true;
										eMsg.append("Please Select All SEGMENT A and B in FVR Grading "+i+". </br>");
										break;
									}									
							}
						}else if(fvrGradingDetailsDTO.getIsSegmentCNotEdit() != null && !fvrGradingDetailsDTO.getIsSegmentCNotEdit()){

							List<NewFVRGradingDTO> fvrGradingDTO2 = fvrGradingDetailsDTO.getNewFvrGradingDTO();
							Boolean isAnySegmentCSelected = false;
							for (NewFVRGradingDTO fvrGradingDTO3 : fvrGradingDTO2) {
									if(fvrGradingDTO3.getSegment() != null && fvrGradingDTO3.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_C) && (fvrGradingDTO3.getCheckFlag() != null && fvrGradingDTO3.getCheckFlag().equals(Boolean.TRUE))) {
										isAnySegmentCSelected = true;
										break;
									}
							}
							
							if(!isAnySegmentCSelected){
								hasError = true;
								eMsg.append("Please Select Any one Check box value in SEGMENT C in FVR Grading "+i+". </br>");
							}
							else if(fvrGradingTableObj.getSegmentCListenerTableSelectCount() > 1 ){
								hasError = true;
								eMsg.append("Please Select only one Check box value in SEGMENT C. </br>");
								break;
							}
						}
					else{
						hasError = true;
						eMsg.append("Please Select Any SEGMENT in FVR Grading "+i+". </br>");
					}
					
				}
			}
		}
		
		
		
		
		if (hasError) {
			setRequired(true);
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			hasError = true;
			return !hasError;
		}else{
			try {
			this.binder.commit();
			return true;
			} catch (CommitException e) {
				e.printStackTrace();
			}
			
			showOrHideValidation(false);
			return false;
		}
			
		}
	
	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}

}
