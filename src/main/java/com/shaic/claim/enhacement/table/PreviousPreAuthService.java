package com.shaic.claim.enhacement.table;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.ViewTmpPreauth;

@Stateless
public class PreviousPreAuthService extends AbstractDAO<Preauth> {

	@PersistenceContext
    protected EntityManager entityManager;

	public PreviousPreAuthService() {
		super();
	}

	@SuppressWarnings("unchecked")
	public List<PreviousPreAuthTableDTO> search(Long claimKey, Boolean isBasedOnAftPremiumAmt) {

	//public List<PreviousPreAuthTableDTO> search(Long claimKey) {

		Query query = entityManager.createNamedQuery("Preauth.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<Preauth> resultList = (List<Preauth>) query.getResultList();
		
		Double preauthInstallAmt =0d;
		Double EnchanInstallAmt =0d;
		boolean isEnchnClm = false;
		
		for (Preauth preauth : resultList) {
			entityManager.refresh(preauth);
			
			if(isBasedOnAftPremiumAmt && preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)){
				preauthInstallAmt = preauth.getPremiumAmt();
			}

			if(isBasedOnAftPremiumAmt && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)
					|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS))){
				isEnchnClm = true;
				EnchanInstallAmt = preauth.getPremiumAmt();
				
			}
		}

		List<PreviousPreAuthTableDTO> processEnhancementTableDTOList = PreviousPreAuthMapper.getInstance()
				.getProcessEnhacementTableDTO(resultList);
		int i = 0;
		if (!processEnhancementTableDTOList.isEmpty()) {
			for (PreviousPreAuthTableDTO processEnhacementTableDTO : processEnhancementTableDTOList) {
				if (processEnhacementTableDTO.getReferenceType() == null && 
						processEnhacementTableDTO.getStatusKey() != null && ! processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)
								&& ! processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_ESCALATION_STATUS)) {
					processEnhacementTableDTO.setReferenceType("Pre Auth");
					if(isBasedOnAftPremiumAmt && processEnhacementTableDTO.getApprovedAmtAftPremiumDeduction() != null) {
						Double preauthAprAmt = 0d;
						preauthAprAmt = Double.valueOf(processEnhacementTableDTO.getApprovedAmt());
						
						processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getApprovedAmtAftPremiumDeduction());
						
						if(isEnchnClm && (preauthInstallAmt != EnchanInstallAmt && EnchanInstallAmt < preauthInstallAmt)){
							System.out.println("Preauth isntalment Amount  :" + preauthInstallAmt + "Enhance isntalment Amount  : " + EnchanInstallAmt + "Preauth Approval Amt  :" + preauthAprAmt);
							Double approveAmt = preauthAprAmt  - EnchanInstallAmt ;
							processEnhacementTableDTO.setApprovedAmt(approveAmt.toString());
						}
						
						
					}
					if(processEnhacementTableDTO.getProcessFlag() != null && processEnhacementTableDTO.getProcessFlag().toLowerCase().equalsIgnoreCase("w")){
						processEnhacementTableDTO.setReferenceType("Withdraw");
						if( processEnhacementTableDTO.getApprovedAmt() != null) {
							if((SHAUtils.getDoubleFromString( processEnhacementTableDTO.getApprovedAmt()) < 0)) {
								processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getApprovedAmt());
							} else {
								processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getApprovedAmt());
							}
							
						}
						
						//IMSSUPPOR-23349
						if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)){
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
						}
						
						if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)){
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
						}
						
						if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)){
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
						}
						//IMSSUPPOR-34631
						if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)){
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
						}
					}
				}else if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
					processEnhacementTableDTO.setReferenceType("Downsize");
					
					processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount());
					if(processEnhacementTableDTO.getProcessFlag() != null && processEnhacementTableDTO.getProcessFlag().toLowerCase().equalsIgnoreCase("d")) {
						
						if((SHAUtils.getDoubleFromString(processEnhacementTableDTO.getDiffAmount()) < 0)) {
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount());
						} else {
							processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getDiffAmount());
						}
