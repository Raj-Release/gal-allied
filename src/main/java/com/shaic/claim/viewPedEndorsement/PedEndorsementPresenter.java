package com.shaic.claim.viewPedEndorsement;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(PedEndorsementView.class)
public class PedEndorsementPresenter  extends AbstractMVPPresenter<PedEndorsementView> {

//
//@EJB
//PedService pedService;




	
	public static final String SEARCH_BUTTON_CLICK = "ptmrSearchClick";
	
//	private List<SearchAcknowledgeHospitalCommunicationTableDTO> searchAcknowledgeHospitalCommunicationTableDTO;
	
	@SuppressWarnings({ "deprecation"})
	public void searchClick(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
			
			
					//view.list(PedEndorsementMapper.getSearchProcessTranslationTableDTO());						
//		
	}
	
	 @Override
	public void viewEntered() {
		
	}

}
