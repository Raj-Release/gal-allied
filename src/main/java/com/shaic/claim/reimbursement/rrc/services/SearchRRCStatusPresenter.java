
/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;

/**
 * @author ntv.vijayar
 *
 */

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.reimbursement.rrc.detailsPage.ViewRRCHistory;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;


@ViewInterface(SearchRRCStatusView.class)
public class SearchRRCStatusPresenter extends AbstractMVPPresenter<SearchRRCStatusView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK_RRC_STATUS = "doStatusForRRC";
	
	public static final String SHOW_RRC_STATUS_VIEW = "show_rrc_status_view";
	
	public static final String DOWNLOAD_EXCEL = "download_excel_status_rrc";
	
	
	@EJB
	private SearchRRCStatusService searchService;
	
	@Inject
	private ViewRRCHistory viewRRCHistory;
	

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK_RRC_STATUS) final ParameterDTO parameters) {
		
		SearchRRCStatusFormDTO searchFormDTO = (SearchRRCStatusFormDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	
	@SuppressWarnings({ "deprecation" })
	public void getdownloadTableData(@Observes @CDIEvent(DOWNLOAD_EXCEL) final ParameterDTO parameters) {
		
		//searchService.getDownloadTableData();
	}
	
	@SuppressWarnings({ "deprecation" })
	public void showSearchView(@Observes @CDIEvent(SHOW_RRC_STATUS_VIEW) final ParameterDTO parameters) {
		
		
		RRCDTO rrcDTO = (RRCDTO) parameters.getPrimaryParameter();
		Window popup = new com.vaadin.ui.Window();
		
		
		
		viewRRCHistory.init(rrcDTO,popup);
		viewRRCHistory.initPresenter(SHAConstants.SEARCH_RRC_REQUEST);
		viewRRCHistory.getContent();
		
		popup.setCaption("View RRC History");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(viewRRCHistory);
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
	}
	
	
	
	
	
	
	@Override
	public void viewEntered() {
		
		
	}

}

