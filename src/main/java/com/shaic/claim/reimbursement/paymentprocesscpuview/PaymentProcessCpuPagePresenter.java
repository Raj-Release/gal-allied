package com.shaic.claim.reimbursement.paymentprocesscpuview;

import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuService;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
@ViewInterface(PaymentProcessCpuPageView.class)

public class PaymentProcessCpuPagePresenter extends AbstractMVPPresenter<PaymentProcessCpuPageView>{
	
	
	public static final String SUBMIT_BUTTON_CLICK = "PaymentProcessCpuSubmit";
	
	public static final String DISCHARGEVOUCHER_BUTTON_CLICK = "Dischargevoucher Button Click";
	
	public static final String PAYMENT_AND_DISCHARGE_BUTTON_CLICK = "Payment and Discharge Button Click";
	
	public static final String HOSP_PAYMENT_LETTER_CLICK = "Hospital Paymentletter Click";
	
	@EJB
	private PaymentProcessCpuService paymentProcessService;    
	
	
	@SuppressWarnings({ "deprecation" })
	public void submitPayment(@Observes @CDIEvent(SUBMIT_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PaymentProcessCpuPageDTO paymentDTO = (PaymentProcessCpuPageDTO) parameters.getPrimaryParameter();
		
     	paymentProcessService.getPaymentClaimsDetails(paymentDTO);
    }
	
	@SuppressWarnings({ "deprecation" })
	public void generateDishcargeVoucher(@Observes @CDIEvent(DISCHARGEVOUCHER_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PaymentProcessCpuPageDTO paymentDTO = (PaymentProcessCpuPageDTO) parameters.getPrimaryParameter();
		
     	String templateName = paymentProcessService.getPaymentTemplateNameWithVersion (SHAConstants.DISCHARGE_VOUCHER, paymentDTO.getModifiedDate());
     	view.generateDischargeVoucherLetter(templateName,paymentDTO);
    }
	
	@SuppressWarnings({ "deprecation" })
	public void generatePaymentAndDishcarge(@Observes @CDIEvent(PAYMENT_AND_DISCHARGE_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PaymentProcessCpuPageDTO paymentDTO = (PaymentProcessCpuPageDTO) parameters.getPrimaryParameter();
		
		Intimation intimationObj = paymentProcessService.getIntimationObject(paymentDTO.getIntimationNo());
		
		Insured  insuredObj = intimationObj.getInsured();
		
		MastersValue masterValueInsured =  insuredObj.getInsuredGender();
		
		if(masterValueInsured.getKey() != null && masterValueInsured.getKey().equals(ReferenceTable.MALE_GENDER))
		{
			paymentDTO.setInsuredGender("Mr.");
		}else if(masterValueInsured.getKey() != null && masterValueInsured.getKey().equals(ReferenceTable.FEMALE_GENDER))
		{
			paymentDTO.setInsuredGender("Mrs.");
		}
		
		paymentDTO.setLegalHeir(paymentProcessService.getlegalHeirListByTransactionKey(paymentDTO.getReimbursementObj().getKey()));
		String templateName = "";
		if(SHAConstants.PROCESS_CLAIM_TYPE.equals(intimationObj.getProcessClaimType())){
			MastersValue masterValue = paymentDTO.getReimbursementObj().getBenefitsId();
			Policy policy = paymentDTO.getClaimDto().getNewIntimationDto().getPolicy();
			if(policy != null && policy.getHomeOfficeCode() != null){
				OrganaizationUnit branchOffice = paymentProcessService.getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
				if(branchOffice != null){
					paymentDTO.setBranchOffice(branchOffice);
					if(branchOffice.getState() != null && branchOffice.getState().getValue() != null){
						paymentDTO.setBranchOfficeState(branchOffice.getState().getValue());
					}
					OrganaizationUnit parentOffice = paymentProcessService.getInsuredOfficeNameByDivisionCode(branchOffice.getParentOrgUnitKey().toString());
					if( parentOffice != null ){
						paymentDTO.setParentOffice(parentOffice);
						
						if(parentOffice.getState() != null && parentOffice.getState().getValue() != null){
							paymentDTO.setParentOfficeState(parentOffice.getState().getValue());
						}
					}
				}
			}
			paymentDTO.setTodayDate(new Date());
			if(paymentDTO.getIntrestAmount() == null || paymentDTO.getIntrestAmount().equals(SHAConstants.DOUBLE_DEFAULT_VALUE)){

				if( masterValue.getKey().equals(ReferenceTable.HOSP_BENEFIT_MASTER_VALUE)){
					MastersValue masterValueDocAcknowLedgement = paymentDTO.getReimbursementObj().getDocAcknowLedgement().getDocumentReceivedFromId();
					if(masterValueDocAcknowLedgement.getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
						templateName = paymentProcessService.getPaymentTemplateNameWithVersion(SHAConstants.PA_PAYMENT_AND_DISCHARGE_VOUCHER_WITH_OUT_INT_INSURED, paymentDTO.getModifiedDate());
					}else{
						Double tdsAmnt = paymentDTO.getTdsAmount();
						if(tdsAmnt != null && !tdsAmnt.equals(SHAConstants.DOUBLE_DEFAULT_VALUE)){
							paymentDTO.setTdsAmountInWords(SHAUtils.getParsedAmount(tdsAmnt));							
						}
						templateName = paymentProcessService.getPaymentTemplateNameWithVersion(SHAConstants.PA_PAYMENT_AND_DISCHARGE_VOUCHER_WITH_OUT_INT_HOSPITAL, paymentDTO.getModifiedDate());
					}
				}
				else if(masterValue.getValue().equals(SHAConstants.PERMANENT_PARTIAL_DISABILITY) || masterValue.getValue().equals(SHAConstants.PERMANENT_TOTAL_DISABILITY) 
						||masterValue.getValue().equals(SHAConstants.TEMPORARY_TOTAL_DISABILITY)){
					templateName = paymentProcessService.getPaymentTemplateNameWithVersion(SHAConstants.PA_PAYMENT_AND_DISCHARGE_VOUCHER_WITH_OUT_INT_DIS, paymentDTO.getModifiedDate());
				}
				else if(masterValue.getValue().equals(SHAConstants.DEATH)){
					templateName = paymentProcessService.getPaymentTemplateNameWithVersion(SHAConstants.PA_PAYMENT_AND_DISCHARGE_VOUCHER_WITH_OUT_INT_DEATH, paymentDTO.getModifiedDate());
				}
				
			}else{

				if( masterValue.getKey().equals(ReferenceTable.HOSP_BENEFIT_MASTER_VALUE) ){
					//WITH INTEREST 
					templateName = paymentProcessService.getPaymentTemplateNameWithVersion(SHAConstants.PA_PAYMENT_AND_DISCHARGE_VOUCHER_WITH_INT_HOS, paymentDTO.getModifiedDate());
				}
				else if(masterValue.getValue().equals(SHAConstants.PERMANENT_PARTIAL_DISABILITY) || masterValue.getValue().equals(SHAConstants.PERMANENT_TOTAL_DISABILITY) 
						||masterValue.getValue().equals(SHAConstants.TEMPORARY_TOTAL_DISABILITY)){
					templateName = paymentProcessService.getPaymentTemplateNameWithVersion(SHAConstants.PA_PAYMENT_AND_DISCHARGE_VOUCHER_WITH_INT_DIS, paymentDTO.getModifiedDate());
				}
				else if(masterValue.getValue().equals(SHAConstants.DEATH)){
					templateName = paymentProcessService.getPaymentTemplateNameWithVersion(SHAConstants.PA_PAYMENT_AND_DISCHARGE_VOUCHER_WITH_INT_DEATH, paymentDTO.getModifiedDate());
				}
			}

		}else{
			templateName = paymentProcessService.getPaymentTemplateNameWithVersion(SHAConstants.PAYMENT_AND_DISCHARGE_VOUCHER, paymentDTO.getModifiedDate());
		}
		view.generatePaymentAndDischargeLetter(templateName,paymentDTO);
    }
	
	@SuppressWarnings({ "deprecation" })
	public void generateHospPaymentLetter(@Observes @CDIEvent(HOSP_PAYMENT_LETTER_CLICK) final ParameterDTO parameters) {
		
		PaymentProcessCpuPageDTO paymentDTO = (PaymentProcessCpuPageDTO) parameters.getPrimaryParameter();
		
		String templateName = paymentProcessService.getPaymentTemplateNameWithVersion(SHAConstants.HOSPITAL_PAYMENT_LETTER, paymentDTO.getModifiedDate());
		view.generateHospPaymentLetter(templateName,paymentDTO);
    }
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	

}