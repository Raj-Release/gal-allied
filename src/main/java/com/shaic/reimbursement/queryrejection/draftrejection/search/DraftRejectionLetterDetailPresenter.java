package com.shaic.reimbursement.queryrejection.draftrejection.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ReimbursementRejectionDto;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReimbursementRejectionService;
import com.vaadin.v7.data.util.BeanItemContainer;
/**
 * 
 * @author Lakshminarayana
 *
 */
@ViewInterface(DraftRejectionLetterDetailView.class)
public class DraftRejectionLetterDetailPresenter extends AbstractMVPPresenter<DraftRejectionLetterDetailView>{
	
	public static final String SUBMIT_REJECTION_LETTER = "Submit Draft Rejection Letter";
	
	public static final String Send_Draft_Rejection_for_Process = "Send Draft Rejection for process";
	public static final String CANCEL_DRAFT_REJECTION_LETTER = "Cancel Draft Rejection Letter";
	
	public static final String REJECT_SUB_CATEG_LAYOUT_DRAFT_REJ = "Generate Rej Sub category Layout for Draft Rej";
	
	@EJB
	private ReimbursementRejectionService reimbursementRejectionService;
	
	@EJB
	private MasterService masterService;
	
	@Override
	public void viewEntered() {
		
	}

	public void generateRejSubCategLayout(
			@Observes @CDIEvent(REJECT_SUB_CATEG_LAYOUT_DRAFT_REJ) final ParameterDTO parameters) {

		Long rejCategId = (Long) parameters.getPrimaryParameter();
		
		BeanItemContainer<SelectValue> rejSubcategContainer = masterService.getRejSubcategContainer(rejCategId);
		
		view.setSubCategContainer(rejSubcategContainer);
	}
	
	protected void cancelDraftRejectionLetter(
			@Observes @CDIEvent(CANCEL_DRAFT_REJECTION_LETTER) final ParameterDTO parameters) {
		view.cancelDraftRejectionLetter();
	}
	
	protected void submitDraftRejectionLetter(
			@Observes @CDIEvent(SUBMIT_REJECTION_LETTER) final ParameterDTO parameters) {
		SearchDraftRejectionLetterTableDTO draftRejectionLetterTableDto = (SearchDraftRejectionLetterTableDTO)parameters.getPrimaryParameter();
		
		ReimbursementRejectionDto bean = draftRejectionLetterTableDto.getReimbursementRejectionDto();
		bean.setUsername(draftRejectionLetterTableDto.getUsername());
		bean.setPassword(draftRejectionLetterTableDto.getPassword());
		//bean.setHumanTask(draftRejectionLetterTableDto.getHumanTaskDTO());
		
		reimbursementRejectionService.updateReimbursementRejection(bean);
		
		view.openPdfFileInWindow();
	}
	

}
