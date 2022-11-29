
package com.shaic.claim.policy.search.ui;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.intimation.CashLessTableDetails;
import com.shaic.claim.intimation.CashlessTable;
import com.shaic.claim.intimation.ViewPreviousIntimationTable;
import com.shaic.claim.productbenefit.view.ViewProductBenefits;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.InsuredService;
import com.shaic.domain.MasterService;
import com.shaic.domain.NewIntimationService;
import com.shaic.domain.PolicyService;


/*
 * create intimation presenter is the presenter for CreateIntimationView. EJBs and other resources should
 * mainly be accessed in the presenter and results are pushed to the view
 * implementation trough it's API.
 */
@SuppressWarnings("serial")
@ViewInterface(NewSearchPolicyView.class)
public class NewSearchPolicyPresenter extends AbstractMVPPresenter<NewSearchPolicyView> {
    // CDI MVP includes a built-in CDI event qualifier @CDIEvent which
    // uses a String (method identifier) as it's member
  
    /**
     * 
     * Above given methods are as per the old create intimation presenter logic.(SearchPolicyPresenter.java)
     * Below given methods are added as per the new archictecture.
     * */
    public static final String SEARCH_BUTTON_CLICK = "policySearchBtn";
    public static final String VIEW_PRODUCT_BENEFITS = "viewProductBenefits";
    public static final String VIEW_PREVIOUS_INTIMATION = "viewPreviousIntimation";
    public static final String ADD_INTIMATION = "addIntimation";
    
    @PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME", type = PersistenceContextType.EXTENDED)
	protected EntityManager entityManager;
	
    @EJB
//	private NewSearchPolicyService newSearchPolicyService;
    
    @Inject
	private Instance<ViewProductBenefits> viewProductBenefitInstance;
    

	@EJB
	private PolicyService policyService;
	
	@EJB
	private InsuredService insuredService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private HospitalService hospitalService;
	

	@Inject
	private CashLessTableDetails cashLessTableDetails;
	
	@Inject
	private CashlessTable cashlessTable;

	@Inject
	private NewIntimationService newIntimationService;
	
	@Inject
	private ViewPreviousIntimationTable viewPreviousIntimationTable;

    
    @SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		NewSearchPolicyFormDTO searchFormDTO = (NewSearchPolicyFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
//		view.list(newSearchPolicyService.search(searchFormDTO,userName,passWord));
	}
    
    @SuppressWarnings({ "deprecation" })
  	public void generatePolicyConditionColumn(@Observes @CDIEvent(VIEW_PRODUCT_BENEFITS) final ParameterDTO parameters) {
    	
    	//Item item = (Item) parameters.getPrimaryParameter();
//    	final TmpPolicy policy = (TmpPolicy)  parameters.getPrimaryParameter();    	
//    	ViewProductBenefits a_viewProductBenefits = viewProductBenefitInstance
//				.get();
//		a_viewProductBenefits.showValues(policy);
//		//UI.getCurrent().addWindow(a_viewProductBenefits);
//		view.showProductBenefits(a_viewProductBenefits);
  		
  	}
    
//    @SuppressWarnings({ "deprecation" })
//  	public void generatePreviousIntimationColumn(@Observes @CDIEvent(VIEW_PREVIOUS_INTIMATION) final ParameterDTO parameters) {
//    	Item itemId = (Item) parameters.getPrimaryParameter();
//    	TmpPolicy tmpPolicy = (TmpPolicy) itemId;    	    	
//    	ViewPreviousIntimation view = new ViewPreviousIntimation(
//    			tmpPolicy,
//		masterService,
//		policyService,
//		hospitalService,
//		claimService,
//		cashLessTableDetails,
//		cashlessTable,
//		cashLessTableMapper,
//		newIntimationService, viewPreviousIntimationTable, entityManager);
//    	
//    	this.view.showPreviousIntimation(view);
//		//UI.getCurrent().addWindow(view);	
//    }
    
    @SuppressWarnings({ "deprecation" })
  	public void generateAddIntimationColumn(@Observes @CDIEvent(ADD_INTIMATION) final ParameterDTO parameters) {
    	
//    	final TmpPolicy policy = (TmpPolicy)  parameters.getPrimaryParameter();    	
//    	Product product = masterService.getProductByProductCode(policy.getPolProductCode());
//		OrganaizationUnit organaizationUnit = policyService.getOrgUnitName(policy.getPolhDivnCode());
//		LinkedHashMap<String, String> policyValues = new LinkedHashMap<String, String>();
//		policyValues.put("Policy Number", policy.getPolNo());
//		policyValues.put("Product Type", policy.getProductType().toString());
//		policyValues.put("Insured Name", "");
//		policyValues.put("Product Name", product.getValue());
//		policyValues.put("PIO Name", organaizationUnit.getOrganizationUnitName());
//		
//		view.setPolicyValueMap(policyValues);
  		
  	}
    
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
  

}
