package com.shaic.claim.search.specialist.search;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.VerticalLayout;

public class SpecialistCompletedCasesUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PopupDateField fromDate;
	
	private PopupDateField toDate;
	
	private Button btnSubmit;
	
	private String userName;
	
	@EJB
	private SubmitSpecialistService specialistService;
	
	@Inject
	private SpecialistCompletedTable specialistTable;

	public void init(String userName){
		
		this.userName = userName;
		
		fromDate = new PopupDateField("From Date");
		toDate = new PopupDateField("To Date");
		fromDate.setDateFormat("dd/MM/yyyy");
		fromDate.setTextFieldEnabled(false);
		toDate.setDateFormat("dd/MM/yyyy");
		toDate.setTextFieldEnabled(false);
//		toDate.setEnabled(false);
		
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -7);
	    Date todate1 = cal.getTime();    
	        
		fromDate.setValue(todate1);
		toDate.setValue(new Date());
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(fromDate,toDate);	
		horizontalLayout.setSpacing(true);
		btnSubmit = new Button("OK");
		

		specialistTable.init("", false, false);
		
		
		
		VerticalLayout firstVertical = new VerticalLayout(horizontalLayout,btnSubmit,specialistTable);
		firstVertical.setComponentAlignment(btnSubmit, Alignment.BOTTOM_CENTER);
		firstVertical.setComponentAlignment(horizontalLayout, Alignment.TOP_CENTER);
		firstVertical.setSpacing(true);

		Panel mainPanel = new Panel(firstVertical);
	
	    setCompositionRoot(mainPanel);
	    
	    addListener();

	}
	
	public void addListener(){
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
			
				Date from = fromDate.getValue();
				Date to = toDate.getValue();
				
				Long diffDays = SHAUtils.getDiffDays(from, to);
				
				if(! from.after(to)){

					if(diffDays <= 30l){
						List<SubmitSpecialistTableDTO> specialistCompletedList = specialistService.getSpecialistCompletedList(fromDate.getValue(), toDate.getValue(),userName);
						
						if(specialistCompletedList != null && ! specialistCompletedList.isEmpty()){
							specialistTable.setTableList(specialistCompletedList);
							
						}else{
							getErrorMessage("No Records found");
							specialistTable.removeRow();
						}
					}else{
						getErrorMessage("Difference of From Date and To Date should be less than or equal to 30 days");
						specialistTable.removeRow();
					}
				}else{
					getErrorMessage("From Date should be less than or equal to To Date");
					specialistTable.removeRow();
				}

			}
		});
		
	}
	
	public void getErrorMessage(String eMsg){
		
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	

}
