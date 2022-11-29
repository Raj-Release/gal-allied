package com.shaic.claim.reports.paymentprocess;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.teemu.wizards.GWizard;

import com.vaadin.v7.ui.VerticalLayout;

public class PaymentProcessPopUpViewImpl extends AbstractMVPView implements PaymentProcessPopUpView{
	@Inject 
	private Instance< PaymentProcessPopUp> paymentProcessCpuPageInstance;

	private  PaymentProcessPopUp paymentProcessCpuPage;
	
	private  PaymentProcessPopUpDTO bean;
	
	private GWizard wizard;
	
	
//	@Inject
//	private PaymentProcessCpuPageView pageViewInstance;

	@PostConstruct
	public void initView() {
		
	}
	
	

	


	@Override
	public void init( PaymentProcessPopUpDTO bean) {
			
		/*HPCreatePreathPatientDetailFirstPageDTO beanCom = new HPCreatePreathPatientDetailFirstPageDTO();
		beanCom.setPolicy(bean.getPolicy());
		beanCom.setInsuredKey(bean.getInsured().getKey());
			this.bean = bean;
			addStyleName("view");
			setSizeFull();
			hpClosePreauthPage = hpClosePreauthPageInstance.get();
			hpClosePreauthPage.init(bean);
			commonCarouselInstance.init(beanCom, "Create Preauth");
			VerticalLayout mainPanel = new VerticalLayout(commonCarouselInstance,hpClosePreauthPage);
			setCompositionRoot(mainPanel);*/
			
		addStyleName("view");
		setSizeFull();
		paymentProcessCpuPage =  paymentProcessCpuPageInstance.get();
		paymentProcessCpuPage.init(bean);
		VerticalLayout mainPanel = new VerticalLayout(paymentProcessCpuPage);
		setCompositionRoot(mainPanel);
	}

	


}
