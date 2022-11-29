package com.shaic.claims.reibursement.addaditionaldocuments;

/*
 import java.util.ArrayList;
 import java.util.List;

 import javax.ejb.EJB;
 import javax.inject.Inject;

 import com.shaic.arch.table.GBaseTable;
 import com.shaic.claim.ViewDetails;
 import com.shaic.domain.Reimbursement;
 import com.shaic.domain.reimbursement.ReimbursementService;
 import com.shaic.reimbursement.acknowledgeinvestigationcompleted.InvestigationDetailsReimbursementTableDTO;
 import com.vaadin.v7.data.util.BeanItemContainer;
 import com.vaadin.ui.Button;
 import com.vaadin.v7.ui.OptionGroup;
 import com.vaadin.v7.ui.Table;
 import com.vaadin.ui.UI;
 import com.vaadin.ui.Window;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Window.CloseEvent;
 import com.vaadin.ui.themes.ValoTheme;

 public class SelectRODtoAddAdditionalDocumentsTable extends
 GBaseTable<SelectRODtoAddAdditionalDocumentsDTO> {

 *//**
 * 
 */
/*

 private static final long serialVersionUID = 1L;

 private Long claimKey = 0l;

 @Inject
 private ViewDetails viewDetails;

 @EJB
 private ReimbursementService reimbursementService;

 public static final Object[] NATURAL_COL_ORDER = new Object[] { "sNo",
 "rodNo", "billClassification", "claimedAmt", "approvedAmt",
 "rodStatus" };

 @Override
 public void removeRow() {
 // TODO Auto-generated method stub

 }

 @Override
 public void initTable() {
 table.setContainerDataSource(new BeanItemContainer<SelectRODtoAddAdditionalDocumentsDTO>(
 SelectRODtoAddAdditionalDocumentsDTO.class));
 table.setVisibleColumns(NATURAL_COL_ORDER);
 table.setWidth("100%");
 table.setHeight("200px");

 ArrayList<Object> itemIds = new ArrayList<Object>(table.getItemIds());
 // final Object selectedRowId = getSelectedRowId(itemIds,
 // investigationKey);
 // System.out.print(";;;;;;;;;;;;;;;;;; selected id = " +
 // selectedRowId);

 try {
 table.removeGeneratedColumn("viewDetails");
 } catch (Exception e) {

 }
 try {
 table.addGeneratedColumn("viewDetails",
 new Table.ColumnGenerator() {

 private static final long serialVersionUID = 1L;

 @Override
 public Object generateCell(final Table source,
 final Object itemId, Object columnId) {
 Button button = new Button("View Claim Status");
 button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
 button.addStyleName(ValoTheme.BUTTON_LINK);
 button.addClickListener(new Button.ClickListener() {

 *//**
 * 
 */
