package com.shaic.claim.reimbursement.talktalktalk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.cashlessprocess.processicac.search.SearchProcessICACTableDTO;
import com.shaic.claim.cashlessprocess.processicac.search.ViewICACTeamFinalResponseTabel;
import com.shaic.claim.cashlessprocess.processicac.search.ViewICACTeamResponseTabel;
import com.shaic.claim.cashlessprocess.processicac.search.ViewProcessingDoctorDetailsTabel;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.rrc.services.ViewTALKTALKTable;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.TalkTalkTalk;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewTalkTalkTalkDetailsUI extends ViewComponent{


	@Inject
	private PreauthDTO bean;

	private VerticalLayout verticalMain;

	private TextArea txtRemarks;
	
	private OptionGroup fiancalDecisionOption;
	
	@Inject
	private Instance<ViewTalkTalkTalkDetailsTable> viewTalkTalkTalkDetailsTableInstance;

	private ViewTalkTalkTalkDetailsTable viewTalkTalkTalkDetailsTableObj;
	
	@Inject
	private Instance<ViewTalkTalkTalkDetailsTableCustomer> viewTalkTalkTalkDetailsTableInstanceCustomer;

	private ViewTalkTalkTalkDetailsTableCustomer viewTalkTalkTalkDetailsTableObjCustomer;

	@Inject
	private Instance<ViewTALKTALKTable> viewTALKTALKTableInstance;

	private ViewTALKTALKTable viewTALKTALKTableObj;

	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private InitiateTalkTalkTalkService talkService;
	
	@EJB
	private ClaimService claimService;

	private TextField intimationNumber;

	private TextField clmType;

	private Label icacHLabel;

	private BeanFieldGroup<PreauthDTO> binder;
	

	@SuppressWarnings("deprecation")
	public void init(PreauthDTO bean,String intimationNo) {

		this.bean=bean;
		this.binder = new BeanFieldGroup<PreauthDTO>(PreauthDTO.class);
		this.binder.setItemDataSource(bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		List<InitiateTalkTalkTalkDTO> talkTalkTalkViewDetailsListHosp = talkService.setViewTalkTalkTalkTableValuesHosp(intimationNo);
		List<InitiateTalkTalkTalkDTO> talkTalkTalkViewDetailsListCust = talkService.setViewTalkTalkTalkTableValuesCustomer(intimationNo);
		
		List<ExtraEmployeeEffortDTO> viewTALKCATDTOs = claimService.getCategoryDetailsFromTALKView(intimationNo);
		
		viewTALKTALKTableObj = viewTALKTALKTableInstance.get();
		viewTALKTALKTableObj.init("", false, false);
		viewTALKTALKTableObj.setCaption("RRC");
		if(viewTALKCATDTOs !=null && !viewTALKCATDTOs.isEmpty()){
			viewTALKTALKTableObj.setTableList(viewTALKCATDTOs);
		}	
	
		
		viewTalkTalkTalkDetailsTableObj= viewTalkTalkTalkDetailsTableInstance.get();
		viewTalkTalkTalkDetailsTableObj.init("", false, false);
//		viewTalkTalkTalkDetailsTableObj.setCaption("Post Processing");
		FormLayout hospFormLayout = new FormLayout();
		TextField hosp = new TextField("Type of Communication");
		hosp.setValue("Spoken to Hospital");
		hosp.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		hospFormLayout.addComponent(hosp);
		
		if(talkTalkTalkViewDetailsListHosp !=null && ! talkTalkTalkViewDetailsListHosp.isEmpty()){
			//setViewTalkTalkTalkTableValues(intimationNo);
			viewTalkTalkTalkDetailsTableObj.setTableList(talkTalkTalkViewDetailsListHosp);
		}
		
		viewTalkTalkTalkDetailsTableObjCustomer= viewTalkTalkTalkDetailsTableInstanceCustomer.get();
		viewTalkTalkTalkDetailsTableObjCustomer.init("", false, false);
		FormLayout custFormLayout = new FormLayout();
		TextField cust = new TextField("Type of Communication");
		cust.setValue("Spoken to Customer");
		cust.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		custFormLayout.addComponent(cust);
		if(talkTalkTalkViewDetailsListCust !=null && ! talkTalkTalkViewDetailsListCust.isEmpty()){
			viewTalkTalkTalkDetailsTableObjCustomer.setTableList(talkTalkTalkViewDetailsListCust);
		}
		
		Label label = new Label("Post Processing");
		
		VerticalLayout tableLayout = null;
		
		tableLayout = new VerticalLayout(viewTALKTALKTableObj,label,hospFormLayout,viewTalkTalkTalkDetailsTableObj,custFormLayout,viewTalkTalkTalkDetailsTableObjCustomer);
		tableLayout.setMargin(true);
		tableLayout.setSpacing(true);
		verticalMain = new VerticalLayout(tableLayout);
		verticalMain.setSizeFull();
		setCompositionRoot(verticalMain);
	}


	
	public void setViewTalkTalkTalkTableValues(String initmationNumber)
	 {

		//Intimation intimation = talkService.getIntimationByNo(initmationNumber);
		if(initmationNumber !=null){
		List<TalkTalkTalk> talkTalkTalkViewDetailsList = talkService.getTalkTalkTalkDetailsByIntimationNumList(initmationNumber);
		 List<InitiateTalkTalkTalkDTO> finalTableDTOList = new ArrayList<InitiateTalkTalkTalkDTO>();
		 if(null != talkTalkTalkViewDetailsList){
			 
			 InitiateTalkTalkTalkDTO initiateTalkTalkTalkDTO = null;
			 
			 for (TalkTalkTalk talkTalkTalk : talkTalkTalkViewDetailsList) {
				
				 initiateTalkTalkTalkDTO = new InitiateTalkTalkTalkDTO();
				 if(talkTalkTalk.getTypeOfCommunication() !=null){
						MastersValue masterValue = talkTalkTalk.getTypeOfCommunication();
						 SelectValue typeOfComm=new SelectValue();
						 typeOfComm.setValue(masterValue.getValue());
						 typeOfComm.setId(masterValue.getKey());
						 initiateTalkTalkTalkDTO.setTypeOfCommunication(typeOfComm);
						
					}
					if(talkTalkTalk.getDateAndTimeofCall() !=null){
						initiateTalkTalkTalkDTO.setTalkSpokenDate(talkTalkTalk.getDateAndTimeofCall());
					}
					
					if(talkTalkTalk.getContactNumber() !=null){
						initiateTalkTalkTalkDTO.setTalkMobto(talkTalkTalk.getContactNumber().toString());
					}
					if(talkTalkTalk.getSpokenTo() !=null){
						initiateTalkTalkTalkDTO.setTalkSpokento(talkTalkTalk.getSpokenTo());
					}
					if(talkTalkTalk.getRemarks() !=null){
						initiateTalkTalkTalkDTO.setRemarks(talkTalkTalk.getRemarks());
					}
					if(talkTalkTalk.getProcessingUserName() !=null){
						initiateTalkTalkTalkDTO.setProcessingUserName(talkTalkTalk.getProcessingUserName());
					}
//					initiateTalkTalkTalkDTO.setFileName("http://192.168.4.157:8157/calls/2022-05-19/out/6385132233_TVC_TVCQ_VP040105_manual_1009_20220519104103.wav");
					if(talkTalkTalk.getReferenceId() != null){
						DialerStatusLog dialerStatus = talkService.getDialerStatusLog(talkTalkTalk.getReferenceId(),talkTalkTalk.getConvoxId().toString());
						if(dialerStatus != null){
							String callstartDateTime = dialerStatus.getCallDate().concat(dialerStatus.getCallHour()).concat(dialerStatus.getCallMin());
							Long callMins = Long.valueOf(dialerStatus.getCallMin());
							String callDurationSplit[] = dialerStatus.getCallDuration().split(":");
							String hour = callDurationSplit[0];
							String min = callDurationSplit[0];
							Long endcallmin = callMins+Long.valueOf(min);
							String endCall = dialerStatus.getCallDate().concat(dialerStatus.getCallHour()).concat(endcallmin.toString());
							initiateTalkTalkTalkDTO.setCallStartTime(callstartDateTime);
							initiateTalkTalkTalkDTO.setCallEndTime(endCall);
							initiateTalkTalkTalkDTO.setCallDuration(dialerStatus.getCallDuration());
//							initiateTalkTalkTalkDTO.setFileName(dialerStatus.getFileName());
							
							
						}
					}
				 
					finalTableDTOList.add(initiateTalkTalkTalkDTO);
			}
			 
			 for (InitiateTalkTalkTalkDTO initiateTalkTalkDTO : finalTableDTOList) {
				 viewTalkTalkTalkDetailsTableObj.addBeanToList(initiateTalkTalkDTO);

				}
			 viewTalkTalkTalkDetailsTableObj.setTableList(finalTableDTOList);
		 }
				 
		}
	 }
	
}
