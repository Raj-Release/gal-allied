package com.shaic.claim.specialist;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.claim.IntimationDetailsCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.v7.ui.themes.Reindeer;

public class SpecialistViewImpl extends AbstractMVPView 
{

	
	private static final long serialVersionUID = -9182066388418239095L;

	@Inject
	private SpecialistTable specialistTable;


	
	@Inject
	private Instance<IntimationDetailsCarousel> commonCarouselInstance;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	private HorizontalLayout buttonHorLayout;
	
	

	public SpecialistViewImpl() {
	}

	@PostConstruct
	public void initView() {
		
		specialistTable.init("",false, false);
		
		
	    Panel tablePanel=new Panel(specialistTable);
	    tablePanel.setWidth("100%");
	    tablePanel.setHeight("200px");

		
		VerticalSplitPanel mainsplitPanel = new VerticalSplitPanel();
		IntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
		intimationDetailsCarousel.init(new NewIntimationDto(),
				"Advice On PED");
		mainsplitPanel.setFirstComponent(intimationDetailsCarousel);
		
		submitBtn=new Button("Submit");
		cancelBtn=new Button("Cancel");
		buttonHorLayout=new HorizontalLayout(submitBtn,cancelBtn);
		buttonHorLayout.setSpacing(true);
		


		VerticalLayout verticalLayout = new VerticalLayout(tablePanel,buttonHorLayout);
		verticalLayout.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		addListener();
		mainsplitPanel.setSecondComponent(verticalLayout);
		
		mainsplitPanel.setSplitPosition(22, Unit.PERCENTAGE);
		setHeight("100%");
		mainsplitPanel.setSizeFull();
		mainsplitPanel.setHeight("650px");
		setCompositionRoot(mainsplitPanel);
	}
	
	

	private HorizontalLayout buildFooterButton() {
		NativeButton submitBtn = new NativeButton("Submit");
		NativeButton cancelBtn = new NativeButton("Cancel");
		HorizontalLayout footerBtnLayout = new HorizontalLayout(submitBtn,
				cancelBtn);
		footerBtnLayout.setSpacing(true);
		footerBtnLayout.setMargin(true);
		footerBtnLayout.setWidth("100%");
		return footerBtnLayout;
	}

public void addListener() {
		
		cancelBtn.addClickListener(new Button.ClickListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
					        "No", "Yes", new ConfirmDialog.Listener() {

					            public void onClose(ConfirmDialog dialog) {
					                if (!dialog.isConfirmed()) {
					                	fireViewEvent(MenuItemBean.SUBMIT_SPECIALLIST_ADVISE, true);
					                } else {
					                    dialog.close();
					                }
					            }
					        });
					dialog.setStyleName(Reindeer.WINDOW_BLACK);
				}
			});
		submitBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Claim record saved successfully !!!",
				        "Ok","", new ConfirmDialog.Listener() {
				            public void onClose(ConfirmDialog dialog) {
				                if (dialog.isConfirmed()) {
				                	fireViewEvent(MenuItemBean.SUBMIT_SPECIALLIST_ADVISE, true);
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
	}

	

	

	


	

}
