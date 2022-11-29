package com.shaic.claim.OMPProcessOmpClaimProcessor.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;

public class OMPClaimDischargeDetailsPage extends ViewComponent {
	private static final long serialVersionUID = 5215840451695776122L;
	private VerticalLayout mainLayout;
	private FormLayout frmLayout;
	private HorizontalLayout buttonHorlayout;
	
//	private TextArea rejectionRemarks;
//	private ComboBoxMultiselect cmbEmpName;
	
	private OptionGroup generateDVOption;
	private TextArea remarks;
	private Button previewDVButton;
	private Button previewCLButton;
	private OptionGroup generateCLOption;
	private DateField dvReceivedDate;
	private Button submitButton;
	private Button closeButton;
	
//	3221
	
	private Window popupWindow;	

	public Window getPopupWindow() {
		return popupWindow;
	}

	public void setPopupWindow(Window popupWindow) {
		this.popupWindow = popupWindow;
	}
	
	private OMPClaimCalculationViewTableDTO dtoBean;
	
	private OMPClaimProcessorDTO processorDTO;
	
	public void init(OMPClaimCalculationViewTableDTO rodDTO, OMPClaimProcessorDTO argProcessorDTO){
		dtoBean = rodDTO;
		processorDTO = argProcessorDTO;
		dtoBean.setClaimProcessorDTO(processorDTO);
		mainLayout = new VerticalLayout();
		frmLayout = new FormLayout();
		buttonHorlayout = new HorizontalLayout();
		
//		rodDTO.setGenDisVoucherFlag((Boolean)generateDVOption.getValue());
//		rodDTO.setDisVoucherRemarks(remarks.getValue());
//		rodDTO.setGenCoveringLetterFlag((Boolean)generateCLOption.getValue());
		
		generateDVOption = new OptionGroup("Generate Discharge Voucher");
		generateDVOption.addItems(getRadioButtonOptions());
		generateDVOption.setItemCaption(true, "Yes");
		generateDVOption.setItemCaption(false, "No");
		generateDVOption.setStyleName("horizontal");
		generateDVOption.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean dvOption = (Boolean)event.getProperty().getValue();
				if(dvOption){
					remarks.setEnabled(Boolean.TRUE);
					previewDVButton.setEnabled(Boolean.TRUE);
					previewCLButton.setEnabled(Boolean.TRUE);
					generateCLOption.setValue(true);
					generateCLOption.setEnabled(Boolean.FALSE);
				}else{
					remarks.setValue("");
					remarks.setEnabled(Boolean.FALSE);
					previewDVButton.setEnabled(Boolean.FALSE);
					previewCLButton.setEnabled(Boolean.FALSE);
					generateCLOption.setValue(null);
					generateCLOption.setEnabled(Boolean.FALSE);
				}
			}
		});	
		
		if(rodDTO.getGenDisVoucherFlag() != null){
			if(rodDTO.getGenDisVoucherFlag().equals("Y")){
				generateDVOption.select(true);
			}else if(rodDTO.getGenDisVoucherFlag().equals("N")){
				generateDVOption.select(false);
			}else{
				generateDVOption.select(null);
			}
		}else{
			generateDVOption.select(null);
		}
		
		remarks = new TextArea("Discharge Voucher Remarks");
		remarks.setCaptionAsHtml(true);
		remarks.setRows(4);
		remarks.setColumns(25);
		remarks.setMaxLength(4000);
		handleTextAreaPopup(remarks,null);
		
		if(!StringUtils.isBlank(rodDTO.getDisVoucherRemarks())){
			remarks.setValue(rodDTO.getDisVoucherRemarks());
		}else{
			remarks.setValue("");
		}
		
		previewDVButton = new Button("Preview Dicharge Voucher");
		previewDVButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(validatepage()){
					rodDTO.setDisVoucherRemarks(remarks.getValue());
					generateLetterPreview(dtoBean, "DichargeVoucher");
				}else{
					String eMsg = "Please fill the Discharge Voucher Remarks  and Generate Discharge Voucher Flag to proceed";
					MessageBox.createError()
			    	.withCaptionCust("Errors").withHtmlMessage(eMsg)
			        .withOkButton(ButtonOption.caption("OK")).open();
				}
				
			}
		});
		
		
		previewCLButton = new Button("Preview Covering Letter");
		previewCLButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				generateLetterPreview(dtoBean, "CoveringLetter");
			}
		});
		
		generateCLOption = new OptionGroup("Discharge Voucher Covering Letter");
		generateCLOption.addItems(getRadioButtonOptions());
		generateCLOption.setItemCaption(true, "Yes");
		generateCLOption.setItemCaption(false, "No");
		generateCLOption.setStyleName("horizontal");
		
		if(rodDTO.getGenDisVoucherFlag() != null){
			generateDVOption.setValue(rodDTO.getGenDisVoucherFlag());
		}
		
		if(!StringUtils.isBlank(rodDTO.getDisVoucherRemarks())){
			remarks.setValue(rodDTO.getDisVoucherRemarks());
		}
		
		if(rodDTO.getGenCoveringLetterFlag() != null){
			generateCLOption.setValue(rodDTO.getGenCoveringLetterFlag());
			generateCLOption.setEnabled(Boolean.FALSE);
		}
		
		dvReceivedDate = new DateField("Discharge Voucher Received Date");
		if(rodDTO.getDvReceivedDate() != null ){
			dvReceivedDate.setValue(rodDTO.getDvReceivedDate());
		}
		dvReceivedDate.setDateFormat("dd-MM-yyyy");
		
		if(rodDTO.getProcessorStatus().intValue() == 3221){
			dvReceivedDate.setVisible(Boolean.TRUE);
		}else{
			dvReceivedDate.setVisible(Boolean.FALSE);
		}
		
		generateCLOption.setVisible(false);
		
		frmLayout.addComponent(generateDVOption);
		frmLayout.addComponent(remarks);
		frmLayout.addComponent(previewDVButton);
		frmLayout.addComponent(previewCLButton);
		frmLayout.addComponent(generateCLOption);
		frmLayout.addComponent(dvReceivedDate);
		
		
		submitButton = new Button("Submit");
		submitButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(validatepage()){
					rodDTO.setGenDisVoucherFlag((Boolean)generateDVOption.getValue());
					rodDTO.setDisVoucherRemarks(remarks.getValue());
					rodDTO.setGenCoveringLetterFlag((Boolean)generateCLOption.getValue());
					if(rodDTO.getProcessorStatus().intValue() == 3221){
						rodDTO.setDvReceivedDate(dvReceivedDate.getValue());
					}
					getPopupWindow().close();
				}else{
					String eMsg = "Please fill the Discharge Voucher Remarks  and Generate Discharge Voucher Flag to proceed";
					MessageBox.createError()
			    	.withCaptionCust("Errors").withHtmlMessage(eMsg)
			        .withOkButton(ButtonOption.caption("OK")).open();
				}
			}
		});
		
		closeButton = new Button("Close");
		closeButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				getPopupWindow().close();
			}
		});
		
		buttonHorlayout.addComponent(submitButton);
		buttonHorlayout.addComponent(closeButton);
		buttonHorlayout.setHeight("75px");
		buttonHorlayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		buttonHorlayout.setComponentAlignment(closeButton, Alignment.MIDDLE_CENTER);
		
		mainLayout.addComponent(frmLayout);
		mainLayout.addComponent(buttonHorlayout);
		mainLayout.setComponentAlignment(frmLayout, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(buttonHorlayout, Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(true);
		mainLayout.setWidth("100%");
		setCompositionRoot(mainLayout);
	}
	
	private boolean validatepage(){
		boolean flag = false;
		if(generateDVOption.getValue() == null){
			flag = false;
		}else{
			flag = true;
		}
		
		if(generateDVOption.getValue() != null && ((Boolean)generateDVOption.getValue()).booleanValue() == true){
			if(StringUtils.isBlank(remarks.getValue())){
				flag = false;
			}else{
				flag = true;
			}

			if(generateCLOption.getValue() == null){
				flag = false;
			}else{
				flag = true;
			}
		}
		return flag;
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
		ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {

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
				txtArea.setReadOnly(false);
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

				String strCaption = "Remarks";

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
	
	private void generateLetterPreview(OMPClaimCalculationViewTableDTO argObj, String letterName){
		DocumentGenerator letterObj = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		reportDto.setClaimId(null);
		List<OMPClaimCalculationViewTableDTO> letterDTOList = new ArrayList<OMPClaimCalculationViewTableDTO>();
		letterDTOList.add(argObj);		
		reportDto.setBeanList(letterDTOList);
		String generatedFilePath = "";
		if(letterName.equals("DichargeVoucher")){
			generatedFilePath = letterObj.generatePdfDocument("OMPDischargeVoucher", reportDto);
		}else if(letterName.equals("CoveringLetter")){
			if(dtoBean.getFinalApprovedAmtInr().longValue() > 100000){
				generatedFilePath = letterObj.generatePdfDocument("OMPCoveringLetterAboveOneLakh", reportDto);
			}else{
				generatedFilePath = letterObj.generatePdfDocument("OMPCoveringLetterLessThanOneLakh", reportDto);
			}
		}
//		System.out.println("In PReview : "+generatedFilePath);
		Window window = new Window();
		window.setWidth("90%");
		window.setHeight("90%");
		BrowserFrame e = null;
		if(letterName.equals("DichargeVoucher")){
			e = new BrowserFrame("DisCharge Voucher"+" Preview", new FileResource(new File(generatedFilePath)));
		}else if(letterName.equals("CoveringLetter")){
			e = new BrowserFrame("Covering Letter"+" Preview", new FileResource(new File(generatedFilePath)));
		}
		e.setWidth("100%");
		e.setHeight("100%");
		window.setContent(e);
		window.center();
		window.setModal(true);
		UI.getCurrent().addWindow(window);
	}
	
	protected Collection<Boolean> getRadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}

}
