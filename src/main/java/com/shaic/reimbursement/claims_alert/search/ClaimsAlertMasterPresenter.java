package com.shaic.reimbursement.claims_alert.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.ClaimRemarksAlerts;
import com.shaic.ClaimRemarksDocs;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.mapper.PreauthMapper;
import com.shaic.domain.Intimation;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.server.VaadinSession;



@ViewInterface(ClaimsAlertMasterView.class)
public class ClaimsAlertMasterPresenter extends AbstractMVPPresenter<ClaimsAlertMasterView> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5998304026505597839L;

	@EJB
	private ClaimsAlertMasterService claimsAlertMasterService;

	public static final String SUBMIT_CLAIMS_ALERT = "submit_claims_alert";
	
	public static final String SEARCH_CLAIMS_ALERT = "search_claims_alert";
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
	}

	protected void searchByIntimationNo(@Observes @CDIEvent(SEARCH_CLAIMS_ALERT) final ParameterDTO parameters) {

		if(parameters.getPrimaryParameter() != null) {
			String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);	
			String intimationNo = (String) parameters.getPrimaryParameter();
			Intimation intimation = claimsAlertMasterService.getIntimationDetails(intimationNo);

			if(intimation != null) {
				List<ClaimsAlertTableDTO> dtoList = new ArrayList<ClaimsAlertTableDTO>();
				ClaimsAlertTableDTO dto = null;
				List<ClaimRemarksAlerts> claimRemarksAlerts = 
						claimsAlertMasterService.getClaimsAlertsByIntitmation(intimation.getIntimationId());
				if(claimRemarksAlerts != null
						&& !claimRemarksAlerts.isEmpty()){

					for(ClaimRemarksAlerts remarksAlerts : claimRemarksAlerts){
						dto =new ClaimsAlertTableDTO();
						dto.setClaimsAlertKey(remarksAlerts.getKey());
						dto.setIntimationNo(remarksAlerts.getIntitmationNo());
						SelectValue catagory = new SelectValue();
						catagory.setId(remarksAlerts.getAlertCategory().getKey());
						catagory.setValue(remarksAlerts.getAlertCategory().getValue());
						dto.setAlertCategory(catagory);
						dto.setRemarks(remarksAlerts.getRemarks());
						dto.setDeleteFlag(remarksAlerts.getDeleteFlag());
						dto.setCreatedBy(remarksAlerts.getCreatedBy());
						dto.setCreatedDate(remarksAlerts.getCreatedDate());
						if(remarksAlerts.getActiveStatus() !=null
								&& remarksAlerts.getActiveStatus().equals("1")){
							dto.setEnable(true);
						}else if(remarksAlerts.getActiveStatus() !=null
								&& remarksAlerts.getActiveStatus().equals("0")){
							dto.setDisable(true);
						}
						if(remarksAlerts.getRemarksDocs() != null
								&& !remarksAlerts.getRemarksDocs().isEmpty()){
							PreauthMapper preauthMapper = PreauthMapper.getInstance();
							dto.setDocsDTOs(preauthMapper.getClaimsAlertDocsDTOList(remarksAlerts.getRemarksDocs()));
						}
						dtoList.add(dto);
					}
				}else{
					dto =new ClaimsAlertTableDTO();	
				}
				dto.setUserName(userId);
				dtoList.add(dto);

				view.generateTableForClaimsAlerts(dtoList,claimsAlertMasterService.getreferenceDatas(intimation.getIntimationId()));
			}else {
				view.buildFailureLayout("No Records found");
			}
		}else {
			view.buildFailureLayout("Please enter policy No. ");
		}
	}


	protected void submitIDA(
			@Observes @CDIEvent(SUBMIT_CLAIMS_ALERT) final ParameterDTO parameters) {
		List<ClaimsAlertTableDTO> dto = (List<ClaimsAlertTableDTO>) parameters.getPrimaryParameter();
		claimsAlertMasterService.submitClaimsAlertTable(dto);
		view.buildSuccessLayout();
	}

}
