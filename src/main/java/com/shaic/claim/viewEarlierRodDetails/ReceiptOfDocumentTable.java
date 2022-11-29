package com.shaic.claim.viewEarlierRodDetails;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryRequest;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewMedicalSummaryPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPAMedicalSummaryPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewRejectionDetailsPage;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.domain.ReimbursementRejectionService;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ReceiptOfDocumentTable extends GBaseTable<ViewDocumentDetailsDTO> {
	
private static final long serialVersionUID = 1L;
	
	////private static Window popup;
	
	private ClaimDto claimDto;
	
	@EJB
	private AcknowledgementDocumentsReceivedService acknowledgmentService;
	
	@Inject
	private Instance<ViewRejectionDetailsPage> rejectionDetailPageInstance;
	
	private ViewRejectionDetailsPage rejectionDetailPage;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private HospitalService hospitalService;
	
	
	
	@Inject
	private ViewQueryDetailsTable queryDetailsTableObj;
	
	@Inject
	private ViewAcknowledgementTable viewAcknowledgementTableObj;
	
	@EJB
	private ReimbursementRejectionService reimbursementRejectionService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	@Inject
	private ViewClaimHistoryRequest viewClaimHistoryRequest;
	
	@Inject

	private ViewClosureRODLevel viewClosureRODLevel;

	@Inject
	private ViewPAMedicalSummaryPage viewPAMedicalSummaryPage;
	
	@Inject
	private ViewMedicalSummaryPage viewMedicalSummaryPage;

	
	@EJB
	private ReimbursementService rodService;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"sno","acknowledgeNumber","rodNumber","receivedFromValue","documentReceivedDate","strLastDocumentReceivedDate","medicalResponseTime","modeOfReceipt"
		,"billClassification","approvedAmount","status"};
	*/
	/*public static final Object[] NATURAL_COL_ORDER_PA = new Object[] {"sno","rodNumber","typeOfClaim","benefits","receivedFromValue","rodType","documentReceivedDate","strLastDocumentReceivedDate","medicalResponseTime","modeOfReceipt"
		,"approvedAmount","status"};*/

	@Override
	public void removeRow() {
		
		// TODO Auto-generated method stub
		table.removeAllItems();
	}
	
	
	public void setPAColumnsForROD()
	{
		Object[] NATURAL_COL_ORDER_PA = new Object[] {"sno","crmFlagged","rodNumber","typeOfClaim","benefits","receivedFromValue","rodType","documentReceivedDate","strLastDocumentReceivedDate","medicalResponseTime","modeOfReceipt"
			,"approvedAmount","status"};
		table.setVisibleColumns(NATURAL_COL_ORDER_PA);
		table.setColumnHeader("benefits", "Benefit/Cover");
		table.setColumnHeader("typeOfClaim", "Claim Type");
		table.setColumnHeader("rodType", "ROD Type");
		
		table.removeGeneratedColumn("viewTrails");
		table.addGeneratedColumn("viewTrails", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 

//					ViewDocumentDetailsDTO documentDetails = (ViewDocumentDetailsDTO) itemId;
//					if(null != documentDetails.getStatus()){
//						
//						if(documentDetails.getStatus())
						
					Button button = new Button("View Trails");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							ViewDocumentDetailsDTO financialDto = (ViewDocumentDetailsDTO)itemId;
							
							Intimation intimation = financialDto.getClaim().getIntimation();
							
							Long rodKey = financialDto.getReimbursementKey();
							
								if (intimation != null) {

									viewClaimHistoryRequest.showReimbursementClaimHistory(intimation.getKey(),rodKey);
								   
									Window popup = new com.vaadin.ui.Window();
									popup.setCaption("View History");
									popup.setWidth("75%");
									popup.setHeight("75%");
									popup.setContent(viewClaimHistoryRequest);
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
								}else{
									getErrorMessage("History is not available");
								}
								
				        }

					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("150px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
					
					
		      }
		});
		
		table.setColumnHeader("viewTrails", "View Trails");
		
		
		table.removeGeneratedColumn("viewAcknowledgement");
		table.addGeneratedColumn("viewAcknowledgement", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 

//					ViewDocumentDetailsDTO documentDetails = (ViewDocumentDetailsDTO) itemId;
//					if(null != documentDetails.getStatus()){
//						
//						if(documentDetails.getStatus())
						
					Button button = new Button("View Acknowledgement");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							ViewDocumentDetailsDTO financialDto = (ViewDocumentDetailsDTO)itemId;
							
							Intimation intimation = financialDto.getClaim().getIntimation();
							
							Long rodKey = financialDto.getReimbursementKey();
							
								if (intimation != null) {

									List<ViewDocumentDetailsDTO> viewAcknowledgementList = acknowledgmentService.getViewAcknowledgementList(rodKey);
									
									viewAcknowledgementTableObj.init(financialDto.getRodNumber(), false, false);
									
									viewAcknowledgementTableObj.setPAColumnsForACK();
									
									viewAcknowledgementTableObj.setTableList(viewAcknowledgementList);
								   
									Window popup = new com.vaadin.ui.Window();
									popup.setCaption("Acknowledgment Details");
									popup.setWidth("75%");
									popup.setHeight("75%");
									popup.setContent(viewAcknowledgementTableObj);
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
								}else{
									getErrorMessage("No Acknowledgement");
								}
								
				        }

					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("150px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
					
					
		      }
		});
		
		table.setColumnHeader("viewAcknowledgement", "View Acknowledgement");

		table.removeGeneratedColumn("viewClaimStatus");
		table.addGeneratedColumn("viewClaimStatus",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						Button button = new Button("View Rejection Details");
						button.addClickListener(new Button.ClickListener() {
//
							@Override
							public void buttonClick(ClickEvent event) {
								
								Window popup = new com.vaadin.ui.Window();
								
								
								popup.setCaption("View Rejection Details");
								popup.setWidth("75%");
								popup.setHeight("85%");
								ViewDocumentDetailsDTO tableDto = (ViewDocumentDetailsDTO)itemId;
								DocAcknowledgement docAcknowledgment = reimbursementService.getDocAcknowledgment(tableDto.getKey());
								ReimbursementRejection rejectionKeyByReimbursement = null;
								Long reimbursementKey = docAcknowledgment.getRodKey();
								if(reimbursementKey != null){
									rejectionKeyByReimbursement = reimbursementRejectionService.getRejectionKeyByReimbursement(reimbursementKey);
								}
								
								Long rejectionKey = null;
								
								if(rejectionKeyByReimbursement != null){
									rejectionKey = rejectionKeyByReimbursement.getKey();
								}
								
								rejectionDetailPage = rejectionDetailPageInstance.get();
								rejectionDetailPage.init(rejectionKey);
								popup.setContent(rejectionDetailPage);
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
//										System.out.println("Close listener called");
									}
								});

								popup.setModal(true);
								UI.getCurrent().addWindow(popup);
//
//
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		table.removeGeneratedColumn("viewDocument");
		table.addGeneratedColumn("viewDocument",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
//						ViewDocumentDetailsDTO documentDetails = (ViewDocumentDetailsDTO) itemId;
//						if(null != documentDetails.getStatus()){
//							
//							if(documentDetails.getStatus())
							
						Button button = new Button("View Query Details");
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								Window popup = new com.vaadin.ui.Window();
								popup.setCaption("");
								popup.setWidth("75%");
								popup.setHeight("85%");
								ViewDocumentDetailsDTO documentDetailsDto = (ViewDocumentDetailsDTO)itemId;
								queryDetailsTableObj.init("View Query Details", false, false);
								
								queryDetailsTableObj.setViewPAQueryDetialsColumn();
								
								if(documentDetailsDto.getRodKey() != null){
									Long rodKey = new Long(documentDetailsDto.getRodKey());
									setQueryValues(rodKey,documentDetailsDto.getClaim());
								}
								popup.setContent(queryDetailsTableObj);
								popup.setClosable(true);
								popup.center();
								popup.setResizable(true);
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
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
						
						}
//					}
				});
		
		
		table.removeGeneratedColumn("viewMedicalSummary");
		table.addGeneratedColumn("viewMedicalSummary", new Table.ColumnGenerator() {
			
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 

//					ViewDocumentDetailsDTO documentDetails = (ViewDocumentDetailsDTO) itemId;
//					if(null != documentDetails.getStatus()){
//						
//						if(documentDetails.getStatus())
						
					Button button = new Button("View Medical Summary");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
						
						
						ViewDocumentDetailsDTO financialDto = (ViewDocumentDetailsDTO)itemId;
							
							Long rodKey = financialDto.getReimbursementKey();
							
									if (rodKey != null) {
													
											viewPAMedicalSummaryPage.init(rodKey);
									
											Window popup = new com.vaadin.ui.Window();
											popup.setWidth("75%");
											popup.setHeight("90%");
											popup.setContent(viewPAMedicalSummaryPage);
											popup.setClosable(true);
											popup.center();
											popup.setResizable(false);
											popup.addCloseListener(new Window.CloseListener() {
												
												private static final long serialVersionUID = 1L;
												
													@Override
													public void windowClose(CloseEvent e) {
														System.out.println("Close listener called");
													}
											});
							
											popup.setModal(true);
											UI.getCurrent().addWindow(popup);
									} else {
										getErrorMessage("Medical Summary Details are not available");
									}
						}

					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("150px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
					
					
		      }
		});
		
		
		table.removeGeneratedColumn("documentReceivedDate");
		table.addGeneratedColumn("documentReceivedDate", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 
		    	  ViewDocumentDetailsDTO documentDTO = (ViewDocumentDetailsDTO)itemId;
		    	  
		    	  String formatDate = SHAUtils.formatDate(documentDTO.getDocumentReceivedDate());
		    	  return formatDate;
		      }
		});

	
	}
	

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<ViewDocumentDetailsDTO>(
				ViewDocumentDetailsDTO.class));
	 Object[] NATURAL_COL_ORDER = new Object[] {"sno","crmFlagged",/*,"acknowledgeNumber",*/"rodNumber","typeOfClaim","billClassification","receivedFromValue","rodType","documentReceivedDate","strLastDocumentReceivedDate","medicalResponseTime","modeOfReceipt"
			 ,"totalBillAmount","approvedAmount","status"};
		
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
		
		table.removeGeneratedColumn("grievanceRepresentation");
		table.addGeneratedColumn("grievanceRepresentation", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	  ViewDocumentDetailsDTO financialDto = (ViewDocumentDetailsDTO)itemId;
		    	  CheckBox grievance = new CheckBox();
		    	  if(financialDto.getGrievanceRepresentationFlag() !=null){
		    		  Boolean grievanceRep = financialDto.getGrievanceRepresentationFlag().equals("Y") ? true : false;
		    		  grievance.setValue(grievanceRep);
		    	  }
		    	  grievance.setReadOnly(true);
		    	  return grievance;
		      }
		});
		table.setColumnHeader("grievanceRepresentation", "Grievance Representation");
		table.setColumnAlignment("grievanceRepresentation", Align.CENTER);
		
		table.removeGeneratedColumn("viewTrails");
		table.addGeneratedColumn("viewTrails", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 

//					ViewDocumentDetailsDTO documentDetails = (ViewDocumentDetailsDTO) itemId;
//					if(null != documentDetails.getStatus()){
//						
//						if(documentDetails.getStatus())
						
					Button button = new Button("View Trails");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							ViewDocumentDetailsDTO financialDto = (ViewDocumentDetailsDTO)itemId;
							
							Intimation intimation = financialDto.getClaim().getIntimation();
							
							Long rodKey = financialDto.getReimbursementKey();
							
								if (intimation != null) {

									viewClaimHistoryRequest.showReimbursementClaimHistory(intimation.getKey(),rodKey);
								   
									Window popup = new com.vaadin.ui.Window();
									popup.setCaption("View History");
									popup.setWidth("75%");
									popup.setHeight("75%");
									popup.setContent(viewClaimHistoryRequest);
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
								}else{
									getErrorMessage("History is not available");
								}
								
				        }

					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("150px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
					
					
		      }
		});
		
		table.setColumnHeader("viewTrails", "View Trails");
		
		
		table.removeGeneratedColumn("viewAcknowledgement");
		table.addGeneratedColumn("viewAcknowledgement", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 

//					ViewDocumentDetailsDTO documentDetails = (ViewDocumentDetailsDTO) itemId;
//					if(null != documentDetails.getStatus()){
//						
//						if(documentDetails.getStatus())
						
					Button button = new Button("View Acknowledgement");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							ViewDocumentDetailsDTO financialDto = (ViewDocumentDetailsDTO)itemId;
							
							Intimation intimation = financialDto.getClaim().getIntimation();
							
							Long rodKey = financialDto.getReimbursementKey();
							
								if (intimation != null) {

									List<ViewDocumentDetailsDTO> viewAcknowledgementList = acknowledgmentService.getViewAcknowledgementList(rodKey);
									
									viewAcknowledgementTableObj.init(financialDto.getRodNumber(), false, false);
									viewAcknowledgementTableObj.setTableList(viewAcknowledgementList);
								   
									Window popup = new com.vaadin.ui.Window();
									popup.setCaption("Acknowledgment Details");
									popup.setWidth("75%");
									popup.setHeight("75%");
									popup.setContent(viewAcknowledgementTableObj);
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
								}else{
									getErrorMessage("No Acknowledgement");
								}
								
				        }

					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("150px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
					
					
		      }
		});
		
		table.setColumnHeader("viewAcknowledgement", "View Acknowledgement");

		table.removeGeneratedColumn("viewClaimStatus");
		table.addGeneratedColumn("viewClaimStatus",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						Button button = new Button("View Rejection Details");
						button.addClickListener(new Button.ClickListener() {
//
							@Override
							public void buttonClick(ClickEvent event) {
								
								Window popup = new com.vaadin.ui.Window();
								
								
								popup.setCaption("View Rejection Details");
								popup.setWidth("75%");
								popup.setHeight("85%");
								ViewDocumentDetailsDTO tableDto = (ViewDocumentDetailsDTO)itemId;
								DocAcknowledgement docAcknowledgment = reimbursementService.getDocAcknowledgment(tableDto.getKey());
								ReimbursementRejection rejectionKeyByReimbursement = null;
								Long reimbursementKey = docAcknowledgment.getRodKey();
								if(reimbursementKey != null){
									rejectionKeyByReimbursement = reimbursementRejectionService.getRejectionKeyByReimbursement(reimbursementKey);
								}
								
								Long rejectionKey = null;
								
								if(rejectionKeyByReimbursement != null){
									rejectionKey = rejectionKeyByReimbursement.getKey();
								}
								
								rejectionDetailPage = rejectionDetailPageInstance.get();
								rejectionDetailPage.init(rejectionKey);
								popup.setContent(rejectionDetailPage);
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
//										System.out.println("Close listener called");
									}
								});

								popup.setModal(true);
								UI.getCurrent().addWindow(popup);
//
//
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		table.removeGeneratedColumn("viewDocument");
		table.addGeneratedColumn("viewDocument",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
//						ViewDocumentDetailsDTO documentDetails = (ViewDocumentDetailsDTO) itemId;
//						if(null != documentDetails.getStatus()){
//							
//							if(documentDetails.getStatus())
							
						Button button = new Button("View Query Details");
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								Window popup = new com.vaadin.ui.Window();
								popup.setCaption("");
								popup.setWidth("75%");
								popup.setHeight("85%");
								ViewDocumentDetailsDTO documentDetailsDto = (ViewDocumentDetailsDTO)itemId;
								queryDetailsTableObj.init("View Query Details", false, false);
								
								if(documentDetailsDto.getRodKey() != null){
									Long rodKey = new Long(documentDetailsDto.getRodKey());
									setQueryValues(rodKey,documentDetailsDto.getClaim());
								}
								popup.setContent(queryDetailsTableObj);
								popup.setClosable(true);
								popup.center();
								popup.setResizable(true);
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
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
						
						}
//					}
				});
		
		
		
		table.removeGeneratedColumn("viewMedicalSummary");
		table.addGeneratedColumn("viewMedicalSummary", new Table.ColumnGenerator() {
			
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 

//					ViewDocumentDetailsDTO documentDetails = (ViewDocumentDetailsDTO) itemId;
//					if(null != documentDetails.getStatus()){
//						
//						if(documentDetails.getStatus())
						
					Button button = new Button("View Medical Summary");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
						
						
						ViewDocumentDetailsDTO financialDto = (ViewDocumentDetailsDTO)itemId;
							
							Long rodKey = financialDto.getReimbursementKey();
							
									if (rodKey != null) {
													
										viewMedicalSummaryPage.init(rodKey);
									
											Window popup = new com.vaadin.ui.Window();
											popup.setWidth("75%");
											popup.setHeight("90%");
											popup.setContent(viewMedicalSummaryPage);
											popup.setClosable(true);
											popup.center();
											popup.setResizable(true);
											popup.addCloseListener(new Window.CloseListener() {
												
												private static final long serialVersionUID = 1L;
												
													@Override
													public void windowClose(CloseEvent e) {
														System.out.println("Close listener called");
													}
											});
							
											popup.setModal(true);
											UI.getCurrent().addWindow(popup);
									} else {
										getErrorMessage("Medical Summary Details are not available");
									}
						}

					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("150px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
					
					
		      }
		});
		
		table.removeGeneratedColumn("documentReceivedDate");
		table.addGeneratedColumn("documentReceivedDate", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 
		    	  ViewDocumentDetailsDTO documentDTO = (ViewDocumentDetailsDTO)itemId;
		    	  
		    	  String formatDate = SHAUtils.formatDate(documentDTO.getDocumentReceivedDate());
		    	  return formatDate;
		      }
		});
		
		
		table.removeGeneratedColumn("viewClosure");
		table.addGeneratedColumn("viewClosure", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 

//					ViewDocumentDetailsDTO documentDetails = (ViewDocumentDetailsDTO) itemId;
//					if(null != documentDetails.getStatus()){
//						
//						if(documentDetails.getStatus())
						
					Button button = new Button("View Closure Details(ROD Level)");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							ViewDocumentDetailsDTO financialDto = (ViewDocumentDetailsDTO)itemId;
							
							Intimation intimation = financialDto.getClaim().getIntimation();
							Long rodKey = financialDto.getReimbursementKey();
							
								if (intimation != null) {

									viewClosureRODLevel.bindFieldGroup(intimation, rodKey);
									
									Window popup = new com.vaadin.ui.Window();
									popup.setCaption("Closure Details(ROD Level)");
									popup.setWidth("75%");
									popup.setHeight("75%");
									popup.setContent(viewClosureRODLevel);
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
								}else{
									getErrorMessage("No Closure");
								}
								
				        }

					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("250px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
					
					
		      }
		});
		
	}
	


	public void setClaimDto(ClaimDto claimDto){
		this.claimDto = claimDto;
	}
	
	public void setQueryValues(Long key,Claim claim){
		
		List<ViewQueryDTO> QuerytableValues = acknowledgmentService.getQueryDetails(key);
		Hospitals hospitalDetails=null;    
		
		String diagnosisName = acknowledgmentService.getDiagnosisName(key);
		
		//need to implement
		if(claim != null){
		Long hospitalKey = claim.getIntimation().getHospital();
		hospitalDetails = hospitalService.getHospitalById(hospitalKey);
		}	
		for (ViewQueryDTO viewQueryDTO : QuerytableValues) {
			viewQueryDTO.setDiagnosis(diagnosisName);
			if(hospitalDetails != null){
				viewQueryDTO.setHospitalName(hospitalDetails.getName());
				viewQueryDTO.setHospitalCity(hospitalDetails.getCity());
			}
			viewQueryDTO.setClaim(claim);
			queryDetailsTableObj.addBeanToList(viewQueryDTO);
		}		
	}

	@Override
	public void tableSelectHandler(ViewDocumentDetailsDTO t) {
		
		
	}
	
	public void generateAuditDetails(){
		
		table.removeGeneratedColumn("auditDetails");
		table.addGeneratedColumn("auditDetails", new Table.ColumnGenerator() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	Button button = new Button("Audit Details");
		    	button.addClickListener(new Button.ClickListener() {
			        /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						ViewDocumentDetailsDTO financialDto = (ViewDocumentDetailsDTO)itemId;
						
						Intimation intimation = financialDto.getClaim().getIntimation();
						
						Long rodKey = financialDto.getReimbursementKey();
						
							if (intimation != null) {

								viewClaimHistoryRequest.showReimbursementClaimHistory(intimation.getKey(),rodKey);
							   
								Window popup = new com.vaadin.ui.Window();
								popup.setCaption("View History");
								popup.setWidth("75%");
								popup.setHeight("75%");
								popup.setContent(viewClaimHistoryRequest);
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
							}else{
								getErrorMessage("History is not available");
							}
							
			        } 
			    });
		    	
		    	button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
		    	return button;
		      }
		});
		
		table.setColumnWidth("auditDetails",180);
		table.setColumnHeader("auditDetails", "Audit Details");
		
	}
	
	public void getErrorMessage(String eMsg) {

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



	@Override
	public String textBundlePrefixString() {
		
		return "document-view-details-";
	}


}
