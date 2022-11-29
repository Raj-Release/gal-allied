package com.shaic.claim.pcc.hrmp;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.MasClmAuditUserMapping;
import com.shaic.domain.MasterService;


@SuppressWarnings("serial")
@ViewInterface(SearchHRMPView.class)
public class SearchHRMPPresenter extends AbstractMVPPresenter<SearchHRMPView>{

	public static final String RESET_SEARCH_VIEW = "CVC Audit Action Reset Search Fields"; 
	public static final String SUBMIT_SEARCH = "searchHRMP";
	public static final String DISABLE_SEARCH_FIELDS = "CVC Audit Action Disable Search Filters";
	public static final String EDIT_INTIMATION_SCREEN = "CVC Audit Action Edit Intimation Screen";

	public static final String GET_AUDIT_USER_CLM_TYPE = "GetUserName";

	@EJB
	private SearchHRMPService searchService;

	@EJB
	private MasterService  masterService;

	private String userName= "";

	@Override
	public void viewEntered() {
		System.out.println("view Entered called");
	}

	public void handleSearch(@Observes @CDIEvent(SUBMIT_SEARCH) final ParameterDTO parameters) {
		SearchHRMPFormDTO searchFormDTO = (SearchHRMPFormDTO) parameters.getPrimaryParameter();
		userName = (String)parameters.getSecondaryParameter(0, String.class);
		
		view.list(searchService.search(searchFormDTO,userName , searchFormDTO.getTabStatus()));
	}

	public void getClmTypeForAuditUser(@Observes @CDIEvent(GET_AUDIT_USER_CLM_TYPE) final ParameterDTO parameters) {

		String userName=(String)parameters.getPrimaryParameter();

		MasClmAuditUserMapping auditUserMap = masterService.getAuditUserByEmpId(userName);

		view.setAuditUser(auditUserMap);
	}

	public Long splitCpuCode(String cpuValue){
		try {
			if(cpuValue != null && !cpuValue.isEmpty())
			{
				String stringList[] = cpuValue.split(" ");
				for (String string : stringList) {
					if(!string.isEmpty()){
						Long cpucode = Long.parseLong(string);
						return cpucode;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
