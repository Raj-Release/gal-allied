package com.shaic.claim.reports.negotiationreport;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.Page;
import com.shaic.claim.ViewNegotiationDetailsDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class SearchUpdateNegotiationViewImpl extends AbstractMVPView implements SearchUpdateNegotiationView{
	
	@Inject
	private SearchUpdateNegotiationPageUI searchUpdateNegUI;
	
	@Inject
	private SearchUpdateNegotiationTable searchUpdateNegTable;
	
	private VerticalSplitPanel mainPanel;
	
	private VerticalLayout verticalLayout;
	
	private Button btnSubmit;
	
	private Button btnCancel;
	
	private TextField txtClaimedAmt;
	
	private TextField txtNegotiationAmt;
	
	private TextField txtSavedAmt;
	
	private FormLayout dynamicFormLayout;
	
	private HorizontalLayout horizonLayout;
	
	private HorizontalLayout negLayout;
	
	private Double preauthAppAmt = 0d;
	
	public ViewNegotiationDetailsDTO negotiationDtls = new ViewNegotiationDetailsDTO();
	
	
	@PostConstruct
	protected void initView(){

		addStyleName("view");
		setSizeFull();
		searchUpdateNegUI.init();
//		searchUpdateNegTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchUpdateNegUI);
		verticalLayout = new VerticalLayout();
		horizonLayout = buildUpdateNegotiaonLayout();
		horizonLayout.setVisible(false);
		negLayout = buildNegotiationFieldsLayout();
		negLayout.setVisible(false);
//		verticalLayout.addComponent(searchUpdateNegTable);
		verticalLayout.addComponent(negLayout);
		verticalLayout.addComponent(horizonLayout);
		verticalLayout.setComponentAlignment(horizonLayout, Alignment.MIDDLE_CENTER);
		mainPanel.setSecondComponent(verticalLayout);
		mainPanel.setSplitPosition(31);
		mainPanel.setSizeFull();
		setHeight("570px");
		setCompositionRoot(mainPanel);
		searchUpdateNegTable.addSearchListener(this);
		searchUpdateNegUI.addSearchListener(this);
		resetView();
	
	}

	@Override
	public void doSearch() {
		ViewNegotiationDetailsDTO searchDTO = searchUpdateNegUI.getSearchDTO();
		if(searchDTO != null && searchDTO.getIntimationNo() != null/* && !searchDTO.getIntimationNo().isEmpty()*/){
			fireViewEvent(SearchUpdateNegotiationPresenter.SEARCH_BUTTON,searchDTO);
		} else {
			getErrorMessage("Intimation No is mandatory to search");
		}
	}

	@Override
	public void resetSearchResultTableValues() {
//		mainPanel.removeComponent(verticalLayout);
		verticalLayout.setVisible(false);
	}

	@Override
	public void resetView() {
		searchUpdateNegUI.refresh();
		/*horizonLayout.setVisible(false);
		negLayout.setVisible(false);*/
		verticalLayout.setVisible(false);
	}
	
	private HorizontalLayout buildUpdateNegotiaonLayout(){
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		//Vaadin8-setImmediate() horizontalLayout.setImmediate(true);
		
		btnSubmit = new Button();
		btnSubmit.setCaption("Submit");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true); 
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		btnCancel = new Button();
		btnCancel.setCaption("Cancel");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.setWidth("-1px");
		btnCancel.setHeight("-10px");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		
		
		addNegotiationListener();
		horizontalLayout.addComponents(btnSubmit,btnCancel);
		horizontalLayout.setSpacing(true);
		return horizontalLayout;
	
	}

	private HorizontalLayout buildNegotiationFieldsLayout(){
		
		HorizontalLayout fields = new HorizontalLayout();
		
		txtClaimedAmt = new TextField("Claimed Amount");
		txtClaimedAmt.setEnabled(false);
		txtNegotiationAmt = new TextField("Negotiation Amount");
		txtNegotiationAmt.setNullRepresentation("");
		txtNegotiationAmt.setValue("");
		
		CSValidator txtAmtToNeg = new CSValidator();
		txtAmtToNeg.setRegExp("^[0-9']*$");
		txtAmtToNeg.setPreventInvalidTyping(true);
		txtAmtToNeg.extend(txtNegotiationAmt);
		
		txtSavedAmt = new TextField("Saved Amount");
		txtSavedAmt.setEnabled(false);
		
		txtNegotiationAmt.addBlurListener(getNegotiationAmt());
		
		dynamicFormLayout = new FormLayout(txtClaimedAmt,txtNegotiationAmt,txtSavedAmt);
		VerticalLayout fielsVerticalLayout = new VerticalLayout();
		fielsVerticalLayout.addComponent(dynamicFormLayout);
		fields.addComponent(fielsVerticalLayout);
		
		return fields;
	}
	
	public void addNegotiationListener() {

		btnSubmit.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				btnSubmit.setEnabled(true);
				
				Label responseLabel = null;
				if (validatePage()) {
					negotiationDtls.setClaimApprovedAmt(Double.valueOf(txtClaimedAmt.getValue()));
					negotiationDtls.setNegotiatedAmt(Double.valueOf(txtNegotiationAmt.getValue()));
					negotiationDtls.setSavedAmt(Double.valueOf(txtSavedAmt.getValue()));
					String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
					fireViewEvent(SearchUpdateNegotiationPresenter.UPDATE_NEGOTIAION, negotiationDtls,userName);				
					responseLabel = new Label("<b style = 'color: black;'>Negotiation updated successfully</b>", ContentMode.HTML);
					Button successBtn = new Button("Update Negotiation Home");
								
					successBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					VerticalLayout layout = new VerticalLayout(responseLabel, successBtn);
					layout.setComponentAlignment(successBtn, Alignment.BOTTOM_CENTER);
					layout.setSpacing(true);
					layout.setMargin(true);
					
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setClosable(false);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
					
					successBtn.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							dialog.close();
							resetView();
							resetSearchResultTableValues();						
						}
					});
				} else {
					
				}
			}

		});
		
		btnCancel.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				verticalLayout.setVisible(false);