/*

 private static final long serialVersionUID = 1L;

 @Override
 public void buttonClick(ClickEvent event) {
 SelectRODtoAddAdditionalDocumentsDTO selectRODtoAddAdditionalDocumentsDTO = (SelectRODtoAddAdditionalDocumentsDTO) itemId;
 if (selectRODtoAddAdditionalDocumentsDTO != null
 && selectRODtoAddAdditionalDocumentsDTO
 .getKey() != null) {
 Reimbursement reimbursement = reimbursementService
 .getReimbursementbyRod(selectRODtoAddAdditionalDocumentsDTO
 .getKey());
 if (reimbursement != null
 && reimbursement.getClaim() != null
 && reimbursement.getClaim()
 .getIntimation() != null
 && reimbursement.getClaim()
 .getIntimation()
 .getIntimationId() != null) {
 viewDetails
 .viewClaimStatusUpdated(reimbursement
 .getClaim()
 .getIntimation()
 .getIntimationId());
 }

 }

 }
 });
 return button;
 }
 });
 } catch (Exception e) {

 }

 try {
 table.removeGeneratedColumn("select");
 } catch (Exception e) {

 }
 try {
 table.addGeneratedColumn("select", new Table.ColumnGenerator() {

 private static final long serialVersionUID = 1L;

 @Override
 public Object generateCell(final Table source,
 final Object itemId, Object columnId) {
 OptionGroup radioButton = new OptionGroup();
 radioButton.addItem("");
 return radioButton;
 }
 });
 } catch (Exception e) {

 }
 }

 @Override
 public void tableSelectHandler(SelectRODtoAddAdditionalDocumentsDTO t) {
 // setRadioButtonr(t);
 }

 @Override
 public String textBundlePrefixString() {
 return "view-reimbursement-selectRODAdditionalDocuments-";
 }

 private void setRadioButtonr(Object itemId) {
 SelectRODtoAddAdditionalDocumentsDTO bean = (SelectRODtoAddAdditionalDocumentsDTO) itemId;
 ArrayList<Object> itemIds = new ArrayList<Object>(table.getItemIds());
 for (Object item : itemIds) {
 if (!item.equals(bean)) {
 try {
 table.removeGeneratedColumn("select");
 } catch (Exception e) {

 }
 try {
 table.addGeneratedColumn("select",
 new Table.ColumnGenerator() {

 private static final long serialVersionUID = 1L;

 @Override
 public Object generateCell(final Table source,
 final Object itemId, Object columnId) {
 OptionGroup radioButton = new OptionGroup();
 radioButton.addItem("");
 return radioButton;
 }
 });
 } catch (Exception e) {
 e.printStackTrace();
 }
 } else {
 try {
 table.removeGeneratedColumn("select");
 } catch (Exception e) {

 }
 try {
 table.addGeneratedColumn("select",
 new Table.ColumnGenerator() {

 private static final long serialVersionUID = 1L;

 @Override
 public Object generateCell(final Table source,
 final Object itemId, Object columnId) {
 OptionGroup radioButton = new OptionGroup();
 radioButton.addItem("");
 radioButton.select("");
 return radioButton;
 }
 });
 } catch (Exception e) {
 e.printStackTrace();
 }
 }
 }
 }

 public void setRadioButtonr(Long claimKey) {
 this.claimKey = claimKey;
 ArrayList<Object> itemIds = new ArrayList<Object>(table.getItemIds());

 final Object selectedRowId = getSelectedRowId(itemIds, claimKey);
 System.out.print(";;;;;;;;;;;;;;;;;; selected id = " + selectedRowId);
 try {
 table.removeGeneratedColumn("select");
 } catch (Exception e) {

 }
 try {
 table.addGeneratedColumn("select", new Table.ColumnGenerator() {

 private static final long serialVersionUID = 1L;

 @Override
 public Object generateCell(final Table source,
 final Object itemId, Object columnId) {
 SelectRODtoAddAdditionalDocumentsDTO selectRODtoAddAdditionalDocumentsDTO = (SelectRODtoAddAdditionalDocumentsDTO) selectedRowId;
 long key1 = selectRODtoAddAdditionalDocumentsDTO.getKey();
 selectRODtoAddAdditionalDocumentsDTO = (SelectRODtoAddAdditionalDocumentsDTO) itemId;
 long key2 = selectRODtoAddAdditionalDocumentsDTO.getKey();
 if (key1 == key2) {
 OptionGroup radioButton = new OptionGroup();
 radioButton.addItem("");
 radioButton.select("");
 return radioButton;
 } else {
 return null;
 }
 }
 });
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 public void setVisibleColumns() {

 table.setVisibleColumns(NATURAL_COL_ORDER);
 }

 private Object getSelectedRowId(ArrayList<Object> ids, Long key) {

 for (Object id : ids) {
 SelectRODtoAddAdditionalDocumentsDTO selectRODtoAddAdditionalDocumentsDTO = (SelectRODtoAddAdditionalDocumentsDTO) id;
 Long key1 = selectRODtoAddAdditionalDocumentsDTO.getKey();
 if (key1.equals(key)) {
 return id;
 }
 }

 return null;

 }

 }*/

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.hene.flexibleoptiongroup.FlexibleOptionGroup;
import org.vaadin.hene.flexibleoptiongroup.FlexibleOptionGroupItemComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PhysicalDocumentVerification;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.data.util.ObjectProperty;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.ColumnGenerator;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SelectRODtoAddAdditionalDocumentsTable extends ViewComponent {

	private static final long serialVersionUID = 1L;

	private static final String ROD_NO = "RODNO";
	private static final String SNO = "SNO";
	private static final String BILL_CLASSSIFICATION = "BILL_CLASSSIFICATION";
	private static final String CLAIMEDAMOUNT = "CLAIMEDAMOUNT";
	private static final String APPROVEDAMOUNT = "APPROVEDAMOUNT";
	private static final String RODSTATUS = "RODSTATUS";
	private static final String ACTION = "ACTION";
	private static final String VIEWDETAIL = "VIEWDETAIL";
	
	private String errorMessage;

	@Inject
	private ReceiptOfDocumentsDTO bean;

	@EJB
	private IntimationService intimationService;

	@Inject
	private DBCalculationService dbCalculationService;

	@Inject
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;

	@Inject
	private CreateRODService createRodService;

	@EJB
	private ClaimService claimService;
	
	private Boolean  isOptionSelected ;
	
	private String screenName;

	@EJB
	private AcknowledgementDocumentsReceivedService acknowledgementDocumentsReceivedService;

	// ROD No, S.NO, Bill Classsification, Claimed Amount, Approved Amount, ROD
	// Status, Action, View Detail

	private static final String ICON_PROPERTY = "icon";
	private static final String SELECTION_PROPERTY = "Select";

	/*public static final Object[] VISIBLE_COL_ORDER = new Object[] {
			"serialNumber", "id", "name", "salary" };*/

	@EJB
	private ReimbursementService reimbursementService;

	@Inject
	private ViewDetails viewDetails;

	/*private static final String[] DOCUMENTS = new String[] { "Word",
			"document-doc.png", "Image", "document-image.png", "PDF",
			"document-pdf.png", "PowerPoint", "document-ppt.png", "Text",
			"document-txt.png", "Web", "document-web.png", "Excel",
			"document-xsl.png" };*/

	public void initTable() {
		VerticalLayout mainLayout = new VerticalLayout();
		isOptionSelected = false;
		mainLayout.setSizeFull();
		mainLayout.setMargin(true);
		setCompositionRoot(mainLayout);
		mainLayout.addComponent(new TableExampleTab());
	}

	private final Container createTestContainer() {
		IndexedContainer cont = new IndexedContainer();
		cont.addContainerProperty(SNO, String.class, null);
		cont.addContainerProperty(ICON_PROPERTY, Resource.class, null);
		cont.addContainerProperty(ROD_NO, String.class, null);
		cont.addContainerProperty(BILL_CLASSSIFICATION, String.class, null);
		cont.addContainerProperty(CLAIMEDAMOUNT, String.class, null);
		cont.addContainerProperty(APPROVEDAMOUNT, String.class, null);
		cont.addContainerProperty(RODSTATUS, String.class, null);
		// cont.addContainerProperty(ACTION, String.class, null);
		// cont.addContainerProperty(VIEWDETAIL, String.class, null);

		Integer index = 1;

		for (SelectRODtoAddAdditionalDocumentsDTO list : this.bean
				.getSelectRODtoAddAdditionalDocumentsDTO()) {
			String rodNo = list.getRodNo().toString();
			Long id = list.getKey();
			String sNO = index.toString();
			String billClasssification = list.getBillClassification();
			String claimedAmount = list.getClaimedAmt();
			String approvedAmount = list.getApprovedAmt();
			String rodStatus = list.getRodStatus();
//			 this.screenName = list.getScreenName();
			// String action = DOCUMENTS[i];
			// String viewDetails = DOCUMENTS[i];
			Item item = cont.addItem(id);
			if(list.getStatusKey().equals(ReferenceTable.ACKNOWLEDGE_STATUS_KEY) || list.getStatusKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY)
					|| list.getStatusKey().equals(ReferenceTable.BILL_ENTRY_STATUS_KEY)){
			valuateTestContainerItem(rodNo, id, sNO, billClasssification,
					claimedAmount, null, rodStatus, item);
			}else{
				valuateTestContainerItem(rodNo, id, sNO, billClasssification,
						claimedAmount, approvedAmount, rodStatus, item);
			}
			index++;
		}
		return cont;
	}

	private Long getAckNoCountByClaimKey(Long claimKey, String presenterString) {
		Long count = 0l;
		if ((presenterString)
				.equalsIgnoreCase(ReferenceTable.ACKNOWLEDGE_DOC_RECEIVED)) {
			count = ackDocReceivedService.getCountOfAckByClaimKey(claimKey);
		} else if ((presenterString)
				.equalsIgnoreCase(ReferenceTable.CREATE_ROD)) {
			count = createRodService
					.getACknowledgeNumberCountByClaimKey(claimKey);
		}

		return count;
	}

	@SuppressWarnings("unchecked")
	private static void valuateTestContainerItem(String rodNo, Long id,
			String sNo, String billClasificaiton, String claimedAmount,
			String approvedAmount, String rodStatus, /* String action, */
			/* String viewDetails, */Item item) {
		item.getItemProperty(SNO).setValue(sNo);
		item.getItemProperty(ROD_NO).setValue(rodNo);
		item.getItemProperty(BILL_CLASSSIFICATION).setValue(billClasificaiton);
		item.getItemProperty(CLAIMEDAMOUNT).setValue(claimedAmount);
		item.getItemProperty(APPROVEDAMOUNT).setValue(approvedAmount);
		item.getItemProperty(RODSTATUS).setValue(rodStatus);
		// item.getItemProperty(ACTION).setValue(action);
		// item.getItemProperty(VIEWDETAIL).setValue("value");
		item.getItemProperty(ICON_PROPERTY).setValue(
				new ThemeResource("../runo/icons/16/" + id));
	}

	public static Label createCaptionLabel(FlexibleOptionGroupItemComponent fog) {
		Label captionLabel = new Label();
		captionLabel.setData(fog);
		captionLabel.setIcon(fog.getIcon());
		captionLabel.setCaption(fog.getCaption());
		captionLabel.setWidth(null);
		return captionLabel;
	}

	private static abstract class AbstractTab extends VerticalLayout {

	}

	public void accessMethod(Reimbursement reimbursement) {
		this.bean.getDocumentDetails().setDocAcknowledgementKey(
				reimbursement.getDocAcknowLedgement().getKey());
		DocAcknowledgement docAcknowlegement = acknowledgementDocumentsReceivedService
				.getDocAcknowledgment(reimbursement.getDocAcknowLedgement()
						.getKey());

		this.bean.getDocumentDetails().setAcknowledgementNumber(
				docAcknowlegement.getAcknowledgeNumber());
		this.bean.getDocumentDetails().setRodKey(reimbursement.getKey());
		this.bean.setRodNumberForUploadTbl(reimbursement.getRodNumber());

		this.bean.getDocumentDetails().setHospitalizationFlag(
				docAcknowlegement.getHospitalisationFlag());
		this.bean.getDocumentDetails().setPreHospitalizationFlag(
				docAcknowlegement.getPreHospitalisationFlag());
		this.bean.getDocumentDetails().setPostHospitalizationFlag(
				docAcknowlegement.getPostHospitalisationFlag());
		this.bean.getDocumentDetails().setPartialHospitalizationFlag(
				docAcknowlegement.getPartialHospitalisationFlag());
		this.bean.getDocumentDetails().setLumpSumAmountFlag(
				docAcknowlegement.getLumpsumAmountFlag());
		
		if(docAcknowlegement.getHospitalizationRepeatFlag() != null){
			this.bean.getDocumentDetails().setHospitalizationRepeatFlag(
					docAcknowlegement.getHospitalizationRepeatFlag());
		}
		
		this.bean.getDocumentDetails().setDocumentReceivedFromValue(docAcknowlegement.getDocumentReceivedFromId().getValue());
		this.bean.getDocumentDetails().setDocumentsReceivedDate(docAcknowlegement.getDocumentReceivedDate());
		this.bean.getDocumentDetails().setModeOfReceiptValue(docAcknowlegement.getModeOfReceiptId().getValue());
//		if(! reimbursement.getStatus().getKey().equals(ReferenceTable.ACKNOWLEDGE_STATUS_KEY) && !reimbursement.getStatus().getKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY)
//				&& !reimbursement.getStatus().getKey().equals(ReferenceTable.BILL_ENTRY_STATUS_KEY))
		if (docAcknowlegement.getHospitalizationClaimedAmount() != null) {
			this.bean.getDocumentDetails().setHospitalizationClaimedAmount(
					docAcknowlegement.getHospitalizationClaimedAmount()
							.toString());
		}
		if(screenName != null && !(screenName.equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT) 
				|| screenName.equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT_CHECKER))){
			
		fireViewEvent(DocumentDetailsPresenter.UPDATE_BEAN, this.bean, null);
		}

	}

	class TableExampleTab extends VerticalLayout {

		protected FlexibleOptionGroup flexibleOptionGroup;
		
		

		protected LayoutClickListener layoutClickListener = new LayoutClickListener() {

			public void layoutClick(LayoutClickEvent event) {
				FlexibleOptionGroupItemComponent c = null;
				boolean allowUnselection = flexibleOptionGroup.isMultiSelect();
				if (event.getChildComponent() instanceof FlexibleOptionGroupItemComponent) {
					c = (FlexibleOptionGroupItemComponent) event
							.getChildComponent();
				} else if (event.getChildComponent() instanceof AbstractComponent) {
					Object data = ((AbstractComponent) event
							.getChildComponent()).getData();
					if (data instanceof FlexibleOptionGroupItemComponent) {
						c = (FlexibleOptionGroupItemComponent) data;
					}
					if (event.getChildComponent() instanceof HorizontalLayout) {
						allowUnselection = false;
					}
				}
				if (c != null) {
					Object itemId = c.getItemId();
					if (flexibleOptionGroup.isSelected(itemId)
							&& allowUnselection) {
						flexibleOptionGroup.unselect(itemId);
					} else {
						flexibleOptionGroup.select(itemId);
					}
				}
			}
		};

		public TableExampleTab() {
			setMargin(true);
			flexibleOptionGroup = new FlexibleOptionGroup(createTestContainer());
			flexibleOptionGroup.setItemCaptionPropertyId(SNO);
			flexibleOptionGroup.setItemCaptionPropertyId(ROD_NO);
			flexibleOptionGroup.setItemIconPropertyId(ICON_PROPERTY);
			flexibleOptionGroup.setItemCaptionPropertyId(BILL_CLASSSIFICATION);
			flexibleOptionGroup.setItemCaptionPropertyId(CLAIMEDAMOUNT);
			flexibleOptionGroup.setItemCaptionPropertyId(APPROVEDAMOUNT);
			flexibleOptionGroup.setItemCaptionPropertyId(RODSTATUS);
			// flexibleOptionGroup.setItemCaptionPropertyId(ACTION);
			// flexibleOptionGroup.setItemCaptionPropertyId(VIEWDETAIL);

			final Table table = new Table(null,
					flexibleOptionGroup.getContainerDataSource());

			flexibleOptionGroup = new FlexibleOptionGroup(createTestContainer()) {
				public void setImmediate(boolean immediate) {
					//Vaadin8-setImmediate() super.setImmediate(immediate);
					//Vaadin8-setImmediate() table.setImmediate(true);
				}

				public void setMultiSelect(boolean multiSelect) {
					super.setMultiSelect(multiSelect);
					table.setMultiSelect(multiSelect);
				}

				public void setEnabled(boolean enabled) {
					super.setEnabled(enabled);
					table.setEnabled(enabled);
				}

				public void setReadOnly(boolean readOnly) {
					super.setReadOnly(readOnly);
					table.setReadOnly(readOnly);
				}
			};
			flexibleOptionGroup.setItemCaptionPropertyId(SNO);
			flexibleOptionGroup.setItemCaptionPropertyId(ROD_NO);
			flexibleOptionGroup.setItemIconPropertyId(ICON_PROPERTY);
			flexibleOptionGroup.setItemCaptionPropertyId(BILL_CLASSSIFICATION);
			flexibleOptionGroup.setItemCaptionPropertyId(CLAIMEDAMOUNT);
			flexibleOptionGroup.setItemCaptionPropertyId(APPROVEDAMOUNT);
			flexibleOptionGroup.setItemCaptionPropertyId(RODSTATUS);
			// flexibleOptionGroup.setItemCaptionPropertyId(ACTION);
			// flexibleOptionGroup.setItemCaptionPropertyId(VIEWDETAIL);

			//Vaadin8-setImmediate() flexibleOptionGroup.setImmediate(true);
			flexibleOptionGroup
					.setPropertyDataSource(new ObjectProperty<Object>(null,
							Object.class));

			flexibleOptionGroup
					.addValueChangeListener(new ValueChangeListener() {

						private ReceiptOfDocumentsDTO rodDto;

						@Override
						public void valueChange(ValueChangeEvent event) {
							System.out.println("Event ; "
									+ event.getProperty().getValue());
							Long rodKey = (Long) event.getProperty().getValue();
							if(null != rodKey)
							{
								isOptionSelected = true;
							}
							Reimbursement reimbursement = reimbursementService
									.getReimbursementByKey(rodKey);
							table.setSelectable(true);
							if (reimbursement != null) {
								if(screenName != null && (screenName.equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT) || screenName.equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT_CHECKER))){
									PhysicalDocumentVerification physicalVerification = reimbursementService.getReimbursementByKeyForPhysicalVerifcation(rodKey);	
								if(physicalVerification == null ) {
									showErrorPopUp("Physical Document Verification is not required for this ROD");
									flexibleOptionGroup.setValue(false);
									table.setSelectable(false);
									isOptionSelected = false;
								}else if(physicalVerification != null && physicalVerification.getCommunicationEnableFlag() != null && physicalVerification.getCommunicationEnableFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
									showErrorPopUp("Physical Document already verified for this ROD");
									flexibleOptionGroup.setValue(false);
									table.setSelectable(false);
									isOptionSelected = false;
								}
								else if(physicalVerification != null && physicalVerification.getCommunicationEnableFlag() != null && physicalVerification.getCommunicationEnableFlag().equalsIgnoreCase(SHAConstants.N_FLAG)
										&& (screenName.equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT_CHECKER) &&
												physicalVerification.getStatus() != null && physicalVerification.getStatus().getKey().equals(ReferenceTable.PAYMENT_VERIFICATION_PENDING_STATUS))){
									showErrorPopUp("Physical Document Maker Verification is pending for this ROD");
									flexibleOptionGroup.setValue(false);
									table.setSelectable(false);
									isOptionSelected = false;
								}
								}
								if(/*reimbursement.getDocAcknowLedgement().getModeOfReceiptId().getValue() != null
											&& !reimbursement.getDocAcknowLedgement().getModeOfReceiptId().getValue().equalsIgnoreCase("Email")
											&& !reimbursement.getDocAcknowLedgement().getModeOfReceiptId().getValue().equalsIgnoreCase("Online")
											&& */!(screenName != null && (screenName.equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT) || screenName.equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT_CHECKER)))){
									if((ackDocReceivedService.getDBTaskForCurrentQ(reimbursement.getClaim().getIntimation(), SHAConstants.BILLING_CURRENT_QUEUE,reimbursement.getKey())))
									{
										showErrorPopUp("This claims has crossed medical stage and you are not allowed to upload the document");
										flexibleOptionGroup.setValue(false);
										table.setSelectable(false);
										isOptionSelected = false;
									}else if((ackDocReceivedService.getDBTaskForCurrentQ(reimbursement.getClaim().getIntimation(), SHAConstants.FA_CURRENT_QUEUE,reimbursement.getKey())))
									{
										showErrorPopUp("This claims has crossed medical stage and you are not allowed to upload the document");
										flexibleOptionGroup.setValue(false);
										isOptionSelected = false;
										table.setSelectable(false);
									}else if(reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE) && ! ((reimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER))
											|| (reimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_REFER_TO_BILL_ENTRY))
											|| (reimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_CANCEL_ROD)))){
										showErrorPopUp("This claims has crossed medical stage and you are not allowed to upload the document");
										flexibleOptionGroup.setValue(false);
										isOptionSelected = false;
										table.setSelectable(false);
									}else if(reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE) && ! ((reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER))
											|| (reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY))
											|| (reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_CANCEL_ROD)))){
										showErrorPopUp("This claims has crossed medical stage and you are not allowed to upload the document");
										flexibleOptionGroup.setValue(false);
										table.setSelectable(false);
										isOptionSelected = false;
									}else if((reimbursement.getStatus().getKey().equals(ReferenceTable.ZONAL_REVIEW_CANCEL_ROD) || 
											reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS) ||
											 reimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_CANCEL_ROD) || 
											reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_CANCEL_ROD) ||
											reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_CANCEL_ROD))){
										showErrorPopUp("This claim has been closed and you are not allowed to upload the document");
										flexibleOptionGroup.setValue(false);
										table.setSelectable(false);
										isOptionSelected = false;
									}else{//Added for online portal claims
										accessMethod(reimbursement);
									}

								}
									else{
										accessMethod(reimbursement);
									}
							
							}

						}
					});

			
			table.setPropertyDataSource(flexibleOptionGroup
					.getPropertyDataSource());
			table.addGeneratedColumn(SELECTION_PROPERTY, new ColumnGenerator() {
				public Component generateCell(Table source, Object itemId,
						Object columnId) {
					return flexibleOptionGroup.getItemComponent(itemId);
				}
			});

			table.addGeneratedColumn(ACTION, new ColumnGenerator() {
				public Component generateCell(Table source, Object itemId,
						Object columnId) {
					System.out.println("itemId" + itemId);
					Button button = new Button("View Claim Status");
					button.setData(itemId);
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
					button.addStyleName(ValoTheme.BUTTON_LINK);
					button.addClickListener(new Button.ClickListener() {

						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							Long rodKey = (Long) event.getButton().getData();
							System.out.println("event.getButton().getData()"
									+ event.getButton().getData());
							Reimbursement reimbursement = reimbursementService
									.getReimbursementByKey(rodKey);
							if (reimbursement != null) {
								viewDetails
										.viewClaimStatusUpdated(reimbursement
												.getClaim().getIntimation()
												.getIntimationId());
							}

						}
					});
					return button;
				}
			});
			table.setRowHeaderMode(Table.RowHeaderMode.HIDDEN);
			table.setItemIconPropertyId(ICON_PROPERTY);
			table.setVisibleColumns(new Object[] { SNO, ROD_NO,
					BILL_CLASSSIFICATION, CLAIMEDAMOUNT, APPROVEDAMOUNT,
					RODSTATUS, ACTION, SELECTION_PROPERTY });

			table.setColumnHeader(SELECTION_PROPERTY, "");
			table.setColumnHeader(SNO, "S.No");
			table.setColumnHeader(ROD_NO, "ROD No");
			table.setColumnHeader(BILL_CLASSSIFICATION, "Bill Classification");
			table.setColumnHeader(CLAIMEDAMOUNT, "Claimed Amount");
			table.setColumnHeader(APPROVEDAMOUNT, "Approved Amount");
			table.setColumnHeader(RODSTATUS, "ROD <br>Status</br>");

	//		table.setWidth("1250px");
			table.setHeight("170px");
			
              if(screenName != null && screenName.equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT_CHECKER)){
			       table.setCaption("Select ROD to Received Physical Document(Checker)");
              }else {
            		table.setCaption("Select ROD to Received Physical Document(Maker)");
              }
			addComponent(table);
		}
		
		public Boolean isValid()
		{
			return isOptionSelected;
		}
	}

	public void init(String string, boolean b, boolean c,
			ReceiptOfDocumentsDTO bean) {
		this.bean = bean;
		this.screenName = bean.getScreenName();
		initTable();

	}
	
	public Boolean isValid()
	{
		if(!isOptionSelected)
		{
			errorMessage = "Please select atleast one rod to proceed";
			return false;
		}
		
		return true;
	}
	
	public void showErrorPopUp(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);
		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(true);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

}
	
	
	public String getErrors()
	{
		return this.errorMessage;
	}
}