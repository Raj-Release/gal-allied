package com.shaic.claim.registration.balancesuminsured.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.InsuredDto;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.InsuredMapper;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PABalanceSumInsured extends ViewComponent {
	
	private VerticalLayout mainLayout;
	private HorizontalLayout balanceSuminsuredhorizontal;
	private TextField insuredName;
	private TextField insuredAge;
	
	@Inject
	private Instance<PABalanceSumInsuredTable> balanceSumInsuredTable;
	
	private PABalanceSumInsuredTable balanceSumInsured;
	
	@Inject
	private Instance<PABenefitsBalanceSumInsuredTable> benefitsBalanceSumInsuredTable;
	
	private PABenefitsBalanceSumInsuredTable balanceSumInsuredBenefits;
	
	@Inject
	private Instance<PAOptionalBalanceSumInsuredTable> optionalBalanceSumInsuredTable;
	
	private PAOptionalBalanceSumInsuredTable balanceSumInsuredOptional;
	
	@Inject
	private Instance<PAAddOnBalanceSumInsuredTable> addOnBalanceSumInsuredTable;
	
	private PAAddOnBalanceSumInsuredTable balanceSumInsuredAddOn;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private ClaimService claimService;
	
	public void init(String intimationNo)
	{
		balanceSumInsured = balanceSumInsuredTable.get();
		balanceSumInsured.init("", false, false);
		
		balanceSumInsuredBenefits = benefitsBalanceSumInsuredTable.get();
		balanceSumInsuredBenefits.init("", false, false);
		
		balanceSumInsuredOptional = optionalBalanceSumInsuredTable.get();
		balanceSumInsuredOptional.init("", false, false);
		balanceSumInsuredOptional.setCaption("Optional Covers");
		
		balanceSumInsuredAddOn = addOnBalanceSumInsuredTable.get();
		balanceSumInsuredAddOn.init("", false, false);
		balanceSumInsuredAddOn.setCaption("Add On Covers");
		
		insuredName = new TextField("Name of the Insured");
		insuredAge = new TextField("Age");
		
		FormLayout form1 = new FormLayout(insuredName);
		
		form1.addStyleName("layoutDesign");
		form1.setMargin(new MarginInfo(false,true,false,true));
		form1.setSpacing(true);
		
		FormLayout form2 = new FormLayout(insuredAge);
		
		form2.addStyleName("layoutDesign");
		form2.setMargin(new MarginInfo(false,true,false,true));
		form2.setSpacing(true);
		
		balanceSuminsuredhorizontal = new HorizontalLayout(form1,form2);
		balanceSuminsuredhorizontal.setWidth("100.0%");
		balanceSuminsuredhorizontal.setHeight("100.0%");
		mainLayout = new VerticalLayout(balanceSuminsuredhorizontal,
				balanceSumInsured,balanceSumInsuredBenefits,balanceSumInsuredAddOn,balanceSumInsuredOptional);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");

		setCompositionRoot(mainLayout);
		
		bindFieldGroup(intimationNo);
		
		setReadOnly(form1);
		setReadOnly(form2);
	
	}
	public void bindFieldGroup(String intimationNo)
	{
		Intimation intimation = intimationService.searchbyIntimationNo(intimationNo);
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		
		Claim claim = claimService.getClaimforIntimation(intimation.getKey());
		
		InsuredMapper insuredMapper = new InsuredMapper();
		InsuredDto insuredDto = insuredMapper.getInsuredDto(intimation
				.getInsured());
		
		Map<String, List> list = new HashMap<String, List>();
		
		if(insuredDto != null)
		{
			insuredName
			.setValue(insuredDto.getInsuredName() != null ? insuredDto
					.getInsuredName() : "");
			insuredName.setNullRepresentation("");
			
			insuredAge
			.setValue(insuredDto.getInsuredAge() != null ? String.valueOf(insuredDto
					.getInsuredAge().intValue()) : "");
			insuredAge.setNullRepresentation("");
		}
		
		if(claim != null)
		{
			if(null != intimation.getPolicy().getProduct().getKey() && 
					!(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey()))){
			list = dbCalculationService.getPABalanceSIView(claim.getKey(),insuredDto.getKey());
			}
			else
			{
				Long namedKey = null != intimation.getUnNamedKey() ? intimation.getUnNamedKey() : 0l;
				list = dbCalculationService.getGPABalanceSIView(insuredDto.getKey(), claim.getKey(), namedKey);
			}
		}
		else
		{
			if(null != intimation.getPolicy().getProduct().getKey() && 
					!(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey()))){
			
			list = dbCalculationService.getPABalanceSIView(0L,insuredDto.getKey());
			}
			else
			{
				Long namedKey = null != intimation.getUnNamedKey() ? intimation.getUnNamedKey() : 0l;
				list = dbCalculationService.getGPABalanceSIView(insuredDto.getKey(),0L,namedKey);
			}
		}
		
		List<PABalanceSumInsuredTableDTO> tableList1 = (List<PABalanceSumInsuredTableDTO>)list.get(SHAConstants.PA_DESC);
		List<PABalanceSumInsuredTableDTO> tableList2 = (List<PABalanceSumInsuredTableDTO>)list.get(SHAConstants.PA_BENEFIT_COVER_DESC);
		List<PABalanceSumInsuredTableDTO> tableList3 = (List<PABalanceSumInsuredTableDTO>)list.get(SHAConstants.PA_OPTIONAL_DESC);
		List<PABalanceSumInsuredTableDTO> tableList4 = (List<PABalanceSumInsuredTableDTO>)list.get(SHAConstants.PA_ADDITIONAL_DESC);
		
			/*balanceSumInsured.setTableList(tableList1);	
			balanceSumInsuredOptional.setTableList(tableList3);	
			balanceSumInsuredAddOn.setTableList(tableList4);	*/
		balanceSumInsured.setTableList(tableList1);	
		balanceSumInsuredBenefits.setTableList(tableList2);
		balanceSumInsuredOptional.setTableList(tableList3);			
		balanceSumInsuredAddOn.setTableList(tableList4);
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
	
	

}
