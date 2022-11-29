package com.shaic.reimbursement.paymentprocess.initiateRecovery;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;


@ViewInterface(PaymentInitiateRecoverySearchView.class)
public class PaymentInitiateRecoverySearchPresenter extends AbstractMVPPresenter<PaymentInitiateRecoverySearchView>{
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Inject
	public PaymentRecoveryService dbService;
	
	public static final String PAYMENT_INITIATE_RECOVERY_SEARCH = "PaymentInitiateRecoverySearch";
	public static final String PAYMENT_INITIATE_RECOVERY_SUBMIT = "PaymentInitiateRecoverySubmit";
	
	public void handleSearch(@Observes @CDIEvent(PAYMENT_INITIATE_RECOVERY_SEARCH) final ParameterDTO parameters) {
		PaymentInitiateRecoverySearchFormDTO searchFormDTO = (PaymentInitiateRecoverySearchFormDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
    	view.resultantTable(dbService.performSearch(searchFormDTO,userName,"ProcessLevelOneSearch"));
	}
	
	public void submitData(@Observes @CDIEvent(PAYMENT_INITIATE_RECOVERY_SUBMIT) final ParameterDTO parameters) {
		PaymentInitiateRecoveryTableDTO dto = (PaymentInitiateRecoveryTableDTO) parameters.getPrimaryParameter();
//		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		dbService.updatePaymentInitiateRecoveryData(dto);
		view.buildSuccessLayout();
		view.resetValues();
//    	view.resultantTable(dbService.performSearch(searchFormDTO,userName,"ProcessLevelOneSearch"));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}