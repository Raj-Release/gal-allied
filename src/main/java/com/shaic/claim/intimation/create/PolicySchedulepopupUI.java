package com.shaic.claim.intimation.create;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.shaic.claim.registration.previousinsurance.view.ViewPreviousInsurance;
import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Window;
@UIScoped
public class PolicySchedulepopupUI extends Window {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ViewPreviousInsurance viewPreviousInsurance;
	
	private Button viewOriginalSchedule;
	private Button viewEndorsedSchedule;
	private Button cancelButton;
	private HorizontalLayout mainLayout;
	
	@PostConstruct
	public void initView(){
		this.setHeight("350px");
		this.setWidth("100%");
		this.setClosable(false);
		
		viewEndorsedSchedule = new Button("View Original Schedule");
		viewOriginalSchedule =new Button("View Endorsed Schedule");
		cancelButton = new Button("Cancel");
		cancelButton.setData(this);
		
		 mainLayout = new HorizontalLayout();
		 mainLayout.setWidth("80%");
		 mainLayout.setHeight("250px");
		mainLayout.addComponent(viewOriginalSchedule);
		mainLayout.addComponent(viewEndorsedSchedule);
		mainLayout.setComponentAlignment(viewOriginalSchedule, Alignment.MIDDLE_RIGHT);
		mainLayout.setComponentAlignment(viewEndorsedSchedule, Alignment.MIDDLE_RIGHT);
		mainLayout.addComponent(cancelButton);
		mainLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		setCaption("Policy Schedule");
		setModal(true);
		setResizable(true);
		setClosable(true);
		setContent(mainLayout);
		addListener();
		
		
	}
	private void addListener(){
		viewOriginalSchedule.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				viewPreviousInsurance.showViewPolicySchedule();
				
			}
		});
		viewEndorsedSchedule.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				viewPreviousInsurance.showViewPolicySchedule();
				
			}
		});
		cancelButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Object obj = cancelButton.getData();
				((Window) obj).close();
			}
		});
		
		
		
	}
}
