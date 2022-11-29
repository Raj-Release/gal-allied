package com.shaic.claim.processdatacorrection.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.shaic.claim.processdatacorrection.dto.ProcessDataCorrectionDTO;
import com.shaic.claim.processdatacorrectionhistorical.search.DataCorrectionHistoricalPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardPresenter;
import com.shaic.claim.scoring.HospitalScoringDTO;
import com.shaic.claim.scoring.HospitalScoringService;
import com.shaic.claim.scoring.HospitalScoringTable;
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.VerticalLayout;

public class HospitalScoringCorrectionView extends ViewComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4622016597036586482L;

	private VerticalLayout mainLayout;
	private HorizontalLayout buttonHorlayout;

	private Button submitButton;
	private Button cancelButton;
	private Button resetButton;

	private String intimationNumber;

	private String screenName;	

	public Long UpdateKey;

	String userName= "";

	@Inject
	private HospitalScoringTable hospitalScroingDetails;

	@EJB
	private SearchProcessDataCorrectionService scoringService;

	@EJB
	private HospitalScoringService hsptscoringService;

	@EJB
	private PreauthService preauthService;

	private ProcessDataCorrectionDTO dtoBean;

	private boolean globalSDFlag;

	private boolean globalHSFlag = false;

	private Window popupWindow;		

	private boolean isButtonVisible;

	private Button parentScoringButton;

	private boolean proposedScoring;

	public Button getParentScoringButton() {
		return parentScoringButton;
	}

	public void setParentScoringButton(Button parentScoringButton) {
		this.parentScoringButton = parentScoringButton;
	}

	public Long getUpdateKey() {
		return UpdateKey;
	}
	public void setUpdateKey(Long updateKey) {
		UpdateKey = updateKey;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public Window getPopupWindow() {
		return popupWindow;
	}

	public void setPopupWindow(Window popupWindow) {
		this.popupWindow = popupWindow;
	}

	public ProcessDataCorrectionDTO getDtoBean() {
		return dtoBean;
	}

	public void setDtoBean(ProcessDataCorrectionDTO dtoBean) {
		this.dtoBean = dtoBean;
	}

	public boolean isProposedScoring() {
		return proposedScoring;
	}

	public void setProposedScoring(boolean proposedScoring) {
		this.proposedScoring = proposedScoring;
	}

	public void init(String argIntimationNumber, boolean isButtonVisible,boolean isoldscoringview){

		userName=(String)UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID);
		intimationNumber = argIntimationNumber;
		this.isButtonVisible =  isButtonVisible;
		mainLayout = new VerticalLayout();
		buttonHorlayout = new HorizontalLayout();
		List<HospitalScoringDTO> listOfScroingDetailsObj =null;
		/*if(getDtoBean().getIsScoringChanged()
				&& proposedScoring){
			listOfScroingDetailsObj = getDtoBean().getScoringDTOs(); 
		}else{*/
		Intimation selectedIntimation  = scoringService.getIntimationByNo(getDtoBean().getIntimationKey());
		String networkType ="NW";
		if(selectedIntimation.getHospitalType().getKey().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
			networkType = "NE";
		}
		else {
			networkType = "NW";
		}
			listOfScroingDetailsObj = scoringService.populateScoringCategory(getDtoBean().getIntimationKey(),isoldscoringview,networkType);
		/*}*/

		hospitalScroingDetails.init(isButtonVisible, intimationNumber);
		hospitalScroingDetails.setViewPageObj(this);
		hospitalScroingDetails.removeRow();
		hospitalScroingDetails.setVisibleColumns();
		hospitalScroingDetails.addList(listOfScroingDetailsObj);
		hospitalScroingDetails.setSizeFull();

		cancelButton = new Button("Cancel");
		if(isButtonVisible){
			submitButton = new Button("Submit");
			resetButton = new Button("Reset");
			submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		}	
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		addListeners();

		
		if(isButtonVisible){
			buttonHorlayout.addComponent(submitButton);
			buttonHorlayout.addComponent(resetButton);
			buttonHorlayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
			buttonHorlayout.setComponentAlignment(resetButton, Alignment.MIDDLE_CENTER);
		}
		buttonHorlayout.addComponent(cancelButton);
		buttonHorlayout.setHeight("75px");
		buttonHorlayout.setVisible(isButtonVisible);
		buttonHorlayout.setSpacing(true);
		buttonHorlayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);


		mainLayout.addComponent(hospitalScroingDetails);
		mainLayout.addComponent(buttonHorlayout);
		mainLayout.setComponentAlignment(buttonHorlayout, Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(true);
		setCompositionRoot(mainLayout);
	}


	public void addListeners(){

		cancelButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				getPopupWindow().close();
			}
		});
		if(isButtonVisible){
			submitButton.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void buttonClick(ClickEvent event) {
					Intimation selectedIntimation  = scoringService.getIntimationByNo(getDtoBean().getIntimationKey());
					boolean flag = validateScoringTable(selectedIntimation);
					if(flag){
						if(getScreenName().equals("Data Validation Historical")){
							fireViewEvent(DataCorrectionHistoricalPresenter.SUBMIT_HOSPITAL_SCORING_HISTORICAL,hospitalScroingDetails.getValues());	
						}else if(getScreenName().equals("Data Validation")){
							fireViewEvent(DataCorrectionPresenter.SUBMIT_HOSPITAL_SCORING,hospitalScroingDetails.getValues());	
						}			
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

			resetButton.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void buttonClick(ClickEvent event) {
					Intimation selectedIntimation  = scoringService.getIntimationByNo(getDtoBean().getIntimationKey());
					resetScoringValues(selectedIntimation);
				}
			});
		}
		
	}

	public boolean validateScoringTable(Intimation argIntimation){
		boolean flag = false;
		globalSDFlag = false;
		globalHSFlag = false;
		List<HospitalScoringDTO> listOfValues = hospitalScroingDetails.getValues();
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
		if(listObjMp.get(7) != null && listObjMp.get(7).getScoringBooleanValue() != null){
			globalHSFlag = false;
			System.out.println("Intimation no: "+argIntimation.getIntimationId()+"listObjMp.get(7).getScoringBooleanValue() "+listObjMp.get(7).getScoringBooleanValue());
			if(listObjMp.get(7).getScoringBooleanValue()){
				globalSDFlag = true;
				/*if(globalSDFlag) {

					scoringService.setSeriousDeficiencyMarked(true);
					scoringService.setModerateDeficiencyMarked(false);
					System.out.println("S Flag in if globalSDFlag"+scoringService.isSeriousDeficiencyMarked());
					System.out.println("M Flag in if globalSDFlag"+scoringService.isModerateDeficiencyMarked());
				}*/
				if(listObjMp.get(1025).getScoringBooleanValue() == null || listObjMp.get(1026).getScoringBooleanValue() == null || listObjMp.get(1027).getScoringBooleanValue() == null || listObjMp.get(1034).getScoringBooleanValue() == null){
					flag = false;
				}else{
					flag = true;
				}
				if(flag && (!listObjMp.get(1025).getScoringBooleanValue() && !listObjMp.get(1026).getScoringBooleanValue() && !listObjMp.get(1027).getScoringBooleanValue() && !listObjMp.get(1034).getScoringBooleanValue())){
					flag = false;
				}

				if(null != listObjMp.get(1025).getScoringBooleanValue() && null != listObjMp.get(1026).getScoringBooleanValue() && null != listObjMp.get(1027).getScoringBooleanValue() && null != listObjMp.get(1034).getScoringBooleanValue()){
					if((listObjMp.get(1025).getScoringBooleanValue() && !listObjMp.get(1026).getScoringBooleanValue() && !listObjMp.get(1027).getScoringBooleanValue() && !listObjMp.get(1034).getScoringBooleanValue())){
						flag = true;
					}
					if((!listObjMp.get(1025).getScoringBooleanValue() && listObjMp.get(1026).getScoringBooleanValue() && !listObjMp.get(1027).getScoringBooleanValue() && !listObjMp.get(1034).getScoringBooleanValue())){
						flag = true;
					}
					if((!listObjMp.get(1025).getScoringBooleanValue() && !listObjMp.get(1026).getScoringBooleanValue() && listObjMp.get(1027).getScoringBooleanValue() && !listObjMp.get(1034).getScoringBooleanValue())){
						flag = true;
					}
					if((!listObjMp.get(1025).getScoringBooleanValue() && !listObjMp.get(1026).getScoringBooleanValue() && !listObjMp.get(1027).getScoringBooleanValue() && listObjMp.get(1034).getScoringBooleanValue())){
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
				}

			}else{
				globalSDFlag = false;
				/*if(!globalSDFlag){
					scoringService.setSeriousDeficiencyMarked(false);
					scoringService.setModerateDeficiencyMarked(true);
					System.out.println("S Flag in nested else globalSDFlag"+scoringService.isSeriousDeficiencyMarked());
					System.out.println("M Flag in nested else globalSDFlag"+scoringService.isModerateDeficiencyMarked());

				}*/
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
				//commented by noufel for hosital scoring CR
//				if (listObjMp.get(1028).getScoringBooleanValue() != null && listObjMp.get(1029).getScoringBooleanValue() != null && listObjMp.get(1030).getScoringBooleanValue() != null 
//						&& listObjMp.get(1031).getScoringBooleanValue() != null && listObjMp.get(1032).getScoringBooleanValue() != null && listObjMp.get(1033).getScoringBooleanValue() != null) {
//					flag = true;
//				} else {
//					flag = false;
//				}
			}
		}else{
			globalSDFlag = false;
			globalHSFlag = true;
			/*if(globalHSFlag) {

				scoringService.setSeriousDeficiencyMarked(false);
				scoringService.setModerateDeficiencyMarked(true);
				System.out.println("S Flag in outer else globalSDFlag"+scoringService.isSeriousDeficiencyMarked());
				System.out.println("M Flag in outer else globalSDFlag"+scoringService.isModerateDeficiencyMarked());

			}*/
			flag = false;
		}

		System.out.println("flag : "+flag);
		System.out.println("----------------------------------------------Intimation no: " +argIntimation.getIntimationId()+"----------------------------------");
		//System.out.println("Flag for Intimation No S Flag:"+scoringService.isSeriousDeficiencyMarked()+"M Flag:"+scoringService.isModerateDeficiencyMarked());
		System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
		return flag;
	}

	public void resetScoringValues(Intimation argIntimation){

		List<HospitalScoringDTO> listOfValues = hospitalScroingDetails.getValues();
		for(HospitalScoringDTO rec : listOfValues){
			if(rec.getActualSubCategoryId() == null && (rec.getActualCategoryId().intValue() == 8)){
				rec.setScoringBooleanValue(false);
				rec.setScoringValue("N");
				rec.setOptionEnabled(true);
			}else{
				rec.setScoringBooleanValue(null);
				rec.setScoringValue(null);
				rec.setOptionEnabled(true);
			}
		}
		hospitalScroingDetails.addList(listOfValues);
		hospitalScroingDetails.refreshTable();
	}

	@SuppressWarnings("static-access")
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

			}
		});

	}

	@SuppressWarnings("static-access")
	private void showErrorMessage(String eMsg) {

		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}
}
