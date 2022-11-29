package com.shaic.claim.intimation.create;

import java.util.Iterator;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewGrievanceTrails extends ViewComponent {
	
	private static final long serialVersionUID = -7874417450890458639L;

	private TextField txtIntimationNo;
	
	private TextField grievanceFlag;
	
	private VerticalLayout mainVertical= null;
	
	//private BeanFieldGroup<ViewGrievanceTrailsTableDTO> binder;
	
	private BeanFieldGroup<ClaimGrievanceDTO> binder;
	
	@Inject
	private Instance<ViewGrievanceTrailsTable> viewGrievanceTrailsTableInst;
	
	private ViewGrievanceTrailsTable viewGrievanceTrailsTable;
	
	private ClaimGrievanceDTO bean;
	
	/*public void setPopup(Window popup) {
		this.popup = popup;
	}
*/
    public void init(ClaimGrievanceDTO claimGrievanceDTO){
    	
    	this.bean=claimGrievanceDTO;   	
    	this.binder = new BeanFieldGroup<ClaimGrievanceDTO>(ClaimGrievanceDTO.class);
		this.binder.setItemDataSource(claimGrievanceDTO);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
    	
        txtIntimationNo = (TextField) binder.buildAndBind("Intimation No:","intimationNumber",TextField.class);
    	
        grievanceFlag = (TextField) binder.buildAndBind("Grievance Flag(Y/N):","grievanceFlag",TextField.class);
    	
    	FormLayout intimation=new FormLayout(txtIntimationNo);
    	//intimation.setHeight("3rem");
    	
    	FormLayout grievanceForm=new FormLayout(grievanceFlag);
    	
    	setReadOnly(intimation,true);
    	setReadOnly(grievanceForm,true);
    	
    	HorizontalLayout grievanceHLayout =new HorizontalLayout(intimation,grievanceForm);  
    	grievanceHLayout.setSpacing(true);
    	grievanceHLayout.setComponentAlignment(intimation, Alignment.TOP_CENTER);
 	    grievanceHLayout.setComponentAlignment(grievanceForm, Alignment.MIDDLE_RIGHT);
 	    grievanceHLayout.setWidth("110%");
    	
    	viewGrievanceTrailsTable = viewGrievanceTrailsTableInst.get();
    	viewGrievanceTrailsTable.init("", false, false);
    	if(claimGrievanceDTO !=null && claimGrievanceDTO.getGrievanceDetails() !=null
    			&& !claimGrievanceDTO.getGrievanceDetails().isEmpty()){
    		viewGrievanceTrailsTable.setTableList(claimGrievanceDTO.getGrievanceDetails());
		}
    	   	
    
    	VerticalLayout tableLayout = new VerticalLayout(grievanceHLayout,viewGrievanceTrailsTable);
    	
    	Panel mainPanel = new Panel(tableLayout);
		mainPanel.addStyleName("girdBorder");
		mainPanel.setSizeFull();
		mainPanel.setHeightUndefined();
		
		//Panel grievanceTablePanel = new Panel(viewGrievanceTrailsTable);
    	//grievanceTablePanel.addStyleName("girdBorder");
    	//grievanceTablePanel.setCaption("Grievance Details");
    	
    	mainVertical=new VerticalLayout(mainPanel);
    	mainVertical.setSpacing(true);
		mainVertical.setMargin(true);
		setCompositionRoot(mainVertical);
		
   }	
    
    @SuppressWarnings({ "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setWidth("15em");
				field.setSizeUndefined();
				field.setNullRepresentation("-");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setWidth("15em");
				field.setNullRepresentation("-");
				field.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
				SHAUtils.handleTextAreaPopupDetails(field,null,getUI(),SHAConstants.STP_REMARKS);
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}
}