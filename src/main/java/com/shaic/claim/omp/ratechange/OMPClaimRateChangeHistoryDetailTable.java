package com.shaic.claim.omp.ratechange;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.omp.OMPIntimationService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

public class OMPClaimRateChangeHistoryDetailTable extends GBaseTable<OMPClaimRateChangeTableDto>{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private OMPClaimRateChangeHistory viewClaimRateDetails;
	
	@EJB
	private OMPIntimationService intimationService;
	
	private OMPClaimRateChangeTableDto bean;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"conversionValue","dateofModification","modifyby","processingStage"};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<OMPClaimRateChangeTableDto>(OMPClaimRateChangeTableDto.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"conversionValue","dateofModification","modifyby","processingStage"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
		
	}

	@Override
	public void tableSelectHandler(OMPClaimRateChangeTableDto t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "ompclaimrate-details-";
	}
	
	
	public void getViewClaimHistory(String registrationBean) {

		final OMPIntimation intimation = intimationService
				.getIntimationByNo(registrationBean);
//		bean.setIntimationId(intimation.getIntimationId()); 
		if (intimation != null) {        //!=
//			viewClaimRateDetails.init(intimation);
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("");
			popup.setWidth("75%");
			popup.setHeight("75%");
			popup.setContent(viewClaimRateDetails);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				/**
			 * 
			 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		}else{
			getErrorMessage("History is not available");
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

}
