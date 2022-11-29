package com.shaic.claim.pcc.wizard;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.pcc.beans.PCCRequest;
import com.shaic.claim.pcc.divisionHead.SearchPccDivisionHeadViewImpl;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestFormDTO;
import com.shaic.claim.pcc.hrmCoordinator.SearchPccHrmCoordinatorViewImpl;
import com.shaic.claim.pcc.processor.SearchPccProcessorViewImpl;
import com.shaic.claim.pcc.reviewer.SearchPccReviewerViewImpl;
import com.shaic.claim.pcc.zonalCoordinator.SearchPccZonalCoordinatorViewImpl;
import com.shaic.claim.pcc.zonalMedicalHead.SearchPccZonalMedicalHeadViewImpl;
import com.shaic.domain.HospitalService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;


@SuppressWarnings("serial")
public class SearchProcessPCCRequestForm extends SearchComponent<SearchProcessPCCRequestFormDTO> {

	private TextField txtIntimationNo;
	
	private ComboBox cmbCpuCode;
	
	private ComboBox cmbSource;
	
	private ComboBox cmbPccCatagoty;
	
	private ComboBox cmbProductName;
		
	private SearchProcessPCCRequestFormDTO searchDto = null;
	
    private String menustring;
	
    private Panel mainPanel;
	
	private BeanItemContainer<SelectValue> cpu;
	
	private BeanItemContainer<SelectValue> source;
	
	private BeanItemContainer<SelectValue> pccCatagory;
	
	private BeanItemContainer<SpecialSelectValue> productName;

	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;

	public void setBeanItemContainer(){
		
		//this.cpu = masterService.getTmpCpuCodeList();
		this.cpu = getCpuCodeList();
		this.source = masterService.getMasterValueByReference(ReferenceTable.PCC_SOURCE_TYPE);			
		this.pccCatagory = preauthService.getPCCCategorys();
		this.productName = masterService.getContainerForProduct();
	}
	
	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue>  getCpuCodeList() {
		
		//VaadinSession session= getSession();
		VaadinSession session=VaadinSession.getCurrent();
		String userName=(String) session.getAttribute(BPMClientContext.USERID);
		System.out.println(String.format("USER NAME :  [%s]", userName));
		String roleCode =  SHAConstants.PCC_COORDINATOR_ROLE;
		userName = userName.toUpperCase();
		BeanItemContainer<SelectValue> cpuCodeRoleBasedContainer = masterService.getPCCCpuCodeList(userName, roleCode);
		return cpuCodeRoleBasedContainer;
		
	}
	
	@PostConstruct
	public void init(String menuString) {

		initBinder();
		setBeanItemContainer();
		this.menustring = menuString;
		mainPanel = new Panel();
		mainPanel.setCaption("PCC Co-Ordinator");
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	@SuppressWarnings("deprecation")
	public VerticalLayout mainVerticalLayout(){
		
		mainVerticalLayout = new VerticalLayout();
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		cmbCpuCode = binder.buildAndBind("CPU Code", "cpuCode", ComboBox.class);
		cmbSource = binder.buildAndBind("Source", "source", ComboBox.class);
		cmbPccCatagoty = binder.buildAndBind("PCC Catagory", "pccCatagory", ComboBox.class);
		cmbProductName = binder.buildAndBind("Product Name/Code","productName",ComboBox.class);

		FormLayout leftFormLayout = new FormLayout(txtIntimationNo,cmbCpuCode);
		FormLayout middleFormLayout = new FormLayout(cmbSource,cmbPccCatagoty);
		FormLayout rightFormLayout = new FormLayout(cmbProductName);

		HorizontalLayout fieldLayout = new HorizontalLayout(leftFormLayout,middleFormLayout,rightFormLayout);		
		
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnGetTask, "top:130.0px;left:370.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:120.0px;left:470.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		mainVerticalLayout.setWidth("950px");
		mainVerticalLayout.setMargin(false);		 
		absoluteLayout_3.setWidth("100.0%");		 
	    absoluteLayout_3.setHeight("184px");
		addListener();
		setDropDownValues();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchProcessPCCRequestFormDTO>(SearchProcessPCCRequestFormDTO.class);
		this.binder.setItemDataSource(new SearchProcessPCCRequestFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	@SuppressWarnings("deprecation")
	public void setDropDownValues(){
		cmbCpuCode.setContainerDataSource(cpu);
		cmbCpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCode.setItemCaptionPropertyId("value");

		cmbSource.setContainerDataSource(source);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");

		cmbPccCatagoty.setContainerDataSource(pccCatagory);
		cmbPccCatagoty.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPccCatagoty.setItemCaptionPropertyId("value");	
		
		cmbProductName.setContainerDataSource(productName);
		cmbProductName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbProductName.setItemCaptionPropertyId("specialValue");

	}

}
