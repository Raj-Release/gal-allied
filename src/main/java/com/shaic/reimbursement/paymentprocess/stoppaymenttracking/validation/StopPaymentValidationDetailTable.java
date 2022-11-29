package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation;

import java.util.HashMap;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestDto;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestWizardImpl;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.BaseTheme;

public class StopPaymentValidationDetailTable extends GBaseTable<StopPaymentRequestDto>{
	

	

	
	private final static Object COLUM_HEADER_SEARCH_INTIMATION_UTR[] = new Object[] {
		"serialNumber","Action","intimationNo","rodNumber","utrNumber","reasonForStopPaymentValue","stopPaymentReqRemarks"};

	
	@Inject
	private Instance<StopPaymentValidateWizardImpl>stopPaymentValidateWizardImplInstance;
	
	private StopPaymentValidateWizardImpl stopPaymentValidateWizardObj;
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {

		BeanItemContainer<StopPaymentRequestDto> newIntimationDtoContainer = new BeanItemContainer<StopPaymentRequestDto>(StopPaymentRequestDto.class);
		
		table.removeGeneratedColumn("Action");
		table.addGeneratedColumn("Action",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						
						final Button viewIntimationDetailsButton = new Button();
						StopPaymentRequestDto bean = (StopPaymentRequestDto) itemId;
					
						viewIntimationDetailsButton
						.setCaption("Validate");
						
						viewIntimationDetailsButton
								.addClickListener(new Button.ClickListener() {
									public void buttonClick(
											ClickEvent event) {
										
										fireViewEvent(MenuPresenter.SHOW_STOP_PAYMENT_VALIDATION_WIZARD, bean);
										
										/*
										Window popup = new com.vaadin.ui.Window();
										StopPaymentRequestDto result=new StopPaymentRequestDto();
										stopPaymentValidateWizardObj = stopPaymentValidateWizardImplInstance.get();
										stopPaymentValidateWizardObj.initView(bean,popup);
										
										VerticalLayout mainVerticalLayout = new VerticalLayout();
										mainVerticalLayout.addComponent(stopPaymentValidateWizardObj);
										
										popup.setCaption("Stop Payment Validation");
										popup.setWidth("75%");
										popup.setHeight("75%");
										popup.setContent(mainVerticalLayout);
										popup.setClosable(true);
										popup.center();
										popup.setResizable(false);
										popup.addCloseListener(new Window.CloseListener() {
											*//**
											 * 
											 *//*
											private static final long serialVersionUID = 1L;
							
											@Override
											public void windowClose(CloseEvent e) {
												System.out.println("Close listener called");
											}
										});
							
										popup.setModal(true);
										UI.getCurrent().addWindow(popup);
										popup = null;
									*/}
								});
						viewIntimationDetailsButton
								.addStyleName(BaseTheme.BUTTON_LINK);
						return viewIntimationDetailsButton;
					}
				});
	
	    table.setColumnHeader("Action", "Action");
	 	
		table.setContainerDataSource(newIntimationDtoContainer);
		table.setVisibleColumns(COLUM_HEADER_SEARCH_INTIMATION_UTR);
		table.setHeight("295px");
		
	}
	
	@Override
	public void tableSelectHandler(StopPaymentRequestDto t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "stopPaymentValidation-";
	}
	
	@SuppressWarnings("deprecation")
	public void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	
	public void getErrorMessage(String eMsg) {

		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}
	



}
