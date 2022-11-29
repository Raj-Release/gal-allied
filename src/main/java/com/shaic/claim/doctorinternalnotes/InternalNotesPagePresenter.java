package com.shaic.claim.doctorinternalnotes;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.fieldvisit.search.SearchFieldVisitTableDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.OrganaizationUnitService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.State;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.TmpFvR;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(InternalNotesPageView.class)
public class InternalNotesPagePresenter extends
		AbstractMVPPresenter<InternalNotesPageView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SUBMIT_DR_NOTE_EVENT = "Submit Doctor Internal Note Events";
	
	@EJB
	private ClaimService claimService;

	@Inject
	private ClaimMapper claimMapper;

	@EJB
	private MasterService masterService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;

	private Intimation intimation;

	@SuppressWarnings("unchecked")
	public void submitEvent(
			@Observes @CDIEvent(SUBMIT_DR_NOTE_EVENT) final ParameterDTO parameters) {
		Map<String, Object> mapDTO = (Map<String, Object>) parameters
				.getPrimaryParameter();
		NewIntimationDto searchTableDTO = (NewIntimationDto) mapDTO
				.get("searchTableDto");
		ClaimDto claimDto = (ClaimDto) mapDTO.get("claimDto");;
		
		if (claimDto != null ) {

			if (claimDto.getKey() != null) {
				Claim result = claimService.updateDoctorInternalRemarks(claimDto);
				view.result();
			}
		}

	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}

}
