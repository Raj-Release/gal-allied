package com.shaic.claim.doctorinternalnotes;

import java.util.List;
import java.util.Map;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchInternalNotesTable extends GBaseTable<NewIntimationDto> {

	private static final long serialVersionUID = 1L;

	public static final Object[] VISIBLE_COL_ORDER = new Object[] {"serialNumber",
			"intimationId", "claimType.value", "policy.policyNumber", "cpuAddress", "insuredPatient.insuredName", "hospitalName"};

	@Override
	public void removeRow() {
		
		table.removeAllItems();

	}

	@Override
	public void initTable() {
		
		BeanItemContainer<NewIntimationDto> newIntimationDtoContainer = new BeanItemContainer<NewIntimationDto>(NewIntimationDto.class);
		
		newIntimationDtoContainer
			.addNestedContainerProperty("policy.policyNumber");
		newIntimationDtoContainer
		.addNestedContainerProperty("claimType.value");
		newIntimationDtoContainer
			.addNestedContainerProperty("insuredPatient.insuredName");
		table.setContainerDataSource(newIntimationDtoContainer);
		table.setVisibleColumns(VISIBLE_COL_ORDER);
		table.setHeight("320px");
	}

	@Override
	public void tableSelectHandler(NewIntimationDto t) {
			
			try{
				String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
				fireViewEvent(MenuPresenter.PROCESS_INTERNAL_NOTES , t);
				
			}catch(Exception e){
				
				e.printStackTrace();
			}
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-internal-notes-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		table.setPageLength(4);
		int length =table.getPageLength();
		if(length>=5){
//			
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
	 
	    public List<NewIntimationDto> getValues(){
	    	return (List<NewIntimationDto>) table.getItemIds();
	    }	 
}
