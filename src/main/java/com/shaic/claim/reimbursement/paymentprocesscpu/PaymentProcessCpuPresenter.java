package com.shaic.claim.reimbursement.paymentprocesscpu;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.UsertoCPUMappingService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PaymentProcessCpuView.class)

public class PaymentProcessCpuPresenter  extends AbstractMVPPresenter<PaymentProcessCpuView>{
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "PaymentProcessCpuPresenter";
	
	public static final String SEARCH_CPU_BRANCH = "Search_cpu_branch";	
	
	@EJB
	private UsertoCPUMappingService userCPUMapService;
	
	@EJB
	private PaymentProcessCpuService searchService;
    
	//private PaymentProcessCpuTableDTO tableDto;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PaymentProcessCpuFormDTO searchFormDTO = (PaymentProcessCpuFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);		
     	view.list(searchService.search(searchFormDTO,userName,passWord,userCPUMapService));
     	   	
     	
	}
	
	@SuppressWarnings({ "deprecation" })
	public void getBranchBasedOnCode(@Observes @CDIEvent(SEARCH_CPU_BRANCH) final ParameterDTO parameters) {
		
		Long cpuCode = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> cpuCodeContainer = searchService.getBranchFromCPUCode(cpuCode);
		view.loadBranchContainer(cpuCodeContainer);
		
		/*String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);		
     	view.list(searchService.search(searchFormDTO,userName,passWord));*/
     	   	
     	
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
