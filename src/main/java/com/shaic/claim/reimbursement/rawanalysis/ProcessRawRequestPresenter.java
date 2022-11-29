package com.shaic.claim.reimbursement.rawanalysis;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.ClaimService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.RawInvsDetails;
import com.shaic.domain.RawInvsHeaderDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(ProcessRawRequestWizard.class)
public class ProcessRawRequestPresenter extends AbstractMVPPresenter<ProcessRawRequestWizard>{
	
	public static final String SET_TABLE_DATA = "setTableData";
	
	public static final String SET_REPLIED_DATA = "setRepliedData";
	
	public static final String SUBMIT_REPLY_DATA = "submitrepliedData";
	
	public static final String GET_RESOLUTION_RAW = "getrawresolutions";
	
	@EJB
	private SearchProcessRawRequestService searchProcessRequest;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;
	
	@SuppressWarnings({ "deprecation" })
	public void setTableData(@Observes @CDIEvent(SET_TABLE_DATA) final ParameterDTO parameters) {
		SearchProcessRawRequestTableDto requestDtls = (SearchProcessRawRequestTableDto) parameters.getPrimaryParameter();
		List<RawInitiatedRequestDTO> escReqList = new ArrayList<RawInitiatedRequestDTO>();	
			List<RawInvsHeaderDetails> rawDetails = claimService.getRawInvestDetailsByIntimation(requestDtls.getIntimationNo());
			for (RawInvsHeaderDetails rawInvsHeaderDetails : rawDetails) {
				
				List<RawInvsDetails> invstDtls = claimService.getRawInvestDetails(rawInvsHeaderDetails.getKey());
				for (RawInvsDetails rawInvsDetails : invstDtls) {
					RawInitiatedRequestDTO reqDtls = new RawInitiatedRequestDTO();
					reqDtls.setCategory(rawInvsDetails.getRawCategory().getCategoryDescription());
					reqDtls.setSubCategory(null != rawInvsDetails.getRawSubCategory()?rawInvsDetails.getRawSubCategory().getSubCategoryDescription():null);
					reqDtls.setRemarksForEscalation(rawInvsDetails.getRequestedRemarks());
					String intitiatedName = masterService.getEmployeeByName(null != rawInvsDetails.getCreatedBy()?rawInvsDetails.getCreatedBy():null);
					String initiatedByName = "";
					if(null != intitiatedName){
						initiatedByName = rawInvsDetails.getCreatedBy()+"-"+intitiatedName;
					}
					reqDtls.setInitiatedBy(initiatedByName);
					reqDtls.setIntiatedDate(rawInvsDetails.getCreatedDate());
					reqDtls.setStatusId(rawInvsDetails.getRequestedStatus());
					reqDtls.setStageId(rawInvsDetails.getRequestedStage());
					Stage stg = preauthService.getStageByKey(rawInvsDetails.getRequestedStage());
					Status sts = preauthService.getStatusByKey(rawInvsDetails.getRequestedStatus());
					reqDtls.setStatusValue(sts.getProcessValue());
					reqDtls.setStageValue(stg.getStageName());
					reqDtls.setResolutionRawValue(rawInvsDetails.getRedolutionType());
					reqDtls.setRawinvestigationKey(rawInvsDetails.getKey());
					reqDtls.setRawRemarksUpdatedBy(rawInvsDetails.getRepliedBy());
					reqDtls.setRawRemarksUpadedDate(rawInvsDetails.getRepliedDate());
					reqDtls.setRemarksfromRaw(rawInvsDetails.getRepliedRemarks());
					reqDtls.setWorkFlowObject(requestDtls.getWorkFlowObject());
					escReqList.add(reqDtls);
				}
			}
		
		view.setTableValues(escReqList);
	}

