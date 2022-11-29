package com.shaic.claim.intimation.search;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryRequest;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Intimation;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PolicyService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.BaseTheme;

public class SearchIntimationDetailTable extends GBaseTable<NewIntimationDto>{
	
	@Inject
	private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
	@EJB
	private CreateRODService billDetailsService;
	
	@Inject
	private ViewClaimHistoryRequest viewClaimHistoryRequest;

	
	private final static Object COLUM_HEADER_DRAFT_INTIMATION[] = new Object[] {
		"Action","policy.policyNumber", "insuredPatient.healthCardNumber",
			"insuredPatientName", "intimaterName", "hospitalDto.name",
			"modeOfIntimation.value", "intimatedBy.value",
			"status.processValue", "callerContactNum" };
	
	private final static Object COLUM_HEADER_SUBMIT_INTIMATION[] = new Object[] {
		"Action",/*"viewDocument","viewTrails",*/"intimationId", "policy.policyNumber","claimNumber",
			"insuredPatient.healthCardNumber", "insuredPatientName",
			"intimaterName", "hospitalDto.name",
			"modeOfIntimation.value", "intimatedBy.value",
			"status.processValue", "callerContactNum" };
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private ClaimService claimService;
	
	@Inject
	private ViewDetails viewDetails;
	
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {

		BeanItemContainer<NewIntimationDto> newIntimationDtoContainer = new BeanItemContainer<NewIntimationDto>(NewIntimationDto.class);
		
newIntimationDtoContainer
		.addNestedContainerProperty("policy.policyNumber");
newIntimationDtoContainer
		.addNestedContainerProperty("insuredPatient.healthCardNumber");
newIntimationDtoContainer
		.addNestedContainerProperty("insuredPatientName");
newIntimationDtoContainer
		.addNestedContainerProperty("intimaterName");
newIntimationDtoContainer
		.addNestedContainerProperty("hospitalDto.name");
newIntimationDtoContainer
		.addNestedContainerProperty("modeOfIntimation.value");
newIntimationDtoContainer
		.addNestedContainerProperty("intimatedBy.value");
newIntimationDtoContainer
		.addNestedContainerProperty("callerContactNum");
newIntimationDtoContainer
		.addNestedContainerProperty("status.processValue");
	
	table.removeGeneratedColumn("Intimation Date");
	table.addGeneratedColumn("Intimation Date",
		new Table.ColumnGenerator() {
			@Override
			public Object generateCell(Table source,
					final Object itemId, Object columnId) {
				if(((NewIntimationDto)itemId).getCreatedDate() != null){
			        return  new SimpleDateFormat("dd/MM/yyyy").format(((NewIntimationDto)itemId).getCreatedDate()); 
			}else{
			                return "";
			}						 
			}
			});
	
	table.removeGeneratedColumn("Action");
	table.addGeneratedColumn("Action",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(final Table source,
						final Object itemId, Object columnId) {

					final Button viewIntimationDetailsButton = new Button();
					viewIntimationDetailsButton.setData(itemId);
					Long intimationKey = ((NewIntimationDto) itemId)
							.getKey();

					Intimation a_intimation = intimationService
							.getIntimationByKey(intimationKey);
					Claim a_claim = claimService
							.getClaimsByIntimationNumber(a_intimation.getIntimationId());

					if (a_claim != null
							&& a_claim.getClaimId() != null)

					{
						viewIntimationDetailsButton
								.setCaption("View Claim Status");
					} else {
						viewIntimationDetailsButton
								.setCaption("View Intimation Details");

					}
					viewIntimationDetailsButton
							.addClickListener(new Button.ClickListener() {
								public void buttonClick(
										ClickEvent event) {

									NewIntimationDto newIntimationDto = (NewIntimationDto) itemId;
									Intimation a_intimation = intimationService
											.getIntimationByKey(newIntimationDto
													.getKey());
									Claim a_claim = claimService
											.getClaimsByIntimationNumber(a_intimation.getIntimationId());
									
									if (a_claim != null) {
											viewDetails.viewClaimStatusUpdated(a_intimation.getIntimationId());
									}
									else if (a_claim == null) {
										viewDetails.getViewGalaxyIntimation(a_intimation.getIntimationId());
									}
								}
							});
					viewIntimationDetailsButton
							.addStyleName(BaseTheme.BUTTON_LINK);
					return viewIntimationDetailsButton;
				}
			});
	
/*	table.removeGeneratedColumn("viewDocument");
	table.addGeneratedColumn("viewDocument",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(final Table source,
						final Object itemId, Object columnId) {

					final Button viewIntimationDetailsButton = new Button("View Document Details");
					viewIntimationDetailsButton.setData(itemId);
					final Long intimationKey = ((NewIntimationDto) itemId)
							.getKey();

					Claim a_claim = claimService
							.getClaimforIntimation(intimationKey);

					viewIntimationDetailsButton
							.addClickListener(new Button.ClickListener() {
								public void buttonClick(
										ClickEvent event) {
									Intimation intimation = intimationService.getIntimationByKey(intimationKey);
									viewUploadedDocumentDetails(intimation.getIntimationId());
								}
							});
					viewIntimationDetailsButton
							.addStyleName(BaseTheme.BUTTON_LINK);
					return viewIntimationDetailsButton;
				}
			});
	
	table.removeGeneratedColumn("view Trails");
	table.addGeneratedColumn("viewTrails",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(final Table source,
						final Object itemId, Object columnId) {

					final Button viewIntimationDetailsButton = new Button("View Trails");
					viewIntimationDetailsButton.setData(itemId);
					final Long intimationKey = ((NewIntimationDto) itemId)
							.getKey();

					Claim a_claim = claimService
							.getClaimforIntimation(intimationKey);

					viewIntimationDetailsButton
							.addClickListener(new Button.ClickListener() {
								public void buttonClick(
										ClickEvent event) {
									Intimation intimation = intimationService.getIntimationByKey(intimationKey);
									getViewClaimHistory(intimation.getIntimationId());
								}
							});
					viewIntimationDetailsButton
							.addStyleName(BaseTheme.BUTTON_LINK);
					return viewIntimationDetailsButton;
				}
			});*/
	
