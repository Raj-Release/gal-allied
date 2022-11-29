package com.shaic.claim.reports.paymentprocess;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.table.Page;

@ViewInterface(PaymentProcessView.class)
public class PaymentProcessPresenter extends AbstractMVPPresenter<PaymentProcessView>{
	
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "PaymentProcessPresenter";
	
	@EJB
	private PaymentProcessService searchService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PaymentProcessFormDTO searchFormDTO = (PaymentProcessFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		PaymentProcessTableDTO PaymentProcessTableDTO = new PaymentProcessTableDTO("fd", "fh", "fh", "fdgh", "fdh", "fhd");
		Page<PaymentProcessTableDTO> page = new Page<PaymentProcessTableDTO>();
		List<PaymentProcessTableDTO> t = new ArrayList<PaymentProcessTableDTO>();
		t.add(PaymentProcessTableDTO);
		page.setPageItems(t);
		view.list(page);
		
//		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
