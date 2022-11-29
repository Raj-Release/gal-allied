package com.shaic.paclaim.addAdditinalDocument.search;

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
import com.shaic.claims.reibursement.addaditionaldocuments.SelectRODtoAddAdditionalDocumentsDTO;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.IntimationService;
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

public class PASelectRODtoAddAdditionalDocumentsTable extends ViewComponent{


	private static final long serialVersionUID = 1L;

	private static final String ROD_NO = "RODNO";
	private static final String SNO = "SNO";
	private static final String COVER_CODE = "COVER_CODE";
	private static final String CLAIMEDAMOUNT = "CLAIMEDAMOUNT";
	private static final String APPROVEDAMOUNT = "APPROVEDAMOUNT";
	private static final String RODSTATUS = "RODSTATUS";
	private static final String ACTION = "ACTION";
	private static final String VIEWDETAIL = "VIEWDETAIL";

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

	@EJB
	private AcknowledgementDocumentsReceivedService acknowledgementDocumentsReceivedService;

	// ROD No, S.NO, Bill Classsification, Claimed Amount, Approved Amount, ROD
	// Status, Action, View Detail

	private static final String ICON_PROPERTY = "icon";
	private static final String SELECTION_PROPERTY = "Select";
	
	private Boolean  isOptionSelected ;
	
	private String errorMessage;

	public static final Object[] VISIBLE_COL_ORDER = new Object[] {
			"serialNumber", "id", "name", "salary" };

	@EJB
	private ReimbursementService reimbursementService;

	@Inject
	private ViewDetails viewDetails;

	private static final String[] DOCUMENTS = new String[] { "Word",
			"document-doc.png", "Image", "document-image.png", "PDF",
			"document-pdf.png", "PowerPoint", "document-ppt.png", "Text",
			"document-txt.png", "Web", "document-web.png", "Excel",
			"document-xsl.png" };

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
		cont.addContainerProperty(COVER_CODE, String.class, null);
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
			String coverCode = list.getCoverCode();
			String claimedAmount = list.getClaimedAmt();
			String approvedAmount = list.getApprovedAmt();
			String rodStatus = list.getRodStatus();
			// String action = DOCUMENTS[i];
			// String viewDetails = DOCUMENTS[i];
			Item item = cont.addItem(id);
			if(list.getStatusKey().equals(ReferenceTable.ACKNOWLEDGE_STATUS_KEY) || list.getStatusKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY)
					|| list.getStatusKey().equals(ReferenceTable.BILL_ENTRY_STATUS_KEY)){
			valuateTestContainerItem(rodNo, id, sNO, coverCode,
					claimedAmount, null, rodStatus, item);
			}else{
				valuateTestContainerItem(rodNo, id, sNO, coverCode,
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

	private static void valuateTestContainerItem(String rodNo, Long id,
			String sNo, String coverCode, String claimedAmount,
			String approvedAmount, String rodStatus, /* String action, */
			/* String viewDetails, */Item item) {
		item.getItemProperty(SNO).setValue(sNo);
		item.getItemProperty(ROD_NO).setValue(rodNo);
		item.getItemProperty(COVER_CODE).setValue(coverCode);
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
		fireViewEvent(PAAddAdditionalDocumentDetailsPresenter.UPDATE_BEAN, this.bean, null);

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
			flexibleOptionGroup.setItemCaptionPropertyId(COVER_CODE);
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
			flexibleOptionGroup.setItemCaptionPropertyId(COVER_CODE);
			flexibleOptionGroup.setItemCaptionPropertyId(CLAIMEDAMOUNT);
			flexibleOptionGroup.setItemCaptionPropertyId(APPROVEDAMOUNT);
			flexibleOptionGroup.setItemCaptionPropertyId(RODSTATUS);
			// flexibleOptionGroup.setItemCaptionPropertyId(ACTION);
			// flexibleOptionGroup.setItemCaptionPropertyId(VIEWDETAIL);

			//Vaadin8-setImmediate() flexibleOptionGroup.setImmediate(true);
			flexibleOptionGroup
					.setPropertyDataSource(new ObjectProperty<Object>(null,
							Object.class));

			/*flexibleOptionGroup
					.addValueChangeListener(new ValueChangeListener() {

						private ReceiptOfDocumentsDTO rodDto;

						@Override
						public void valueChange(ValueChangeEvent event) {
							System.out.println("Event ; "
									+ event.getProperty().getValue());
							Long rodKey = (Long) event.getProperty().getValue();
							Reimbursement reimbursement = reimbursementService
									.getReimbursementByKey(rodKey);
							if (reimbursement != null) {
								accessMethod(reimbursement);
							}
						}
					});*/
			
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
						}else{
							accessMethod(reimbursement);
						}
					}

				}
			});

			table.setSelectable(true);
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
					COVER_CODE, CLAIMEDAMOUNT, APPROVEDAMOUNT,
					RODSTATUS, ACTION, SELECTION_PROPERTY });

			table.setColumnHeader(SELECTION_PROPERTY, "Select");
			table.setColumnHeader(SNO, "S.No");
			table.setColumnHeader(ROD_NO, "ROD No");
			table.setColumnHeader(COVER_CODE, "Cover Code");
			table.setColumnHeader(CLAIMEDAMOUNT, "Claimed Amount");
			table.setColumnHeader(APPROVEDAMOUNT, "Approved Amount");
			table.setColumnHeader(RODSTATUS, "ROD <br>Status</br>");

	//		table.setWidth("1250px");
			table.setHeight("170px");

			table.setCaption("Select ROD to Add Additional Documents");
			addComponent(table);
		}
	}

	public void init(String string, boolean b, boolean c,
			ReceiptOfDocumentsDTO bean) {
		this.bean = bean;
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
