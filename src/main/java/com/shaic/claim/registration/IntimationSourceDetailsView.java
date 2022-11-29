package com.shaic.claim.registration;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Preauth;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings({"serial","deprecation"})
public class IntimationSourceDetailsView extends ViewComponent {

	private TextField txtIntimationNo;
	private TextField txtIntimationSource;
	private TextField txtIntimationMode;
	
	private FormLayout formContainer;
	private VerticalLayout verticalLayout;
	
	@Inject
	private IntimationSourceDetailTable sourceDetailTable;

	public void init(Intimation intimationObj, List<Preauth> listofDocuments) {

		txtIntimationNo = new TextField("Intimation Number");
		txtIntimationNo.setValue(intimationObj.getIntimationId());
		txtIntimationNo.setReadOnly(true);
		txtIntimationNo.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		txtIntimationSource = new TextField("Source");
		String temp = intimationObj.getSource();
		if(!StringUtils.isBlank(temp) && (temp.equalsIgnoreCase("BCT") || temp.equalsIgnoreCase("ZOHO_CRM"))){
			temp = "Customer Care";
		}
		txtIntimationSource.setValue(temp);
		txtIntimationSource.setReadOnly(true);
		txtIntimationSource.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		txtIntimationMode = new TextField("Mode");
		txtIntimationMode.setValue(intimationObj.getIntimationMode().getValue());
		txtIntimationMode.setReadOnly(true);
		txtIntimationMode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		formContainer = new FormLayout();
		formContainer.setWidth("100%");
		formContainer.setMargin(false);
		formContainer.setSpacing(true);
		formContainer.addStyleName("layoutDesign");

		formContainer.addComponents(txtIntimationNo, txtIntimationSource, txtIntimationMode);
		
		verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(formContainer, 0);
		
		if(intimationObj.getClaimType().getKey().intValue() == ReferenceTable.CASHLESS_CLAIM_TYPE_KEY.intValue() && listofDocuments.size() > 0){
			sourceDetailTable.init("Cashless Document(s)", false, false);
			sourceDetailTable.setTableList(listofDocuments);
			
			verticalLayout.addComponent(sourceDetailTable, 1);
			verticalLayout.setSpacing(false);
			verticalLayout.setMargin(false);
		}
		setCompositionRoot(verticalLayout);
	}
}