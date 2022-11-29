package com.shaic.reimbursement.manageclaim.ClaimWiseAllowApprovalPages;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.IntimationDetailsCarousel;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.reimbursement.manageclaim.searchClaimwiseApproval.SearchClaimWiseAllowApprovalDto;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class ClaimWiseAllowApprovalPage extends ViewComponent{
	
	@Inject
	private ClaimWiseAllowApprovalPreauthRodTable claimWiseApprovalTable;
	
	@Inject
	private Instance<IntimationDetailsCarousel> commonCarouselInstance;
	
	private TextArea txtapprovalComments;
	
	private CheckBox chkBoxApproval;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	private BeanFieldGroup<ClaimWiseApprovalDto> binder;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private ClaimWiseApprovalDto bean;
	
	private SearchClaimWiseAllowApprovalDto dto;
	
	private NewIntimationDto intimationDto;
	
	@PostConstruct
	protected void initView(){
		
	}
	
	public void init(SearchClaimWiseAllowApprovalDto dto){
		
		this.dto = dto;
		this.bean = new ClaimWiseApprovalDto();
		this.binder = new BeanFieldGroup<ClaimWiseApprovalDto>(ClaimWiseApprovalDto.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		this.intimationDto = dto.getIntimationDto();
		
		IntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(this.intimationDto,"");
		
		claimWiseApprovalTable.init("", false, false);
		
		fireViewEvent(ClaimWiseApprovalPagePresenter.SET_TABLE_DATA, dto);
		
		txtapprovalComments = (TextArea)binder.buildAndBind("Allow Approval Comments", "approvalComments", TextArea.class);
		txtapprovalComments.setRequired(true);
		txtapprovalComments.setMaxLength(4000);
		txtapprovalComments.setWidth("400px");
		
		approvalCommentsChangeListener(txtapprovalComments,null);
		
		Label allowApprovalLabel = new Label("<p><b>Allow Approval for this claim <span style='color:black;'></span></b></p>", ContentMode.HTML);
		
		chkBoxApproval = binder.buildAndBind("","allowedApproval",CheckBox.class);
		//Vaadin8-setImmediate() chkBoxApproval.setImmediate(true);

		FormLayout chkLayout = new FormLayout(chkBoxApproval);
		chkLayout.setCaption("Allow Approval for this claim");
//		chkLayout.setComponentAlignment(allowApprovalLabel, Alignment.MIDDLE_RIGHT);
		
		FormLayout chkBoxCommtLayout = new FormLayout(chkLayout,txtapprovalComments);
//		chkBoxLayout.setCaption("Allow Approval for this claim");
//		FormLayout formLayout = new FormLayout(txtapprovalComments);
		
		submitBtn = new Button("Submit");
		cancelBtn = new Button("Cancel");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		FormLayout dummyForm = new FormLayout();
		dummyForm.setWidth("50%");
		
		
		VerticalLayout vLayout = new VerticalLayout(chkBoxCommtLayout);
		//Vaadin8-setImmediate() vLayout.setImmediate(false);
		
		HorizontalLayout formHor = new HorizontalLayout(dummyForm,vLayout);
		
		HorizontalLayout buttonHor = new HorizontalLayout(submitBtn,cancelBtn);
		buttonHor.setSpacing(true);
		
		VerticalLayout mainVertical = new VerticalLayout(claimWiseApprovalTable,formHor,buttonHor);
		mainVertical.setSpacing(true);
		mainVertical.setComponentAlignment(claimWiseApprovalTable, Alignment.MIDDLE_CENTER);
		mainVertical.setComponentAlignment(formHor, Alignment.BOTTOM_CENTER);
		mainVertical.setComponentAlignment(buttonHor,Alignment.BOTTOM_CENTER);
		
		mandatoryFields.add(txtapprovalComments);
		
		showOrHideValidation(false);
		
		addListener();
		
		setCompositionRoot(mainVertical); 
		
	}
	
	public void addListener(){
		
		submitBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Policy is cancelled", "Do you want to allow approval for this claim ?",
				        "No", "Yes", new ConfirmDialog.Listener() {

				            public void onClose(ConfirmDialog dialog) {
				                if (!dialog.isConfirmed()) {
				                	if(validatePage()){
				                		if(chkBoxApproval.getValue() != null && chkBoxApproval.getValue()){
				        					bean.setAllowedApproval(SHAConstants.YES_FLAG);
				        				} else{
				        					bean.setAllowedApproval(SHAConstants.N_FLAG);
				        				}
				        				bean.setApprovalComments(txtapprovalComments.getValue());
				        				bean.setIntimationNo(dto.getIntimationNo());
				        				String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
				                		fireViewEvent(ClaimWiseApprovalPagePresenter.SUBMIT_DATA, bean, userName);
				                	}
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
				dialog.setClosable(false);
			}
		});
		cancelBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
					fireViewEvent(MenuItemBean.CLAIM_WISE_ALLOW_APPROVAL, true);
			}
		});
		
	}
	
	public void setTableList(List<ClaimWiseApprovalDto> listDetails) {
		claimWiseApprovalTable.setTableList(listDetails);
	}
	
	@SuppressWarnings("unused")
	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	public  void approvalCommentsChangeListener(TextArea textArea, final  Listener listener) {
	    @SuppressWarnings("unused")
		ShortcutListener enterShortCut = new ShortcutListener("ShortcutForApprovalComments", ShortcutAction.KeyCode.F8, null) {
	    	private static final long serialVersionUID = -2267576464623389044L;
	    	@Override
	    	public void handleAction(Object sender, Object target) {
	    		((ShortcutListener) listener).handleAction(sender, target);
	    	}
	    };	  
	    handleShortcut(textArea, getApprovalCommentsShortCutListener(textArea));
	}
	
	public  void handleShortcut(final TextArea textArea, final ShortcutListener shortcutListener) {	
		textArea.addFocusListener(new FocusListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void focus(FocusEvent event) {				
				textArea.addShortcutListener(shortcutListener);
			}
		});
		textArea.addBlurListener(new BlurListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void blur(BlurEvent event) {			
				textArea.removeShortcutListener(shortcutListener);		
			}
		});
	}
	
	private ShortcutListener getApprovalCommentsShortCutListener(final TextArea textAreaField) {
		ShortcutListener listener =  new ShortcutListener("ShortcutForApprovalComments", KeyCodes.KEY_F8,null) {
			private static final long serialVersionUID = 1L;
			@SuppressWarnings({ "static-access", "deprecation" })
			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				txtArea.setData(bean);
				txtArea.setValue(textAreaField.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setRows(25);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
				txtArea.setReadOnly(false);
				
				final Window dialog = new Window();
				dialog.setHeight("75%");
		    	dialog.setWidth("65%");
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void valueChange(ValueChangeEvent event) {
						textAreaField.setValue(((TextArea) event.getProperty()).getValue());						
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				dialog.setCaption("Allow Approval Comments");
				dialog.setClosable(true);
				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(textAreaField);
				
				dialog.addCloseListener(new Window.CloseListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void windowClose(CloseEvent e) {
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
						dialog.close();
					}
				});	
			}
		};
		return listener;
	}

	public Boolean validatePage(){
		
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
		
		if(txtapprovalComments.getValue() == null || txtapprovalComments.getValue().isEmpty()){
			hasError = true;
			eMsg.append("Please Enter Approval Comments");
		}
		if (hasError) {
			setRequired(true);
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
		} else{
			try{
				
			}catch(Exception e){
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}
	}

}
