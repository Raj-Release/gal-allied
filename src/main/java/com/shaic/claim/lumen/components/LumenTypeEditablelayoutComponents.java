package com.shaic.claim.lumen.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.lumen.LumenDetailsDTO;
import com.shaic.claim.lumen.create.LumenRequestDTO;
import com.shaic.domain.MasterService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class LumenTypeEditablelayoutComponents extends ViewComponent{

	private ComboBox cmbLumenType;
	private ComboBox cmbErrorType;
	private TextArea txtComments;

	public ComboBox getCmbLumenType() {
		return cmbLumenType;
	}

	public void setCmbLumenType(ComboBox cmbLumenType) {
		this.cmbLumenType = cmbLumenType;
	}

	public ComboBox getCmbErrorType() {
		return cmbErrorType;
	}

	public void setCmbErrorType(ComboBox cmbErrorType) {
		this.cmbErrorType = cmbErrorType;
	}

	public TextArea getTxtComments() {
		return txtComments;
	}

	public void setTxtComments(TextArea txtComments) {
		this.txtComments = txtComments;
	}

	private TextField txtAddParticipant;
	private ComboBox cmbClaimParticipant;
	private Button buttonParticipant;
	private Button buttonClaimParticipant;

	@EJB
	private MasterService masterService;

	private VerticalLayout mainVLayout;

	private HorizontalLayout firstHLayout;
	private HorizontalLayout secondHLayout;

	@Inject
	private LapseTable lapseDetails;

	@Inject
	private ParticipantOtherTable participantsOtherDetails;
	
	private LumenRequestDTO lumenObj;

	private List<LumenDetailsDTO> listOfDetailsObj;

	private List<ParticipantsDTO> listOfPaticipants;
	private List<LapseDTO> listOfLapsePaticipants;

	private boolean isOnloadFlag = false;
	private boolean swapTableFlag;

	@SuppressWarnings("unchecked")
	public void init(Map<String, Object> argMapObj, boolean tableButtonFlag) {
		mainVLayout = new VerticalLayout();
		firstHLayout = new HorizontalLayout();
		secondHLayout  = new HorizontalLayout();
		swapTableFlag = tableButtonFlag;
		//firstHLayout.addComponent(buildLayout());		
		mainVLayout.addComponent(buildLayout());
		lumenObj = (LumenRequestDTO)argMapObj.get("lumenObj");
		int i = 0;

		Collection<SelectValue> typeValues = (Collection<SelectValue>) cmbLumenType.getContainerDataSource().getItemIds();
		for (SelectValue s : typeValues) {
			if(lumenObj.getLumenTypeId().longValue() == s.getId().longValue()){
				// the initiateParticipantAndLapseTable method must be called only when user changes the lumen type value. Hence setting the flag as true.
				isOnloadFlag = true;
				SelectValue lumenType = (SelectValue) cmbLumenType.getContainerDataSource().getItemIds().toArray()[i];
				cmbLumenType.setValue(lumenType);
			}
			i++;
		}

//		i = 0;
//		if(lumenObj.getLumenTypeId().longValue() == Long.valueOf(7347L).longValue()){
//			Collection<SelectValue> errValues = (Collection<SelectValue>) cmbErrorType.getContainerDataSource().getItemIds();
//			for (SelectValue s : errValues) {
//				if(lumenObj.getErrorTypeId().longValue() == s.getId().longValue()){
//					SelectValue lumenErrType = (SelectValue) cmbErrorType.getContainerDataSource().getItemIds().toArray()[i];
//					cmbErrorType.setValue(lumenErrType);
//				}
//				i++;
//			}
//		}
		if(lumenObj.getRemarks() == null){
			txtComments.setValue("");	
		}else{
			txtComments.setValue(lumenObj.getRemarks());
		}

		listOfDetailsObj = (List<LumenDetailsDTO>) argMapObj.get("participantObj");
		prepareAllTypeOfParticipants(listOfDetailsObj);

		initiateParticipantAndLapseTable(String.valueOf(cmbLumenType.getValue()), swapTableFlag);

		//setting flag as false, to enable the method call for initiateParticipantAndLapseTable method when user changes the lumenType drop-down.
		isOnloadFlag = false;

		participantsOtherDetails.setLapTable(lapseDetails);
		lapseDetails.setParTable(participantsOtherDetails);

		mainVLayout.addComponent(firstHLayout);
		mainVLayout.addComponent(secondHLayout);
		setCompositionRoot(mainVLayout);
	}

	public AbsoluteLayout buildLayout(){
		cmbLumenType = new ComboBox("Lumen Type");

		BeanItemContainer<SelectValue> lumenContainer = masterService.getLumenType();
		cmbLumenType.setContainerDataSource(lumenContainer);
		cmbLumenType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbLumenType.setItemCaptionPropertyId("value");
		cmbLumenType.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue type = (SelectValue) event.getProperty().getValue();
				if(type != null && type.getId() != null){
					/*if(type.getValue().equals("Hospital Errors")){
						// show hospital errors field
						cmbErrorType.setVisible(true);
					}else{
						// hide hospital errors field
						cmbErrorType.setVisible(false);
					}*/
					if(!isOnloadFlag){
						initiateParticipantAndLapseTable(type.getValue(), swapTableFlag);
					}
				}else{
					/*// if type == null
					// hide hospital errors field
					cmbErrorType.setVisible(false);*/
					
					if(!isOnloadFlag){
						initiateParticipantAndLapseTable("", swapTableFlag);
					}
				}
			}
		});

