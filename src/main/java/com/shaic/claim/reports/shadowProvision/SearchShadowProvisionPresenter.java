package com.shaic.claim.reports.shadowProvision;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.ClaimService;

@ViewInterface(SearchShowdowView.class)
public class SearchShadowProvisionPresenter extends AbstractMVPPresenter<SearchShowdowView>{
	
	public static final String EXPORT_EXCEL = "export_excel_file";
	
	@EJB
	private ClaimService claimService;
	
	protected void exportReminderDetailsToExcel(
			@Observes @CDIEvent(EXPORT_EXCEL) final ParameterDTO parameters) {	

		SearchShadowProvisionDTO tableDTO = (SearchShadowProvisionDTO)parameters.getPrimaryParameter();
		
		List<SearchShadowProvisionDTO> errorLogDetailsForShadow = claimService.getErrorLogDetailsForShadow(tableDTO.getBatchNumber());
		view.exportToExcelList(errorLogDetailsForShadow);
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
