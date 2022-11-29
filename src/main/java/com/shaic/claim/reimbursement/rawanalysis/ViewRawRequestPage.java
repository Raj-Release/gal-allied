package com.shaic.claim.reimbursement.rawanalysis;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.ClaimService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.RawInvsDetails;
import com.shaic.domain.RawInvsHeaderDetails;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewRawRequestPage extends ViewComponent{
	@Inject
	private ViewRawRequestTable viewRawRequestTable;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private PreauthService preAuthService;
	
	@EJB
	private MasterService masterService;
	
	private VerticalLayout mainLayout;

	
	public void init(String intimationNo) {		
		
		viewRawRequestTable.init("", false, false);	
		List<RawInitiatedRequestDTO> rawDetails = getRawDetails(intimationNo);
		viewRawRequestTable.setTableList(rawDetails);
		mainLayout = new VerticalLayout(/*labelHeader,*/ viewRawRequestTable);
		setCompositionRoot(mainLayout);
	}
	
	public List<RawInitiatedRequestDTO> getRawDetails(String intimationNo)
	{
		List<RawInitiatedRequestDTO> requestDetails = new ArrayList<RawInitiatedRequestDTO>();
		List<RawInvsHeaderDetails> rawDetails = claimService.getRawInvestDetailsByIntimation(intimationNo);
			for (RawInvsHeaderDetails rawInvsHeaderDetails : rawDetails) {				
				List<RawInvsDetails>  invstDtls = claimService.getRawDetailsWithoutFilter(rawInvsHeaderDetails.getKey());
				for (RawInvsDetails rawInvsDetails : invstDtls) {
					RawInitiatedRequestDTO reqDtls = new RawInitiatedRequestDTO();					
					reqDtls.setCategory(rawInvsDetails.getRawCategory().getCategoryDescription());
					if(null != rawInvsDetails.getRawSubCategory() && null !=  rawInvsDetails.getRawSubCategory().getSubCategoryDescription())
					{
						String categoryWithSubCategory = "";
						reqDtls.setSubCategory(rawInvsDetails.getRawSubCategory().getSubCategoryDescription());
						/*String categoryWithSubCategory = "";
						{	
							categoryWithSubCategory = rawInvsDetails.getRawCategory().getCategoryDescription()+"("+ rawInvsDetails.getRawSubCategory().getSubCategoryDescription()+")";
							reqDtls.setCategory(categoryWithSubCategory);
						}*/
					}
					reqDtls.setRemarksForEscalation(rawInvsDetails.getRequestedRemarks());
					String intitiatedName = masterService.getEmployeeByName(null != rawInvsDetails.getCreatedBy()?rawInvsDetails.getCreatedBy():null);
					String initiatedByName = "";
					if(null != intitiatedName){
						initiatedByName = rawInvsDetails.getCreatedBy()+"-"+intitiatedName;
					}
					reqDtls.setInitiatedBy(initiatedByName);
					reqDtls.setIntiatedDate(rawInvsDetails.getCreatedDate());
					Stage stg = preAuthService.getStageByKey(rawInvsDetails.getRequestedStage());
					Status sts = preAuthService.getStatusByKey(rawInvsDetails.getRequestedStatus());
					reqDtls.setStageValue(stg.getStageName());
					reqDtls.setResolutionRawValue(rawInvsDetails.getRedolutionType());
					reqDtls.setRemarksfromRaw(rawInvsDetails.getRepliedRemarks());
					String updatedNameWithId = "";
					String modifiedNameWithId = "";
					if(null != rawInvsDetails.getRepliedBy()){
						String updatorName = masterService.getEmployeeByName(rawInvsDetails.getRepliedBy());
						if(null != intitiatedName){
							updatedNameWithId = rawInvsDetails.getRepliedBy()+"-"+updatorName;
						}
					}
					
					//Additonal change for CR2019023
					if(null != rawInvsDetails.getModifyby()){
						String updatorName = masterService.getEmployeeByName(rawInvsDetails.getModifyby());
						if(null != intitiatedName){
							modifiedNameWithId = rawInvsDetails.getModifyby()+"-"+updatorName;
						}
					}
					if(rawInvsDetails.getRequestedStatus() == 271l){
						reqDtls.setRawRemarksUpdatedBy(modifiedNameWithId);
						reqDtls.setRawRemarksUpadedDate(rawInvsDetails.getModifiedDate());						
					} else {
						reqDtls.setRawRemarksUpdatedBy(updatedNameWithId);
						reqDtls.setRawRemarksUpadedDate(rawInvsDetails.getRepliedDate());						
					}						
					reqDtls.setStatusValue(sts.getProcessValue());
					reqDtls.setStatusId(rawInvsDetails.getRequestedStatus());
						requestDetails.add(reqDtls);
				}
				
			}
			return requestDetails;
	}
}
