package com.shaic.claim.scoring.ppcoding;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.processdatacorrection.search.DataCorrectionPresenter;
import com.shaic.claim.processdatacorrectionhistorical.search.DataCorrectionHistoricalPresenter;
import com.shaic.claim.processdatacorrectionpriority.search.DataCorrectionPriorityPresenter;
import com.shaic.claim.reimbursement.billing.wizard.HospitalizationCalcualtionUI;
import com.shaic.claim.reimbursement.billing.wizard.PostHospitalizationCalcualtionUI;
import com.shaic.claim.scoring.HospitalScoringService;
import com.shaic.claim.scoring.HospitalScoringView;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.VerticalLayout;

public class ScoringAndPPTabUI extends ViewComponent {
	
	@Inject
	private HospitalScoringView hospitalScoringView;
		
	@Inject
	private PPCodingView ppCodingView;
	
	private VerticalLayout mainLayout;
	
	private HorizontalLayout buttonHorlayout;
	
	private String intimationNumber;
	
	private String screenName;
	
	private PreauthDTO dtoBean;
	
	private Button parentScoringButton;
	
	private Button cancelButton;
	
	private Button submitButton;
	
	private Window popupWindow;
	
	private Boolean scoringcompleted = false;
	
	private Boolean ppcompleted = false;
	
	@EJB
	private HospitalScoringService scoringService;
	
	@EJB
	private PPCodingService codingService;

	public Window getPopupWindow() {
		return popupWindow;
	}

	public void setPopupWindow(Window popupWindow) {
		this.popupWindow = popupWindow;
	}

