package com.shaic.claim.doctorinternalnotes;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class InternalNotesPageViewImpl extends AbstractMVPView implements
		InternalNotesPageView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private RevisedCashlessCarousel preauthIntimationDetailsCarousel;

	private NewIntimationDto newIntimationDto;

	private ClaimDto claimDTO;
	
	private PreauthDTO preauthDTO;

	@Inject
	private ViewDetails viewDetails;

	private BeanFieldGroup<NewIntimationDto> binder;

	private TextArea doctorInternalRemarkstxta;

	private VerticalLayout mainVertical;

	private Button btnSubmit;

	private Button btnCancel;

	private HorizontalLayout buttonHorLayout;

	public void initView(NewIntimationDto newIntimationDto, ClaimDto claimDto) {
		this.newIntimationDto = newIntimationDto;
		this.claimDTO = claimDto;
		

		this.binder = new BeanFieldGroup<NewIntimationDto>(NewIntimationDto.class);
		this.binder.setItemDataSource(this.newIntimationDto);
		setHeight("100%");
		if (newIntimationDto != null && claimDTO != null) {
			preauthIntimationDetailsCarousel.init(this.newIntimationDto, claimDTO,
					"Update Internal Note",newIntimationDto.getDiagnosis());
		}
		
		btnSubmit = new Button("Submit");
		btnSubmit.setStyleName(ValoTheme.BUTTON_FRIENDLY);

		btnCancel = new Button("Cancel");
		btnCancel.setStyleName(ValoTheme.BUTTON_DANGER);
		
		buttonHorLayout = new HorizontalLayout(btnSubmit, btnCancel);
		buttonHorLayout.setSpacing(true);
		
		if(claimDTO.getRodKeyForDischargeDate() != null){
			viewDetails.initView(this.newIntimationDto.getIntimationId(), claimDTO.getRodKeyForDischargeDate(), ViewLevels.INTIMATION,"Doctor Internal Remarks");
		}
		else{
		viewDetails.initView(this.newIntimationDto.getIntimationId(),	ViewLevels.INTIMATION);
		}
		
		VerticalLayout viewDetailsLayout = new VerticalLayout(viewDetails,commonButtonsLayout());
		viewDetailsLayout.setWidth("100%");
		viewDetailsLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);

		mainVertical = new VerticalLayout(preauthIntimationDetailsCarousel,viewDetailsLayout,buttonHorLayout);
		mainVertical.setComponentAlignment(buttonHorLayout,Alignment.BOTTOM_CENTER); 
		mainVertical.setSpacing(true);

		addListener();
		
		setCompositionRoot(mainVertical);
	}
	
	public VerticalLayout commonButtonsLayout()
	{
				
		doctorInternalRemarkstxta = (TextArea) binder.buildAndBind("Doctor Internal Remarks", "remarks", TextArea.class);
		doctorInternalRemarkstxta.setNullRepresentation("");
		doctorInternalRemarkstxta.setMaxLength(4000);
		doctorInternalRemarkstxta.setWidth("75%");
		doctorInternalRemarkstxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		handleRemarksPopup(doctorInternalRemarkstxta, null);
		
		
		VerticalLayout vLayout1 = new VerticalLayout(doctorInternalRemarkstxta);

		vLayout1.setWidth("100%");
		return vLayout1;
	}
	
	@SuppressWarnings("serial")
	public void addListener() {

		btnSubmit.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				newIntimationDto.setUsername(UI.getCurrent().getUI().getSession()
						.getAttribute(BPMClientContext.USERID).toString());
				
				if (validatePage()) {
					newIntimationDto.setRemarks(doctorInternalRemarkstxta.getValue());
						claimDTO.setDoctorNote(doctorInternalRemarkstxta.getValue());
						claimDTO.setModifiedBy(newIntimationDto.getUsername());
						Map<String, Object> mapDTO = new HashMap<String, Object>();
						mapDTO.put("searchTableDto", newIntimationDto);
						mapDTO.put("claimDto", claimDTO);
						fireViewEvent(InternalNotesPagePresenter.SUBMIT_DR_NOTE_EVENT,
								mapDTO);
				}				
			}
		});
		
		btnCancel.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),
						"Confirmation", "Are you sure You want to Cancel ?",
						"Yes", "No", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								if(dialog.isCanceled()){
									dialog.close();
								}else if (dialog.isConfirmed()) {
									fireViewEvent(
											MenuItemBean.SEARCH_INTERNAL_NOTES,
											null);
									
								
								} else {
									dialog.close();
								}
							}
						});
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
	}
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("serial")
	@Override
	public void result() {
		ConfirmDialog dialog = ConfirmDialog.show(getUI(),
				"Claim record saved successfully !!!",
				new ConfirmDialog.Listener() {

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
						
							fireViewEvent(MenuItemBean.SEARCH_INTERNAL_NOTES, true);
							
						} else {
							dialog.close();
						}
					}
				});
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		dialog.getCancelButton().setVisible(false);
	}

	@SuppressWarnings("static-access")
	private boolean validatePage() {
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();

		if(doctorInternalRemarkstxta.getValue() == null || (doctorInternalRemarkstxta.getValue() != null && doctorInternalRemarkstxta.getValue().isEmpty())){
			eMsg.append("Please Enter Doctor Internal Remarks");
			hasError = true;
		}
		if (hasError) {
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			hasError = true;
			return !hasError;
		}
		return true;
	}
	
	public  void handleRemarksPopup(TextArea searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "ShortcutForRemarks", ShortcutAction.KeyCode.F8, null) {
		
	      private static final long serialVersionUID = 1L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    handleShortcutForRedraft(searchField, getShortCutListenerForRedraftRemarks(searchField));
	    
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
	private ShortcutListener getShortCutListenerForRedraftRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();
				
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(4000);
				txtArea.setHeight("30%");
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
				
				String strCaption = "Doctor Internal Remarks";
				
				dialog.setCaption(strCaption);
				
				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);
				
				dialog.setContent(vLayout);
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
					dialog.setPositionX(250);
					dialog.setPositionY(100);
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