//						processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getDiffAmount());
					}
				}else if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_ESCALATION_STATUS)){
					processEnhacementTableDTO.setReferenceType("Escalation");
					if(resultList.get(i).getDiffAmount() != null && resultList.get(i).getDiffAmount() > 0d){
						if((SHAUtils.getDoubleFromString(processEnhacementTableDTO.getDiffAmount()) < 0)) {
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount());
						} else {
							processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getDiffAmount());
						}
//						processEnhacementTableDTO.setApprovedAmt("-" +processEnhacementTableDTO.getDiffAmount());
					}else{
						Double diffAmount = resultList.get(i).getDiffAmount();
						if(diffAmount != null && diffAmount != 0d){
							diffAmount = (-1d) * diffAmount;
						}
						processEnhacementTableDTO.setApprovedAmt(diffAmount != null ? diffAmount.toString() : "0");
					}
				}else if(processEnhacementTableDTO.getProcessFlag() != null && processEnhacementTableDTO.getProcessFlag().toLowerCase().equalsIgnoreCase("w")){
					processEnhacementTableDTO.setReferenceType("Withdraw");
					
					//IMSSUPPOR-23037
					if(processEnhacementTableDTO.getStatusKey() != null && (processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT) || processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS))){
						processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
					}else{
						if( processEnhacementTableDTO.getApprovedAmt() != null) {
							if((SHAUtils.getDoubleFromString( processEnhacementTableDTO.getApprovedAmt()) < 0)) {
								processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getApprovedAmt());
							} else {
								processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getApprovedAmt());
							}
//							processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getApprovedAmt());
						}
						
						//IMSSUPPOR-23349
						if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)){
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
						}
						if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)){
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
						}
						
						if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)){
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
						}
					}
					
				}
				else{
					if (processEnhacementTableDTO.getReferenceType() != null && processEnhacementTableDTO.getReferenceType().equals("F")) {
						processEnhacementTableDTO
								.setReferenceType("Final Enhancecement");
						
					}
					else{
						if (processEnhacementTableDTO.getReferenceType() != null && processEnhacementTableDTO.getReferenceType().equals("I")) {
							processEnhacementTableDTO
									.setReferenceType("Interim Enhacement");
						}
					}	
					
					processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount());
					if(processEnhacementTableDTO.getProcessFlag() != null && processEnhacementTableDTO.getProcessFlag().toLowerCase().equalsIgnoreCase("d")) {
						if((SHAUtils.getDoubleFromString(processEnhacementTableDTO.getDiffAmount()) < 0)) {
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount());
						} else {
							processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getDiffAmount());
						}
//						processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getDiffAmount());
					}else if(processEnhacementTableDTO.getProcessFlag() != null && processEnhacementTableDTO.getProcessFlag().toLowerCase().equalsIgnoreCase("w")){
						processEnhacementTableDTO.setReferenceType("Withdraw");
						
						//IMSSUPPOR-23037
						if(processEnhacementTableDTO.getStatusKey() != null && (processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT) || processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS))){
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
						}else{
							if( processEnhacementTableDTO.getApprovedAmt() != null) {
								if((SHAUtils.getDoubleFromString( processEnhacementTableDTO.getApprovedAmt()) < 0)) {
									processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getApprovedAmt());
								} else {
									processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getApprovedAmt());
								}
//								processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getApprovedAmt());
							}
							if(isBasedOnAftPremiumAmt && processEnhacementTableDTO.getApprovedAmtAftPremiumDeduction() != null) {
								if((SHAUtils.getDoubleFromString( processEnhacementTableDTO.getApprovedAmt()) < 0)) {
									processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getApprovedAmtAftPremiumDeduction());
								} else {
									processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getApprovedAmtAftPremiumDeduction());
								}
