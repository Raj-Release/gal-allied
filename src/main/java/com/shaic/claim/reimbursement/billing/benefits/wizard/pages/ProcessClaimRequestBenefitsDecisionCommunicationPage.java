package com.shaic.claim.reimbursement.billing.benefits.wizard.pages;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billreview.FinancialReviewPageViewImpl;
import com.shaic.claim.reimbursement.financialapproval.pages.communicationPage.NonPayableReasonDto;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.pages.HopsitalCashBenefitDTO;
import com.shaic.claim.viewEarlierRodDetails.dto.PreHospitalizationDTO;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;

public class ProcessClaimRequestBenefitsDecisionCommunicationPage extends ViewComponent {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private InsuredService insuredService;
	
	private GWizard wizard;
	
	private WeakHashMap fileMap;
	
	@Override
	public String getCaption() {
		return "Confirmation";
	}
	
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

	}
	
	@SuppressWarnings("rawtypes")
	public void init(ReceiptOfDocumentsDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		this.fileMap = new WeakHashMap();
	}
	
	
	public Component getContent() {
		
		
		DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		
		reportDto.setBillAssessmentVersion(SHAUtils.getBillAssessmentVersion(insuredService.getEntityManager(), this.bean.getPreauthDTO().getRodNumber(), SHAConstants.BILL_ASSESSMENT_HOSPITAL_CASH,
				SHAConstants.BILLASSESSMENTSHEETSCRC));
		
		List<ReceiptOfDocumentsDTO> preauthDTOList = new ArrayList<ReceiptOfDocumentsDTO>();
		
		preauthDTOList.add(this.bean);
		
		reportDto.setClaimId(this.bean.getClaimDTO().getClaimId());
		reportDto.setBeanList(preauthDTOList);		
			
		String templateName = SHAConstants.BILL_ASSESSMENT_HOSPITAL_CASH;
		
		/**
		 * *************************************  The below Code is part of CR R1030 *************************************************************
		 * Non payable Remarks length exceeds 75 Characters then the remarks will be shown in new refer note Table
		 */
		
		bean.setNewIntimationDTO(bean.getClaimDTO().getNewIntimationDto());
		List<NonPayableReasonDto> nonPayableReasonListDto = new ArrayList<NonPayableReasonDto>();
		NonPayableReasonDto nonPayableReasonDto = null;
		int entityCode = 1;
		
				List<AddOnBenefitsDTO> hospitalCashBenefitListDto = this.bean.getAddOnBenefitsDTO();
				if(hospitalCashBenefitListDto != null && !hospitalCashBenefitListDto.isEmpty()){
					
					for (AddOnBenefitsDTO hospitalCashBenefitDto : hospitalCashBenefitListDto) {
						if(hospitalCashBenefitDto.getDisallowanceRemarks() != null && !hospitalCashBenefitDto.getDisallowanceRemarks().isEmpty() && hospitalCashBenefitDto.getDisallowanceRemarks().length()>75){
							nonPayableReasonDto = new NonPayableReasonDto();
							nonPayableReasonDto.setDeductibleOrNonPayableReason(hospitalCashBenefitDto.getDisallowanceRemarks());
							String deductibleOrNonPayableReason = "  Refer Note #"+entityCode;
							System.out.println("Deductible Reason    ***************** ==========  : " + deductibleOrNonPayableReason);
							nonPayableReasonDto.setSno("#"+entityCode);
							hospitalCashBenefitDto.setDisallowanceRemarksForBillAssessment(deductibleOrNonPayableReason);
							entityCode++;
							nonPayableReasonListDto.add(nonPayableReasonDto);
						}
						else{
							hospitalCashBenefitDto.setDisallowanceRemarksForBillAssessment(hospitalCashBenefitDto.getDisallowanceRemarks());
						}
					}
				}
				
	
			this.bean.getPreauthDTO().setNonPayableReasonListDto(nonPayableReasonListDto);		
		

		Panel firstVertical = getContentofTemplate(templateName,reportDto,docGen);
		this.bean.getPreauthDTO().setFilePathAndTypeMap(fileMap);
		firstVertical.setHeight("420px");

		return firstVertical;
	}

	private Panel getContentofTemplate(String templateName,ReportDto reportDto,DocumentGenerator docGen) {
		
		
		String filePath = docGen.generatePdfDocument(templateName, reportDto);	
		final String finalFilePath = filePath;
			
		fileMap.put("BillSummaryDocFilePath", finalFilePath);
		fileMap.put("BillSummaryDocType", SHAConstants.BILL_ASSESSMENT_HOSPITAL_CASH);
		fileMap.put("docSources", SHAConstants.FINANCIAL_APPROVER);
		fileMap.put("version", reportDto.getBillAssessmentVersion());
		Path p = Paths.get(finalFilePath);
		String fileName = p.getFileName().toString();
		StreamResource.StreamSource s = SHAUtils.getStreamResource(finalFilePath);

		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
//		e.setHeightUndefined();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		if(s!=null && s.getStream()!=null){
			SHAUtils.closeStreamResource(s);
		}
		Panel panel = new Panel();	
		panel.setHeight("100%");
		panel.setContent(e);
		return panel;
	}
	
}
