package com.shaic.claim.reimbursement.paymentprocesscpuview;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;

import com.vaadin.v7.ui.VerticalLayout;

public class PaymentProcessCpuPageViewImpl extends AbstractMVPView implements PaymentProcessCpuPageView{
	
	@Inject 
	private Instance<PaymentProcessCpuPage> paymentProcessCpuPageInstance;

	private PaymentProcessCpuPage paymentProcessCpuPage;
	
	private PaymentProcessCpuPageDTO bean;
	
	private GWizard wizard;

	@PostConstruct
	public void initView() {
		
	}
	
	@Override
	public void resetView() {
		
		
	}


	@Override
	public void init(PaymentProcessCpuPageDTO bean) {
			
		addStyleName("view");
		setSizeFull();
		paymentProcessCpuPage = paymentProcessCpuPageInstance.get();
		paymentProcessCpuPage.init(bean);
		VerticalLayout mainPanel = new VerticalLayout(paymentProcessCpuPage);
		setCompositionRoot(mainPanel);
	}
	
	@Override
	public void generateDischargeVoucherLetter(String templateName, PaymentProcessCpuPageDTO paymentDTO){
		paymentProcessCpuPage.generateDischargeVoucherLetter(templateName, paymentDTO);
	}
	
	@Override
	public void generatePaymentAndDischargeLetter(String templateName, PaymentProcessCpuPageDTO paymentDTO){
		
		paymentProcessCpuPage.generatePaymentAndDischargeLetter(templateName, paymentDTO);
	}

	@Override
	public void generateHospPaymentLetter(String templateName, PaymentProcessCpuPageDTO updatedDto){
		paymentProcessCpuPage.generateHospitalPaymentLetter(templateName, updatedDto);
	}
	
	private void showSuccessMessage(String msg) {
		Label label = new Label(msg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		
	}
}