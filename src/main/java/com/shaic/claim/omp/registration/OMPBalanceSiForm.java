package com.shaic.claim.omp.registration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.domain.InsuredDto;
import com.shaic.domain.InsuredService;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.PolicyService;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.InsuredMapper;
import com.shaic.newcode.wizard.domain.PolicyMapper;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class OMPBalanceSiForm extends ViewComponent{
	
		private VerticalLayout mainLayout;
		private HorizontalLayout balanceSuminsuredhorizontal;
		private FormLayout formlayoutRight;
		private FormLayout formlayoutleft;
		private TextField txtProductName;
		private TextField txtBalanceSIInsuredName;
		private Panel mainPanel;

		@EJB
		private OMPIntimationService intimationService;

		@EJB
		private PolicyService policyService;

		@EJB
		private ReimbursementService reimbursementService;

		@EJB
		private OMPClaimService claimService;

		@EJB
		private DBCalculationService dbCalculationService;

		@EJB
		private InsuredService insuredService;
		
		@Inject
		private Instance<OMPBalanceSiDetailTable> balancesumInsuredTable;
		
		private OMPBalanceSiDetailTable  balancesumInsuredTableInstance;
		
		private FormLayout formlayoutBottom;
		

		public OMPBalanceSiForm() {

		}

//		@PostConstruct
		public void initView() {

			Panel buildMainLayout = buildMainLayout();
			setCompositionRoot(buildMainLayout);

		}

		public VerticalLayout bindFieldGroup(String intimationNo, Long claimKey, Long rodKey) {
			
			initView();
			OMPIntimation intimation = intimationService
					.searchbyIntimationNo(intimationNo);
			if(intimation!= null){
				OMPClaim claim = claimService.getClaimforIntimation(intimation.getKey());
			}
			PolicyMapper mapper = new PolicyMapper();
			PolicyDto policyDto = null;
			if(intimation!= null){
				 policyDto = mapper.getPolicyDto(intimation.getPolicy());
			}
//
			InsuredMapper insuredMapper = new InsuredMapper();
			InsuredDto insuredDto = null;
			if(intimation!= null){
			 insuredDto = insuredMapper.getInsuredDto(intimation
					 .getInsured());
			}

			if (intimation != null && intimation.getInsured() != null) {
				if (intimation.getInsured().getInsuredGender() != null) {
					SelectValue insuredGender = new SelectValue();
					insuredGender.setId(intimation.getInsured().getInsuredGender()
							.getKey());
					insuredGender.setValue(intimation.getInsured()
							.getInsuredGender().getValue());
					insuredDto.setInsuredGender(insuredGender);
				}
				if (intimation.getInsured().getInsuredGender() != null) {
					SelectValue relationshipwithInsuredId = new SelectValue();
					if(intimation.getInsured()
							.getRelationshipwithInsuredId() != null) {
						relationshipwithInsuredId.setId(intimation.getInsured()
								.getRelationshipwithInsuredId().getKey());
						relationshipwithInsuredId.setValue(intimation.getInsured()
								.getRelationshipwithInsuredId().getValue());
						insuredDto
								.setRelationshipwithInsuredId(relationshipwithInsuredId);
					}
					
				}
			}

			BeanItem<PolicyDto> item = new BeanItem<PolicyDto>(policyDto);
			FieldGroup binder = new FieldGroup(item);
			binder.bindMemberFields(this);
			if (policyDto != null && insuredDto != null) {
			
				txtProductName.setValue(policyDto.getProduct().getValue() != null ? policyDto.getProduct().getValue() : "");
				txtProductName.setNullRepresentation("");
				txtBalanceSIInsuredName
						.setValue(insuredDto.getInsuredName() != null ? insuredDto
								.getInsuredName() : "");
				txtBalanceSIInsuredName.setNullRepresentation("");
			}
			setReadOnly(formlayoutleft);
			setReadOnly(formlayoutRight);
			
			buildTableLayout(intimation,claimKey,rodKey);
			mainLayout.setSpacing(true);
			
			return mainLayout;
		}

		@SuppressWarnings({ "rawtypes", "deprecation" })
		private void setReadOnly(FormLayout a_formLayout) {
			Iterator<Component> formLayoutLeftComponent = a_formLayout
					.getComponentIterator();
			while (formLayoutLeftComponent.hasNext()) {
				Component c = formLayoutLeftComponent.next();
				if (c instanceof com.vaadin.v7.ui.AbstractField) {
					TextField field = (TextField) c;
					field.setWidth("440px");
					field.setReadOnly(true);
					field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}
			}
		}

		private Panel buildMainLayout() {
			// common part: create layout
			
			balancesumInsuredTableInstance = balancesumInsuredTable.get();
			balancesumInsuredTableInstance.init("", false, false);
			
			
			balanceSuminsuredhorizontal = new HorizontalLayout(
					buildFormlayoutleft(), buildFormlayoutRight());
			balanceSuminsuredhorizontal.setWidth("100.0%");
			balanceSuminsuredhorizontal.setHeight("100.0%");
			
			mainLayout = new VerticalLayout(balanceSuminsuredhorizontal,balancesumInsuredTableInstance.getBSITable());
			mainLayout.setWidth("100%");
			mainLayout.setHeight("100%");
			mainLayout.setMargin(true);
			
			mainPanel = new Panel();
			mainPanel.setSizeFull();
			mainPanel.setContent(mainLayout);

			return mainPanel;
		}

		private FormLayout buildFormlayoutleft() {
				
			txtBalanceSIInsuredName = new TextField("Name of the Insured");
			formlayoutleft = new FormLayout(txtBalanceSIInsuredName);
			formlayoutleft.addStyleName("layoutDesign");
			formlayoutleft.setWidth("100.0%");
			formlayoutleft.setMargin(false);
			formlayoutleft.setSpacing(true);
//			formlayoutleft.setReadOnly(false);
			return formlayoutleft;
		}

		private FormLayout buildFormlayoutRight() {
			txtProductName = new TextField("Product Name");
			
			formlayoutRight = new FormLayout( txtProductName);
			formlayoutRight.addStyleName("layoutDesign");
			formlayoutRight.setWidth("100.0%");
			formlayoutRight.setMargin(false);
			formlayoutRight.setSpacing(true);

//			formlayoutRight.setReadOnly(false);

			return formlayoutRight;
		}
		
		private FormLayout buildFormlayoutBottom() {
		
			TextField dummyField1 = new TextField();
			dummyField1.setEnabled(false);
			dummyField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dummyField1.setHeight("100%");
			
			TextField dummyField2 = new TextField();
			dummyField2.setEnabled(false);
			dummyField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dummyField2.setHeight("100%");
			
			TextField dummyField3 = new TextField();
			dummyField3.setEnabled(false);
			dummyField3.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dummyField3.setHeight("100%");
			
			TextField dummyField4 = new TextField();
			dummyField4.setEnabled(false);
			dummyField4.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dummyField4.setHeight("100%");
			
			TextField dummyField5 = new TextField();
			dummyField5.setEnabled(false);
			dummyField5.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dummyField5.setHeight("100%");
			
			formlayoutBottom = new FormLayout(txtBalanceSIInsuredName,dummyField1,dummyField2,dummyField3,dummyField4,dummyField5);
			formlayoutBottom.addStyleName("layoutDesign");
			formlayoutBottom.setWidth("100.0%");
			formlayoutBottom.setMargin(false);
			formlayoutBottom.setSpacing(true);

//			formlayoutBottom.setReadOnly(false);

			return formlayoutBottom;
		}
		
	private void buildTableLayout(OMPIntimation intimation,Long claimKey, Long rodKey) {
		
//		OMPClaim claim = claimService.getClaimforIntimation(intimation.getKey());
		List<OMPBalanceSiTableDTO> ompBsiList = new ArrayList<OMPBalanceSiTableDTO>();
		if(claimKey == null){
			claimKey = 0l;
		}
		ompBsiList = dbCalculationService.getOMPBalanceSIView(intimation.getInsured().getKey(), claimKey,rodKey);
		
		balancesumInsuredTableInstance.setTableList(ompBsiList);
		if(ompBsiList.isEmpty()){
			balancesumInsuredTableInstance.setVisible(false);
		}else{
			balancesumInsuredTableInstance.setVisible(true);
		}
	}
}
