package com.shaic.claim;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.reports.negotiationreport.NegotiationAmountDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.Stage;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewNegotiationDetailsPage extends ViewComponent{
	
	@Inject
	private ViewNegotiationDetailTable viewNegotiationTable;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;
	
	private VerticalLayout mainLayout;
	
	public void init(Long intimaitonkey){
		viewNegotiationTable.init("", false, false);
		viewNegotiationTable.setTableList(getNegotiationList(intimaitonkey));
		mainLayout = new VerticalLayout(viewNegotiationTable);
		setCompositionRoot(mainLayout);
	}
	
	private List<ViewNegotiationDetailsDTO> getNegotiationList(Long intimaitonkey){
		List<ViewNegotiationDetailsDTO> negotiationDtls = new ArrayList<ViewNegotiationDetailsDTO>();
		
		List<NegotiationAmountDetails> negotionAmtDtls  = preauthService.getNegotiationDetails(intimaitonkey);
		Stage statusValue = null;
		if(negotionAmtDtls != null){
			for (NegotiationAmountDetails negotiationAmountDetails : negotionAmtDtls) {
				ViewNegotiationDetailsDTO neg = new ViewNegotiationDetailsDTO();
				neg.setNegotiatedAmt(negotiationAmountDetails.getNegotiatedAmt());
				neg.setIntimationNo(negotiationAmountDetails.getIntimationNo());
				neg.setSavedAmt(negotiationAmountDetails.getSavedAmt());
				neg.setClaimApprovedAmt(negotiationAmountDetails.getClaimAppAmt());
				String employeename = masterService.getEmployeeByName(negotiationAmountDetails.getModifiedBy());
				neg.setNegotiatedUpdateBy(employeename+"-"+negotiationAmountDetails.getModifiedBy().toUpperCase());
				String updateDate = SHAUtils.getDateFormat(negotiationAmountDetails.getModifiedDate().toString());
				neg.setUpdateDate(negotiationAmountDetails.getModifiedDate().toString());
				if(negotiationAmountDetails.getStatus() != null && negotiationAmountDetails.getStatus().getKey() !=null) {
					statusValue = preauthService.getStageByKey(negotiationAmountDetails.getStage().getKey());
					neg.setIntimationStage(statusValue.getStageName());
				}
				negotiationDtls.add(neg);
			}
		}
		
		return negotiationDtls;
	}
	
	

}
