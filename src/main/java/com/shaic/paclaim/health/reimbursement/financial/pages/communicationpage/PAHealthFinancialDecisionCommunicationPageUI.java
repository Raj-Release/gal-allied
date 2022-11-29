package com.shaic.paclaim.health.reimbursement.financial.pages.communicationpage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.OtherBenefitsTableDto;
import com.shaic.claim.reimbursement.financialapproval.pages.communicationPage.NonPayableReasonDto;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.dto.PreHospitalizationDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;

public class PAHealthFinancialDecisionCommunicationPageUI extends ViewComponent {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PreauthDTO bean;
	
	private GWizard wizard;
	
	private WeakHashMap fileMap;
	
	@EJB
	private MasterService masterService;
	@Override
	public String getCaption() {
		return "Confirmation";
	}
	
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

	}
	
	public void init(PreauthDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		this.fileMap = new WeakHashMap();
	}
	
	
	public Component getContent() {
		
		if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null) {
			this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(SHAUtils.getParsedAmount(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()));
		}
		
//		if(bean.getBillEntryDetailsDTO() != null && !bean.getBillEntryDetailsDTO().isEmpty()) {
//			List<BillEntryDetailsDTO> billEntryDtoList = bean.getBillEntryDetailsDTO();
////			for(BillEntryDetailsDTO billDto : billEntryDtoList ) {
//			for(int index = 0; index < billEntryDtoList.size(); index++){
//				BillEntryDetailsDTO billDto = billEntryDtoList.get(index);
//				if( StringUtils.containsIgnoreCase(billDto.getItemName(),"Room Rent and Nursing")){
//					billEntryDtoList.remove(billDto);
//					continue;
//				}
//				if( StringUtils.containsIgnoreCase(billDto.getItemName(),"a)Room")){
//					billEntryDtoList.remove(billDto);
//					continue;
//				}
//				if( StringUtils.containsIgnoreCase(billDto.getItemName(),"b)Nursing")){
//					billEntryDtoList.remove(billDto);
//					continue;
//				}
//				if( StringUtils.containsIgnoreCase(billDto.getItemName(),"Sub Total")){
//					billEntryDtoList.remove(billDto);
//					continue;
//				}
//				if( StringUtils.containsIgnoreCase(billDto.getItemName(),"Total Room Rent")){
//					billEntryDtoList.get(billEntryDtoList.indexOf(billDto)).setItemName("Room Rent and Nursing Charges");					
//					continue;
//				}
//				if( StringUtils.containsIgnoreCase(billDto.getItemName(),"Room Rent and Nursing Charges")){
//					billEntryDtoList.remove(billDto);
//					continue;
//				}
//				if( StringUtils.containsIgnoreCase(billDto.getItemName(),"ICU Charges")){
//					billEntryDtoList.remove(billDto);
//					continue;
//				}
//				if( StringUtils.containsIgnoreCase(billDto.getItemName(),"a)ICU Room")){
//					billEntryDtoList.remove(billDto);
//					continue;
//				}
//				if( StringUtils.containsIgnoreCase(billDto.getItemName(),"b)ICU Nursing")){
//					billEntryDtoList.remove(billDto);
//					continue;
//				}
//				if( StringUtils.containsIgnoreCase(billDto.getItemName(),"Total ICU")){
//					billEntryDtoList.get(billEntryDtoList.indexOf(billDto)).setItemName("ICU Charges");
//					continue;
//				}
//			}
//			bean.setBillEntryDetailsDTO(billEntryDtoList);
//		}
			
		
		
		
		DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		
		reportDto.setBillAssessmentVersion(SHAUtils.getBillAssessmentVersion(masterService.getEntityManager(), this.bean.getRodNumber(), SHAConstants.BILL_SUMMARY_OTHER_PRODUCTS,
				SHAConstants.BILLASSESSMENTSHEETSCRC));
		
		List<PreauthDTO> preauthDTOList = new ArrayList<PreauthDTO>();
		preauthDTOList.add(this.bean);
		if(!ReferenceTable.SENIOR_CITIZEN_RED_CARPET.equals(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && ! ReferenceTable.SENIOR_CITIZEN_RED_CARPET_PRODUCT.equals(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			calculateTotalForReport();	
		}
		
		Integer balancePayableToInsured = 0;
		
		if(this.bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() != null && this.bean.getHospitalizationCalculationDTO().getAmountAlreadyPaid() != null){
			balancePayableToInsured = (bean.getHospitalizaionFlag() && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && bean.getIsReconsiderationRequest()) ? (bean.getHospitalizationCalculationDTO().getPayableToHospitalAftTDSAmt() - this.bean.getHospitalizationCalculationDTO().getAmountAlreadyPaid()) : this.bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() - this.bean.getHospitalizationCalculationDTO().getAmountAlreadyPaid();
			this.bean.getHospitalizationCalculationDTO().setBalanceToBePaid(balancePayableToInsured);
		}
		if(this.bean.getIsReconsiderationRequest() != null && ! this.bean.getIsReconsiderationRequest() && this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
			this.bean.getHospitalizationCalculationDTO().setBalanceToBePaid(0);
		}
		
		if(this.bean.getHospitalDiscount() != null && this.bean.getToatlNonPayableAmt() != null){
			
//			Double hospitalDiscount = this.bean.getHospitalDiscount();
//			if(hospitalDiscount < 0){
//				hospitalDiscount = hospitalDiscount * -1;
//			}
//			
//			this.bean.setToatlNonPayableAmt(this.bean.getToatlNonPayableAmt() - hospitalDiscount);
			
			
			
//			if(this.bean.getReasonableDeductionTotalAmt() != null && this.bean.getReasonableDeductionTotalAmt() >0){
//				
//				Double hosptialDiscount = this.bean.getHospitalDiscount();
//				
//				if(this.bean.getHospitalDiscount() < 0){
//					hosptialDiscount = Math.abs(hosptialDiscount);
//				}
//				
//			 this.bean.setReasonableDeductionTotalAmt(this.bean.getReasonableDeductionTotalAmt() - hosptialDiscount);
//			}
//			
//		}
//		if(this.bean.getDeductions() != null && this.bean.getToatlNonPayableAmt() != null){
//			
//			if(this.bean.getReasonableDeductionTotalAmt() != null && this.bean.getReasonableDeductionTotalAmt() >0){
//				
//				Double deductions = this.bean.getDeductions();
//				
//				if(this.bean.getDeductions() < 0){
//					deductions = Math.abs(deductions);
//				}
//			
//				this.bean.setReasonableDeductionTotalAmt(this.bean.getReasonableDeductionTotalAmt() - deductions);
//			}
//			Double deductions = this.bean.getDeductions();
//			if(deductions < 0){
//				deductions = deductions * -1;
//			}
//			this.bean.setToatlNonPayableAmt(this.bean.getToatlNonPayableAmt() - deductions);
		}
		
		if(! this.bean.getPreHospitalizaionFlag()){
			this.bean.setPrehospitalizationDTO(null);
		}
		if(!this.bean.getPostHospitalizaionFlag()){
			this.bean.setPostHospitalizationDTO(null);
		}
		
		/**
		 * *************************************  The below Code is part of CR R1030 *************************************************************
		 * Non payable Remarks length exceeds 75 Characters then the remarks will be shown in new refer note Table
		 */
		
		List<NonPayableReasonDto> nonPayableReasonListDto = new ArrayList<NonPayableReasonDto>();
		NonPayableReasonDto nonPayableReasonDto = null;
//		int entityCode = 9312;  
		int entityCode = 1;
		
		if(bean.getNetworkHospitalDiscount() != null){
			this.bean.getHospitalizationCalculationDTO().setLessHospitalNetworkDis(Integer.valueOf(bean.getNetworkHospitalDiscount().intValue()));
		}
		if(bean.getHospitalizationCalculationDTO().getHospitalDiscount() != null && bean.getHospitalizationCalculationDTO().getHospitalDiscount() > 0){
			this.bean.getHospitalizationCalculationDTO().setLessHospitalNetworkDis(bean.getHospitalizationCalculationDTO().getHospitalDiscount());
		}
		
		if(bean.getNetworkHospitalDiscount() != null){
			Integer networkHospitalDiscountNegative = 0;
			 networkHospitalDiscountNegative -= Integer.valueOf(bean.getNetworkHospitalDiscount().intValue());
			this.bean.setNetworkHospitalDiscountNegative(networkHospitalDiscountNegative);
		}

		List<BillEntryDetailsDTO> hospitalisationListDto = this.bean.getBillEntryDetailsDTO(); 
		if(hospitalisationListDto != null && !hospitalisationListDto.isEmpty()){
			
			for (BillEntryDetailsDTO hospitalisationDto : hospitalisationListDto) {
				if(hospitalisationDto.getDeductibleOrNonPayableReason() != null && !hospitalisationDto.getDeductibleOrNonPayableReason().isEmpty() && hospitalisationDto.getDeductibleOrNonPayableReason().length()>75){
					nonPayableReasonDto = new NonPayableReasonDto();
//						nonPayableReasonDto.setBillNo(hospitalisationDto.getBillNo());
//						nonPayableReasonDto.setItemName(hospitalisationDto.getItemName());
					nonPayableReasonDto.setDeductibleOrNonPayableReason(hospitalisationDto.getDeductibleOrNonPayableReason());
//					String deductibleOrNonPayableReason = hospitalisationDto.getDeductibleOrNonPayableReason().substring(0, 75) + /*" <style isBold='true' pdfFontName='CombiNumeralsLtd'>&#"+entityCode+";</style>";*/
//																																  "  *<sup>"+entityCode+"</sup>";
					String deductibleOrNonPayableReason = "  Refer Note #"+entityCode;
					System.out.println("Deductible Reason    ***************** ==========  : " + deductibleOrNonPayableReason);
//					nonPayableReasonDto.setSno(" &#"+entityCode+";");
//					nonPayableReasonDto.setSno("*<sup>"+entityCode+"</sup>");
					nonPayableReasonDto.setSno("#"+entityCode);
					hospitalisationDto.setNonPayableRmrksForAssessmentSheet(deductibleOrNonPayableReason);
					entityCode++;
					nonPayableReasonListDto.add(nonPayableReasonDto);
				}
				else{
					hospitalisationDto.setNonPayableRmrksForAssessmentSheet(hospitalisationDto.getDeductibleOrNonPayableReason());
				}
			}
			
			if(this.bean.getHospitalDiscountRemarksForAssessmentSheet() != null && !this.bean.getHospitalDiscountRemarksForAssessmentSheet().isEmpty() && this.bean.getHospitalDiscountRemarksForAssessmentSheet().length()>75){
				nonPayableReasonDto = new NonPayableReasonDto();
				nonPayableReasonDto.setDeductibleOrNonPayableReason(this.bean.getHospitalDiscountRemarksForAssessmentSheet());
//				String deductibleOrNonPayableReason = this.bean.getHospitalDiscountRemarks().substring(0, 75) + /*" <style isBold='true' pdfFontName='CombiNumeralsLtd'>&#"+entityCode+";</style>";*/
//																															  "  *<sup>"+entityCode+"</sup>";	
				String deductibleOrNonPayableReason = "  Refer Note #"+entityCode;
				System.out.println("Deductible Reason    ***************** ==========  : " + deductibleOrNonPayableReason);
//						nonPayableReasonDto.setSno(" &#"+entityCode+";");
//				nonPayableReasonDto.setSno("*<sup>"+entityCode+"</sup>");
				nonPayableReasonDto.setSno("#"+entityCode);
				this.bean.setHospitalDiscountRemarks(deductibleOrNonPayableReason);
				entityCode++;
				nonPayableReasonListDto.add(nonPayableReasonDto);
			}
			else{
				this.bean.setHospitalDiscountRemarks(this.bean.getHospitalDiscountRemarksForAssessmentSheet());
			}
			
			
			if(this.bean.getDeductionRemarksForAssessmentSheet() != null && !this.bean.getDeductionRemarksForAssessmentSheet().isEmpty() && this.bean.getDeductionRemarksForAssessmentSheet().length()>75){
				nonPayableReasonDto = new NonPayableReasonDto();
				nonPayableReasonDto.setDeductibleOrNonPayableReason(this.bean.getDeductionRemarksForAssessmentSheet());
//				String deductibleRemarks = this.bean.getDeductionRemarks().substring(0, 75) + /*" <style isBold='true' pdfFontName='CombiNumeralsLtd'>&#"+entityCode+";</style>";*/
//																															  "  *<sup>"+entityCode+"</sup>";
				String deductibleRemarks = "  Refer Note #"+entityCode;
				System.out.println("Deductible Reason    ***************** ==========  : " + deductibleRemarks);
//						nonPayableReasonDto.setSno(" &#"+entityCode+";");
//				nonPayableReasonDto.setSno("*<sup>"+entityCode+"</sup>");
				nonPayableReasonDto.setSno("#"+entityCode);
				this.bean.setDeductionRemarks(deductibleRemarks);
				entityCode++;
				nonPayableReasonListDto.add(nonPayableReasonDto);
			}
			else{
				this.bean.setDeductionRemarks(this.bean.getDeductionRemarksForAssessmentSheet());
			}
		}
		
		List<OtherBenefitsTableDto> otherBenefitsList = this.bean.getPreauthDataExtractionDetails().getOtherBenefitsList();
		if(otherBenefitsList != null && !otherBenefitsList.isEmpty()){
			for (OtherBenefitsTableDto otherBenefitsTableDto : otherBenefitsList) {
				if(((otherBenefitsTableDto.getAmtClaimed() != null && otherBenefitsTableDto.getAmtClaimed().intValue() != 0) || (otherBenefitsTableDto.getBalancePayable() != null && otherBenefitsTableDto.getBalancePayable().intValue() != 0)) && 
						otherBenefitsTableDto.getRemarks() != null && !otherBenefitsTableDto.getRemarks().isEmpty() && otherBenefitsTableDto.getRemarks().length()>75){
					nonPayableReasonDto = new NonPayableReasonDto();
//						nonPayableReasonDto.setBillNo(hospitalisationDto.getBillNo());
//						nonPayableReasonDto.setItemName(hospitalisationDto.getItemName());
					nonPayableReasonDto.setDeductibleOrNonPayableReason(otherBenefitsTableDto.getRemarks());
//					String deductibleOrNonPayableReason = otherBenefitsTableDto.getRemarks().substring(0, 75) + /*" <style isBold='true' pdfFontName='CombiNumeralsLtd'>&#"+entityCode+";</style>";*/
//																																  "  *<sup>"+entityCode+"</sup>";
					String deductibleOrNonPayableReason = "  Refer Note #"+entityCode;
					System.out.println("Deductible Reason    ***************** ==========  : " + deductibleOrNonPayableReason);
//					nonPayableReasonDto.setSno(" &#"+entityCode+";");
//					nonPayableReasonDto.setSno("*<sup>"+entityCode+"</sup>");
					nonPayableReasonDto.setSno("#"+entityCode);
					otherBenefitsTableDto.setNonPayableRmrksForAssessmentSheet(deductibleOrNonPayableReason);
					entityCode++;
					nonPayableReasonListDto.add(nonPayableReasonDto);
				}
				else{
					otherBenefitsTableDto.setNonPayableRmrksForAssessmentSheet(otherBenefitsTableDto.getRemarks());
				}
			}
		}
		

	
	List<PreHospitalizationDTO> prehospitalizationListDTO = this.bean.getPrehospitalizationDTO();
		if(prehospitalizationListDTO != null && !prehospitalizationListDTO.isEmpty()){
			for (PreHospitalizationDTO preHospitalizationDTO : prehospitalizationListDTO) {
				if(preHospitalizationDTO.getReason() != null && !preHospitalizationDTO.getReason().isEmpty() && preHospitalizationDTO.getReason().length()>75){
					nonPayableReasonDto = new NonPayableReasonDto();
//					nonPayableReasonDto.setBillNo(hospitalisationDto.getBillNo());
//					nonPayableReasonDto.setItemName(hospitalisationDto.getItemName());
					nonPayableReasonDto.setDeductibleOrNonPayableReason(preHospitalizationDTO.getReason());
//					String deductibleOrNonPayableReason = preHospitalizationDTO.getReason().substring(0, 75) + /*" <style isBold='true' pdfFontName='CombiNumeralsLtd'>&#"+entityCode+";</style>";*/
//																																  "  *<sup>"+entityCode+"</sup>";	
					String deductibleOrNonPayableReason = "  Refer Note #"+entityCode;
					System.out.println("Deductible Reason    ***************** ==========  : " + deductibleOrNonPayableReason);
//					nonPayableReasonDto.setSno(" &#"+entityCode+";");
//					nonPayableReasonDto.setSno("*<sup>"+entityCode+"</sup>");
					nonPayableReasonDto.setSno("#"+entityCode);
					preHospitalizationDTO.setNonPayableRmrksForAssessmentSheet(deductibleOrNonPayableReason);
					entityCode++;
					nonPayableReasonListDto.add(nonPayableReasonDto);
				}
				else{
					preHospitalizationDTO.setNonPayableRmrksForAssessmentSheet(preHospitalizationDTO.getReason());
				}
			}
		}
		
		List<PreHospitalizationDTO> postHospitalizationListDTO = this.bean.getPostHospitalizationDTO();
		if(postHospitalizationListDTO != null && !postHospitalizationListDTO.isEmpty()){
			for (PreHospitalizationDTO postHospitalizationDTO : postHospitalizationListDTO) {
				if(postHospitalizationDTO.getReason() != null && !postHospitalizationDTO.getReason().isEmpty() && postHospitalizationDTO.getReason().length()>75){
					nonPayableReasonDto = new NonPayableReasonDto();
//					nonPayableReasonDto.setBillNo(postHospitalizationDTO.getBillNo());
//					nonPayableReasonDto.setItemName(postHospitalizationDTO.getItemName());
					nonPayableReasonDto.setDeductibleOrNonPayableReason(postHospitalizationDTO.getReason());
//					String deductibleOrNonPayableReason = postHospitalizationDTO.getReason().substring(0, 75) + /*" <style isBold='true' pdfFontName='CombiNumeralsLtd'>&#"+entityCode+";</style>";*/
//																																  "  *<sup>"+entityCode+"</sup>";
					String deductibleOrNonPayableReason = "  Refer Note #"+entityCode;
					System.out.println("Deductible Reason    ***************** ==========  : " + deductibleOrNonPayableReason);
//					nonPayableReasonDto.setSno(" &#"+entityCode+";");
//					nonPayableReasonDto.setSno("*<sup>"+entityCode+"</sup>");
					nonPayableReasonDto.setSno("#"+entityCode);
					postHospitalizationDTO.setNonPayableRmrksForAssessmentSheet(deductibleOrNonPayableReason);
					entityCode++;
					nonPayableReasonListDto.add(nonPayableReasonDto);
				}
				else{
					postHospitalizationDTO.setNonPayableRmrksForAssessmentSheet(postHospitalizationDTO.getReason());
				}
			}
		}

		this.bean.setNonPayableReasonListDto(nonPayableReasonListDto);
		
		//***************************************  Code Ends for CR R1030 ***********************************************************************

		reportDto.setClaimId(this.bean.getClaimDTO().getClaimId());
		reportDto.setBeanList(preauthDTOList);		
			
		String templateName = SHAConstants.BILL_SUMMARY_OTHER_PRODUCTS;
		
//		if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET) || this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_PRODUCT))
//		{
//			templateName = SHAConstants.BILLASSESSMENTSHEETSCRC;
//		}
		
		Panel firstVertical = getContentofTemplate(templateName,reportDto,docGen);
		this.bean.setFilePathAndTypeMap(fileMap);
//		firstVertical.setWidth("100%");
//		firstVertical.setHeight("100%");
		
//        VerticalLayout secondVertical= getContentofTemplate("DischargeVoucher",reportDto,docGen);
//        secondVertical.setWidth("700px");
//        secondVertical.setHeight("100%");
//		HorizontalLayout horizontalLayout = new HorizontalLayout(firstVertical/*,secondVertical*/);
//		horizontalLayout.setHeight("400px");
//		horizontalLayout.setWidth("100%");
//		
		
//		Panel panel = new Panel(firstVertical);
//		panel.setHeight("420px");
		firstVertical.setHeight("420px");

		return firstVertical;
	}
public void calculateTotalForReport(){ 	
		
		List<BillEntryDetailsDTO> itemIconPropertyId = this.bean.getBillEntryDetailsDTO();
		//Long netAmount =0l;
		Double claimedAmount = 0d;
		Double allowableAmount = 0d;
		Double nonPayablePdtBased = 0d;
		Double nonPayableAmount = 0d;
		Double proportionateDeduction = 0d;
		Double totalDisallowances = 0d;
		Double reasonableDeduction = 0d;
		Double netAmount = 0d;
		/*Long amount =0l;
		Long nonPayableAmount =0l;
		Long payableAmount =0l;*/
		
		if(itemIconPropertyId != null && !itemIconPropertyId.isEmpty()){
			for (BillEntryDetailsDTO billEntryDetailsDTO : itemIconPropertyId) {
			
			if(null != billEntryDetailsDTO.getItemValue())
			{
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") || billEntryDetailsDTO.getItemName().contains("Total ICU Charges"))))
						{
							claimedAmount += billEntryDetailsDTO.getItemValue();
							if(("Hospital Discount").equalsIgnoreCase(billEntryDetailsDTO.getItemName()))
							{
								claimedAmount -= billEntryDetailsDTO.getItemValue();
							}
						}
			}
			
			if(null != billEntryDetailsDTO.getAmountAllowableAmount())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") || billEntryDetailsDTO.getItemName().contains("Total ICU Charges"))))
						{
							allowableAmount += billEntryDetailsDTO.getAmountAllowableAmount();
						}
			}
			
			if(null != billEntryDetailsDTO.getNonPayableProductBased())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") 
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")  || billEntryDetailsDTO.getItemName().equalsIgnoreCase(SHAConstants.AMBULANCE_FEES)
						)))
						{
							nonPayablePdtBased += billEntryDetailsDTO.getNonPayableProductBased();
						}
			}
			
			if(null != billEntryDetailsDTO.getNonPayable())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
						)))
						{
							nonPayableAmount += billEntryDetailsDTO.getNonPayable();
						}
			}
			
			if(null != billEntryDetailsDTO.getProportionateDeduction())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
						)))
						{
							proportionateDeduction += billEntryDetailsDTO.getProportionateDeduction();
						}
			}
			
			if(null != billEntryDetailsDTO.getReasonableDeduction())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
						)))
						{
							reasonableDeduction += billEntryDetailsDTO.getReasonableDeduction();
						}
			}
			
			if(null != billEntryDetailsDTO.getTotalDisallowances())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				/*if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
						)))*/
				if(!( billEntryDetailsDTO.getItemName().contains("Total Room Rent")
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
						))
						{
							totalDisallowances += billEntryDetailsDTO.getTotalDisallowances();
						}
			}
			if(null != billEntryDetailsDTO.getNetPayableAmount())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total"))))// || billEntryDetailsDTO.getItemName().contains("Total Room Rent"))))
						{
							netAmount += billEntryDetailsDTO.getNetPayableAmount();
						}
			}
			
		  }
		}
