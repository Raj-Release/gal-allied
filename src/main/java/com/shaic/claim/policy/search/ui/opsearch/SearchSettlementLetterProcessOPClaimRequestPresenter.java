package com.shaic.claim.policy.search.ui.opsearch;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpDTO;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpService;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpView;
import com.shaic.domain.Insured;
import com.shaic.domain.outpatient.OPHealthCheckup;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

@ViewInterface(SearchSettlementLetterProcessOPClaimRequestView.class)
public class SearchSettlementLetterProcessOPClaimRequestPresenter extends AbstractMVPPresenter<SearchSettlementLetterProcessOPClaimRequestView>{

	@EJB
	private SearchSettlementLetterProcessOPClaimRequestService searchService;
	
	public static final String CREATE_SETTLEMENT_LETTER_OP = "Create_Settlement_Letter_Search_Op";
	
	public static final String CREATE_SETTLEMENT_LETTER_GENERATE = "Create_Settlement_Letter_Generate_Op";
	

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(CREATE_SETTLEMENT_LETTER_OP) final ParameterDTO parameters) {
		
		CreateBatchOpDTO searchFormDTO = (CreateBatchOpDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		
			view.list(searchService.search(searchFormDTO,userName,passWord));
		
	}
	
	@SuppressWarnings({ "deprecation" })
	public void generateSettlementLetter(@Observes @CDIEvent(CREATE_SETTLEMENT_LETTER_GENERATE) final ParameterDTO parameters) {
	Long opCheckupKey=	(Long)parameters.getPrimaryParameter();
	OPHealthCheckup opHealthCheckup= searchService.getOPByKey(opCheckupKey);
	if(opHealthCheckup != null){
		ClaimDto claimDTO= new ClaimDto(); 
		NewIntimationDto intimationDTO = new NewIntimationDto();
		Insured insured = opHealthCheckup.getClaim().getIntimation().getInsured();
		intimationDTO.setIntimationId(opHealthCheckup.getClaim().getIntimation().getIntimationId());
		claimDTO.setInrTotalAmount(opHealthCheckup.getAmountPayable());
		claimDTO.setCreatedDate(opHealthCheckup.getChequeDate());
		intimationDTO.setPolicy(insured.getPolicy());
		intimationDTO.setInsuredPatient(insured);
		claimDTO.setNewIntimationDto(intimationDTO);
		claimDTO.setClaimId(opHealthCheckup.getClaim().getClaimId());
		
		List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
		claimDtoList.add(claimDTO);
		DocumentGenerator docGenarator = new DocumentGenerator();
		String fileUrl = null;
		ReportDto reportDto = new ReportDto();
		reportDto.setClaimId(claimDTO.getClaimId());
		reportDto.setBeanList(claimDtoList);
		
		fileUrl = docGenarator.generatePdfDocument("OPSettlementLetter", reportDto);
		claimDTO.setDocFilePath(fileUrl);
		claimDTO.setDocType(SHAConstants.OP_SETTLEMENT_LETTER);
		searchService.uploadOPSettlementLetterToDMs(claimDTO,opHealthCheckup);
		if(fileUrl != null){
			view.buildSuccessLayout("Records has been saved successfully!");
		}
		
	}
	}
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
}
