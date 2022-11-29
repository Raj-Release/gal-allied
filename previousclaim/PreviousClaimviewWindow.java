package com.shaic.claim.previousclaim;

import java.util.List;

import javax.annotation.PostConstruct;

import com.google.inject.Inject;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
<<<<<<< HEAD
import com.vaadin.ui.VerticalLayout;
=======
import com.vaadin.v7.ui.VerticalLayout;
>>>>>>> ccd5ce4a0dbca1a78d4b18ee9f9b548659c38081
import com.vaadin.ui.Window;

public class PreviousClaimviewWindow extends Window {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7181320560132564580L;
	
	private VerticalLayout mainlayout;
	
	private CheckBox policywise;
	
	private CheckBox insuredwise;
	
	private ComboBox yearwise;
	
	@Inject 
	private PreviousClaimViewPage previousclaimviewPage;
	
	private List<PreviousClaimsTableDTO> previousClaimTableDTO;

	@PostConstruct
	public void init()
	{
	
		setWidth("800px");
		setHeight("280px");
		setModal(true);
		setClosable(true);
		setResizable(true);
	
		setCaption("View PreviousCliam Details");
//		previousclaimviewPage.init(previousClaimTableDTO);
		
		setContent(previousclaimviewPage);
		
	}

	

}
