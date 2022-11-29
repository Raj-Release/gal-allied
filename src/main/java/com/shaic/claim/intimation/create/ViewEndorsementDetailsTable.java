package com.shaic.claim.intimation.create;

import javax.ejb.EJB;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.policy.search.ui.PremPolicySchedule;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyEndorsementDetails;
import com.shaic.domain.PolicyService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewEndorsementDetailsTable extends GBaseTable<PolicyEndorsementDetails>{

	@EJB
	private PolicyService policyService;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
		"endorsementNumber",
		"endoresementDate",
		"effectiveFromDate",
		"effectiveToDate",
		"endorsementType",
		"endorsementText",
		"sumInsured",
		"revisedSumInsured",
		"premium",
	};*/
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
        table.setContainerDataSource(new BeanItemContainer<PolicyEndorsementDetails>(PolicyEndorsementDetails.class));
        Object[] NATURAL_COL_ORDER = new Object[] {
    		"endorsementNumber",
    		"endoresementDate",
    		"effectiveFromDate",
    		"effectiveToDate",
    		"endorsementType",
    		"endorsementText",
    		"sumInsured",
    		"revisedSumInsured",
    		"premium",
    		"endorsementKey"
    	};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		table.removeGeneratedColumn("effectiveFromDate");
		table.addGeneratedColumn("effectiveFromDate", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 
		    	  PolicyEndorsementDetails policyEndorsement = (PolicyEndorsementDetails)itemId;
		    	  
		    	  String formatDate = SHAUtils.formatDate(policyEndorsement.getEffectiveFromDate());
		    	  return formatDate;
		      }
		});
		table.removeGeneratedColumn("endoresementDate");
		table.addGeneratedColumn("endoresementDate", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 
		    	  PolicyEndorsementDetails policyEndorsement = (PolicyEndorsementDetails)itemId;
		    	  
		    	  String formatDate = SHAUtils.formatDate(policyEndorsement.getEffectiveFromDate());
		    	  return formatDate;
		      }
		});
		
		table.removeGeneratedColumn("effectiveToDate");
		table.addGeneratedColumn("effectiveToDate", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 
		    	  PolicyEndorsementDetails policyEndorsement = (PolicyEndorsementDetails)itemId;
		    	  
		    	  String formatDate = SHAUtils.formatDate(policyEndorsement.getEffectiveToDate());
		    	  return formatDate;
		      }
		});
		
		table.removeGeneratedColumn("endorsementNumber");
		table.addGeneratedColumn("endorsementNumber", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	  PolicyEndorsementDetails policyEndorsement = (PolicyEndorsementDetails)itemId;
		    	  Policy policy = policyEndorsement.getPolicy();
		    	  String strPolicyNo = policy.getPolicyNumber();
		    	  String endorsementNo = "";
		  		  endorsementNo = strPolicyNo != null ? strPolicyNo +"/"+policyEndorsement.getEndorsementNumber() :policyEndorsement.getEndorsementNumber();
		    	  return endorsementNo;
		      }
		});
		
		table.removeGeneratedColumn("endorsementKey");
		table.addGeneratedColumn("endorsementKey", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	  Button btn = new Button("View Endorsement Schedule");
		    	  btn.setStyleName("link");
		          btn.addClickListener(new Button.ClickListener() {
		        	  PolicyEndorsementDetails policyEndorsement = (PolicyEndorsementDetails)itemId;
			    	  Policy policy = policyEndorsement.getPolicy();
		              @Override
		              public void buttonClick(Button.ClickEvent event) {
		            	  getViewPolicySchedule(policy.getPolicyNumber(), Integer.parseInt(policyEndorsement.getEndorsementNumber()));
		              }
		          });
		          return btn;
		      }
		});
	}
	
	public void getViewPolicySchedule(String policyNo, int endIdx) {
		if(policyNo != null) {
			PremPolicySchedule fetchPolicyScheduleFromPremia = policyService.fetchPolicyScheduleFromPremia(policyNo, endIdx);
			if(fetchPolicyScheduleFromPremia != null && fetchPolicyScheduleFromPremia.getResultUrl() != null) {
				String url = fetchPolicyScheduleFromPremia.getResultUrl();
				getUI().getPage().open(url, "_blank", 1550, 650, BorderStyle.NONE);
			} else {
				getErrorMessage("Policy Schedule Not Available");
			}
		} else {
			getErrorMessage("Policy Not Available");
		}
	}
	
	public void getErrorMessage(String eMsg) {
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
	public void tableSelectHandler(PolicyEndorsementDetails t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-endorsement-details-";
	}
	protected void setTablesize(){
		table.setPageLength(table.size());
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
}
