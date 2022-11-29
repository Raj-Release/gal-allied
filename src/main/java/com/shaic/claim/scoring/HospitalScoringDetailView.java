package com.shaic.claim.scoring;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.processdatacorrection.search.SearchProcessDataCorrectionService;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class HospitalScoringDetailView extends ViewComponent{

	private static final long serialVersionUID = 1L;
	
	private VerticalLayout mainLayout;
//	private HorizontalLayout buttonHorlayout;
//	
//	private Button submitButton;
//	private Button cancelButton;
//	private Button resetButton;
	
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

	@Inject
	private HospitalScoringDetailTable hospitalScroingDetails;
	
	@EJB
	private HospitalScoringService scoringService;
	
	private PreauthDTO dtoBean;

	private Boolean isoldscoringview = false;

	private boolean isButtonVisible;

	private Button parentScoringButton;
	
	@EJB
	private SearchProcessDataCorrectionService correctionService;
	
	private List<HospitalScoringDTO> listOfScroingDetailsObj;
	
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

	public Button getParentScoringButton() {
		return parentScoringButton;
	}

	public void setParentScoringButton(Button parentScoringButton) {
		this.parentScoringButton = parentScoringButton;
	}

	public void setIsoldscoringview(Boolean isoldscoringview) {
		this.isoldscoringview = isoldscoringview;
	}

	public void init(String argIntimationNumber, boolean isButtonVisible){
		intimationNumber = argIntimationNumber;
		this.isButtonVisible =  isButtonVisible;
		mainLayout = new VerticalLayout();
//		buttonHorlayout = new HorizontalLayout();

		Intimation selectedIntimation  = scoringService.getIntimationByNo(intimationNumber);
		String networkType ="NW";
		if(selectedIntimation.getHospitalType().getKey().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
			networkType = "NE";
		}else {
			networkType = "NW";
		}
		
		if(isoldscoringview){
			listOfScroingDetailsObj = correctionService.populateScoringCategory(selectedIntimation.getKey(),isoldscoringview,networkType);
		}else{
			listOfScroingDetailsObj = scoringService.getScoringDetails(intimationNumber);
		}
		
		hospitalScroingDetails.init(isButtonVisible, intimationNumber, listOfScroingDetailsObj.size());
		hospitalScroingDetails.setViewPageObj(this);
		hospitalScroingDetails.removeRow();
		
		hospitalScroingDetails.setVisibleColumns();
		hospitalScroingDetails.addList(listOfScroingDetailsObj);
		hospitalScroingDetails.setSizeFull();


//		submitButton = new Button("Submit");
//		cancelButton = new Button("Cancel");
//		resetButton = new Button("Reset");
//		addListeners();

//		buttonHorlayout.addComponent(submitButton);
//		buttonHorlayout.addComponent(cancelButton);
//		buttonHorlayout.addComponent(resetButton);
//		buttonHorlayout.setHeight("75px");
//		buttonHorlayout.setVisible(isButtonVisible);
//		buttonHorlayout.setSpacing(true);
//		buttonHorlayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
//		buttonHorlayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);
//		buttonHorlayout.setComponentAlignment(resetButton, Alignment.MIDDLE_CENTER);

		mainLayout.addComponent(hospitalScroingDetails);
//		mainLayout.addComponent(buttonHorlayout);
//		mainLayout.setComponentAlignment(buttonHorlayout, Alignment.MIDDLE_CENTER);
//		mainLayout.setSpacing(true);
		setCompositionRoot(mainLayout);
	}
	
	
	/*public void addListeners(){

		submitButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				Intimation selectedIntimation  = scoringService.getIntimationByNo(intimationNumber);
				boolean flag = validateScoringTable(selectedIntimation);
				if(flag){
					scoringService.saveHospitalScoring(hospitalScroingDetails.getValues(), intimationNumber, getScreenName(), getDtoBean().getKey());
					getDtoBean().setScoringClicked(scoringService.isScoringEnable());
					if(scoringService.isScoringEnable()){
						getDtoBean().setHospitalScoreFlag("Y");
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
				}

			}
		});
		cancelButton.addClickListener(new Button.ClickListener() {
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
		});

		resetButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				Intimation selectedIntimation  = scoringService.getIntimationByNo(intimationNumber);
				resetScoringValues(selectedIntimation);
			}
		});
	}*/
	
	/*public boolean validateScoringTable(Intimation argIntimation){
		boolean flag = false;
		List<HospitalScoringDTO> listOfValues = hospitalScroingDetails.getValues();
		
		List<Integer> R3SCatList =  new ArrayList<Integer>();
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
		}
		return flag;
	}
	
	public void resetScoringValues(Intimation argIntimation){
		List<Integer> listofSC =  new ArrayList<Integer>();
		listofSC.add(1006);
		listofSC.add(1007);
		listofSC.add(1023);
//		listofSC.add("OT Medicines");
		listofSC.add(1010);
		
		List<HospitalScoringDTO> listOfValues = hospitalScroingDetails.getValues();
		for(HospitalScoringDTO rec : listOfValues){
//			if(rec.getScoringName().equals("Procedure Charges  as per") || rec.getScoringName().equals("If Not Justifiable, Violations/Excess  in")){
			if(rec.getActualSubCategoryId() == null && ((rec.getActualCategoryId().intValue() == 2) || (rec.getActualCategoryId().intValue() == 3))){
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
		boolean isNetworkHospital = false;
		if(argIntimation.getHospitalType().getKey() ==  ReferenceTable.NETWORK_HOSPITAL_TYPE_ID){
			isNetworkHospital = true;
		}else if(argIntimation.getHospitalType().getKey() ==  ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID){
			isNetworkHospital = false;
		}
		//To be Removed - End
		
		for(HospitalScoringDTO rec : listOfValues){
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
		}
		hospitalScroingDetails.addList(listOfValues);
		hospitalScroingDetails.refreshTable();
	}
	
	@SuppressWarnings("static-access")
	private void showSubmitMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);

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
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
	}
	
	@SuppressWarnings("static-access")
	private void showErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
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
	}*/
			
}
