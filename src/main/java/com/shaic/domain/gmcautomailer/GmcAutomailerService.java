package com.shaic.domain.gmcautomailer;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.domain.preauth.NegotiationDetails;

@Stateless
public class GmcAutomailerService  extends AbstractDAO<NegotiationDetails>{

	@PersistenceContext
	protected EntityManager entityManager;

	public GmcAutomailerService() {
		super();
	}
	
	public GmcAutomailerTableDTO search(
			GmcAutomailerFormDTO formDTO) {
		try 
		{
			String strPolicyNo = formDTO.getPolicyNo();		
			String strBranchCode = formDTO.getBranchCode();
			GmcAutomailerTableDTO gmcAutomailerTableDTO = new GmcAutomailerTableDTO();
			if(strPolicyNo != null && !strPolicyNo.isEmpty()) {
				MasGMCServiceProvider masGMCServiceProvider = getDetailsByPolicyNo(strPolicyNo);
				if(masGMCServiceProvider != null && masGMCServiceProvider.getPolicyNo() != null) {
				gmcAutomailerTableDTO.setPolicyNo_branchCode(masGMCServiceProvider.getPolicyNo()); 
				gmcAutomailerTableDTO.setEmailId(masGMCServiceProvider.getEmailId()); }
			
				if(masGMCServiceProvider.getActiveStatus() != null && masGMCServiceProvider.getActiveStatus().equalsIgnoreCase(SHAConstants.YES_FLAG)) {
					gmcAutomailerTableDTO.setDisable(false);
				}else{
					gmcAutomailerTableDTO.setDisable(true);
				}
				
			}else if(strBranchCode != null && !strBranchCode.isEmpty()) {
				MasBranchEmail masBranchEmail = getDetailsByBranchCode(strBranchCode);
				if(masBranchEmail != null && masBranchEmail.getDivCode() != null) {
				gmcAutomailerTableDTO.setPolicyNo_branchCode(masBranchEmail.getDivCode().toString());
				gmcAutomailerTableDTO.setEmailId(masBranchEmail.getBranchEmailId());  }
				if(masBranchEmail.getActiveStatus() != null && masBranchEmail.getActiveStatus().equalsIgnoreCase(SHAConstants.YES_FLAG)) {
					gmcAutomailerTableDTO.setDisable(false);
				}else{
					gmcAutomailerTableDTO.setDisable(true);
				}
			}
			return gmcAutomailerTableDTO;
		}
			catch(Exception e){
				e.printStackTrace();
			}
		return null;		
	}
	
	@Override
	public Class<NegotiationDetails> getDTOClass() {
		return NegotiationDetails.class;
	}
	
	public void submitEmailId(List<GmcAutomailerTableDTO> emailDetails,String userName)
	{
		if(emailDetails != null && !emailDetails.isEmpty()) {
		if(emailDetails.get(0).getPolicyNo_branchCode().startsWith("P") || emailDetails.get(0).getPolicyNo_branchCode().startsWith("C")) {
			String policyNo = emailDetails.get(0).getPolicyNo_branchCode();
			MasGMCServiceProvider masGMCServiceProvider = getDetailsByPolicyNo(emailDetails.get(0).getPolicyNo_branchCode());
			if(null != masGMCServiceProvider){
				if(emailDetails.get(0).getDisable().equals(Boolean.FALSE)){
					masGMCServiceProvider.setEmailId(emailDetails.get(0).getEmailId());
					masGMCServiceProvider.setActiveStatus(SHAConstants.YES_FLAG);
					masGMCServiceProvider.setModifiedDate(new java.sql.Timestamp(System
									.currentTimeMillis()));
					masGMCServiceProvider.setModifiedBy(userName);
					entityManager.merge(masGMCServiceProvider);
					showInformation("Value Update Successfully");
				}else {

					masGMCServiceProvider.setEmailId(emailDetails.get(0).getEmailId());
					masGMCServiceProvider.setActiveStatus(SHAConstants.N_FLAG);
					masGMCServiceProvider.setModifiedDate(new java.sql.Timestamp(System
									.currentTimeMillis()));
					masGMCServiceProvider.setModifiedBy(userName);
					entityManager.merge(masGMCServiceProvider);
					showInformation("Value Update Successfully");
				}
			}
		}else{
			char[] branchCode = emailDetails.get(0).getPolicyNo_branchCode().toCharArray();
			int codeCheck=0;
			for(int i=0;i<branchCode.length;i++) {
				if(!Character.isDigit(branchCode[i])) {
					codeCheck++;
				}
			}
			if(codeCheck==0) {
				MasBranchEmail masBranchEmail = getDetailsByBranchCode(emailDetails.get(0).getPolicyNo_branchCode());
				if(null != masBranchEmail) {
					if(emailDetails.get(0).getDisable().equals(Boolean.FALSE)){
						masBranchEmail.setBranchEmailId(emailDetails.get(0).getEmailId());
						masBranchEmail.setActiveStatus(SHAConstants.YES_FLAG);
						masBranchEmail.setModifiedDate(new java.sql.Timestamp(System
								.currentTimeMillis()));
						entityManager.merge(masBranchEmail);
						showInformation("Value Update Successfully");
					}else {
						masBranchEmail.setBranchEmailId(emailDetails.get(0).getEmailId());
						masBranchEmail.setActiveStatus(SHAConstants.N_FLAG);
						masBranchEmail.setModifiedDate(new java.sql.Timestamp(System
								.currentTimeMillis()));
						entityManager.merge(masBranchEmail);
						showInformation("Value Update Successfully");
					}
				}
			}
		}
	}
}
	
