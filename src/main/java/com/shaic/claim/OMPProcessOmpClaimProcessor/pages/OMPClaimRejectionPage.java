package com.shaic.claim.OMPProcessOmpClaimProcessor.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.domain.MastersEvents;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.omp.OMPClaimService;
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
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;

public class OMPClaimRejectionPage extends ViewComponent {
	private static final long serialVersionUID = 5215840451695776122L;
	private VerticalLayout mainLayout;
	private FormLayout frmLayout;
	private HorizontalLayout buttonHorlayout;
	
	private TextArea rejectionRemarks;
	private ComboBoxMultiselect cmbReject;
	
	private Button previewRLButton;
	private Button submitButton;
	private Button closeButton;
	
	private Window popupWindow;	
	
	@Inject
	private OMPClaimService ompClaimService;

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
		
		cmbReject = new ComboBoxMultiselect("Reason Rejection <b style= 'color: red'>*</b>");
		cmbReject.setCaptionAsHtml(true);
		cmbReject.setShowSelectedOnTop(true);
		
		OMPClaim claimrec = ompClaimService.getOMPClaimByKey(argProcessorDTO.getClaimDto().getKey());
		OMPReimbursement rodrec = ompClaimService.getOMPRodByKey(rodDTO.getRodKey());
		Long productKey = claimrec.getIntimation().getPolicy().getProduct().getKey();
		MastersEvents eventrec = ompClaimService.getEventTypeByKey(processorDTO.getEventCode().getId());
		String eventCode = eventrec.getEventCode();
		BeanItemContainer<SelectValue> rejectionCategoryList = ompClaimService.getRejectionCategories(productKey, eventCode);
		cmbReject.setContainerDataSource(rejectionCategoryList);
		cmbReject.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReject.setItemCaptionPropertyId("value");	
		if(rodDTO.getRejectionIds() != null){
			cmbReject.setValue(rodDTO.getRejectionIds());
		}
		
		rejectionRemarks = new TextArea("Rejection Remarks <b style= 'color: red'>*</b>");
		rejectionRemarks.setCaptionAsHtml(true);
		rejectionRemarks.setRows(4);
		rejectionRemarks.setColumns(25);
		rejectionRemarks.setMaxLength(4000);
		if(!StringUtils.isBlank(rodDTO.getRejectionRemarks())){
			rejectionRemarks.setValue(rodDTO.getRejectionRemarks());
		}else{
			//  Commented below line as per Document of CR2019176 
			/*rejectionRemarks.setValue("Policy conditions under above section read as follows:");*/    
		}
		handleTextAreaPopup(rejectionRemarks,null);
		
		previewRLButton = new Button("Preview Rejection Letter");
		previewRLButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(validatepage()){
					dtoBean.setRejectionRemarks(rejectionRemarks.getValue());
					generateLetterPreview(dtoBean);
				}else{
					String eMsg = "Please fill the Reason Rejection  & Rejection Remarks to proceed";
					MessageBox.createError()
			    	.withCaptionCust("Errors").withHtmlMessage(eMsg)
			        .withOkButton(ButtonOption.caption("OK")).open();
				}
			}
		});
		
		
		
		frmLayout.addComponent(cmbReject);
		frmLayout.addComponent(rejectionRemarks);
		frmLayout.addComponent(previewRLButton);
		
		submitButton = new Button("Submit");
		submitButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(validatepage()){
					dtoBean.setRejectionRemarks(rejectionRemarks.getValue());
					dtoBean.setRejectionIds(cmbReject.getValue());
					getPopupWindow().close();
				}else{
					String eMsg = "Please fill the Reason Rejection  & Rejection Remarks to proceed";
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
		
		//IMSSUPPOR-28644
		if(rodrec != null){
			if(rodrec.getStatus() != null){
				if(((rodrec.getStatus().getKey().intValue() == 3181) || (rodrec.getStatus().getKey().intValue() == 3183))){
					//if(dtoBean.getReconsiderFlag() != null && dtoBean.getReconsiderFlag().equals("N")){
					previewRLButton.setEnabled(Boolean.FALSE);
					submitButton.setEnabled(Boolean.FALSE);
					/*}else{
					previewRLButton.setEnabled(Boolean.TRUE);
					submitButton.setEnabled(Boolean.TRUE);
				}*/
				}
			}else if(rodrec.getStatus() == null){
				previewRLButton.setEnabled(Boolean.TRUE);
				submitButton.setEnabled(Boolean.TRUE);
			}
		}
		
		buttonHorlayout.addComponent(submitButton);
		buttonHorlayout.addComponent(closeButton);
		buttonHorlayout.setHeight("75px");
		buttonHorlayout.setSpacing(true);
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
		boolean flag = true;
		if(StringUtils.isBlank(rejectionRemarks.getValue())){
			flag = false;
		}
		
		HashSet temp = new HashSet<>((Collection) cmbReject.getValue());
		if(cmbReject.getValue() != null && temp.isEmpty()){
			flag = false;
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

				String strCaption = "Rejection Remarks";

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
	
	private void generateLetterPreview(OMPClaimCalculationViewTableDTO argObj){
		DocumentGenerator letterObj = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		reportDto.setClaimId(null);
		List<OMPClaimCalculationViewTableDTO> letterDTOList = new ArrayList<OMPClaimCalculationViewTableDTO>();
		letterDTOList.add(argObj);		
		reportDto.setBeanList(letterDTOList);
		String generatedFilePath = letterObj.generatePdfDocument("OMPRejectionLetter", reportDto);
		System.out.println("In PReview : "+generatedFilePath);
		Window window = new Window();
		window.setWidth("90%");
		window.setHeight("90%");
		BrowserFrame e = new BrowserFrame("OMP_Claim_Rejection_letter"+" Preview", new FileResource(new File(generatedFilePath)));
		e.setWidth("100%");
		e.setHeight("100%");
		window.setContent(e);
		window.center();
		window.setModal(true);
		UI.getCurrent().addWindow(window);
	}

}
