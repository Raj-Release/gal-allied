package com.shaic.claim.intimation.create;

import java.util.HashMap;

import javax.ejb.EJB;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.policy.search.ui.PremPolicySchedule;
import com.shaic.domain.Insured;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyEndorsementDetails;
import com.shaic.domain.PolicyService;
import com.shaic.domain.preauth.GmcMainMemberList;
import com.shaic.newcode.wizard.pages.IntimationDetailsPage;
import com.shaic.newcode.wizard.pages.IntimationDetailsPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class SearchGMCInsuredTable extends GBaseTable<GmcMainMemberListDTO>{

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
        table.setContainerDataSource(new BeanItemContainer<GmcMainMemberListDTO>(GmcMainMemberListDTO.class));
        Object[] NATURAL_COL_ORDER = new Object[] {
    		"mainMemberName",
    		"insuredName",
    		"memberId",
    		"employeeId",
    		"age",
    		"idCardNumber",
    	};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.removeGeneratedColumn("select");
		table.addGeneratedColumn("select", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	  Button btnSelect = new Button("select");
		    	  btnSelect.addStyleName("link");
		    	  btnSelect.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	  
		    	  btnSelect.addClickListener(new ClickListener() {
		  			@Override
		  			@SuppressWarnings("unchecked")
		  			public void buttonClick(ClickEvent event) {

		  				GmcMainMemberListDTO selectedInsured = (GmcMainMemberListDTO)itemId;
		  				GmcMainMemberList gmcMainMemberList = selectedInsured.getGmcMainMemberList();
		  				fireViewEvent(IntimationDetailsPresenter.GMC_INSURED_SELECTED, gmcMainMemberList);
		  				
//		  				fireViewEvent(IntimationDetailPage.INSURED_SELECTED, insured);
//		  				IntimationDetailPage intimationDetailsPage =  (IntimationDetailPage) objectMap.get("intimation");
//		  				if(intimationDetailsPage != null) {
//		  					Collection<?> itemIds = intimationDetailsPage.cmbInsuredPatiend.getContainerDataSource().getItemIds();
//		  					intimationDetailsPage.cmbInsuredPatiend.setValue(insured);
//		  				}
		  			}
		  		});
		    	  
		    	  return btnSelect;
		      }
		});
		
		table.setColumnHeader("select", "Select");
		
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
	public void tableSelectHandler(GmcMainMemberListDTO t) {
		// TODO Auto-generated method stub
		
	}
	
	public void removeAllItems(){
		table.removeAllItems();
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "search-gmcinsured-details-";
	}
	protected void setTablesize(){
		table.setPageLength(table.size());
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	
	public void setColumnHeader(){
		
		table.setColumnHeader("mainMemberName","Main Member Name");
		table.setColumnHeader("insuredName","Insured Patient Name");
		table.setColumnHeader("memberId","Main Member ID");
		table.setColumnHeader("employeeId","Employee ID");
		table.setColumnHeader("age","Age");
		table.setColumnHeader("idCardNumber","ID Card No");
	}
}
