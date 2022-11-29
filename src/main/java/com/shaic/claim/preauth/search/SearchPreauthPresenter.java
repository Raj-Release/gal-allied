package com.shaic.claim.preauth.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthFormDTO;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.domain.MasUserAutoAllocation;
import com.shaic.domain.PreauthService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(SearchPreauthView.class)
public class SearchPreauthPresenter extends AbstractMVPPresenter<SearchPreauthView> {

	private static final long serialVersionUID = -5504472929540762973L;
	
	//public static final String SHOW_PRE_AUTH_FORM = "preauth_show_pre_auth_form";
	
	public static final String AUTO_ALLOCATION_VIEW = "autoAllocationViewPreauth";
	public static final String SEARCH_BUTTON_CLICK = "preauthSearchBtn";
	public static final String TREATMENT_TYPE_CHANGED = "cmbTreatmentType";
	public static final String SPECIALITY_LIST = "Speciality List";
	
	@EJB
	private PreAuthSearchService preAuthSearchService;
	
	@EJB
	private PreauthService preauthService;
	
	
	/* public void setupList(@Observes @CDIEvent(MenuItemBean.PROCESS_PREAUTH) final ParameterDTO parameters) {
		 view.list(PreauthMapper.getSearchPreauthTableDTO());
	 }
	
	 @Override
	public void viewEntered() {
		
	}*/
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		
		SearchPreauthFormDTO searchFormDTO = (SearchPreauthFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);

		Page<SearchPreauthTableDTO> search = preAuthSearchService.search(searchFormDTO,userName,passWord);
		view.list(search);
	}
	
	@SuppressWarnings({ "deprecation" })
	public void populateSpeciality(@Observes @CDIEvent(TREATMENT_TYPE_CHANGED)  final  ParameterDTO parameters) {
		BeanItemContainer<SelectValue> selectSpecialityContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		
		String strValue = (String) parameters.getPrimaryParameter();
		System.out.println("---inside the presenter for value change--"+strValue);
		if((SHAConstants.MEDICAL).equalsIgnoreCase(strValue))
		{
			selectSpecialityContainer = preauthService.getSpecialityType("M");
		}
		else if((SHAConstants.SURGICAL).equalsIgnoreCase(strValue))
		{
			selectSpecialityContainer = preauthService.getSpecialityType("S");
		}
		view.initSpeciality(selectSpecialityContainer);
	}
	
	@SuppressWarnings({ "deprecation" })
	public void getSpecialityList(@Observes @CDIEvent(SPECIALITY_LIST)  final  ParameterDTO parameters) {
		BeanItemContainer<SelectValue> selectSpecialityContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		
		selectSpecialityContainer = preauthService.getSpecialistTypeList();
		
		view.initSpeciality(selectSpecialityContainer);
	}
	
	@SuppressWarnings({ "deprecation" })
	public void handleAutoAllocationView(@Observes @CDIEvent(AUTO_ALLOCATION_VIEW) final ParameterDTO parameters) {
		
		
		SearchPreauthFormDTO searchFormDTO = (SearchPreauthFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		
		MasUserAutoAllocation user = preAuthSearchService.searchUserType(userName);
		view.changeView(searchFormDTO, user);
	}

	@Override
	public void viewEntered() {

	}
}