//		this.bean.setTotalClaimedAmt(claimedAmount);
//		this.bean.setToatlNonPayableAmt(reasonableDeduction + nonPayableAmount + proportionateDeduction+ nonPayablePdtBased);
//		this.bean.setTotalApprovedAmt(netAmount);
		/*table.setColumnFooter("itemValue", String.valueOf(claimedAmount));
		table.setColumnFooter("amountAllowableAmount" , String.valueOf(allowableAmount));
		table.setColumnFooter("nonPayableProductBased" , String.valueOf(nonPayablePdtBased));
		table.setColumnFooter("nonPayable" , String.valueOf(nonPayableAmount));
		table.setColumnFooter("proportionateDeduction"  , String.valueOf(proportionateDeduction));
		table.setColumnFooter("reasonableDeduction" , String.valueOf(reasonableDeduction));
		table.setColumnFooter("totalDisallowances"  , String.valueOf(totalDisallowances));
		table.setColumnFooter("netPayableAmount"  , String.valueOf(netAmount));*/

		/*table.setColumnFooter("amount", String.valueOf(amount));
		table.setColumnFooter("deductingNonPayable", String.valueOf(nonPayableAmount));
		table.setColumnFooter("payableAmount", String.valueOf(payableAmount));*/
		//table.setColumnFooter("itemName", "Total");
		
}

	private Panel getContentofTemplate(String templateName,ReportDto reportDto,DocumentGenerator docGen) {
		
		
		String filePath = docGen.generatePdfDocument(templateName, reportDto);	
		final String finalFilePath = filePath;
		if((SHAConstants.BILL_SUMMARY_OTHER_PRODUCTS).equalsIgnoreCase(templateName))
		{
			
			fileMap.put("BillSummaryDocFilePath", finalFilePath);
			fileMap.put("BillSummaryDocType",SHAConstants.BILL_SUMMARY_OTHER_PRODUCTS);
		}
		else if((SHAConstants.BILLASSESSMENTSHEETSCRC).equalsIgnoreCase(templateName))
		{
			
			fileMap.put("BillSummaryDocFilePath", finalFilePath);
			fileMap.put("BillSummaryDocType",SHAConstants.BILLASSESSMENTSHEETSCRC);
		}
		else if((SHAConstants.DISCHARGE_VOUCHER).equalsIgnoreCase(templateName))
		{
			
			fileMap.put("DischargeVoucherFilePath", finalFilePath);
			fileMap.put("DischargeVoucherDocType",SHAConstants.DISCHARGE_VOUCHER);
		}
		fileMap.put("docSources",SHAConstants.FINANCIAL_APPROVER);
		fileMap.put("version", reportDto.getBillAssessmentVersion());
		Path p = Paths.get(finalFilePath);
		String fileName = p.getFileName().toString();
		StreamResource.StreamSource s = SHAUtils.getStreamResource(finalFilePath);

		/*StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = new File(finalFilePath);
					FileInputStream fis = new FileInputStream(f);
					return fis;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};*/

		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
//		e.setHeightUndefined();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		SHAUtils.closeStreamResource(s);
		Panel panel = new Panel();	
		panel.setHeight("100%");
		panel.setContent(e);
		
//		VerticalLayout templateLayout = new VerticalLayout(e);
//		templateLayout.setSizeFull();
//		templateLayout.setHeight("450px");
		return panel;
	}
	
	
	

}