	    table.setColumnHeader("Action", "Action");
	    table.setColumnHeader("viewDocument", "View Document");
		
		table.setContainerDataSource(newIntimationDtoContainer);
//		table.setVisibleColumns(COLUM_HEADER_DRAFT_INTIMATION);
		table.setSizeFull();
		
	}
	
	public void setDraftTableHeader(){
		table.setVisibleColumns(COLUM_HEADER_DRAFT_INTIMATION);
		table.removeGeneratedColumn("Action");
		table.removeGeneratedColumn("viewDocument");
		table.removeGeneratedColumn("viewTrails");
	}

	public void setSubmitTableHeader(){
		table.setVisibleColumns(COLUM_HEADER_SUBMIT_INTIMATION);
		table.setColumnHeader("Action", "Action");
		 table.setColumnHeader("viewDocument", "View Document");
		 table.setColumnHeader("viewTrails","View Trails");
	}
	
	@Override
	public void tableSelectHandler(NewIntimationDto t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "searchIntimation-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

	public void addClaimNumberColum(){
		table.removeGeneratedColumn("claimNumber");
		table.addGeneratedColumn("claimNumber",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
						String claimNum = "";

						Claim a_claim = claimService
								.getClaimforIntimation(((NewIntimationDto) itemId)
										.getKey());

						if (a_claim != null && a_claim.getClaimId() != null) {
							claimNum = a_claim.getClaimId();
						}
						Label claimNumber = new Label(claimNum);
						// source.getContainerDataSource().addItem(claimNumber);
						return claimNumber;
					}
				});
	}
	
	public void addEditColumn(){
		table.removeGeneratedColumn("Edit Intimation");
		table.addGeneratedColumn("Edit Intimation",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
						final Button viewIntimationDetailsButton = new Button(
								"View / Edit Intimation");

						viewIntimationDetailsButton.setData(itemId);
						viewIntimationDetailsButton
								.addClickListener(new Button.ClickListener() {
									public void buttonClick(
											ClickEvent event) {
										NewIntimationDto newIntimationDto = (NewIntimationDto) event
												.getButton()
												.getData();
										// fireViewEvent(MenuItemBean.EDIT_INTIMATION,
										// newIntimationDto);
										fireViewEvent(
												MenuItemBean.REVISED_EDIT_INTIMATION,
												newIntimationDto);

									}
								});
						viewIntimationDetailsButton
								.addStyleName(BaseTheme.BUTTON_LINK);
						return viewIntimationDetailsButton;
					}
				});

	}
	
	public void addViewIntimationDetailsColumn(){
		table.removeGeneratedColumn("Action");
		table.addGeneratedColumn("Action",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {

						final Button viewIntimationDetailsButton = new Button();
						viewIntimationDetailsButton.setData(itemId);
						Long intimationKey = ((NewIntimationDto) itemId)
								.getKey();

						Intimation a_intimation = intimationService
								.getIntimationByKey(intimationKey);
						Claim a_claim = claimService
								.getClaimsByIntimationNumber(a_intimation.getIntimationId());

						if (a_claim != null
								&& a_claim.getClaimId() != null)

						{
							viewIntimationDetailsButton
									.setCaption("View Claim Status");
						} else {
							viewIntimationDetailsButton
									.setCaption("View Intimation Details");

						}
						viewIntimationDetailsButton
								.addClickListener(new Button.ClickListener() {
									public void buttonClick(
											ClickEvent event) {

										NewIntimationDto newIntimationDto = (NewIntimationDto) itemId;
										Intimation a_intimation = intimationService
												.getIntimationByKey(newIntimationDto
														.getKey());
										Claim a_claim = claimService
												.getClaimsByIntimationNumber(a_intimation.getIntimationId());
										
										if (a_claim != null) {
												viewDetails.viewClaimStatusUpdated(a_intimation.getIntimationId());
										}
										else if (a_claim == null) {
											viewDetails.getViewIntimation(a_intimation.getIntimationId());
										}
									}
								});
						viewIntimationDetailsButton
								.addStyleName(BaseTheme.BUTTON_LINK);
						return viewIntimationDetailsButton;
					}
				});
	}
	
	public void viewUploadedDocumentDetails(String intimationNo) {
		
		BPMClientContext bpmClientContext = new BPMClientContext();
		Map<String,String> tokenInputs = new HashMap<String, String>();
		 tokenInputs.put("intimationNo", intimationNo);
		 String intimationNoToken = null;
		  try {
			  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
		  } catch (NoSuchAlgorithmException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  } catch (ParseException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		  tokenInputs = null;
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
		/*Below code commented due to security code
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNo;*/
	//	getUI().getPage().open(url, "_blank");
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);

	/*	DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		if(claim != null){
		dmsDTO.setClaimNo(claim.getClaimId());
		}
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = billDetailsService
				.getDocumentDetailsData(intimationNo);
		if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
			dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO); 
		}

		popup = new com.vaadin.ui.Window();

		dmsDocumentDetailsViewPage.init(dmsDTO, popup);
		dmsDocumentDetailsViewPage.getContent();
		

		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		//popup.setSizeFull();
		popup.setContent(dmsDocumentDetailsViewPage);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
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
		UI.getCurrent().addWindow(popup);*/

	}
	
	
	public void getViewClaimHistory(String intimationNo) {
		
		if(intimationNo != null){
		Intimation intimation = intimationService
				.getIntimationByNo(intimationNo);
		
		Boolean result = true;
		
		if (intimation != null) {

		result = viewClaimHistoryRequest.showCashlessAndReimbursementHistory(intimation);
			
			
			if(result){
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
		
		}else{
			getErrorMessage("History is not available");
		}
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

}
