package com.claim.view.document;


//import javax.enterprise.inject.Instance;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextArea;
//import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
//import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ViewdocumentdetailsPage extends ViewComponent {

	private static final long serialVersionUID = 9068538802949873636L;

	private ComboBox documentsrcvdfrom;

	private DateField documentsrcvddate;

	private TextField personemailID;

	// private VerticalLayout vlayout;

	private ComboBox modeofreceipt;

	private TextField personContactNumber;
	
	private TextArea additionalRemarks;

	private VerticalLayout vlayout;

	private FormLayout leftformlayout;

	private FormLayout rightformlayout;
	
	private FormLayout arealayout;

	private HorizontalLayout hlayout;
	
	@Inject
	private ViewdocumentdetailTable viewDocumentTableInstance;
	
	//private ViewdocumentdetailTable viewDocumentTable;
	
	//private List<DocumentCheckListDTO> documentCheckListDTOList;
	public void init(List<ViewDocumentDetailsDTO> viewDocumentDetailsDTOsList, List<DocumentCheckListDTO> documentCheckListDTOList) {
		
		viewDocumentTableInstance.init("",false,false);
//		for (DocumentCheckListDTO documentCheckListDTO : documentCheckListDTOList) {
//			viewDocumentTableInstance.addBeanToList(documentCheckListDTO);
//		}
		viewDocumentTableInstance.setTableList(documentCheckListDTOList);
		
		setHeight(750, Unit.PIXELS);
		
		hlayout = new HorizontalLayout();
		hlayout.setSpacing(true);
		documentsrcvdfrom = new ComboBox("Documents Recieved From");
		documentsrcvdfrom.setReadOnly(false);
		
		if(viewDocumentDetailsDTOsList != null && ! viewDocumentDetailsDTOsList.isEmpty()){
		ViewDocumentDetailsDTO viewDocumentDetailsDTO = viewDocumentDetailsDTOsList.get(0);
		
		
		final BeanItemContainer<SelectValue> selectValueContainer2 = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainer2.addBean(viewDocumentDetailsDTO.getReceivedFrom());
		documentsrcvdfrom.setContainerDataSource(selectValueContainer2);
		documentsrcvdfrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		documentsrcvdfrom.setItemCaptionPropertyId("value");
		
		documentsrcvdfrom.setValue(viewDocumentDetailsDTO.getReceivedFrom());
		}
		documentsrcvdfrom.setReadOnly(true);

		leftformlayout = new FormLayout();
		rightformlayout = new FormLayout();
		
		
		documentsrcvdfrom.setVisible(true);
		leftformlayout.addComponent(documentsrcvdfrom);

		documentsrcvddate = new DateField("Documents Recieved Date");
		documentsrcvddate.setDateFormat("dd/MM/yyyy");
		leftformlayout.addComponent(documentsrcvddate);
		documentsrcvddate.setReadOnly(false);

		modeofreceipt = new ComboBox("Mode of Reciept");
		
		modeofreceipt.setReadOnly(false);
		
		personContactNumber = new TextField("Acknowledge Contact Number");
		personContactNumber.setReadOnly(false);
		
		personemailID = new TextField("Email ID");
		personemailID.setNullRepresentation("-");
		personemailID.setReadOnly(false);
		
		additionalRemarks=new TextArea("Additional Documents Record");
		additionalRemarks.setReadOnly(false);
		
		if(viewDocumentDetailsDTOsList != null && ! viewDocumentDetailsDTOsList.isEmpty()){
		ViewDocumentDetailsDTO viewDocumentDetailsDTO = viewDocumentDetailsDTOsList.get(0);
		
		
		final BeanItemContainer<SelectValue> selectValueContainer2 = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainer2.addBean(viewDocumentDetailsDTO.getModeOfReceipt());
		modeofreceipt.setContainerDataSource(selectValueContainer2);
		modeofreceipt.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		modeofreceipt.setItemCaptionPropertyId("value");
		
		modeofreceipt.setValue(viewDocumentDetailsDTO.getModeOfReceipt());
		documentsrcvddate.setValue(viewDocumentDetailsDTO.getDocumentReceivedDate());
		//personContactNumber.setValue(viewDocumentDetailsDTO.getPersonContactNumber());
		personContactNumber.setValue(viewDocumentDetailsDTO.getPersonContactNumber().toString());
		personemailID.setValue(viewDocumentDetailsDTO.getPersonemailID());
		additionalRemarks.setValue(viewDocumentDetailsDTO.getAdditionalRemarks());
		additionalRemarks.setNullRepresentation("--");
		
		
		}
		documentsrcvdfrom.setReadOnly(true);
		leftformlayout.addComponent(modeofreceipt);
		documentsrcvddate.setReadOnly(true);
		additionalRemarks.setReadOnly(true);
		personContactNumber.setReadOnly(true);
		rightformlayout.addComponent(personContactNumber);

		personemailID.setReadOnly(true);
		rightformlayout.addComponent(personemailID);
		

		hlayout.addComponent(leftformlayout);
		hlayout.addComponent(rightformlayout);
		hlayout.setSpacing(true);
		hlayout.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		//hlayout.addComponent(viewDocumentTable);
		
		arealayout=new FormLayout();
		arealayout.setSpacing(true);
		arealayout.addComponent(additionalRemarks);
		
		
		vlayout = new VerticalLayout(hlayout,viewDocumentTableInstance,arealayout);
		vlayout.setSpacing(true);
		vlayout.setSizeFull();
		
		setCompositionRoot(vlayout);

	}

	public ComboBox getModeofreceipt() {
		return modeofreceipt;
	}

	public void setModeofreceipt(ComboBox modeofreceipt) {
		this.modeofreceipt = modeofreceipt;
	}

	public TextField getPersonemailID() {
		return personemailID;
	}

	public void setPersonemailID(TextField personemailID) {
		this.personemailID = personemailID;
	}

	public TextField getPersonContactNumber() {
		return personContactNumber;
	}

	public void setPersonContactNumber(TextField personContactNumber) {
		this.personContactNumber = personContactNumber;
	}

}
