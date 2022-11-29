package com.shaic.paclaim.reimbursement.draftquery;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.ReimbursementQueryService;
import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterTableDTO;

/**
 * 
 * @author Lakshminarayana
 *
 */
@ViewInterface(DraftPAQueryLetterDetailView.class)
public class DecideOnDraftPAQueryPresenter extends AbstractMVPPresenter<DraftPAQueryLetterDetailView> {
	
	public static final String SAVE_DRAFT_PA_QUERY_LETTER = "Save Draft PA Query Letter";
	
	@EJB
	private ReimbursementQueryService submitService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void buildApproveQueryLayout(
			@Observes @CDIEvent(SAVE_DRAFT_PA_QUERY_LETTER) final ParameterDTO parameters) {
		
		SearchDraftQueryLetterTableDTO updatedBean = (SearchDraftQueryLetterTableDTO)parameters.getPrimaryParameter();
		
		if(updatedBean != null){
//			submitService.saveReimbursementDraftQuery(updatedBean.getReimbursementQueryDto());
			
		}
		
	}

}
