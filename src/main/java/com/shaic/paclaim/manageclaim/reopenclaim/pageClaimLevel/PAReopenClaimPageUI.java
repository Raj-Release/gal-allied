package com.shaic.paclaim.manageclaim.reopenclaim.pageClaimLevel;

import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.enhacement.table.PreviousPreAuthDetailsTable;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class PAReopenClaimPageUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PAReopenClaimPageDTO bean;
	
	@Inject
	private PAReopenClaimTable pAReopenClaimTable;
	
	@Inject
	private Instance<PreviousPreAuthDetailsTable> previousPreauthDetailsTableInstance;
	
	private PreviousPreAuthDetailsTable previousPreAuthDetailsTableObj;
	
//	@Inject
//	private Instance<ReopenClaimReimbursementTable> reopenClaimReimbursementTableInstance;
//	
//	private ReopenClaimReimbursementTable reopenClaimReimbursementTableObj;
	
	@Inject
	private Instance<PAReopenReimbursementEditTable> reopenClaimReimbursementTableInstance;
	
	private PAReopenReimbursementEditTable reopenClaimReimbursementTableObj;
	
	

	private VerticalLayout mainLayout;
	
	private TextField txtBalanceSumInsured;
	
	private Button btnViewBalanceSI;
	
	private TextField txtProvisionAmount;
	
	private ComboBox cmbReasonForReopen;
	
	private TextArea txtReopnRemarks;
	
	private Button btnSubmit;
	
	private Button btnCancel;
	
	
	public void init(PAReopenClaimPageDTO bean){
		
		this.bean = bean;
		
		pAReopenClaimTable.init("Re-Open Claim Details", false, false);
		
		List<PAReopenClaimTableDTO> reopenClaimList = bean.getReopenClaimList();
		pAReopenClaimTable.setTableList(reopenClaimList);
		
		this.previousPreAuthDetailsTableObj = previousPreauthDetailsTableInstance
				.get();
		this.previousPreAuthDetailsTableObj.init("Pre-auth Details", false, false);
		
		
		List<PreviousPreAuthTableDTO> previousPreauthDetailsList = bean.getPreviousPreauthDetailsList();
		if(previousPreauthDetailsList != null && ! previousPreauthDetailsList.isEmpty()){
			
			this.previousPreAuthDetailsTableObj.setTableList(previousPreauthDetailsList);
		}else{
			this.previousPreAuthDetailsTableObj.setVisible(false);
		}
		
		this.reopenClaimReimbursementTableObj = reopenClaimReimbursementTableInstance
				.get();
		this.reopenClaimReimbursementTableObj.init("ROD Details");
		
		
		List<ViewDocumentDetailsDTO> rodDocumentDetailsList = bean.getRodDocumentDetailsList();
		if(rodDocumentDetailsList != null && ! rodDocumentDetailsList.isEmpty()){
			this.reopenClaimReimbursementTableObj.addList(rodDocumentDetailsList);
		}else{
			this.reopenClaimReimbursementTableObj.setVisible(false);
		}
	
		txtProvisionAmount = new TextField("Provision Amount");
		txtProvisionAmount.setEnabled(false);
		if(bean.getProvisionAmount() != null){
			txtProvisionAmount.setValue(bean.getProvisionAmount().toString());
		}

		cmbReasonForReopen = new ComboBox("Reason For Re-Open");
		cmbReasonForReopen.setRequired(true);
		
		cmbReasonForReopen.setContainerDataSource(bean.getReopenContainer());
		cmbReasonForReopen.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReasonForReopen.setItemCaptionPropertyId("value");
		
		txtReopnRemarks = new TextArea("Re-Open Remarks");
		
		FormLayout reasonsLayout = new FormLayout(txtProvisionAmount,cmbReasonForReopen,txtReopnRemarks);
		
		
		btnSubmit=new Button("Submit");
		btnCancel=new Button("Cancel");
        
        btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnSubmit.setWidth("-1px");
        btnSubmit.setHeight("-10px");
		
        btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
        btnCancel.setWidth("-1px");
        btnCancel.setHeight("-10px");
		
        HorizontalLayout buttonHorLayout=new HorizontalLayout(btnSubmit,btnCancel);
        buttonHorLayout.setSpacing(true);
        buttonHorLayout.setMargin(false);
		
		
		mainLayout = new VerticalLayout(pAReopenClaimTable,this.previousPreAuthDetailsTableObj,this.reopenClaimReimbursementTableObj,reasonsLayout,buttonHorLayout);
		mainLayout.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
		mainLayout.setComponentAlignment(reasonsLayout, Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(true);
		
		setCompositionRoot(mainLayout);
		
		addListener();

	}
	
	public void addListener(){
		
		btnCancel.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
				        "No", "Yes", new ConfirmDialog.Listener() {

				            public void onClose(ConfirmDialog dialog) {
				                if (!dialog.isConfirmed()) {
				                	fireViewEvent(MenuItemBean.PA_RE_OPEN_CLAIM_CLAIM_LEVEL, true);
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
				dialog.setClosable(false);
			}
		});
		btnSubmit.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(validatePage()){
					
				String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			    bean.setUserName(userName);	

				fireViewEvent(PAReopenClaimPresenter.SUBMIT_REOPEN_CLAIM, bean);
				}
				
				
			}
		});
		
		this.reopenClaimReimbursementTableObj.dummyField
		.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4843316375590220412L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Integer totalApprovedAmt = SHAUtils
						.getIntegerFromString((String) event
								.getProperty().getValue());
				txtProvisionAmount.setValue(String
						.valueOf(totalApprovedAmt));

			}
		});
		
		
	}
	

		public boolean validatePage() {
			Boolean hasError = false;
			
			String eMsg = "";		
			
			if(cmbReasonForReopen != null && cmbReasonForReopen.getValue() == null){
				eMsg += "Please Choose Reason for Re-Open Claim </br>";
				hasError = true;
			}
			
			if(txtProvisionAmount != null && ((txtProvisionAmount.getValue() == null) || (txtProvisionAmount.getValue() != null && txtProvisionAmount.getValue().equalsIgnoreCase("")))){
				eMsg += "Please Enter Provision Amount </br>";
				hasError = true;
			}
			
			if(txtReopnRemarks != null && ((txtReopnRemarks.getValue() == null) || (txtReopnRemarks.getValue() != null && txtReopnRemarks.getValue().equalsIgnoreCase("")))){
				eMsg += "Please Enter Re-Open Remarks </br>";
				hasError = true;
			}
			
			if(this.reopenClaimReimbursementTableObj != null && this.reopenClaimReimbursementTableObj.getValues() != null 
					&& ! this.reopenClaimReimbursementTableObj.getValues().isEmpty()){
				for (ViewDocumentDetailsDTO rodList : this.reopenClaimReimbursementTableObj.getValues()) {
					
					if(rodList.getApprovedAmount() == null){
						eMsg += "Please Enter Provision Amount for All ROD </br>";
						hasError = true;
					}
					
				}
			}
			
			if(txtProvisionAmount != null && txtProvisionAmount.getValue() != null){
				
				String value = txtProvisionAmount.getValue();
				
				Double doubleFromStringWithComma = SHAUtils.getDoubleFromStringWithComma(value);
				if(bean.getBalanceSI() != null && doubleFromStringWithComma != null && doubleFromStringWithComma > bean.getBalanceSI()){
					
					eMsg += "Provison Amount should be less than balance Sum Insured. </br>";
					hasError = true;
					
				}
				
			}
			
			
		
			if (hasError) {
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
		
				hasError = true;
				return !hasError;
			} else {
		
				
				if(cmbReasonForReopen != null && cmbReasonForReopen.getValue() != null){
					SelectValue reasonId = (SelectValue) cmbReasonForReopen.getValue();
					
					bean.setReasonForReopen(reasonId);
				}
				bean.setReOpenRemarks(txtReopnRemarks.getValue());
				
				if(txtProvisionAmount != null && txtProvisionAmount.getValue() != null){
					bean.setProvisionAmount(SHAUtils.getDoubleFromStringWithComma(txtProvisionAmount.getValue()));
				}
				
				if(this.reopenClaimReimbursementTableObj != null && this.reopenClaimReimbursementTableObj.getValues() != null 
						&& ! this.reopenClaimReimbursementTableObj.getValues().isEmpty()){
					bean.setRodDocumentDetailsList(this.reopenClaimReimbursementTableObj.getValues());
				}
				
				
				return true;
			}
		}
		
	

}