	public void setParentScoringButton(Button parentScoringButton) {
		this.parentScoringButton = parentScoringButton;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public void setDtoBean(PreauthDTO dtoBean) {
		this.dtoBean = dtoBean;
	}
	
	public Button getParentScoringButton() {
		return parentScoringButton;
	}

	public PreauthDTO getDtoBean() {
		return dtoBean;
	}

	public void init(boolean isButtonVisible){

		mainLayout = new VerticalLayout();
		buttonHorlayout = new HorizontalLayout();
		
		cancelButton = new Button("Cancel");
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		addListeners();
		
		buttonHorlayout.addComponent(submitButton);
		buttonHorlayout.addComponent(cancelButton);
		buttonHorlayout.setHeight("75px");
		buttonHorlayout.setVisible(isButtonVisible);
		buttonHorlayout.setSpacing(true);
		buttonHorlayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		buttonHorlayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);
		
		mainLayout.addComponent(buildscoringTabs());
		mainLayout.addComponent(buttonHorlayout);
		mainLayout.setComponentAlignment(buttonHorlayout, Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(true);
		setCompositionRoot(mainLayout);
	}
	
	private TabSheet buildscoringTabs() {
		TabSheet scoringPPTab = new TabSheet();
		scoringPPTab.setSizeFull();
		scoringPPTab.setStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
		
		TabSheet scoringTab = new TabSheet();
		hospitalScoringView.init(intimationNumber, true);
		hospitalScoringView.setScreenName(screenName);
		hospitalScoringView.setDtoBean(dtoBean);
		hospitalScoringView.setParentScoringButton(parentScoringButton);
		scoringTab.setHeight("100.0%");
		scoringTab.addComponent(hospitalScoringView);
		scoringPPTab.addTab(scoringTab, "Hospital Scoring", null);
		
		TabSheet ppTab = new TabSheet();
		ppCodingView.setScreenName(screenName);
		ppCodingView.setDtoBean(dtoBean);
		ppCodingView.init(intimationNumber, true);
		ppTab.setHeight("100.0%");
		ppTab.addComponent(ppCodingView);
		scoringPPTab.addTab(ppTab, "PP coding", null);
		
		return scoringPPTab;
	}
	
	public void generatePPCoadingField(Boolean isChecked,List<PPCodingDTO> codingDTOs,Map<String,Boolean> selectedPPCoding) {
		ppCodingView.setSelectedPPCoding(selectedPPCoding);
		ppCodingView.generatePPCoadingField(codingDTOs, isChecked);
	}
	
	public void addListeners(){

		submitButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(validatePage()){
					if(screenName.equals("Data Validation Historical")){
						fireViewEvent(DataCorrectionHistoricalPresenter.SUBMIT_HOSPITAL_SCORING_HISTORICAL,hospitalScoringView.getScoringValues(),ppCodingView.isppCodingSelected(),ppCodingView.getSelectedPPCoding(),intimationNumber);	
					}else if(screenName.equals("Data Validation")){
						fireViewEvent(DataCorrectionPresenter.SUBMIT_HOSPITAL_SCORING,hospitalScoringView.getScoringValues(),ppCodingView.isppCodingSelected(),ppCodingView.getSelectedPPCoding(),intimationNumber);	
					}else if(screenName.equals(SHAConstants.DATA_VALIDATION_PRIORITY)){
						fireViewEvent(DataCorrectionPriorityPresenter.SUBMIT_HOSPITAL_SCORING_PRIORITY,hospitalScoringView.getScoringValues(),ppCodingView.isppCodingSelected(),ppCodingView.getSelectedPPCoding(),intimationNumber);	
					}else{
						scoringcompleted = hospitalScoringView.saveScoring();
						if(ppCodingView !=null && ppCodingView.isppCodingSelected() != null){
							ppCodingView.savePPCoding();
						}else{
							if(intimationNumber !=null){
								codingService.deletePPCoding(intimationNumber);
							}
						}
						if(scoringcompleted){
							getParentScoringButton().setStyleName(ValoTheme.BUTTON_FRIENDLY);
						}else{
							getParentScoringButton().setStyleName(ValoTheme.BUTTON_DANGER);
						}	
					}
					getPopupWindow().close();
				}
			}
		});

		cancelButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(getParentScoringButton() !=null){
					if(getDtoBean().getScoringClicked()){
						getParentScoringButton().setStyleName(ValoTheme.BUTTON_FRIENDLY);
					}else{
						getParentScoringButton().setStyleName(ValoTheme.BUTTON_DANGER);
					}
				}
				if(screenName != null && screenName.equals("Data Validation")){
					fireViewEvent(DataCorrectionPresenter.RESET_HOSPITAL_SCORING,null);
				}else if(screenName != null && screenName.equals(SHAConstants.DATA_VALIDATION_PRIORITY)){
					fireViewEvent(DataCorrectionPriorityPresenter.RESET_HOSPITAL_SCORING_PRIORITY,null);
				} 
				getPopupWindow().close();
			}
		});
	}
	
	private Boolean validatePage(){
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();	
		Intimation selectedIntimation  = scoringService.getIntimationByNo(intimationNumber);
		Boolean ppmandatory = false;
		
		if(screenName.equals("Claim Request")){
			ppmandatory = true;
		}
		
		if(ppmandatory){
			if(ppCodingView !=null && ppCodingView.isppCodingSelected() == null){
				hasError = true;
				eMsg.append("Please Select Policy Holders Health Protected. </br>"); 
			}
		}
		
		if(ppCodingView !=null && ppCodingView.isppCodingSelected() != null
				&& !ppCodingView.isppCodingSelected()){
			Boolean isSelected = false;
			Boolean isallSelected = true;
			for (Map.Entry<String, Boolean> entry : ppCodingView.getselectedPPCoding().entrySet()) {
				if(entry.getValue()){
					isSelected = true;
				}else{
					isallSelected = false;
				}
			}
			if(!isSelected){
				hasError = true;
				eMsg.append("Please Select one resone for non PP . </br>"); 
			}
			if(isallSelected){
				hasError = true;
				eMsg.append("Please unselect one resone for non PP . </br>"); 
			}
		}
		System.out.println("Has error before Scoring for "+selectedIntimation.getIntimationId()+"------"+hasError);
		if(hospitalScoringView !=null){
			boolean flag = hospitalScoringView.validateScoringTable(selectedIntimation);
			System.out.println("Is valid scoring for "+selectedIntimation.getIntimationId()+"------"+flag);
			if(!flag){
				hasError = true;
				
				String errMsg = "";
				if(!hospitalScoringView.isGlobalHSFlag()){
					if(hospitalScoringView.isGlobalSDFlag()){
						errMsg = 	"Please provide scoring for all Serious Deficiency categories, "+"</br>"+" "
								+ "any one of the Serious Deficiency has to marked as Yes.";
					}else{
						errMsg = 	"Please provide scoring for all Moderate Deficiency categories.";
					}
				}else{
					errMsg = 	"Please provide scoring for relevant categories.";
				}
				
				eMsg.append(errMsg+ "</br>"); 
			}
		}
		System.out.println("Has error after Scoring for "+selectedIntimation.getIntimationId()+"------"+hasError);
		if (hasError) {
			
			MessageBox.createError()
	    	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
	        .withOkButton(ButtonOption.caption("OK")).open();	
		}
		return !hasError;
	}
}
