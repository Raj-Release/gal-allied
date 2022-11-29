package com.shaic.claim.coporatebuffer.view;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewCoporateBuffer extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout;
	private TextField txtBufferSI;
	private TextField txtUtilizedAmt;
	private TextField txtCurrentClaimAmt;
	private TextField txtBalance;

	private BeanFieldGroup<ViewCorporateBufferDTO> binder;

	public void init() {
		buildMainLayout();
	}

	public void buildMainLayout() {
		initBinder();
		txtBufferSI = binder.buildAndBind("Buffered SI", "bufferedSI",
				TextField.class);
		txtUtilizedAmt = binder.buildAndBind("Utilized Amount", "utilizedAmt",
				TextField.class);
		txtCurrentClaimAmt = binder.buildAndBind("Current Claim Amount",
				"currentClaimAmt", TextField.class);
		txtBalance = binder.buildAndBind("Balance", "balance", TextField.class);
		FormLayout formLayout = new FormLayout(txtBufferSI, txtUtilizedAmt,
				txtCurrentClaimAmt, txtBalance);
		/*formLayout.setComponentAlignment(txtBalance, Alignment.MIDDLE_CENTER);
		formLayout.setComponentAlignment(txtBufferSI, Alignment.MIDDLE_CENTER);
		formLayout.setComponentAlignment(txtUtilizedAmt,
				Alignment.MIDDLE_CENTER);
		formLayout.setComponentAlignment(txtCurrentClaimAmt,
				Alignment.MIDDLE_CENTER);*/
		mainLayout = new VerticalLayout(formLayout);
		mainLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);
		mainLayout.setMargin(true);
		setCompositionRoot(mainLayout);
	}

	private void initBinder() {
		this.binder = new BeanFieldGroup<ViewCorporateBufferDTO>(
				ViewCorporateBufferDTO.class);
		this.binder.setItemDataSource(new ViewCorporateBufferDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

}
