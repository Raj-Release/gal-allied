package com.shaic.claim.reimbursement.talktalktalk;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.common.APIService;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestWizardView;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(InitiateTalkTalkTalkWizardView.class)
public class InitiateTalkTalkTalkWizardPresenter extends AbstractMVPPresenter<InitiateTalkTalkTalkWizardView>{




	public static final String SUBMIT_TALK_TALK_TALK_VALUES = "submit_talk_talk_talk_values";
	
	public static final String ENABLE_DIALER_BUTTONS = "enableDialerButtons";

	@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;


	@EJB
	private ReimbursementService reimbursementService;

	@EJB
	private MasterService masterService;
	
	@EJB
	private InitiateTalkTalkTalkService talkTalkTalkService;

	/**
	 * Added for Talk Talk Talk Starts
	 * */

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}

	public void submitTalkTalkTalkValues(
			@Observes @CDIEvent(SUBMIT_TALK_TALK_TALK_VALUES) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		talkTalkTalkService.saveInitiateTalkTalkTalkDetails(preauthDTO);
		view.builTalkTalkTalkSuccessLayout();
	}
	
	public void enableDialerButtons(
			@Observes @CDIEvent(ENABLE_DIALER_BUTTONS) final ParameterDTO parameters) {
		view.enabledCallButtons();
	}
	

}

