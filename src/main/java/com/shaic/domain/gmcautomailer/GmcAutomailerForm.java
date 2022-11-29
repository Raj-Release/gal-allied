package com.shaic.domain.gmcautomailer;

import java.util.ArrayList;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.table.Searchable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class GmcAutomailerForm extends SearchComponent<GmcAutomailerFormDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    @Inject
    private GmcAutomailerTable automailerTable;
    
	private BeanFieldGroup<GmcAutomailerFormDTO> binder;

	private TextField txtBranchCode;
	
	private TextField txtPolicyNo;

	private Button searchButton;
	
	private Button resetBtn;
	
	private Button createBtn;
	
	private VerticalLayout negotiationLayout;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private Searchable searchable;
	
	private GmcAutomailerFormDTO bean;
	
	private TextField txtCreatePolicyNo;
	
	private TextArea txtEmailId;
	
	public void addSearchListener(Searchable searchable) {
		this.searchable = searchable;
	}

	public GmcAutomailerFormDTO getSearchDTO() {
		try {
			this.binder.commit();
			 bean = this.binder.getItemDataSource().getBean();
			return bean;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	//@PostConstruct
	public void initView(){
		
	}
	
	
	public void init() {
		initBinder();
		negotiationLayout  = new VerticalLayout();
		Panel downsizePreauthPanel	= new Panel();
		//Vaadin8-setImmediate() downsizePreauthPanel.setImmediate(false);
		downsizePreauthPanel.setWidth("100%");
		downsizePreauthPanel.setHeight("50%");
		downsizePreauthPanel.setCaption("GMC Automailer");
		downsizePreauthPanel.addStyleName("panelHeader");
		downsizePreauthPanel.addStyleName("g-search-panel");
		downsizePreauthPanel.setContent(buildDownsizeLayout());
		negotiationLayout.addComponent(downsizePreauthPanel);
		negotiationLayout.setComponentAlignment(downsizePreauthPanel, Alignment.MIDDLE_LEFT);
		setCompositionRoot(negotiationLayout);
		addListener();
	}
	
	

	private VerticalLayout buildDownsizeLayout() {
		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 VerticalLayout verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setWidth("100.0%");
		 verticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("148px");
		

		txtPolicyNo = binder.buildAndBind("Policy No", "policyNo",
					TextField.class);
		CSValidator policyNumValidator = new CSValidator();
		policyNumValidator.extend(txtPolicyNo);
		policyNumValidator.setRegExp("^[a-zA-Z 0-9/-]*$");
		policyNumValidator.setPreventInvalidTyping(true);
		
		txtBranchCode = binder.buildAndBind("Branch Code", "branchCode",
				TextField.class);
		CSValidator branchCodeValidator = new CSValidator();
		branchCodeValidator.extend(txtBranchCode);
		branchCodeValidator.setRegExp("^[0-9]*$");
		branchCodeValidator.setPreventInvalidTyping(true);		

		FormLayout formLayout1 = new FormLayout(txtPolicyNo);
		FormLayout formLayout2 = new FormLayout(txtBranchCode);
		
		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1,formLayout2);
		searchFormLayout.setMargin(true);
		
		searchFormLayout.setWidth("100%");
		searchFormLayout.setComponentAlignment(formLayout1 , Alignment.MIDDLE_LEFT);
		searchFormLayout.setComponentAlignment(formLayout2 , Alignment.MIDDLE_RIGHT);
		absoluteLayout_3.addComponent(searchFormLayout);
		
		searchButton = new Button();
		searchButton.setCaption("Search");
		//Vaadin8-setImmediate() searchButton.setImmediate(true);
		searchButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		searchButton.setWidth("-1px");
		searchButton.setHeight("-10px");
		searchButton.setDisableOnClick(true);
		absoluteLayout_3
		.addComponent(searchButton, "top:70.0px;left:250.0px;");
		//Vaadin8-setImmediate() searchButton.setImmediate(true);
		
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		absoluteLayout_3.addComponent(resetBtn, "top:70.0px;left:350.0px;");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		
		createBtn = new Button();
		createBtn.setCaption("Create");
		//Vaadin8-setImmediate() createBtn.setImmediate(true);
		createBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		createBtn.setWidth("-1px");
		createBtn.setHeight("-10px");
		absoluteLayout_3.addComponent(createBtn, "top:70.0px;left:450.0px;");
		//Vaadin8-setImmediate() createBtn.setImmediate(true);
		
		Label mandatoryLabel = new Label("<b style = 'color: red;'>*Seperate the email ids by using comma(,)</b>", ContentMode.HTML);
		FormLayout formLayout = new FormLayout(mandatoryLabel);
		HorizontalLayout mandatoryFormLayout = new HorizontalLayout(formLayout);
		mandatoryFormLayout.setMargin(false);
		mandatoryFormLayout.setWidth("100%");
		mandatoryFormLayout.setComponentAlignment(formLayout , Alignment.BOTTOM_LEFT);
		absoluteLayout_3.addComponent(mandatoryFormLayout, "top:95.0px;left:1.0px;");
		
		verticalLayout.addComponent(absoluteLayout_3);
		verticalLayout.setWidth("650px");
		
		return verticalLayout; 
	}
	private void initBinder() {
		this.binder = new BeanFieldGroup<GmcAutomailerFormDTO>(GmcAutomailerFormDTO.class);
		this.binder.setItemDataSource(new GmcAutomailerFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	public void addListener() {
		searchButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				searchButton.setEnabled(true);
				GmcAutomailerFormDTO searchForm  = getSearchDTO();
				if(null != txtBranchCode.getValue() && !("").equals(txtBranchCode.getValue()) && null != txtPolicyNo.getValue() && !("").equals(txtPolicyNo.getValue())){
					getErrorMessage("Both Policy and branch code cannot be searched at once.");
				}else 
				if(null != txtBranchCode.getValue() && !("").equals(txtBranchCode.getValue())|| null != txtPolicyNo.getValue() && !("").equals(txtPolicyNo.getValue())){
					if(null != txtBranchCode.getValue() && !("").equals(txtBranchCode.getValue())) {
						fireViewEvent(GmcAutomailerPresenter.SEARCH_BUTTON_CLICK,searchForm); 
					}else if(null != txtPolicyNo.getValue() && !("").equals(txtPolicyNo.getValue())) {
						fireViewEvent(GmcAutomailerPresenter.SEARCH_BUTTON_CLICK,searchForm); 
					}
				}
				else
				{
					getErrorMessage("Atleast One Field is mandatory for search");
				}
					
			}			
		});
		
		resetBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				resetAlltheValues();
			}
		});
		
		createBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				createBtn.setEnabled(true);
				
				/*if(null != txtBranchCode.getValue() && !("").equals(txtBranchCode.getValue())|| null != txtPolicyNo.getValue() && !("").equals(txtPolicyNo.getValue())){
					if(null != txtPolicyNo.getValue() && !("").equals(txtPolicyNo.getValue())){
						fireViewEvent(GmcAutomailerPresenter.SEARCH_BUTTON_CLICK,bean.getPolicyNo()); 
					}else {
						getErrorMessage("Create auto mailer for policy number");
					}
				}
				else
				{
					getErrorMessage("Atleast One Field is mandatory for search");
				}*/
				
				txtCreatePolicyNo = binder.buildAndBind("Policy No/Branch Code", "newPolicyNo",
						TextField.class);
				CSValidator policyNumValidator = new CSValidator();
				policyNumValidator.extend(txtCreatePolicyNo);
				policyNumValidator.setRegExp("^[a-zA-Z 0-9/-]*$");
				policyNumValidator.setPreventInvalidTyping(true);
				
				txtEmailId = binder.buildAndBind("Email Id", "emailId",
						TextArea.class);
				CSValidator emailIdValidator = new CSValidator();
				emailIdValidator.extend(txtEmailId);
				emailIdValidator.setRegExp("^[a-zA-Z 0-9 / @ . , - _]*$");
				emailIdValidator.setPreventInvalidTyping(true);
				txtEmailId.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
				SHAUtils.handleTextAreaPopupDetails(txtEmailId,null,getUI(),SHAConstants.EMAIL_ID);
				
				Button btnSubmit = new Button();
				btnSubmit.setCaption("Submit");
				btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				btnSubmit.setWidth("-1px");
				btnSubmit.setHeight("-10px");
				//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
				
				Button btnCancel = new Button();
				btnCancel.setCaption("Cancel");
				btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
				btnCancel.setWidth("-1px");
				btnCancel.setHeight("-10px");
				//Vaadin8-setImmediate() btnCancel.setImmediate(true);
				
				FormLayout formLayout = new FormLayout(txtCreatePolicyNo,txtEmailId);
				
				HorizontalLayout createFormLayout = new HorizontalLayout(formLayout);
				createFormLayout.setMargin(true);
				
				HorizontalLayout createBtnLayout = new HorizontalLayout(btnSubmit,btnCancel);
				createBtnLayout.setMargin(true);
				createBtnLayout.setSpacing(true);
				
				VerticalLayout vLayout = new VerticalLayout(createFormLayout,createBtnLayout);
				
				Label mandatoryLabel = new Label("<b style = 'color: red;'>*Seperate the email ids by using comma(,)</b>", ContentMode.HTML);
				FormLayout formLabelLayout = new FormLayout(mandatoryLabel);
				HorizontalLayout mandatoryFormLayout = new HorizontalLayout(formLabelLayout);
				mandatoryFormLayout.setMargin(false);
				mandatoryFormLayout.setWidth("100%");
				mandatoryFormLayout.setComponentAlignment(formLabelLayout , Alignment.BOTTOM_LEFT);
				vLayout.addComponent(mandatoryFormLayout);
				
				final Window popup = new com.vaadin.ui.Window();
				
				popup.setCaption("");
				popup.setWidth("50%");
				popup.setHeight("50%");
				popup.setContent(vLayout);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				
				btnSubmit.addClickListener(new Button.ClickListener() {
					
					private static final long serialVersionUID = 1L;
			
					@Override
					public void buttonClick(ClickEvent event) {
							//binder.commit();
							//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
						String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
						GmcAutomailerFormDTO tableDTO = getSearchDTO();
						if (null != txtCreatePolicyNo.getValue() && !txtCreatePolicyNo.getValue().isEmpty() 
								&& null != txtEmailId.getValue() && !txtEmailId.getValue().isEmpty()) {
							/*tableDTO.setPolicyNo_branchCode(txtCreatePolicyNo.getValue());
							tableDTO.setEmailId(txtEmailId.getValue());*/
							fireViewEvent(GmcAutomailerPresenter.SUBMIT_NEW_ENTRY,tableDTO,userName);
							tableDTO.setNewPolicyNo("");;
							tableDTO.setBranchCode("");
							tableDTO.setEmailId("");
							if(tableDTO.getCheck() != null && tableDTO.getCheck().equalsIgnoreCase(SHAConstants.YES_FLAG)) {
							popup.close();
							}
						}else {
							getErrorMessage("Kindly enter Policy number and Email id details");
						}
						//popup.close();
					}
					
				});
				
				btnCancel.addClickListener(new Button.ClickListener() {
					
					private static final long serialVersionUID = 1L;
			
					@Override
					public void buttonClick(ClickEvent event) {
						popup.close();
					}
					
				});

				popup.addCloseListener(new Window.CloseListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
						unbindField(txtCreatePolicyNo);
						unbindField(txtEmailId);
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
					
			}			
		});
	}
	
	/**
	 * Method to reset all form values 
	 *
	 * */
	
	public void resetAlltheValues() 
	{
		
		Iterator<Component> componentIterator = negotiationLayout.iterator();
			while(componentIterator.hasNext()) 
			{
				Component searchScrnComponent = componentIterator.next() ;
				if(searchScrnComponent instanceof  Panel )
				{	
					Panel panel = (Panel)searchScrnComponent;
					Iterator<Component> searchScrnCompIter = panel.iterator();
					while (searchScrnCompIter.hasNext())
					{
						Component verticalLayoutComp = searchScrnCompIter.next();
						VerticalLayout vLayout = (VerticalLayout)verticalLayoutComp;
						Iterator<Component> vLayoutIter = vLayout.iterator();
						while(vLayoutIter.hasNext())
						{
							Component absoluteComponent = vLayoutIter.next();
							AbsoluteLayout absLayout = (AbsoluteLayout)absoluteComponent;
							Iterator<Component> absLayoutIter = absLayout.iterator();
							while(absLayoutIter.hasNext())
							{
								Component horizontalComp = absLayoutIter.next();
								if(horizontalComp instanceof HorizontalLayout)
								{
									HorizontalLayout hLayout = (HorizontalLayout)horizontalComp;
									Iterator<Component> formLayComp = hLayout.iterator();
									while(formLayComp.hasNext())
									{
										Component formComp = formLayComp.next();
										FormLayout fLayout = (FormLayout)formComp;
										Iterator<Component> formComIter = fLayout.iterator();
									
										while(formComIter.hasNext())
										{
											Component indivdualComp = formComIter.next();
											if(indivdualComp != null) 
											{
												if(indivdualComp instanceof Label) 
												{
													continue;
												}	
												if(indivdualComp instanceof TextField) 
												{
													TextField field = (TextField) indivdualComp;
													field.setValue("");
												} 
												else if(indivdualComp instanceof ComboBox)
												{
													ComboBox field = (ComboBox) indivdualComp;
													field.setValue(null);
												}	 
									// Remove the table if exists..	
									//removeTableFromLayout();
											}
										}
									}
								}
							}
						}
					}
					//Method to reset search table.
					removeTableFromLayout();
				}
			}
	}
	
	private void removeTableFromLayout()
	{
		if(null != searchable)
		{
			searchable.resetSearchResultTableValues();
		}
	}

	public void refresh()
	{
		System.out.println("---inside the refresh----");
		resetAlltheValues();
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
	
	private void unbindField(Field<?> field) {
		if (field != null) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field != null && propertyId != null) {
				this.binder.unbind(field);
			}

		}
	}

	}

