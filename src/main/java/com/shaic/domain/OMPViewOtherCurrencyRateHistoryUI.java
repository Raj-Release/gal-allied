package com.shaic.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPProcessRODBillEntryService;
import com.shaic.claim.claimhistory.view.ompView.OMPEarlierRodMapper;
import com.shaic.claim.omp.ratechange.OMPClaimRateChangeHistory;
import com.shaic.claim.omp.ratechange.OMPViewCurrencyRateHistoryDetailTable;
import com.shaic.domain.omp.OMPIntimationService;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

public class OMPViewOtherCurrencyRateHistoryUI extends ViewComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ComboBox txtRODNo;
		
	 @Inject
	 private Instance<OMPViewCurrencyRateHistoryDetailTable> ompcurrencyhistoryDetailsInstance;
	    
	 private OMPViewCurrencyRateHistoryDetailTable ompcurrencyhistoryDetailsObj;
	 
	@EJB
	private OMPIntimationService ompIntimationService;
	
	@EJB
    private OMPProcessRODBillEntryService rodBillentryService;
	 
	private Button currencyBtn;
	@Inject
	private OMPClaimRateChangeHistory ompClaimRateChangeHistory;
	 
		public void init(OMPClaim claim,final Long rodKey){
			final List<OMPReimbursement> reimbursement = ompIntimationService.getRembursementDetailsByClaimKey(claim.getKey());
			
			String rodNo = null;
			//SelectValue Noselect = null;
			//long count = 0;
			ArrayList<SelectValue> listNo = new ArrayList<SelectValue>();
			BeanItemContainer<SelectValue> ompModeOfIntimation = new BeanItemContainer<SelectValue>(SelectValue.class);
			if(reimbursement != null && !reimbursement.isEmpty()) {
				for (OMPReimbursement reimbursement2 : reimbursement) {
					if(reimbursement2.getClaim().getKey().equals(claim.getKey())) {
						 rodNo = reimbursement2.getRodNumber();
						 SelectValue Noselect = new SelectValue();
						 Noselect.setValue(rodNo);
						// count = count +1;
						 Noselect.setId(reimbursement2.getKey());
						// count++;
						 listNo.add(Noselect);
						 ompModeOfIntimation.addBean(Noselect);
						 
					}
				}
				
			}
					
			txtRODNo = new ComboBox("ROD No");
			txtRODNo.setContainerDataSource(ompModeOfIntimation);
			txtRODNo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			txtRODNo.setItemCaptionPropertyId("value");
			
			currencyBtn = new Button();
			currencyBtn.setIcon(new ThemeResource("images/rupee.png"));
			currencyBtn.setStyleName("link");
			currencyBtn.setWidth("300px");
			currencyBtn.setHeight("200px");
//			 Button viewOtherCurrency = new Button("View Other Currency Rate");
			currencyBtn.addClickListener(new ClickListener() {
				 private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {Window popup =null;
					popup = new com.vaadin.ui.Window();
					popup.setWidth("800px");
					popup.setHeight("280px");
					popup.setCaption("View Claimant Details");
					if(txtRODNo.getValue()!= null){
						SelectValue value = (SelectValue) txtRODNo.getValue();
						if(value!=null){
							final OMPReimbursement reimbursementByKey = rodBillentryService.getReimbursementByRodNo(value.getValue());
							ompClaimRateChangeHistory.init(reimbursementByKey);
							
						}
					}
					popup.setContent(ompClaimRateChangeHistory);
					popup.setClosable(true);
					popup.center();
					popup.setResizable(false);
					popup.addCloseListener(new Window.CloseListener() {
						/**
					 * 
					 */
						private static final long serialVersionUID = 1L;

						@Override
						public void windowClose(CloseEvent e) {
							System.out.println("Close listener called");
						}
					});

					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
					}
				
		});
			FormLayout mainForm = new FormLayout(txtRODNo);
			mainForm.setSpacing(true);
			FormLayout currencylayout = new FormLayout(currencyBtn);
			HorizontalLayout hLayout = new HorizontalLayout(mainForm,currencylayout);
			hLayout.setSpacing(Boolean.TRUE);
			ompcurrencyhistoryDetailsObj = ompcurrencyhistoryDetailsInstance.get();
			ompcurrencyhistoryDetailsObj.init("", false, false);
			ompcurrencyhistoryDetailsObj = addListenerForRODNo();
			
			if(rodKey!=null){
				final OMPReimbursement reimbursementByKey = ompIntimationService.getReimbursementByKey(rodKey);
				if(reimbursementByKey!=null){
					SelectValue selectValue = new SelectValue();
					selectValue.setId(reimbursementByKey.getKey());
					selectValue.setValue(reimbursementByKey.getRodNumber());
					txtRODNo.setValue(selectValue.getValue());
					currencyBtn = new Button();
					currencyBtn.setIcon(new ThemeResource("images/rupee.png"));
					currencyBtn.setStyleName("link");
					currencyBtn.setWidth("300px");
					currencyBtn.setHeight("200px");
//					 Button viewOtherCurrency = new Button("View Other Currency Rate");
					currencyBtn.addClickListener(new ClickListener() {
						 private static final long serialVersionUID = 1L;

							public void buttonClick(ClickEvent event) {Window popup =null;
							popup = new com.vaadin.ui.Window();
							popup.setWidth("800px");
							popup.setHeight("280px");
							popup.setCaption("View Claimant Details");
							ompClaimRateChangeHistory.init(reimbursementByKey);
							popup.setContent(ompClaimRateChangeHistory);
							popup.setClosable(true);
							popup.center();
							popup.setResizable(false);
							popup.addCloseListener(new Window.CloseListener() {
								/**
							 * 
							 */
								private static final long serialVersionUID = 1L;

								@Override
								public void windowClose(CloseEvent e) {
									System.out.println("Close listener called");
								}
							});

							popup.setModal(true);
							UI.getCurrent().addWindow(popup);
							}
						
				});
					
					
				}
			}
			
			VerticalLayout mainLayout = new VerticalLayout(hLayout,ompcurrencyhistoryDetailsObj);
			
			Panel mainPanel = new Panel(mainLayout);
			mainPanel.addStyleName("layoutDesign");
			mainPanel.setCaption("");
			
			setCompositionRoot(mainPanel);
//					}
//				}
			}