//								processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getApprovedAmt());
							}
							
							//IMSSUPPOR-23349
							if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)){
								processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
							}
							if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)){
								processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
							}
							if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)){
								processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
							}
						}
						
						
						
					}
				}
				
				if(processEnhacementTableDTO.getCreatedDate() != null){
					processEnhacementTableDTO.setCreatedDateStr(SHAUtils.formatDate(processEnhacementTableDTO.getCreatedDate()));
				}
				if(processEnhacementTableDTO.getModifiedDate() != null){
					processEnhacementTableDTO.setStrmodifiedDate(SHAUtils.formatDate(processEnhacementTableDTO.getModifiedDate()));
				}
				i++;
			}
		}
 
		return processEnhancementTableDTOList;
	}
	
	@SuppressWarnings("unchecked")
	public List<PreviousPreAuthTableDTO> searchPrevPreAuthReport(Long intimationKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByStatusAndIntimation");
		query.setParameter("intimationKey", intimationKey);
		List<Preauth> resultList = (List<Preauth>) query.getResultList();
		List<PreviousPreAuthTableDTO> processEnhancementTableDTOList = PreviousPreAuthMapper.getInstance()
				.getProcessEnhacementTableDTO(resultList);
		return processEnhancementTableDTOList;
		
	}
	
	
	@SuppressWarnings("unchecked")
	public List<PreviousPreAuthTableDTO> searchFromTmpPreauth(Long claimKey) {

		Query query = entityManager.createNamedQuery("ViewTmpPreauth.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<ViewTmpPreauth> resultList = (List<ViewTmpPreauth>) query.getResultList();
		
		for (ViewTmpPreauth preauth : resultList) {
			entityManager.refresh(preauth);
		}

		List<PreviousPreAuthTableDTO> processEnhancementTableDTOList = PreviousPreAuthMapper.getInstance()
				.getProcessEnhacementTableDTOFromTmpPreauth(resultList);
		int i = 0;
		if (!processEnhancementTableDTOList.isEmpty()) {
			for (PreviousPreAuthTableDTO processEnhacementTableDTO : processEnhancementTableDTOList) {
				if (processEnhacementTableDTO.getReferenceType() == null && 
						processEnhacementTableDTO.getStatusKey() != null && ! processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)
								&& ! processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_ESCALATION_STATUS)
								&& !processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.NEGOTIATION_AGREED)) {
					processEnhacementTableDTO.setReferenceType("Pre Auth");
					if(processEnhacementTableDTO.getProcessFlag() != null && processEnhacementTableDTO.getProcessFlag().toLowerCase().equalsIgnoreCase("w")){
						processEnhacementTableDTO.setReferenceType("Withdraw");
						if( processEnhacementTableDTO.getApprovedAmt() != null) {
							if((SHAUtils.getDoubleFromString( processEnhacementTableDTO.getApprovedAmt()) < 0)) {
								processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getApprovedAmt());
							} else {
								processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getApprovedAmt());
							}
							
						}
						
						//IMSSUPPOR-23349
						if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)){
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
						}
						if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)){
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
						}
						
						//added for withdraw CR-R1180
						if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)){
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
						}
						//IMSSUPPOR-34631
						if(processEnhacementTableDTO.getStatusKey() != null && (processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS))){
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
						}
					}
				}else if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
					processEnhacementTableDTO.setReferenceType("Downsize");
					
					processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount());
					if(processEnhacementTableDTO.getProcessFlag() != null && processEnhacementTableDTO.getProcessFlag().toLowerCase().equalsIgnoreCase("d")) {
						
						if((SHAUtils.getDoubleFromString(processEnhacementTableDTO.getDiffAmount()) < 0)) {
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount());
						} else {
							processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getDiffAmount());
						}
//						processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getDiffAmount());
					}
				}else if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.DOWNSIZE_ESCALATION_STATUS)){
					processEnhacementTableDTO.setReferenceType("Escalation");
					if(resultList.get(i).getDiffAmount() != null && resultList.get(i).getDiffAmount() > 0d){
						if((SHAUtils.getDoubleFromString(processEnhacementTableDTO.getDiffAmount()) < 0)) {
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount());
						} else {
							processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getDiffAmount());
						}