	public void submitNewEntry(GmcAutomailerFormDTO emailDetails,String userName)
	{
		if(emailDetails.getNewPolicyNo() != null && (emailDetails.getNewPolicyNo().startsWith("P")
				||emailDetails.getNewPolicyNo().startsWith("C"))) {
		String policyNo = emailDetails.getNewPolicyNo();
		MasGMCServiceProvider masGMCServiceProvider = getDetailsByPolicyNo(emailDetails.getNewPolicyNo());
		if(null == masGMCServiceProvider){
			masGMCServiceProvider = new MasGMCServiceProvider();
			masGMCServiceProvider.setPolicyNo(emailDetails.getNewPolicyNo().trim());
			masGMCServiceProvider.setEmailId(emailDetails.getEmailId());
			masGMCServiceProvider.setActiveStatus(SHAConstants.YES_FLAG);
			masGMCServiceProvider.setCreatedDate(new java.sql.Timestamp(System
							.currentTimeMillis()));
			masGMCServiceProvider.setCreatedBy(userName);
			entityManager.persist(masGMCServiceProvider);
			showInformation("New Record Added Successfully");
			emailDetails.setCheck(SHAConstants.YES_FLAG);
		}else{
			showError("Policy already exists");
			emailDetails.setCheck(SHAConstants.N_FLAG);
		}
		}else{
			
			char[] branchCode = emailDetails.getNewPolicyNo().toCharArray();
			int codeCheck=0;
			for(int i=0;i<branchCode.length;i++) {
				if(!Character.isDigit(branchCode[i])) {
					codeCheck++;
				}
			}
			if(codeCheck==0) {
				MasBranchEmail masBranchEmail = getDetailsByBranchCode(emailDetails.getNewPolicyNo());
				if(null == masBranchEmail) {
					masBranchEmail = new MasBranchEmail();
					Long divCode = Long.parseLong(emailDetails.getNewPolicyNo());
					masBranchEmail.setDivCode(divCode);
					masBranchEmail.setBranchEmailId(emailDetails.getEmailId());
					masBranchEmail.setCreatedDate(new java.sql.Timestamp(System
							.currentTimeMillis()));
					masBranchEmail.setActiveStatus(SHAConstants.YES_FLAG);
					entityManager.persist(masBranchEmail);
					showInformation("New Record Added Successfully");
					emailDetails.setCheck(SHAConstants.YES_FLAG);
				}else{
					showError("Branch already exists");
					emailDetails.setCheck(SHAConstants.N_FLAG);
				}
			}
		}
	}
	
	public MasGMCServiceProvider getDetailsByPolicyNo(
			String policyNo) {

		Query query = entityManager
				.createNamedQuery("MasGMCServiceProvider.getByPolicyNo");
		query.setParameter("policyNo", policyNo);
		List<MasGMCServiceProvider> gmcDetails = query.getResultList();
		if (gmcDetails != null && !gmcDetails.isEmpty()) {
			return gmcDetails.get(0);
		}

		return null;

	}
	
	
	public MasBranchEmail getDetailsByBranchCode(
			String divnCode) {
		Long divCode = Long.parseLong(divnCode); 
		Query query = entityManager
				.createNamedQuery("MasBranchEmail.getRoleByUser");
		query.setParameter("divCode", divCode);
		List<MasBranchEmail> gmcDetails = query.getResultList();
		if (gmcDetails != null && !gmcDetails.isEmpty()) {
			return gmcDetails.get(0);
		}

		return null;

	}
	
	public void showInformation(String eMsg) {
		MessageBox.create()
		.withCaptionCust("Success").withHtmlMessage(eMsg.toString())
	    .withOkButton(ButtonOption.caption("OK")).open();
	}
	public void showError(String eMsg) {
		MessageBox.createError()
		.withCaptionCust("Error").withHtmlMessage(eMsg.toString())
	    .withOkButton(ButtonOption.caption("OK")).open();
	}
}
