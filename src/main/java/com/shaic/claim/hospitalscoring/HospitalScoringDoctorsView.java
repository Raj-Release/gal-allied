package com.shaic.claim.hospitalscoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.scoring.HospitalScoringDTO;
import com.shaic.claim.scoring.HospitalScoringService;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class HospitalScoringDoctorsView extends ViewComponent{

	private static final long serialVersionUID = 1L;
	
	private VerticalLayout mainLayout;
	private HorizontalLayout buttonHorlayout;
	
	private Button submitButton;
	private Button cancelButton;
	private Button resetButton;
	
	private String intimationNumber;
	
//	private String screenName;
	
	private int seriousDeficiencyEnabled = 0;
	private int moderateDeficienceEnabled = 0;
	
/*	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}*/
	
	public Long UpdateKey;
	
	public Long getUpdateKey() {
		return UpdateKey;
	}
	public void setUpdateKey(Long updateKey) {
		UpdateKey = updateKey;
	}

	@Inject
	private HospitalScoringDoctorsTable hospitalScroingDetails;
	
	@EJB
	private HospitalScoringService scoringService;
	
	//CR2019017 - Start
	@EJB
	private PreauthService preauthService;
	//CR2019017 - End
	
//	private PreauthDTO dtoBean;
	
	private boolean globalSDFlag;
	
	
	String userName= "";

	private boolean globalHSFlag = false;
	
/*	public PreauthDTO getDtoBean() {
		return dtoBean;
	}

	public void setDtoBean(PreauthDTO dtoBean) {
		this.dtoBean = dtoBean;
	}*/
	
	private Window popupWindow;	

	public Window getPopupWindow() {
		return popupWindow;
	}

	public void setPopupWindow(Window popupWindow) {
		this.popupWindow = popupWindow;
	}
	
	private boolean isButtonVisible;
	
/*//	private Button parentScoringButton;

	public Button getParentScoringButton() {
		return parentScoringButton;
	}

	public void setParentScoringButton(Button parentScoringButton) {
		this.parentScoringButton = parentScoringButton;
	}*/

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

//		hospitalScroingDetails.setHeight("100%");

		submitButton = new Button("Submit");
		cancelButton = new Button("Cancel");
		resetButton = new Button("Reset");
		addListeners();

		buttonHorlayout.addComponent(submitButton);
		buttonHorlayout.addComponent(cancelButton);
		buttonHorlayout.addComponent(resetButton);
		buttonHorlayout.setHeight("75px");
		buttonHorlayout.setVisible(isButtonVisible);
		buttonHorlayout.setSpacing(true);
		buttonHorlayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		buttonHorlayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);
		buttonHorlayout.setComponentAlignment(resetButton, Alignment.MIDDLE_CENTER);

		mainLayout.addComponent(hospitalScroingDetails);
		mainLayout.addComponent(buttonHorlayout);
		mainLayout.setComponentAlignment(buttonHorlayout, Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(true);
		setCompositionRoot(mainLayout);
	}
	
	
	public void addListeners(){

		submitButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				Intimation selectedIntimation  = scoringService.getIntimationByNo(intimationNumber);
				boolean flag = validateScoringTable(selectedIntimation);
				if(flag){
					scoringService.saveHospitalScoring(hospitalScroingDetails.getValues(), intimationNumber, "Doctors View", null);
					showSubmitMessage("Hospital Scoring Submitted Successfully");
					getPopupWindow().close();
				}else{
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
		});
		cancelButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				getPopupWindow().close();
			}
		});

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
		
		List<Integer> MDList =  new ArrayList<Integer>();
		MDList.add(1028);
		MDList.add(1029);
		MDList.add(1030);
		MDList.add(1031);
		MDList.add(1032);
		MDList.add(1033);

		// If SD header value is YES
		if(listObjMp.get(7) != null && listObjMp.get(7).getScoringBooleanValue() != null){
			globalHSFlag = false;
			if(listObjMp.get(7).getScoringBooleanValue()){
				globalSDFlag = true;
				if(globalSDFlag) {
					/*Map<String, Object> temp = scoringService.getScoringMapValues();
					temp.clear();
					temp.put("seriousDeficiencyEnabled", 1);
					temp.put("moderateDeficienceEnabled", 0);*/
					scoringService.setSeriousDeficiencyMarked(true);
					scoringService.setModerateDeficiencyMarked(false);
					System.out.println("doc Vw S Flag in if globalSDFlag"+scoringService.isSeriousDeficiencyMarked());
					System.out.println("doc Vw M Flag in if globalSDFlag"+scoringService.isModerateDeficiencyMarked());
					
//					seriousDeficiencyEnabled = 1;
//					moderateDeficienceEnabled = 0;
				}
				if(listObjMp.get(1025).getScoringBooleanValue() == null || listObjMp.get(1026).getScoringBooleanValue() == null || listObjMp.get(1027).getScoringBooleanValue() == null){
					flag = false;
				}else{
//					System.out.println("All null values: ELSE");
					flag = true;
					
				}
				if(flag && (!listObjMp.get(1025).getScoringBooleanValue() && !listObjMp.get(1026).getScoringBooleanValue() && !listObjMp.get(1027).getScoringBooleanValue())){
					flag = false;
				}
				
				if(null != listObjMp.get(1025).getScoringBooleanValue() && null != listObjMp.get(1026).getScoringBooleanValue() && null != listObjMp.get(1027).getScoringBooleanValue()){
					// SD values filled conditions....
					if(/*flag && */(listObjMp.get(1025).getScoringBooleanValue() && !listObjMp.get(1026).getScoringBooleanValue() && !listObjMp.get(1027).getScoringBooleanValue())){
						flag = true;
					}
					if(/*flag && */(!listObjMp.get(1025).getScoringBooleanValue() && listObjMp.get(1026).getScoringBooleanValue() && !listObjMp.get(1027).getScoringBooleanValue())){
						flag = true;
					}
					if(/*flag && */(!listObjMp.get(1025).getScoringBooleanValue() && !listObjMp.get(1026).getScoringBooleanValue() && listObjMp.get(1027).getScoringBooleanValue())){
						flag = true;
					}
					
					if((listObjMp.get(1025).getScoringBooleanValue() && listObjMp.get(1026).getScoringBooleanValue() && !listObjMp.get(1027).getScoringBooleanValue())){
						flag = true;
					}
					if((listObjMp.get(1025).getScoringBooleanValue() && !listObjMp.get(1026).getScoringBooleanValue() && listObjMp.get(1027).getScoringBooleanValue())){
						flag = true;
					}
					if((!listObjMp.get(1025).getScoringBooleanValue() && listObjMp.get(1026).getScoringBooleanValue() && listObjMp.get(1027).getScoringBooleanValue())){
						flag = true;
					}
					if((listObjMp.get(1025).getScoringBooleanValue() && listObjMp.get(1026).getScoringBooleanValue() && listObjMp.get(1027).getScoringBooleanValue())){
						flag = true;
					}
//					System.out.println("Selected all values in SD");
				}
				
			}else{
				globalSDFlag = false;
				if(!globalSDFlag){
					/*Map<String, Object> temp = scoringService.getScoringMapValues();
					temp.clear();
					temp.put("seriousDeficiencyEnabled", 0);
					temp.put("moderateDeficienceEnabled", 1);*/
					
					scoringService.setSeriousDeficiencyMarked(false);
					scoringService.setModerateDeficiencyMarked(true);
					System.out.println("doc Vw S Flag in nested else globalSDFlag"+scoringService.isSeriousDeficiencyMarked());
					System.out.println("doc Vw M Flag in nested else globalSDFlag"+scoringService.isModerateDeficiencyMarked());
					
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
				//commented by noufel since new moderate benefit introduced
//				if (listObjMp.get(1028).getScoringBooleanValue() != null && listObjMp.get(1029).getScoringBooleanValue() != null && listObjMp.get(1030).getScoringBooleanValue() != null 
//						&& listObjMp.get(1031).getScoringBooleanValue() != null && listObjMp.get(1032).getScoringBooleanValue() != null && listObjMp.get(1033).getScoringBooleanValue() != null) {
////					System.out.println("Selected all values in MD");
//					flag = true;
//					//moderateDeficienceEnabled = 1;
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
				temp.put("seriousDeficiencyEnabled", 0);
				temp.put("moderateDeficienceEnabled", 1);*/
				
				scoringService.setSeriousDeficiencyMarked(false);
				scoringService.setModerateDeficiencyMarked(true);
				System.out.println("doc Vw S Flag in outer else globalSDFlag"+scoringService.isSeriousDeficiencyMarked());
				System.out.println("doc Vw M Flag in outer else globalSDFlag"+scoringService.isModerateDeficiencyMarked());
				
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
	
	private void showSubmitMessage(String eMsg) {
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
	}
	
	/*private void showErrorMessage(String eMsg) {
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}*/
			
}