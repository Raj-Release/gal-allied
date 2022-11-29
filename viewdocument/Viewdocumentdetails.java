package com.shaic.claim.viewdocument;

//import com.vaadin.v7.ui.TextField;
//import java.util.Date;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.vaadin.ui.Window;
//import com.google.inject.Inject;
//import com.google.inject.Inject;

public class Viewdocumentdetails extends Window {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	//private static final int DocumentCheckListDTO = 0;
	
	
	
	@Inject 
	private ViewdocumentdetailsPage viewDocumentdetailsPage;
	
	private List<DocumentCheckListDTO> documentCheckListDTOList;
	
	private List<ViewDocumentDetailsDTO> viewDocumentDetailsDTOsList;
	
	
	@PostConstruct
	public void init()
	{
		
		setWidth("800px");
		setHeight("280px");
	
		setModal(true);
		setClosable(false);
		setResizable(true);

		//Vaadin8-setImmediate() setImmediate(false);

//		setImmediate(false);

		setCaption("View Document Details");
		viewDocumentdetailsPage.init(viewDocumentDetailsDTOsList,documentCheckListDTOList);		
		setContent(viewDocumentdetailsPage);
		
	}
	
}

