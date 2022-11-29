package com.shaic.claim.pincodemapping;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;


@ViewInterface(SearchPinCodeView.class)
public class SearchPinCodePresenter extends AbstractMVPPresenter<SearchPinCodeView> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private SearchPinCodeService searchService;
	
	public static final String SEARCH_BUTTON_CLICK = "doSearchTablePinCode";
	
	public static final String BUILD_SUBMIT_LAYOUT = "Build_submit_layout_PinCode";
	
	public static final String SUBMIT_PIN_CODE = "Submit_PinCode";
	
	public static final String SHOW_PINCODE_HISTORY_VIEW = "show_pincode_history_view";
	
	@Inject
	PinCodeZoneClassHistory zoneClassHistory;
	
	
	@EJB
	private SearchPinCodeService searchDoctorDetailsService;

	private Window popup;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchPinCodeDTO searchFormDTO = (SearchPinCodeDTO) parameters.getPrimaryParameter();

		
		view.getResultList(searchDoctorDetailsService.search(searchFormDTO));
	}
	
	
	public void generateLayoutBasedOnType(@Observes @CDIEvent(BUILD_SUBMIT_LAYOUT) final ParameterDTO parameters) {
		
		view.buildSubmitLayout();
	}
	
	public void submitPinCodeZoneClass(@Observes @CDIEvent(SUBMIT_PIN_CODE) final ParameterDTO parameters){
		List<SearchPinCodeTableDTO> tableDTOList = (List<SearchPinCodeTableDTO>) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		
		Integer count = searchService.updatePincodeMaster(tableDTOList,userName);
		view.buildSuccessLayout(count);
	}
	
	@SuppressWarnings({ "deprecation" })
	public void showHistoryDetailsView(@Observes @CDIEvent(SHOW_PINCODE_HISTORY_VIEW) final ParameterDTO parameters) {
		
		SearchPinCodeTableDTO tableDTO = (SearchPinCodeTableDTO) parameters.getPrimaryParameter();
		
		
		zoneClassHistory.bindFieldGroup(Long.valueOf(tableDTO.getPinCode()));
//		UI.getCurrent().addWindow(a_viewBalenceSumInsured);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("View History");
		popup.setWidth("65%");
		popup.setHeight("50%");
		popup.setContent(zoneClassHistory);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
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
