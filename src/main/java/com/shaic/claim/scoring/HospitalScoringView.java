package com.shaic.claim.scoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardPresenter;
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.VerticalLayout;
import com.shaic.domain.Claim;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class HospitalScoringView extends ViewComponent{

	private static final long serialVersionUID = 1L;
	
	private VerticalLayout mainLayout;
	private HorizontalLayout buttonHorlayout;
	
	private Button submitButton;
	private Button cancelButton;
	private Button resetButton;
	
	private String intimationNumber;
	
	private String screenName;
	
	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	
	public Long UpdateKey;
	
	public Long getUpdateKey() {
		return UpdateKey;
	}
	public void setUpdateKey(Long updateKey) {
		UpdateKey = updateKey;
	}
	
	private int seriousDeficiencyEnabled = 0;
	private int moderateDeficienceEnabled = 0;
	
	String userName= "";

	@Inject
	private HospitalScoringTable hospitalScroingDetails;
	
	@EJB
	private HospitalScoringService scoringService;
	
	//CR2019017 - Start
	@EJB
	private PreauthService preauthService;
	//CR2019017 - End
	
	private PreauthDTO dtoBean;
	
	private boolean globalSDFlag;
	
	private boolean globalHSFlag = false;
	
	public PreauthDTO getDtoBean() {
		return dtoBean;
	}

	public void setDtoBean(PreauthDTO dtoBean) {
		this.dtoBean = dtoBean;
	}
	
	private Window popupWindow;	

	public Window getPopupWindow() {
		return popupWindow;
	}

	public void setPopupWindow(Window popupWindow) {
		this.popupWindow = popupWindow;
	}
	
	private boolean isButtonVisible;
	
	private Button parentScoringButton;

	public Button getParentScoringButton() {
		return parentScoringButton;
	}

	public void setParentScoringButton(Button parentScoringButton) {
		this.parentScoringButton = parentScoringButton;
	}

	public boolean isGlobalSDFlag() {
		return globalSDFlag;
	}

	public boolean isGlobalHSFlag() {
		return globalHSFlag;
	}

	public void init(String argIntimationNumber, boolean isButtonVisible){
		
		 userName=(String)UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID);

		intimationNumber = argIntimationNumber;
		this.isButtonVisible =  isButtonVisible;
		mainLayout = new VerticalLayout();
		buttonHorlayout = new HorizontalLayout();

//		intimationNumber = "CLI/2019/141100/3001259"; // Reimbursement Record
		Intimation selectedIntimation  = scoringService.getIntimationByNo(intimationNumber);
		String networkType ="NW";
		if(selectedIntimation.getHospitalType().getKey().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
			networkType = "NE";
		}
		else {
			networkType = "NW";
		}
		List<HospitalScoringDTO> listOfScroingDetailsObj = scoringService.populateScoringCategory(intimationNumber,networkType);
		/*Intimation selectedIntimation  = scoringService.getIntimationByNo(intimationNumber);
		if(selectedIntimation.getHospitalType().getKey() ==  ReferenceTable.NETWORK_HOSPITAL_TYPE_ID){
			listOfScroingDetailsObj.remove(6);
		}else{
			listOfScroingDetailsObj.remove(5);
		}*/
		
		hospitalScroingDetails.init(isButtonVisible, intimationNumber);
		hospitalScroingDetails.setViewPageObj(this);
		hospitalScroingDetails.removeRow();
		
		hospitalScroingDetails.setVisibleColumns();
		hospitalScroingDetails.addList(listOfScroingDetailsObj);
		hospitalScroingDetails.setSizeFull();


		/*submitButton = new Button("Submit");
		cancelButton = new Button("Cancel");*/
		resetButton = new Button("Reset");
		addListeners();

		/*buttonHorlayout.addComponent(submitButton);
		buttonHorlayout.addComponent(cancelButton);*/
		buttonHorlayout.addComponent(resetButton);
		buttonHorlayout.setHeight("75px");
		buttonHorlayout.setVisible(isButtonVisible);
		buttonHorlayout.setSpacing(true);
		/*buttonHorlayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		buttonHorlayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);*/
		buttonHorlayout.setComponentAlignment(resetButton, Alignment.MIDDLE_CENTER);

		mainLayout.addComponent(hospitalScroingDetails);
		mainLayout.addComponent(buttonHorlayout);
		mainLayout.setComponentAlignment(buttonHorlayout, Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(true);
		setCompositionRoot(mainLayout);
	}
	
	
	public void addListeners(){

		/*submitButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				Intimation selectedIntimation  = scoringService.getIntimationByNo(intimationNumber);
				Claim claim = scoringService.getClaimByIntimationkey(selectedIntimation.getKey());
				boolean flag = validateScoringTable(selectedIntimation);
				if(flag){
					scoringService.saveHospitalScoring(hospitalScroingDetails.getValues(), intimationNumber, getScreenName(), getDtoBean().getKey());
					getDtoBean().setScoringClicked(scoringService.isScoringEnable());
					if(scoringService.isScoringEnable()){
						getDtoBean().setHospitalScoreFlag("Y");
						getDtoBean().setIsSDEnabled(preauthService.getScoringDetails(selectedIntimation.getKey()));
						//CR2019017 - Start
						if(getScreenName().equals("Pre-Auth")){
							if(getDtoBean().getIsSDEnabled()){
								getDtoBean().getProcessPreauthButtonObj().getApproveBtn().setEnabled(false);
								//CR2019179
								getDtoBean().getProcessPreauthButtonObj().getQueryBtn().setEnabled(false);
							}else{
								//getDtoBean().getProcessPreauthButtonObj().getApproveBtn().setEnabled(true);
								fireViewEvent(PreauthWizardPresenter.PREAUTH_HOSP_SCORING_EVENT,getDtoBean().getProcessPreauthButtonObj());	
							}
						}
						
						if(getScreenName().equals("Enhancement")){
							if(getDtoBean().getIsSDEnabled()){
								getDtoBean().getProcessEnhancementButtonObj().getApproveBtn().setEnabled(false);
								//CR2019179
								getDtoBean().getProcessEnhancementButtonObj().getQueryBtn().setEnabled(false);
							}else{
								//getDtoBean().getProcessPreauthButtonObj().getApproveBtn().setEnabled(true);
								fireViewEvent(PreauthEnhancemetWizardPresenter.ENHN_HOSP_SCORING_EVENT,getDtoBean().getProcessEnhancementButtonObj());	
							}
						}
						
						if(getScreenName().equals("Claim Request")){
							if(getDtoBean().getIsSDEnabled()){
								getDtoBean().getClaimReqButtonObj().getApproveBtn().setEnabled(false);
								getDtoBean().getClaimRequestButtonsWizard().getQueryBtn().setEnabled(false);
							}else{
								//getDtoBean().getClaimReqButtonObj().getApproveBtn().setEnabled(true);
								//CR2019179
								getDtoBean().getClaimRequestButtonsWizard().getQueryBtn().setEnabled(true);
								fireViewEvent(ClaimRequestWizardPresenter.MA_HOSP_SCORING_EVENT,getDtoBean().getClaimReqButtonObj());
							}
							
							//CR2019056
							if(SHAConstants.YES_FLAG.equalsIgnoreCase(getDtoBean().getInvPendingFlag())) {
								getDtoBean().getClaimReqButtonObj().getApproveBtn().setEnabled(false);
							}
							 
						}
						
						//CR2019017 - End
						getParentScoringButton().setStyleName(ValoTheme.BUTTON_FRIENDLY);
					}else{
						getParentScoringButton().setStyleName(ValoTheme.BUTTON_DANGER);
					}
					showSubmitMessage("Hospital Scoring Submitted Successfully");
					getPopupWindow().close();
				}else{
					
					showErrorMessage("Please provide scoring for relevant category If there is any violation, "+"</br>"+" "
							+ "select yes for all violation category and no for non-violation categories"+"</br>"+" "
							+ "in Professional charges / OT Charges / Implant.");
							+ "in Professional charges / OT consumables / OT Medicines / Implant.");
					
					String eMsg = "Please provide scoring for relevant category If there is any violation, "+"</br>"+" "
							+ "select yes for all violation category and no for non-violation categories"+"</br>"+" "
							+ "in Professional charges / OT consumables / OT Medicines / Implant.";
					String eMsg = "";
					if(!globalHSFlag){
						if(globalSDFlag){
							eMsg = 	"Please provide scoring for all Serious Deficiency categories, "+"</br>"+" "
									+ "any one of the Serious Deficiency has to marked as Yes.";
						}else{
							eMsg = 	"Please provide scoring for all Moderate Deficiency categories.";
						}
					}else{
						eMsg = 	"Please provide scoring for relevant categories.";
					}
					
					MessageBox.createError()
			    	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
			        .withOkButton(ButtonOption.caption("OK")).open();
				}

			}
		});*/
		
		/*cancelButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(getDtoBean().getScoringClicked()){
					getParentScoringButton().setStyleName(ValoTheme.BUTTON_FRIENDLY);
				}else{
					getParentScoringButton().setStyleName(ValoTheme.BUTTON_DANGER);
				}
				getPopupWindow().close();
			}
		});*/

		resetButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				Intimation selectedIntimation  = scoringService.getIntimationByNo(intimationNumber);
				resetScoringValues(selectedIntimation);
			}
		});
	}
	
	public boolean validateScoringTable(Intimation argIntimation){
		boolean flag = false;
		globalSDFlag = false;
		globalHSFlag = false;
		List<HospitalScoringDTO> listOfValues = hospitalScroingDetails.getValues();
//		HospitalScoringDTO sdObj = null;
		HashMap<Integer, HospitalScoringDTO> listObjMp = new HashMap<Integer, HospitalScoringDTO>();
		for(HospitalScoringDTO rec : listOfValues){
			if(rec.getComponentId().endsWith("0")){
				listObjMp.put(Integer.parseInt(String.valueOf(rec.getActualCategoryId().intValue())), rec);
			}else{
				listObjMp.put(Integer.parseInt(String.valueOf(rec.getActualSubCategoryId().intValue())), rec);
			}
		}
		
		List<Integer> SDList =  new ArrayList<Integer>();
		SDList.add(1025);
		SDList.add(1026);
		SDList.add(1027);
		SDList.add(1034);
		
		List<Integer> MDList =  new ArrayList<Integer>();
		MDList.add(1028);
		MDList.add(1029);
		MDList.add(1030);
		MDList.add(1031);
		MDList.add(1032);
		MDList.add(1033);

		System.out.println("Intimation no: "+argIntimation.getIntimationId()+"listObjMp.get(7) "+listObjMp.get(7));
		// If SD header value is YES
		if(listObjMp.get(7) != null && listObjMp.get(7).getScoringBooleanValue() != null){
			globalHSFlag = false;
			System.out.println("Intimation no: "+argIntimation.getIntimationId()+"listObjMp.get(7).getScoringBooleanValue() "+listObjMp.get(7).getScoringBooleanValue());
			if(listObjMp.get(7).getScoringBooleanValue()){
				globalSDFlag = true;
				if(globalSDFlag) {
					/*Map<String, Object> temp = scoringService.getScoringMapValues();
					temp.clear();
					System.out.println("Intimation no: "+argIntimation.getIntimationId()+"Before SD "+temp.toString());
					temp.put("seriousDeficiencyEnabled", 1);
					temp.put("moderateDeficienceEnabled", 0);
					System.out.println("Intimation no: "+argIntimation.getIntimationId()+"After SD "+temp.toString());*/
					
					scoringService.setSeriousDeficiencyMarked(true);
					scoringService.setModerateDeficiencyMarked(false);
					System.out.println("S Flag in if globalSDFlag"+scoringService.isSeriousDeficiencyMarked());
					System.out.println("M Flag in if globalSDFlag"+scoringService.isModerateDeficiencyMarked());
//					seriousDeficiencyEnabled = 1;
//					moderateDeficienceEnabled = 0;
				}
				if(listObjMp.get(1025).getScoringBooleanValue() == null || listObjMp.get(1026).getScoringBooleanValue() == null || listObjMp.get(1027).getScoringBooleanValue() == null || listObjMp.get(1034).getScoringBooleanValue() == null){
					flag = false;
				}else{
//					System.out.println("All null values: ELSE");
					flag = true;
				}
				if(flag && (!listObjMp.get(1025).getScoringBooleanValue() && !listObjMp.get(1026).getScoringBooleanValue() && !listObjMp.get(1027).getScoringBooleanValue() && !listObjMp.get(1034).getScoringBooleanValue())){
					flag = false;
				}
				
				if(null != listObjMp.get(1025).getScoringBooleanValue() && null != listObjMp.get(1026).getScoringBooleanValue() && null != listObjMp.get(1027).getScoringBooleanValue() && null != listObjMp.get(1034).getScoringBooleanValue()){
					// SD values filled conditions....
					if(/*flag && */(listObjMp.get(1025).getScoringBooleanValue() && !listObjMp.get(1026).getScoringBooleanValue() && !listObjMp.get(1027).getScoringBooleanValue() && !listObjMp.get(1034).getScoringBooleanValue())){
						flag = true;
					}
					if(/*flag && */(!listObjMp.get(1025).getScoringBooleanValue() && listObjMp.get(1026).getScoringBooleanValue() && !listObjMp.get(1027).getScoringBooleanValue() && !listObjMp.get(1034).getScoringBooleanValue())){
						flag = true;
					}
					if(/*flag && */(!listObjMp.get(1025).getScoringBooleanValue() && !listObjMp.get(1026).getScoringBooleanValue() && listObjMp.get(1027).getScoringBooleanValue() && !listObjMp.get(1034).getScoringBooleanValue())){
						flag = true;
					}
					if(/*flag && */(!listObjMp.get(1025).getScoringBooleanValue() && !listObjMp.get(1026).getScoringBooleanValue() && !listObjMp.get(1027).getScoringBooleanValue() && listObjMp.get(1034).getScoringBooleanValue())){
						flag = true;
					}
					
					if((listObjMp.get(1025).getScoringBooleanValue() && listObjMp.get(1026).getScoringBooleanValue() && !listObjMp.get(1027).getScoringBooleanValue() && !listObjMp.get(1034).getScoringBooleanValue())){
						flag = true;
					}
					if((listObjMp.get(1025).getScoringBooleanValue() && !listObjMp.get(1026).getScoringBooleanValue() && listObjMp.get(1027).getScoringBooleanValue() && !listObjMp.get(1034).getScoringBooleanValue())){
						flag = true;
					}
					if((listObjMp.get(1025).getScoringBooleanValue() && !listObjMp.get(1026).getScoringBooleanValue() && !listObjMp.get(1027).getScoringBooleanValue() && listObjMp.get(1034).getScoringBooleanValue())){
						flag = true;
					}
					if((!listObjMp.get(1025).getScoringBooleanValue() && listObjMp.get(1026).getScoringBooleanValue() && listObjMp.get(1027).getScoringBooleanValue() && !listObjMp.get(1034).getScoringBooleanValue())){
						flag = true;
					}
					if((!listObjMp.get(1025).getScoringBooleanValue() && !listObjMp.get(1026).getScoringBooleanValue() && listObjMp.get(1027).getScoringBooleanValue() && listObjMp.get(1034).getScoringBooleanValue())){
						flag = true;
					}
					if((listObjMp.get(1025).getScoringBooleanValue() && listObjMp.get(1026).getScoringBooleanValue() && listObjMp.get(1027).getScoringBooleanValue() && listObjMp.get(1034).getScoringBooleanValue())){
						flag = true;
					}
//					System.out.println("Selected all values in SD");
				}
				
			}else{
				globalSDFlag = false;
				if(!globalSDFlag){
					/*Map<String, Object> temp = scoringService.getScoringMapValues();
					temp.clear();
					System.out.println("Intimation no: "+argIntimation.getIntimationId()+"Before MD "+temp.toString());
					temp.put("moderateDeficienceEnabled", 1);
					temp.put("seriousDeficiencyEnabled", 0);
					System.out.println("Intimation no: "+argIntimation.getIntimationId()+"After MD "+temp.toString());*/
					
					scoringService.setSeriousDeficiencyMarked(false);
					scoringService.setModerateDeficiencyMarked(true);
					System.out.println("S Flag in nested else globalSDFlag"+scoringService.isSeriousDeficiencyMarked());
					System.out.println("M Flag in nested else globalSDFlag"+scoringService.isModerateDeficiencyMarked());
					
//					moderateDeficienceEnabled = 1;
//					seriousDeficiencyEnabled = 0;
					}
				if(argIntimation.getHospitalType().getKey().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
					if (listObjMp.get(1035).getScoringBooleanValue() != null && listObjMp.get(1036).getScoringBooleanValue() != null && listObjMp.get(1037).getScoringBooleanValue() != null 
							&& listObjMp.get(1038).getScoringBooleanValue() != null && listObjMp.get(1039).getScoringBooleanValue() != null && listObjMp.get(1040).getScoringBooleanValue() != null) {
						flag = true;
					} else {
						flag = false;
					}	
				}else if(argIntimation.getHospitalType().getKey().equals(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID)){
					if (listObjMp.get(1041).getScoringBooleanValue() != null && listObjMp.get(1042).getScoringBooleanValue() != null && listObjMp.get(1043).getScoringBooleanValue() != null 
							&& listObjMp.get(1044).getScoringBooleanValue() != null && listObjMp.get(1045).getScoringBooleanValue() != null && listObjMp.get(1046).getScoringBooleanValue() != null
							&& listObjMp.get(1047).getScoringBooleanValue() != null) {
						flag = true;
					} else {
						flag = false;
					}
				}
				//commented by noufel since new moderate deficeny is added in master
//				if (listObjMp.get(1028).getScoringBooleanValue() != null && listObjMp.get(1029).getScoringBooleanValue() != null && listObjMp.get(1030).getScoringBooleanValue() != null 
//						&& listObjMp.get(1031).getScoringBooleanValue() != null && listObjMp.get(1032).getScoringBooleanValue() != null && listObjMp.get(1033).getScoringBooleanValue() != null) {
////					System.out.println("Selected all values in MD");
//					flag = true;
//				} else {
//					flag = false;
//				}

			}
		}else{
//			System.out.println("Else condition");
			globalSDFlag = false;
			globalHSFlag = true;
			if(globalHSFlag) {
				/*Map<String, Object> temp = scoringService.getScoringMapValues();
				temp.clear();
				System.out.println("Intimation no: "+argIntimation.getIntimationId()+"Before MD1 "+temp.toString());
				temp.put("moderateDeficienceEnabled", 1);
				temp.put("seriousDeficiencyEnabled", 0);
				System.out.println("Intimation no: "+argIntimation.getIntimationId()+"After MD1 "+temp.toString());*/
				
				scoringService.setSeriousDeficiencyMarked(false);
				scoringService.setModerateDeficiencyMarked(true);
				System.out.println("S Flag in outer else globalSDFlag"+scoringService.isSeriousDeficiencyMarked());
				System.out.println("M Flag in outer else globalSDFlag"+scoringService.isModerateDeficiencyMarked());
				
//				moderateDeficienceEnabled = 1;
				}
			flag = false;
		}

		
		//To be Removed in version 4 - Start
		/*List<Integer> R3SCatList =  new ArrayList<Integer>();
		if(argIntimation.getHospitalType().getKey() ==  ReferenceTable.NETWORK_HOSPITAL_TYPE_ID){
			R3SCatList.add("Index Price/SOC/Market Rate  - For NANH");
		}else if(argIntimation.getHospitalType().getKey() ==  ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID){
			R3SCatList.add("ANH Package – For ANH");
		}
//		R3SCatList.add("Professional charges");
//		R3SCatList.add("OT consumables");
//		R3SCatList.add("OT Medicines");
//		R3SCatList.add("Implant");
		
		R3SCatList.add(1007);
		R3SCatList.add(1023);
		R3SCatList.add(1010);
		
		boolean isViolationsDisabled = true;
		for(HospitalScoringDTO rec : listOfValues){
//			if((rec.getScoringName().equals("ANH Package – For ANH / Index Price/SOC/Market Rate  - For NANH")) && rec.getScoringBooleanValue() != null && rec.getScoringBooleanValue()){
			if((rec.getActualSubCategoryId() != null && rec.getActualSubCategoryId() == 1004) && rec.getScoringBooleanValue() != null && rec.getScoringBooleanValue()){
				R3SCatList.add(1006); //If Violation of ANH/Excess of Index Price ,Justifiable
			}
			
//			if(rec.getScoringName().equals("If Violation of ANH/Excess of Index Price ,Justifiable")){
			if(rec.getActualSubCategoryId() != null && rec.getActualSubCategoryId() == 1006){
				if(rec.getScoringBooleanValue() == null || rec.getScoringBooleanValue()){
					isViolationsDisabled = true;
				}else{
					isViolationsDisabled = false;
				}
			}
			
			if(!R3SCatList.contains("If Violation of ANH/Excess of Index Price ,Justifiable")){
				if((rec.getScoringName().equals("Index Price/SOC/Market Rate  - For NANH")) && rec.getScoringBooleanValue() != null && rec.getScoringBooleanValue()){
					R3SCatList.add("If Violation of ANH/Excess of Index Price ,Justifiable");
				}
			}
		}
		
		if(isViolationsDisabled){
			for(HospitalScoringDTO rec : listOfValues){
				if((!R3SCatList.contains(rec.getActualSubCategoryId())) && rec.getScoringValue() == null){
//					System.out.println("If : "+rec.getScoringName());
					flag = false;
					break;
				}else{
					flag = true;
				}
			}
		}else{
			Boolean PCFlag = null;
			Boolean OTCon = null;
//			Boolean OTMed = null;
			Boolean Implant = null;
			for(HospitalScoringDTO rec : listOfValues){
				if(rec.getScoringValue() == null){
//					System.out.println("Else : "+rec.getScoringName());
					flag = false;
					break;
				}else{
					if(rec.getActualSubCategoryId() != null && rec.getActualSubCategoryId() == 1007){
						PCFlag = new Boolean(rec.getScoringBooleanValue());
					}
					if(rec.getActualSubCategoryId() != null && rec.getActualSubCategoryId() == 1023){
						OTCon = new Boolean(rec.getScoringBooleanValue());
					}
//					if(rec.getScoringName().equals("OT Medicines")){
//						OTMed = new Boolean(rec.getScoringBooleanValue());
//					}
					if(rec.getActualSubCategoryId() != null && rec.getActualSubCategoryId() == 1010){
						Implant = new Boolean(rec.getScoringBooleanValue());
					}
					flag = true;
				}
			}
			
			if(flag){
				if(!PCFlag && !OTCon && ! Implant){
					flag = false;
				}
			}
		}*/
		//To be Removed in version 4 - End
		System.out.println("flag : "+flag);
//		System.out.println("Intimation no: " +argIntimation.getIntimationId() +"Return of flag: "+scoringService.getScoringMapValues().toString());
		System.out.println("----------------------------------------------Intimation no: " +argIntimation.getIntimationId()+"----------------------------------");
		System.out.println("Flag for Intimation No S Flag:"+scoringService.isSeriousDeficiencyMarked()+"M Flag:"+scoringService.isModerateDeficiencyMarked());
		System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
		return flag;
	}
	
	public void resetScoringValues(Intimation argIntimation){
		//To be Removed in version 4 - Start
/*		List<Integer> listofSC =  new ArrayList<Integer>();
		listofSC.add(1006);
		listofSC.add(1007);
		listofSC.add(1023);
//		listofSC.add("OT Medicines");
		listofSC.add(1010);*/
		//To be Removed in version 4 - End
		
		List<HospitalScoringDTO> listOfValues = hospitalScroingDetails.getValues();
		for(HospitalScoringDTO rec : listOfValues){
//			if(rec.getScoringName().equals("Procedure Charges  as per") || rec.getScoringName().equals("If Not Justifiable, Violations/Excess  in")){
//			if(rec.getActualSubCategoryId() == null && ((rec.getActualCategoryId().intValue() == 2) || (rec.getActualCategoryId().intValue() == 3))){
			if(rec.getActualSubCategoryId() == null && (rec.getActualCategoryId().intValue() == 8)){
//				System.out.println("RESET IF : "+rec.getActualSubCategoryId());
				rec.setScoringBooleanValue(false);
				rec.setScoringValue("N");
				rec.setOptionEnabled(true);
			}else{
//				System.out.println("RESET ELSE  : "+rec.getActualSubCategoryId());
				rec.setScoringBooleanValue(null);
				rec.setScoringValue(null);
				rec.setOptionEnabled(true);
			}
		}
		//To be Removed - Start
		/*boolean isNetworkHospital = false;
		if(argIntimation.getHospitalType().getKey() ==  ReferenceTable.NETWORK_HOSPITAL_TYPE_ID){
			isNetworkHospital = true;
		}else if(argIntimation.getHospitalType().getKey() ==  ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID){
			isNetworkHospital = false;
		}*/
		//To be Removed - End
		
		
		//To be Removed in version 4 - Start
		/*for(HospitalScoringDTO rec : listOfValues){
			//To be Removed - Start
			if(isNetworkHospital){
				if(rec.getScoringName().equals("ANH Package – For ANH")){
					rec.setOptionEnabled(true);
				}else if(rec.getScoringName().equals("Index Price/SOC/Market Rate  - For NANH")){
					rec.setOptionEnabled(false);
				}
				
			}else if(!isNetworkHospital){
				if(rec.getScoringName().equals("ANH Package – For ANH")){
					rec.setOptionEnabled(false);
				}else if(rec.getScoringName().equals("Index Price/SOC/Market Rate  - For NANH")){
					rec.setOptionEnabled(true);
				}
			}
			//To be Removed - End
			
			if(listofSC.contains(rec.getActualSubCategoryId())){
				rec.setOptionEnabled(false);
			}			
		}*/
		//To be Removed in version 4 - End
		hospitalScroingDetails.addList(listOfValues);
		hospitalScroingDetails.refreshTable();
	}
	
	@SuppressWarnings("static-access")
	private void showSubmitMessage(String eMsg) {
		/*Label label = new Label(eMsg, ContentMode.HTML);

		Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(label, homeButton);
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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(eMsg, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
			}
		});
		
		/*final MessageBox msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Information")
			    .withMessage(eMsg.toString())
			    .withOkButton(ButtonOption.caption("Ok"))
			    .open();
		
		Button homeButton=msgBox.getButton(ButtonType.OK);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				msgBox.close();
			}
		});*/
		
		
	}
	
	@SuppressWarnings("static-access")
	private void showErrorMessage(String eMsg) {
		/*Label label = new Label(eMsg, ContentMode.HTML);
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
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}
	
	public boolean saveScoring(){

		Intimation selectedIntimation  = scoringService.getIntimationByNo(intimationNumber);
		scoringService.saveHospitalScoring(hospitalScroingDetails.getValues(), intimationNumber, getScreenName(), getDtoBean().getKey());
		getDtoBean().setScoringClicked(scoringService.isScoringEnable());
		if(scoringService.isScoringEnable()){
			getDtoBean().setHospitalScoreFlag("Y");
			getDtoBean().setIsSDEnabled(preauthService.getScoringDetails(selectedIntimation.getKey()));
			//CR2019017 - Start
			if(getScreenName().equals("Pre-Auth")){
				if(getDtoBean().getIsSDEnabled()){
					getDtoBean().getProcessPreauthButtonObj().getApproveBtn().setEnabled(false);
					//CR2019179
					getDtoBean().getProcessPreauthButtonObj().getQueryBtn().setEnabled(false);
				}else{
					fireViewEvent(PreauthWizardPresenter.PREAUTH_HOSP_SCORING_EVENT,getDtoBean().getProcessPreauthButtonObj());	
				}
			}

			if(getScreenName().equals("Enhancement")){
				if(getDtoBean().getIsSDEnabled()){
					getDtoBean().getProcessEnhancementButtonObj().getApproveBtn().setEnabled(false);
					//CR2019179
					getDtoBean().getProcessEnhancementButtonObj().getQueryBtn().setEnabled(false);
				}else{
					//getDtoBean().getProcessPreauthButtonObj().getApproveBtn().setEnabled(true);
					fireViewEvent(PreauthEnhancemetWizardPresenter.ENHN_HOSP_SCORING_EVENT,getDtoBean().getProcessEnhancementButtonObj());	
				}
			}

			if(getScreenName().equals("Claim Request")){
				if(getDtoBean().getIsSDEnabled()){
					getDtoBean().getClaimReqButtonObj().getApproveBtn().setEnabled(false);
					getDtoBean().getClaimRequestButtonsWizard().getQueryBtn().setEnabled(false);
				}else{
					//getDtoBean().getClaimReqButtonObj().getApproveBtn().setEnabled(true);
					//CR2019179
					getDtoBean().getClaimRequestButtonsWizard().getQueryBtn().setEnabled(true);
					fireViewEvent(ClaimRequestWizardPresenter.MA_HOSP_SCORING_EVENT,getDtoBean().getClaimReqButtonObj());
				}

				//CR2019056
				if(SHAConstants.YES_FLAG.equalsIgnoreCase(getDtoBean().getInvPendingFlag())) {
					getDtoBean().getClaimReqButtonObj().getApproveBtn().setEnabled(false);
				}

			}
		}
		return scoringService.isScoringEnable();
	}
	
	public List<HospitalScoringDTO> getScoringValues(){
		
		if(hospitalScroingDetails !=null){
			return hospitalScroingDetails.getValues();
		}
		return null;
	}
			
}