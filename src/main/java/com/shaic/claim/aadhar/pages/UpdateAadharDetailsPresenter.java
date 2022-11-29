package com.shaic.claim.aadhar.pages;

import java.util.Date;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.aadhar.search.SearchUpdateAadharTableDTO;
import com.shaic.claim.fileUpload.MultipleUploadDocumentDTO;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(UpdateAadharDetailsView.class)
public class UpdateAadharDetailsPresenter extends AbstractMVPPresenter<UpdateAadharDetailsView>{
	
	public static final String UPLOAD_EVENT = "Upload Aadhar file";
	
	public static final String SUBMIT_EVENT = "Submit Aadhar details";
	
	@EJB
	private ReimbursementService reimbursementService;

	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private InsuredService insuredService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public void setUploadDocument(
			@Observes @CDIEvent(UPLOAD_EVENT) final ParameterDTO parameters) {

		Long intimationKey = (Long) parameters.getPrimaryParameter();

		String fileName = (String) parameters.getSecondaryParameter(0,
				String.class);
		
		String token = (String)parameters.getSecondaryParameter(1, String.class);
		
		String screenName = (String)parameters.getSecondaryParameter(2, String.class);
		String userName = (String)parameters.getSecondaryParameter(3, String.class);
		
		MultipleUploadDocumentDTO dto = new MultipleUploadDocumentDTO();
		dto.setFileName(fileName);
		dto.setTransactionKey(intimationKey);
		dto.setFileToken(token);
		dto.setTransactionName(screenName);
		dto.setUploadedDate(new Date());
		dto.setUsername(userName);
		reimbursementService.updateDocumentDetails(dto);

		Intimation intimationByKey = intimationService.getIntimationByKey(intimationKey);
		intimationService.updateAadharDocumentDetails(dto,intimationByKey.getIntimationId());
		view.updateTableValues(intimationByKey.getKey());
		
	}
	
	public void submitEvent(
			@Observes @CDIEvent(SUBMIT_EVENT) final ParameterDTO parameters) {


		SearchUpdateAadharTableDTO tableDto = (SearchUpdateAadharTableDTO) parameters.getPrimaryParameter();
		
		String screenName = (String) parameters.getSecondaryParameter(0,String.class);
		String policyNo = tableDto.getPolicyNo();
		Long insuredKey  = tableDto.getInsuredKey();
		Long aadharNo = Long.valueOf(tableDto.getAadharCardNo());
		String insuredGender = null;
		String insuredName = null;
		Date insuredDOb = null;
		Boolean succesMsg = false;
		String formatDate;
		
		Insured insuredDtls = insuredService.getCLSInsured(insuredKey.toString());
		if(insuredDtls != null){
			insuredGender = insuredDtls.getInsuredGender() !=null ? insuredDtls.getInsuredGender().getValue():"";
			insuredName = insuredDtls.getInsuredName();
			insuredDOb = insuredDtls.getInsuredDateOfBirth();
			if(insuredDOb != null){
			 formatDate = SHAUtils.formatDate(insuredDOb);
		} else {
			formatDate = "";
		}
		/*	java.sql.Date sqlInsuredDob;
			if(insuredDOb != null){
				sqlInsuredDob = new java.sql.Date(insuredDOb.getTime());
			} else {
				sqlInsuredDob = null;
			}*/
		
		Map<String, Object> getGenerateAadhar = dbCalculationService.getAadharGenaratorNo(aadharNo, insuredName, formatDate,insuredGender);
		if(getGenerateAadhar != null){
			if(getGenerateAadhar.containsKey("refFlag")){
				String refMsg = String.valueOf(getGenerateAadhar.get("refFlag"));
				if(refMsg.equals(SHAConstants.AADHAR_N)){
					String generateNo = String.valueOf(getGenerateAadhar.get("refGenerateNo"));
					insuredDtls.setAadharNo(Long.valueOf(generateNo));
					insuredDtls.setAadharRemarks(tableDto.getAadharRemarks());
					insuredService.updateAadharCardDetails(insuredDtls);
					succesMsg = Boolean.TRUE;
				} else if(refMsg.equals(SHAConstants.AADHAR_D)){
//					view.popforAadhar();
					String generateNo = String.valueOf(getGenerateAadhar.get("refGenerateNo"));
					insuredDtls.setAadharNo(Long.valueOf(generateNo));
					insuredDtls.setAadharRemarks(tableDto.getAadharRemarks());
					insuredService.updateAadharCardDetails(insuredDtls);
					succesMsg = Boolean.TRUE;
				} else if(refMsg.equals(SHAConstants.AADHAR_E)){
					succesMsg = Boolean.FALSE;
				}
			}
		}
		}
		if(succesMsg){
			if(screenName!=null && screenName.equalsIgnoreCase(ReferenceTable.UPDATE_AADHAR_SCREEN)){
				view.result();
			}
		} else {
			view.aadharfailureLayout();
		}
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
