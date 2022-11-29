package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.persistence.Query;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.PendingFvrInvsQueryPageUI;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.Investigation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;

import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class InvesAndQueryAndFvrParallelFlowTable extends
		GBaseTable<InvesAndQueryAndFvrParallelFlowTableDTO> {

	private static final Object[] NATURAL_COL_ORDER = new Object[] { "type",
			"initiatedDate", "remarks", "status", "proceedwithoutreport",
			"cancelrequest", "cancelremarks" };

	@EJB
	private ReimbursementService reimbursementService;

	private PendingFvrInvsQueryPageUI pendingFvrInvsQueryPageObj;
	private PreauthDTO bean;

	@Override
	public void removeRow() {
		table.removeAllItems();

	}

	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<InvesAndQueryAndFvrParallelFlowTableDTO>(
				InvesAndQueryAndFvrParallelFlowTableDTO.class));
		generatecolumns();
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("200px");

	}
	
	public void initTable(PreauthDTO bean) {
		this.bean = bean;
	}

	@Override
	public void tableSelectHandler(InvesAndQueryAndFvrParallelFlowTableDTO t) {
		// fireViewEvent(MenuPresenter.FVR_ASSINMENT_REPORT, t);
	}

	@Override
	public String textBundlePrefixString() {
		return "fvrInvesQueryParallelFlow-";
	}

	protected void tablesize() {
		table.setPageLength(table.size() + 1);
		int length = table.getPageLength();
		if (length >= 7) {
			table.setPageLength(7);
		}

	}

	private void generatecolumns() {
		table.removeGeneratedColumn("proceedwithoutreport");
		table.addGeneratedColumn("proceedwithoutreport",
				new Table.ColumnGenerator() {

					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {

						final InvesAndQueryAndFvrParallelFlowTableDTO invesFvrQueryDTO = (InvesAndQueryAndFvrParallelFlowTableDTO) itemId;

						final CheckBox chkBox = new CheckBox();

						if ((SHAConstants.PARALLEL_QUERY_TYPE).equalsIgnoreCase(invesFvrQueryDTO.getType())) {
							chkBox.setVisible(Boolean.FALSE);
						}
						if (invesFvrQueryDTO.getIsCanCelRodStatus()) {
							chkBox.setEnabled(false);
						}
												
						final Map<String, Object> workFlowObj = (Map<String, Object>) invesFvrQueryDTO.getDbOutArray();	
						if ((SHAConstants.PARALLEL_INVESTIGATION_TYPE).equalsIgnoreCase(invesFvrQueryDTO.getType())) {
							Investigation investigation = getInvestigationDetails(workFlowObj);				
							if (investigation != null) {
							/*	if(null != investigation.getTransactionFlag() && 
										investigation.getTransactionFlag().equalsIgnoreCase(SHAConstants.TRANSACTION_FLAG_CASHLESS))
								{
									if(null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() 
											&& bean.getPreauthDataExtractionDetails().getInvestigatorsCount()==1){
											chkBox.setEnabled(Boolean.TRUE);
									}
									else{
										chkBox.setEnabled(Boolean.FALSE);
									}
								}
								else{*/
									if((null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount()))
									{
										if (!invesFvrQueryDTO.getIsCanCelRodStatus()) {
											if((bean.getPreauthDataExtractionDetails().getInvestigatorsCount()>1)
												&& (null != bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount() 
												&& bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount()>=1)){
												
												chkBox.setEnabled(Boolean.FALSE);
											}
											else
											{
												chkBox.setEnabled(Boolean.TRUE);
											}
										}				
									
									}	
									
								//}
							}
						}
						chkBox.addValueChangeListener(new Property.ValueChangeListener() {

							@Override
							public void valueChange(ValueChangeEvent event) {

								Boolean value = (Boolean) event.getProperty().getValue();
								if (value != null) {
									if (value) {
										invesFvrQueryDTO.setProceedWithOutCheckStatus(Boolean.TRUE);
									} else {
										chkBox.setValue(false);
										invesFvrQueryDTO.setProceedWithOutCheckStatus(Boolean.FALSE);
									}
								}
							}
						});
						return chkBox;
					}
				});

		table.removeGeneratedColumn("cancelrequest");
		table.addGeneratedColumn("cancelrequest", new Table.ColumnGenerator() {

			// CheckBox chkbox = new CheckBox();

			@Override
			public Object generateCell(Table source, final Object itemId,
					Object columnId) {

				final InvesAndQueryAndFvrParallelFlowTableDTO invesFvrQueryDTO = (InvesAndQueryAndFvrParallelFlowTableDTO) itemId;

				final CheckBox chkBox = new CheckBox();

				if ((SHAConstants.PARALLEL_FVR_TYPE)
						.equalsIgnoreCase(invesFvrQueryDTO.getType())) {

					chkBox.setEnabled(Boolean.FALSE);
				}
				final Map<String, Object> workFlowObj = (Map<String, Object>) invesFvrQueryDTO.getDbOutArray();

				if ((SHAConstants.PARALLEL_INVESTIGATION_TYPE).equalsIgnoreCase(invesFvrQueryDTO.getType())) {
					Investigation investigation = getInvestigationDetails(workFlowObj);				
						if (investigation != null) {
							if(null != investigation.getTransactionFlag() && 
									investigation.getTransactionFlag().equalsIgnoreCase(SHAConstants.TRANSACTION_FLAG_CASHLESS)){
								if(null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() 
										&& bean.getPreauthDataExtractionDetails().getInvestigatorsCount()==1){
										chkBox.setEnabled(Boolean.FALSE);
								}
								else{
									chkBox.setEnabled(Boolean.TRUE);
								}
							}
							else if (!investigation.getStatus().getKey().equals(ReferenceTable.ZONAL_REVIEW_INITATE_INV_STATUS)
									&&!investigation.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS)
									&& !investigation.getStatus().getKey().equals(ReferenceTable.INITIATE_INVESTIGATION_APPROVED)
									&& !investigation.getStatus().getKey().equals(ReferenceTable.DRAFT_INVESTIGATION)									
									&& null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() 
									&& bean.getPreauthDataExtractionDetails().getInvestigatorsCount()==1) 
							{
											chkBox.setEnabled(Boolean.FALSE);
							}
							else
							{													
								if( null != bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount() 
										&& bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount()>=1)
								{
									chkBox.setEnabled(Boolean.TRUE);
								}
								else
								{
									if (!investigation.getStatus().getKey().equals(ReferenceTable.ZONAL_REVIEW_INITATE_INV_STATUS)
											&&!investigation.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS)
											&& !investigation.getStatus().getKey().equals(ReferenceTable.INITIATE_INVESTIGATION_APPROVED)
											&& !investigation.getStatus().getKey().equals(ReferenceTable.DRAFT_INVESTIGATION))
											{	
												chkBox.setEnabled(Boolean.FALSE);
									}
									else{
										
										chkBox.setEnabled(Boolean.TRUE);
									}
								}
							}
						}

					}

				chkBox.addValueChangeListener(new Property.ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {

						Boolean value = (Boolean) event.getProperty()
								.getValue();
						if (value != null) {
							if (value) {

								if ((SHAConstants.PARALLEL_INVESTIGATION_TYPE)
										.equalsIgnoreCase(invesFvrQueryDTO
												.getType())) {

									if (null != invesFvrQueryDTO.getStatusKey()&& !(ReferenceTable.INITIATE_INVESTIGATION).equals(invesFvrQueryDTO.getStatusKey())) {
										fireViewEvent(ClaimRequestWizardPresenter.INVS_CANCEL_REQUEST_VALIDATION,invesFvrQueryDTO);
									}
								}

								invesFvrQueryDTO.setFvrInvsQueryCancelStatus(Boolean.TRUE);

								if ((SHAConstants.PARALLEL_QUERY_TYPE).equalsIgnoreCase(invesFvrQueryDTO.getType())) {

									Long queryKey = (Long) workFlowObj.get(SHAConstants.PAYLOAD_QUERY_KEY);
									String currentQueue = (String) workFlowObj.get(SHAConstants.CURRENT_Q);
									Long wkKey = (Long) workFlowObj.get(SHAConstants.WK_KEY);
									
									if (null != queryKey) {

										ReimbursementQuery reimbQuery = reimbursementService.getReimbursementQuery(queryKey);
										if (null != reimbQuery) {
											if (null != reimbQuery.getStatus().getKey()
													&& (ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS.equals(reimbQuery.getStatus().getKey()))) 
											{
													if(reimbQuery.getQueryReply() != null && reimbQuery.getQueryReply().equalsIgnoreCase("Y"))
													{
														fireViewEvent(ClaimRequestWizardPresenter.CANCEL_QUERY_VALIDATION,invesFvrQueryDTO);
														chkBox.setValue(false);
														invesFvrQueryDTO.setFvrInvsQueryCancelStatus(Boolean.FALSE);
												}
												
											} 											
											else if (null != reimbQuery.getStatus().getKey()
													&& (ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS.equals(reimbQuery.getStatus().getKey()))
													&& null != currentQueue	&& (SHAConstants.BILL_ENTRY_CURRENT_KEY).equalsIgnoreCase(currentQueue)) {

												fireViewEvent(ClaimRequestWizardPresenter.CANCEL_QUERY_VALIDATION,invesFvrQueryDTO);
												chkBox.setValue(false);
												invesFvrQueryDTO.setFvrInvsQueryCancelStatus(Boolean.FALSE);
											}
											else {
												Boolean documentUploaded = pendingFvrInvsQueryPageObj
														.isDocumentUploaded();
												if (!documentUploaded) {
													fireViewEvent(ClaimRequestWizardPresenter.UPLOAD_QUERY_LETTER_VALIDATION,invesFvrQueryDTO);
													chkBox.setValue(false);
													invesFvrQueryDTO
															.setFvrInvsQueryCancelStatus(Boolean.FALSE);
												}
											}

										}
									}

								}
							} else {
								chkBox.setValue(false);
								invesFvrQueryDTO
										.setFvrInvsQueryCancelStatus(Boolean.FALSE);
							}
						}

					}
				});
				return chkBox;

			}
		});

		table.removeGeneratedColumn("cancelremarks");
		table.addGeneratedColumn("cancelremarks", new Table.ColumnGenerator() {

			@Override
			public Object generateCell(Table source, final Object itemId,
					Object columnId) {

				final InvesAndQueryAndFvrParallelFlowTableDTO invesFvrQueryDTO = (InvesAndQueryAndFvrParallelFlowTableDTO) itemId;

				final TextField txtRemarksField = new TextField();
				txtRemarksField.setHeight("30px");
				txtRemarksField
						.addValueChangeListener(new Property.ValueChangeListener() {

							@Override
							public void valueChange(ValueChangeEvent event) {

								String value = (String) event.getProperty()
										.getValue();
								if (null != value
										&& !("").equalsIgnoreCase(value)) {

									invesFvrQueryDTO.setCancelRemarks(value);
								}
							}
						});
				txtRemarksField.setData(invesFvrQueryDTO);
				cancelRemarksListener(txtRemarksField, null);
				
				return txtRemarksField;

			}
		});

	}

	public List<InvesAndQueryAndFvrParallelFlowTableDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<InvesAndQueryAndFvrParallelFlowTableDTO> itemIds = (List<InvesAndQueryAndFvrParallelFlowTableDTO>) this.table
				.getItemIds();
		return itemIds;
	}

	public void setPendingFvrInvsQueryPageObj(
			PendingFvrInvsQueryPageUI pendingFvrInvsQueryPageObj) {
		this.pendingFvrInvsQueryPageObj = pendingFvrInvsQueryPageObj;
	}
	
	public  void cancelRemarksListener(TextField searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "cancelremarks", ShortcutAction.KeyCode.F8, null) {
		
	      private static final long serialVersionUID = 1L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    handleShortcutForCancelRemarks(searchField, getShortCutListenerForCancelRemarks(searchField));
	    
	  }
	
	
	private ShortcutListener getShortCutListenerForCancelRemarks(final TextField txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();
				final Window dialog = new Window();
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				InvesAndQueryAndFvrParallelFlowTableDTO mainDto = (InvesAndQueryAndFvrParallelFlowTableDTO)txtFld.getData();
				txtArea.setData(mainDto);
				//txtArea.setStyleName("Boldstyle");
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				//txtArea.setSizeFull();
				txtArea.setRows(25);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
				dialog.setHeight("75%");
			    dialog.setWidth("65%");
			    txtArea.setReadOnly(false);
				
				
			    txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
							txtFld.setValue(((TextArea)event.getProperty()).getValue());						
							InvesAndQueryAndFvrParallelFlowTableDTO mainDto = (InvesAndQueryAndFvrParallelFlowTableDTO)txtFld.getData();
							mainDto.setCancelRemarks(txtFld.getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				
				String strCaption = "";
			   				    	
				dialog.setCaption(strCaption);
						
				
				dialog.setClosable(true);
				
				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);
				
				dialog.addCloseListener(new Window.CloseListener() {
					
					@Override
					public void windowClose(CloseEvent e) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});
				
				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(250);
					dialog.setPositionY(100);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});	
			}
		};
		
		return listener;
	}

	public  void handleShortcutForCancelRemarks(final TextField textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {
			
			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);
				
			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				textField.removeShortcutListener(shortcutListener);

			}
		});
	}
	public Investigation getInvestigationDetails(Map<String, Object> workFlowObj){
		if(null != workFlowObj){
			Long investigationKey = (Long) workFlowObj.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY);
			if (investigationKey != null) {
				Investigation investigation = reimbursementService.getByInvestigationKey(investigationKey);
				return investigation;
			}
		}
		return null;
	}
}
