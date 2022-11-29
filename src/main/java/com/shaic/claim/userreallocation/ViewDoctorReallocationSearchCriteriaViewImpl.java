package com.shaic.claim.userreallocation;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ViewDoctorReallocationSearchCriteriaViewImpl extends AbstractMVPView implements ViewDoctorReallocationSearchCriteriaView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private SearchReallocationDoctorTable doctorTable;
	
	@Inject 
	private DoctorReallocationSearchCriteriaService doctorSearchCriteriaServiceObj;
	
	@Inject
	private SearchReallocationDoctorNameDTO searchDTO;
	
	private String presenterString;
	
	private Window popup;
	
	private Panel panelLayout;
	
	private TextField doctorName;
	
	private Button searchDoctor;
	
	private VerticalLayout mainLayout;
	
	@PostConstruct
	public void init(){
		
	}
	

	public void initView(Window popup) {
		
//		addStyleName("view");
		mainLayout = new VerticalLayout();
		
		doctorName = new TextField("Enter Doctor Name");
		
		searchDoctor = new Button();
		searchDoctor.setStyleName(ValoTheme.BUTTON_LINK);
		searchDoctor.setIcon(new ThemeResource("images/search.png"));
		
		addDoctorListner();
		
		FormLayout formLayoutRight = new FormLayout(searchDoctor);
		FormLayout formLayoutLeft = new FormLayout(doctorName);
		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);

		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");
		
		doctorTable.init("",false,false);
		doctorTable.setWindowObject(popup);
		
		doctorTable.setTableList(doctorSearchCriteriaServiceObj.search());
		
		
		mainLayout.addComponent(fieldLayout);
		mainLayout.addComponent(doctorTable);
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.setMargin(false);		 
		 
		panelLayout = new Panel();
		panelLayout.setContent(mainLayout);
		panelLayout.setWidth("100%");
		
		setCompositionRoot(panelLayout);
		
	}

	
public void addDoctorListner() {
		
		searchDoctor.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(doctorName != null && doctorName.getValue() != null && !doctorName.getValue().isEmpty()){
					doctorTable.setTableList(doctorSearchCriteriaServiceObj.searchNameWise(doctorName.getValue()));
				}else{
					showErrorMessage("Please Enter Valid Doctor Name");
				}
			}
		});
		
	}

private void showErrorMessage(String eMsg) {
	Label label = new Label(eMsg, ContentMode.HTML);
	label.setStyleName("errMessage");
	VerticalLayout layout = new VerticalLayout();
	layout.setMargin(true);
	layout.addComponent(label);

	ConfirmDialog dialog = new ConfirmDialog();
	dialog.setCaption("Errors");
	dialog.setClosable(true);
	dialog.setContent(layout);
	dialog.setResizable(false);
	dialog.setModal(true);
	dialog.show(getUI().getCurrent(), null, true);
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
