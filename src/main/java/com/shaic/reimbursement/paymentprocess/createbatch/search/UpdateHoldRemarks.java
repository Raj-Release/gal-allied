package com.shaic.reimbursement.paymentprocess.createbatch.search;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class UpdateHoldRemarks extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TextArea txtHoldRemarks;
	
	private Button btnSubmit;
	
	private Button btnCancel;
	
	private HorizontalLayout btnHorizontalLayout;
	
	private SearchCreateBatchViewImpl currentPage;
	
	private SearchCreateBatchListenerTable page;
	
	private Window popUp;
	
	@EJB
	private SearchCreateBatchService createBatchService;
	
	public void init(SearchCreateBatchViewImpl currentPage,Window popUp){
		
		this.popUp = popUp;
		
		txtHoldRemarks = new TextArea("Hold Remarks");
		
		this.currentPage = currentPage;
		
		
		btnSubmit = new Button("Submit");
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		
		btnCancel = new Button("Cancel");
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.setWidth("-1px");
		btnCancel.setHeight("-10px");

		btnHorizontalLayout = new HorizontalLayout(btnSubmit,btnCancel);
		btnHorizontalLayout.setSpacing(true);
		
		VerticalLayout mainVertical = new VerticalLayout(txtHoldRemarks,btnHorizontalLayout);
		mainVertical.setComponentAlignment(txtHoldRemarks, Alignment.TOP_CENTER);
		mainVertical.setComponentAlignment(btnHorizontalLayout, Alignment.BOTTOM_CENTER);
		
		addListener();

		setCompositionRoot(mainVertical);
		
	}
	
public void init(SearchCreateBatchListenerTable currentPage,Window popUp,CreateAndSearchLotTableDTO tableDTO,String presenter){
		
		this.popUp = popUp;
		
		txtHoldRemarks = new TextArea("Hold Remarks");
		
		this.page = currentPage;
		
		
		btnSubmit = new Button("Submit");
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		
		btnCancel = new Button("Cancel");
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.setWidth("-1px");
		btnCancel.setHeight("-10px");

		btnHorizontalLayout = new HorizontalLayout(btnSubmit,btnCancel);
		btnHorizontalLayout.setSpacing(true);
		
		VerticalLayout mainVertical = new VerticalLayout(txtHoldRemarks,btnHorizontalLayout);
		mainVertical.setComponentAlignment(txtHoldRemarks, Alignment.TOP_CENTER);
		mainVertical.setComponentAlignment(btnHorizontalLayout, Alignment.BOTTOM_CENTER);
		btnSubmit.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(SearchCreateBatchPresenter.CREATE_BATCH_PAYMENT_LVL1_HOLD, tableDTO, txtHoldRemarks.getValue(),popUp);
				
			}
		});
		btnCancel.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
			if(popUp != null){
				popUp.close();
			}
			

			}
		});
		setCompositionRoot(mainVertical);
		
		
	}
	
	public void addListener(){
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
			  currentPage.moveToHoldRecords(txtHoldRemarks.getValue(),popUp);

			}
		});
		
		btnCancel.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
			  currentPage.submitClosePopUp();

			}
		});
		
	}
	

}