//				doSearch();
			}
		});
	}
	
	public Boolean validatePage(){
		if(txtNegotiationAmt.getValue() == null || txtNegotiationAmt.getValue().isEmpty()){
			getErrorMessage("Please Enter Negotiation Amount");
			return false;
		} else {
			return true;
		}
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

	@Override
	public void list(Page<ViewNegotiationDetailsDTO> tableRows) {
		if(tableRows != null) {
			searchUpdateNegTable.setTableList(tableRows);
		} else {
			
			Label successLabel = new Label("<b style = 'color: red;'>No Records Found</b>", ContentMode.HTML);			
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(MenuItemBean.UPDATE_NEGOTIATION, null);
					
				}
			});
		
		}
		
	}

	@Override
	public void setNegotiationDetails(ViewNegotiationDetailsDTO negDtls,Boolean isClmProcessed,Boolean isNegotiated) {
		if(isClmProcessed && !isNegotiated)	{
			horizonLayout.setVisible(true);
			negLayout.setVisible(true);
			verticalLayout.setVisible(true);
			
			txtClaimedAmt.setValue(String.valueOf(negDtls.getClaimApprovedAmt()));
			txtNegotiationAmt.setValue("");
			txtSavedAmt.setValue("");
			negotiationDtls.setIntimationKey(negDtls.getIntimationKey());
			negotiationDtls.setIntimationNo(negDtls.getIntimationNo());
			negotiationDtls.setTransactionFlag(negDtls.getTransactionFlag());
			negotiationDtls.setLatestTransKey(negDtls.getLatestTransKey());
			negotiationDtls.setCpuKey(negDtls.getCpuKey());
			
		} else {
			
			/*Label successLabel = new Label("<b style = 'color: red;'>Negotiated amount is already updated for this Intimation</b>", ContentMode.HTML);			
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(MenuItemBean.UPDATE_NEGOTIATION, null);
					
				}
			});*/
			
			StringBuffer successLabel= null;
			if(isClmProcessed && isNegotiated){
				successLabel= new StringBuffer("Negotiated amount is already updated for this Intimation");
			} else {
				successLabel=new StringBuffer("No Records Found");
			}
			final MessageBox msgBox = MessageBox
				    .createInfo()
				    .withCaptionCust("Information")
				    .withMessage(successLabel.toString())
				    .withOkButton(ButtonOption.caption("Update Negotiation Home"))
				    .open();
			Button homeButton=msgBox.getButton(ButtonType.OK);

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					msgBox.close();
					fireViewEvent(MenuItemBean.UPDATE_NEGOTIATION, null);
				}
		
			});
		}
	}
	
	private BlurListener getNegotiationAmt(){
		
			BlurListener listener = new BlurListener() {
				private static final long serialVersionUID = 1L;
		
				@Override
				public void blur(BlurEvent event){
					
					TextField value = (TextField) event.getComponent();
					if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
						Integer negotiationAmt = SHAUtils.getIntegerFromStringWithComma(value.getValue());
						String claimedAmt = txtClaimedAmt.getValue();
						Integer amountConsidered = SHAUtils.getDoubleFromString(claimedAmt);
						Integer savedAmt = amountConsidered - negotiationAmt;
							if(negotiationAmt > amountConsidered) {
								showErrorMsgForNegotiation();
								txtNegotiationAmt.setValue("");
							} else {
								txtSavedAmt.setValue(savedAmt.toString());
							}
						
					}
					
				}
			
		};
		return listener;
	}
	
	public void showErrorMsgForNegotiation(){
		
		Label label = new Label("Entered amount is greater than Claimed amount", ContentMode.HTML);
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
		
	}
	


}
