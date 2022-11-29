package com.shaic.claim.userproduct.document.search;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;

public class ViewDoctorSearchCriteriaViewImpl extends AbstractMVPView implements ViewDoctorSearchCriteriaView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private SearchDoctorTable doctorTable;
	
	@Inject 
	private DoctorSearchCriteriaService doctorSearchCriteriaServiceObj;
	
	@Inject
	private SearchDoctorNameDTO searchDTO;
	
	private String presenterString;
	
	private Window popup;
	
	private Panel panelLayout;
	
	@PostConstruct
	public void init(){
		
	}
	

	public void initView(Window popup) {
		
//		addStyleName("view");
		
		doctorTable.init("",false,false);
		doctorTable.setWindowObject(popup);
		
		doctorTable.setTableList(doctorSearchCriteriaServiceObj.search());
		
		panelLayout = new Panel(doctorTable);
		panelLayout.setWidth("100%");
		
		setCompositionRoot(panelLayout);
		
	}
	
//	public void setWindowObject(Window popup){
//		this.popup = popup;
//		doctorTable.setWindowObject(popup);
//	}
//	
//	public void setPresenterString(String presenterString)
//	{
//		this.presenterString = presenterString;
//	}
	
	
	
}