	public void setRepliedData(@Observes @CDIEvent(SET_REPLIED_DATA) final ParameterDTO parameters) {
		SearchProcessRawRequestTableDto requestDtls = (SearchProcessRawRequestTableDto) parameters.getPrimaryParameter();
		List<RawInitiatedRequestDTO> repliedList = new ArrayList<RawInitiatedRequestDTO>();
			List<RawInvsHeaderDetails> rawDetails = claimService.getRawInvestDetailsByIntimation(requestDtls.getIntimationNo());
			for (RawInvsHeaderDetails rawInvsHeaderDetails : rawDetails) {
				
				List<RawInvsDetails> invstDtls = claimService.getRepliedRawData(rawInvsHeaderDetails.getKey());
				for (RawInvsDetails rawInvsDetails : invstDtls) {
					RawInitiatedRequestDTO reqDtls = new RawInitiatedRequestDTO();
					reqDtls.setCategory(rawInvsDetails.getRawCategory().getCategoryDescription());
					if(null != rawInvsDetails.getRawSubCategory() && null !=  rawInvsDetails.getRawSubCategory().getSubCategoryDescription())
					{
						String categoryWithSubCategory = "";
						{	
							/*categoryWithSubCategory = rawInvsDetails.getRawCategory().getCategoryDescription()+"("+ rawInvsDetails.getRawSubCategory().getSubCategoryDescription()+")";
							reqDtls.setCategory(categoryWithSubCategory);*/
							reqDtls.setSubCategory(null != rawInvsDetails.getRawSubCategory()?rawInvsDetails.getRawSubCategory().getSubCategoryDescription():null);

						}
					}
					reqDtls.setRemarksForEscalation(rawInvsDetails.getRequestedRemarks());
					String intitiatedName = masterService.getEmployeeByName(null != rawInvsDetails.getCreatedBy()?rawInvsDetails.getCreatedBy():null);
					String initiatedByName = "";
					if(null != intitiatedName){
						initiatedByName = rawInvsDetails.getCreatedBy()+"-"+intitiatedName;
					}
					reqDtls.setInitiatedBy(initiatedByName);
					reqDtls.setIntiatedDate(rawInvsDetails.getCreatedDate());
					reqDtls.setStatusId(rawInvsDetails.getRequestedStatus());
					reqDtls.setStageId(rawInvsDetails.getRequestedStage());
					Stage stg = preauthService.getStageByKey(rawInvsDetails.getRequestedStage());
					Status sts = preauthService.getStatusByKey(rawInvsDetails.getRequestedStatus());
					reqDtls.setStatusValue(sts.getProcessValue());
					reqDtls.setStageValue(stg.getStageName());
					reqDtls.setResolutionRawValue(rawInvsDetails.getRedolutionType());
					reqDtls.setRawinvestigationKey(rawInvsDetails.getKey());
					String updatorName = masterService.getEmployeeByName(null != rawInvsDetails.getRepliedBy()?rawInvsDetails.getRepliedBy():null);
					String updatedNameWithId = "";
					if(null != intitiatedName){
						updatedNameWithId = rawInvsDetails.getRepliedBy()+"-"+updatorName;
					}
					reqDtls.setRawRemarksUpdatedBy(updatedNameWithId);
					reqDtls.setRawRemarksUpadedDate(rawInvsDetails.getRepliedDate());
					reqDtls.setRemarksfromRaw(rawInvsDetails.getRepliedRemarks());
					reqDtls.setKey(rawInvsDetails.getKey());
					repliedList.add(reqDtls);
				}
		}
	
		view.setRepliedRawTableValues(repliedList);
	}
	
	@SuppressWarnings({ "deprecation" })
	public void setSubmitReplyData(@Observes @CDIEvent(SUBMIT_REPLY_DATA) final ParameterDTO parameters) {
		
		List<RawInitiatedRequestDTO> submitData = (List<RawInitiatedRequestDTO>) parameters.getPrimaryParameter();
		String userName = (String) parameters.getSecondaryParameter(0, String.class);
		searchProcessRequest.submitRepliedData(submitData,userName);
		view.buildSuccessLayout();
	
	}
	
	public void setResolutionRaw(@Observes @CDIEvent(GET_RESOLUTION_RAW) final ParameterDTO parameters) {
		BeanItemContainer<SelectValue> resolutionRaws =  masterService.getRawCategory(SHAConstants.RAW_RESOLUTION_CATEGORY_TYPE);
		view.setResolutionData(resolutionRaws);
	}
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