//		cmbErrorType = new ComboBox("Hospital Errors");
//		BeanItemContainer<SelectValue> errorTypeContainer = masterService.getLumenHospitalErrorTypes();
//		cmbErrorType.setContainerDataSource(errorTypeContainer);
//		cmbErrorType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		cmbErrorType.setItemCaptionPropertyId("value");
//		cmbErrorType.setVisible(false);

		txtComments = new TextArea("Remarks");
		txtComments.setWidth(30, Unit.EM);
		txtComments.setMaxLength(4000);
		handleTextAreaPopup(txtComments,null);

		FormLayout firstForm1 = new FormLayout(cmbLumenType,txtComments);
		firstForm1.setSizeFull();
		firstForm1.setMargin(new MarginInfo(true, false, true, true));

		HorizontalLayout hLayout1 = new HorizontalLayout(firstForm1);
		hLayout1.setSizeFull();

		AbsoluteLayout lumenFields_layout =  new AbsoluteLayout();
		lumenFields_layout.addComponent(hLayout1, "left: 25%; top: 0%;");		
		lumenFields_layout.setWidth("100%");
		lumenFields_layout.setHeight("210px");

		return lumenFields_layout;

	}

	public void initiateParticipantAndLapseTable(String typeValue, boolean swapTableFlag){
		if(firstHLayout.getComponentCount() > 0){
			firstHLayout.removeAllComponents();
		}
		AbsoluteLayout alay = getFormFields(typeValue);
		firstHLayout.addComponent(alay);
		firstHLayout.setWidth("100%");
	
		participantsOtherDetails.init("", false, false);
		participantsOtherDetails.setButtonEnableFlag(swapTableFlag);
		participantsOtherDetails.getTable().refreshRowCache();

		for(ParticipantsDTO rec : listOfPaticipants){
			participantsOtherDetails.getTable().addItem(rec);
		}
		secondHLayout.addComponent(participantsOtherDetails, 0);
		
		lapseDetails.init("", false, false);
		lapseDetails.setButtonEnableFlag(swapTableFlag);
		lapseDetails.getTable().refreshRowCache();

		for(LapseDTO rec : listOfLapsePaticipants){
			lapseDetails.getTable().addItem(rec);
		}
		
		secondHLayout.addComponent(lapseDetails, 1);		
		secondHLayout.setSpacing(true);
		secondHLayout.setMargin(true);
		secondHLayout.setSizeFull();
	}

	private AbsoluteLayout getFormFields(final String typeValue){
		txtAddParticipant = new TextField("Participant Name");
		txtAddParticipant.setWidth("-1");
		txtAddParticipant.setMaxLength(250);
		cmbClaimParticipant = new ComboBox ("Claim Participants");
		cmbClaimParticipant.setWidth("-1");

		DBCalculationService dbCalService = new DBCalculationService();
		BeanItemContainer<SelectValue> lmnTypeSelectValue = dbCalService.getParticipantsFromProcedure(lumenObj.getIntimationNumber());
		cmbClaimParticipant.setContainerDataSource(lmnTypeSelectValue);
		cmbClaimParticipant.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimParticipant.setItemCaptionPropertyId("value");

		buttonParticipant = new Button("Add Participant");
		buttonParticipant.setWidth("-1");
		buttonClaimParticipant = new Button("Add Participant");
		buttonClaimParticipant.setWidth("-1");

		buttonParticipant.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(!typeValue.equals("Claim Processing Errors")){
					if(!StringUtils.isBlank(txtAddParticipant.getValue())){
						addToParticipantsList(txtAddParticipant.getValue());
						txtAddParticipant.setValue("");
					}
				}else{
					if(txtAddParticipant.getValue() != null){
						if(StringUtils.isNotBlank(txtAddParticipant.getValue())){
							addToParticipantsList(txtAddParticipant.getValue());
							txtAddParticipant.setValue("");
						}
					}
				}
			}
		});	

		buttonClaimParticipant.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(!typeValue.equals("Claim Processing Errors")){
					if(cmbClaimParticipant.getValue() != null){
						addToParticipantsList(cmbClaimParticipant.getValue().toString());
					}
				}else{
					if(cmbClaimParticipant.getValue() != null){
						if(StringUtils.isNotBlank(cmbClaimParticipant.getValue().toString())){
							addToParticipantsList(cmbClaimParticipant.getValue().toString());
						}
					}
				}
			}
		});	

		FormLayout flayout = null;

		if(!typeValue.equals("Claim Processing Errors") || typeValue.equals("")){
			flayout = new FormLayout(txtAddParticipant,buttonParticipant);
			flayout.setSizeFull();
			flayout.setMargin(new MarginInfo(true, false, true, true));
		}else{
			flayout = new FormLayout(cmbClaimParticipant,buttonClaimParticipant,txtAddParticipant,buttonParticipant);
			flayout.setSizeFull();
			flayout.setMargin(new MarginInfo(true, false, true, true));
		}	
		
		HorizontalLayout hLayout1 = new HorizontalLayout(flayout);
		hLayout1.setSizeFull();

		AbsoluteLayout participant_layout =  new AbsoluteLayout();
		participant_layout.addComponent(hLayout1, "left: 25%; top: 0%;");		
		participant_layout.setWidth("100%");
		participant_layout.setHeight("210px");
		
		return participant_layout;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addToParticipantsList(String empIdWithEmpName){
		Set<ParticipantsDTO> newHolder = new HashSet<ParticipantsDTO>();
		Set<String> participants = new HashSet<String>();
		Collection<?> parlist = getParticipantsOtherDetails().getTable().getItemIds();

		if(parlist != null && !parlist.isEmpty()){
			Property property = null;
			for(Object  rec : parlist){
				property = getParticipantsOtherDetails().getTable().getContainerProperty(rec, "empName");
				if(property.getValue() != null){
					participants.add(String.valueOf(property.getValue()));
				}
			}
		}
		participants.add(empIdWithEmpName);
		
		if(!participants.isEmpty()){
			for (String s : participants) {
				newHolder.add(new ParticipantsDTO(s));
			}
		}
		
		List<ParticipantsDTO> justConvertionToList = new ArrayList<ParticipantsDTO>(newHolder);
		Page<ParticipantsDTO> participantPage = new Page<ParticipantsDTO>();
		participantPage.setPageItems(justConvertionToList);
		participantPage.setTotalRecords(justConvertionToList.size());
		participantPage.setTotalList(justConvertionToList);
		participantsOtherDetails.setTableList(participantPage.getTotalList());
		participantsOtherDetails.setSubmitTableHeader();
		
	}

	public void prepareAllTypeOfParticipants(List<LumenDetailsDTO> listOfDetailsObj){
		listOfPaticipants = new ArrayList<ParticipantsDTO>();
		listOfLapsePaticipants = new ArrayList<LapseDTO>();
		LapseDTO lapseObj =  null;
		ParticipantsDTO participantsObj =  null;
		if(listOfDetailsObj != null && !listOfDetailsObj.isEmpty()){
			for(LumenDetailsDTO rec : listOfDetailsObj){
				if(rec.getParticipantType().equals("Lapse")){
					lapseObj = new LapseDTO(rec.getEmployeeName());
					listOfLapsePaticipants.add(lapseObj);
				}else{
					participantsObj = new ParticipantsDTO(rec.getEmployeeName());
					listOfPaticipants.add(participantsObj);
				}
			}
		}
	}

	public LapseTable getLapseDetails() {
		return lapseDetails;
	}

	public void setLapseDetails(LapseTable lapseDetails) {
		this.lapseDetails = lapseDetails;
	}

	public ParticipantOtherTable getParticipantsOtherDetails() {
		return participantsOtherDetails;
	}

	public void setParticipantsOtherDetails(ParticipantOtherTable participantsOtherDetails) {
		this.participantsOtherDetails = participantsOtherDetails;
	}

	public void makeFieldsReadOnly(boolean flag){
		
		// Making all the fields ReadOnly Based on screen.
		cmbLumenType.setReadOnly(flag);
//		cmbErrorType.setReadOnly(flag);
		txtComments.setReadOnly(flag);

		txtAddParticipant.setReadOnly(flag);
		cmbClaimParticipant.setReadOnly(flag);
		buttonParticipant.setEnabled(!flag);
		buttonClaimParticipant.setEnabled(!flag);
	}
	
	@SuppressWarnings("unused")
	public  void handleTextAreaPopup(TextArea searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForRedraft(searchField, getShortCutListenerForRemarks(searchField));

	}

	public  void handleShortcutForRedraft(final TextArea textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);

			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				textField.removeShortcutListener(shortcutListener);

			}
		});
	}
	
	private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Level I Remarks",KeyCodes.KEY_F8,null) {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "static-access", "deprecation" })
			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();

				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setStyleName("Boldstyle"); 
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(4000);
				if(txtComments.isReadOnly()){
					txtArea.setReadOnly(true);
				}else{
					txtArea.setReadOnly(false);
				}
				txtArea.setRows(25);

				txtArea.addValueChangeListener(new ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();
				String strCaption = "Level I Remarks";

				dialog.setCaption(strCaption);

				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};

		return listener;
	}

}
