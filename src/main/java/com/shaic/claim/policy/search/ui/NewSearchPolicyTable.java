package com.shaic.claim.policy.search.ui;

import java.util.Date;
import java.util.LinkedHashMap;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

public class NewSearchPolicyTable extends GBaseTable<NewSearchPolicyTableDTO> {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"polNo",
			"healthCardNo", "insuredName", "address", "registeredMobNo",
			"insuredOffice", "polProductName", "sumInsured", "telephoneNo",
			"dateOfIntimation", "endDate"
	// "type"
	};*/
	
	LinkedHashMap<String, String> policyValues = new LinkedHashMap<String, String>();
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<NewSearchPolicyTableDTO>(NewSearchPolicyTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"polNo",
			"healthCardNo", "insuredName", "address", "registeredMobNo",
			"insuredOffice", "polProductName", "sumInsured", "telephoneNo",
			"dateOfIntimation", "endDate"
	// "type"
	};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.addGeneratedColumn("ViewPolicyConditions", new Table.ColumnGenerator() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		        /* When the chekboc value changes, add/remove the itemId from the selectedItemIds set */
		        final Button viewPolicyConditonsButton = new Button("View Policy Condtions");
//		    	final TmpPolicy policy = populateTmpPolicyValue(itemId);
//		        viewPolicyConditonsButton.addClickListener(new Button.ClickListener() {
//			    public void buttonClick(ClickEvent event) {
//				    	Item item = source.getItem(itemId);
//				    	fireViewEvent(NewSearchPolicyPresenter.VIEW_PRODUCT_BENEFITS, policy);
//			        } 
//			    });
		    	viewPolicyConditonsButton.addStyleName("link");
		        return viewPolicyConditonsButton;
		      }
		    });
		
		table.addGeneratedColumn("ViewPreviousIntimation", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		        /* When the chekboc value changes, add/remove the itemId from the selectedItemIds set */
		    	final Button viewPreviousIntimationButton = new Button("View Previous Intimation");
		    	viewPreviousIntimationButton.addClickListener(new Button.ClickListener() {
			        public void buttonClick(ClickEvent event) {
			             Item item = source.getItem(itemId);
			             fireViewEvent(NewSearchPolicyPresenter.VIEW_PREVIOUS_INTIMATION, item);
			        } 
			    });
		    	viewPreviousIntimationButton.addStyleName(ValoTheme.BUTTON_LINK);
		        return viewPreviousIntimationButton;
		      }
		    });
		
		table.addGeneratedColumn("AddIntimation", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		        /* When the chekboc value changes, add/remove the itemId from the selectedItemIds set */
		    	final Button addIntimationButton = new Button("Add Intimation");
//		    	final TmpPolicy policy = populateTmpPolicyValue(itemId);
		    	addIntimationButton.addClickListener(new Button.ClickListener() {
			        public void buttonClick(ClickEvent event) {
//			        	fireViewEvent(MenuPresenter.POPULATE_POLICY_FROM_TMPPOLICY , policy);
//			        //	System.out.println("------policy dates----"+policy.getPolFmDt() + "  "+policy.getPolToDt());
//			        	if((!("ACTIVE").equalsIgnoreCase(policy.getPolStatus())) && !isDateOfIntimationWithPolicyRange(policy.getPolFmDt(), policy.getPolToDt())  ){
//			        		Notification.show("Message","This Policy is not an ACTIVE.",Notification.Type.WARNING_MESSAGE);
//				    	} else {
//				             fireViewEvent(NewSearchPolicyPresenter.ADD_INTIMATION, policy);
//				    		fireViewEvent(MenuItemBean.NEW_INTIMATION,policy, policyValues);
//				    	}
			        } 
			    });
		    	addIntimationButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	addIntimationButton.addStyleName(ValoTheme.BUTTON_LINK);
		        return addIntimationButton;
		      }
		    });
		
	}

	@Override
	public void tableSelectHandler(NewSearchPolicyTableDTO t) {
		fireViewEvent(MenuItemBean.CREATE_NEW_INTIMATION, t.getKey());
		// TODO Auto-generated method stub
		
	}
	
	private Table getRowHeaderMode()
	{
		
		return table;
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-createIntimation-new-";
	}
	
	
	public void intializePolicyValueMap(LinkedHashMap<String, String> policyValues)
	{
		this.policyValues = policyValues;
	}
	
//	private TmpPolicy populateTmpPolicyValue (final Object itemId)
//	{
//		NewSearchPolicyTableDTO tableDTO = (NewSearchPolicyTableDTO) itemId;
//		TmpPolicy policy = new TmpPolicy();
//    	policy.setPolAssrCode(tableDTO.getPolicyProposerCode());
//    	policy.setPolType(tableDTO.getPolicyType());
//    	policy.setProductType(tableDTO.getProductTypeId());
//    	policy.setPolhDivnCode(tableDTO.getTmpPolIssueCode());
//    	policy.setPolNo(tableDTO.getPolNo());
//    	policy.setProductName(tableDTO.getPolProductName());
//    	policy.setPolProductCode(tableDTO.getProductCode());
//    	policy.setLineofBusiness(tableDTO.getLineOfBusiness());
//    	policy.setPolStatus(tableDTO.getPolicyStatus());
//    	policy.setPolFmDt(tableDTO.getDateOfIntimation());
//    	policy.setPolToDt(tableDTO.getEndDate());
//    	return  policy;
//	}
	
	/**
	 * Method to validate whether date of intimation is within policy start and
	 * end date
	 * */
	private boolean isDateOfIntimationWithPolicyRange(Date policyFrmDate , Date policyToDate)
	{
		Date intimationCreationDate = new Date();
		if((policyFrmDate.equals(intimationCreationDate) || policyFrmDate.before(intimationCreationDate)) 
			&& (policyToDate.equals(intimationCreationDate) || policyToDate.after(intimationCreationDate)))
		{
			return true;
		}	
		else
		{
			return false;
		}
	}
	
	

}