//						processEnhacementTableDTO.setApprovedAmt("-" +processEnhacementTableDTO.getDiffAmount());
					}else{
						Double diffAmount = resultList.get(i).getDiffAmount();
						if(diffAmount != null && diffAmount != 0d){
							diffAmount = (-1d) * diffAmount;
						}
						processEnhacementTableDTO.setApprovedAmt(diffAmount != null ? diffAmount.toString() : "0");
					}
				}else if(processEnhacementTableDTO.getProcessFlag() != null && processEnhacementTableDTO.getProcessFlag().toLowerCase().equalsIgnoreCase("w")){
					processEnhacementTableDTO.setReferenceType("Withdraw");
					
					if(processEnhacementTableDTO.getStatusKey() != null && (processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT) || processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS))){
						processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
					}
					else{
						if( processEnhacementTableDTO.getApprovedAmt() != null) {
							if((SHAUtils.getDoubleFromString( processEnhacementTableDTO.getApprovedAmt()) < 0)) {
								processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getApprovedAmt());
							} else {
								processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getApprovedAmt());
							}
//							processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getApprovedAmt());
						}
						
						//IMSSUPPOR-23349
						if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)){
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
						}
						if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)){
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
						}
						if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)){
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
						}
					}
					
				}
				else{
					if (processEnhacementTableDTO.getReferenceType() != null && processEnhacementTableDTO.getReferenceType().equals("F")) {
						processEnhacementTableDTO
								.setReferenceType("Final Enhancecement");
						
					}
					else{
						if (processEnhacementTableDTO.getReferenceType() != null && processEnhacementTableDTO.getReferenceType().equals("I")) {
							processEnhacementTableDTO
									.setReferenceType("Interim Enhacement");
						}
					}	
					
					processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount());
					if(processEnhacementTableDTO.getProcessFlag() != null && processEnhacementTableDTO.getProcessFlag().toLowerCase().equalsIgnoreCase("d")) {
						if((SHAUtils.getDoubleFromString(processEnhacementTableDTO.getDiffAmount()) < 0)) {
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount());
						} else {
							processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getDiffAmount());
						}
//						processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getDiffAmount());
					}else if(processEnhacementTableDTO.getProcessFlag() != null && processEnhacementTableDTO.getProcessFlag().toLowerCase().equalsIgnoreCase("w")){
						processEnhacementTableDTO.setReferenceType("Withdraw");
						
						if(processEnhacementTableDTO.getStatusKey() != null && (processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT) || processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS))){
							processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
						}else{
							if( processEnhacementTableDTO.getApprovedAmt() != null) {
								if((SHAUtils.getDoubleFromString( processEnhacementTableDTO.getApprovedAmt()) < 0)) {
									processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getApprovedAmt());
								} else {
									processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getApprovedAmt());
								}
//								processEnhacementTableDTO.setApprovedAmt("-" + processEnhacementTableDTO.getApprovedAmt());
							}
							
							//IMSSUPPOR-23349
							if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)){
								processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
							}
							if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)){
								processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
							}	
							if(processEnhacementTableDTO.getStatusKey() != null && processEnhacementTableDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)){
								processEnhacementTableDTO.setApprovedAmt(processEnhacementTableDTO.getDiffAmount() != null ? processEnhacementTableDTO.getDiffAmount() : "0");
							}
						}
						
					}
				}
				
				if(processEnhacementTableDTO.getCreatedDate() != null){
					processEnhacementTableDTO.setPreauthCreatedDate(SHAUtils.formatDateTime(resultList.get(i).getCreatedDate()));
				}
				
				if(null != resultList.get(i).getModifiedDate()){
					processEnhacementTableDTO.setPreauthModifiedDate(SHAUtils.formatDateTime(resultList.get(i).getModifiedDate()));
				}
				i++;
			}
		}
 
		return processEnhancementTableDTOList;
	}


	@Override
	public Class<Preauth> getDTOClass() {
		return Preauth.class;
	}

}