//		}
		
		
		private OMPViewCurrencyRateHistoryDetailTable addListenerForRODNo()
		{
			if(txtRODNo != null) {
				txtRODNo.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 7455756225751111662L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						SelectValue currencyRateSelectValue = (SelectValue) event.getProperty().getValue();
						if(currencyRateSelectValue!=null && currencyRateSelectValue.getValue()!=null){
							String rodNo = currencyRateSelectValue.getValue();
							OMPReimbursement reimbursement2 = rodBillentryService.getReimbursementByRodNo(rodNo);
							
							List<OMPOtherCurrencyRateHistoryDto> claimHistoryForCashless = new ArrayList<OMPOtherCurrencyRateHistoryDto>();
							List<OMPCurrencyHistory> claimRateChange=	rodBillentryService.getCurrencyRateHistory(reimbursement2.getKey());
						    claimHistoryForCashless = OMPEarlierRodMapper.getInstance().getOMPCurrencyHistoryDetails(claimRateChange);
						    if(claimHistoryForCashless != null && ompcurrencyhistoryDetailsObj!=null){
							    ompcurrencyhistoryDetailsObj.setTableList(claimHistoryForCashless);
//							    ompClaimRateDetailsObj.setCaption("");
							    }
							ompcurrencyhistoryDetailsObj.setCaption("");
						}
					}
				});
			}
			return ompcurrencyhistoryDetailsObj;
		}
		
		
		
}
