package com.shaic.claim;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.utils.Props;
import com.vaadin.annotations.Theme;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Validator;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.Align;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

@Theme(Props.THEME_NAME)
public class SumInsuredBonusAlertTable extends  ViewComponent{

	private static final long serialVersionUID = 1L;

	private Table table;

	private Map<SumInsuredBonusAlertDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<SumInsuredBonusAlertDTO,HashMap<String, AbstractField<?>>>();

	BeanItemContainer<SumInsuredBonusAlertDTO> data = new BeanItemContainer<SumInsuredBonusAlertDTO>(SumInsuredBonusAlertDTO.class);
		
	private TextField currentPolicytxt;

	private Map<String, Object> referenceData;
	
	private HorizontalLayout captionLayout;
	
	private HorizontalLayout policyLayout;
	
	private HorizontalLayout blinkLayout;
	
	public void init(String strPolicyNo,SumInsuredBonusAlertDTO bonusAlertDTO) {
								
		/*currentPolicytxt = new TextField("Current Ploicy Number");
		currentPolicytxt.setNullRepresentation("");
		currentPolicytxt.setWidth("50%");
		currentPolicytxt.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		if(strPolicyNo !=null){
			currentPolicytxt.setValue(strPolicyNo);
			currentPolicytxt.setEnabled(false);
		}*/
		
		Label currentPolicy = new Label("Current Policy Number : ");
		currentPolicy.setStyleName(ValoTheme.LABEL_H3);
		Label PolicyNumber = new Label(strPolicyNo);
		PolicyNumber.setStyleName(ValoTheme.LABEL_H3);
						
		Label lblCaption = new Label("Previous Policy Details");
		captionLayout = new HorizontalLayout(lblCaption);	
		captionLayout.setStyleName("v-window-header");
		captionLayout.setSizeFull();
		
		//FormLayout formLayout = new FormLayout(currentPolicytxt);
		
		policyLayout = new HorizontalLayout(currentPolicy,PolicyNumber);
		VerticalLayout vplocylayout = new VerticalLayout(policyLayout);
		vplocylayout.setComponentAlignment(policyLayout, Alignment.MIDDLE_CENTER);
		
		if(bonusAlertDTO != null
				&& bonusAlertDTO.getUtilizedAmount() > 0){
			Label blinkLabel = new Label("<h3 style='color: Tomato;text-align: center;'><b>Claim(s) present in previous policy. Please validate the bonus before approving the claim.</b></h3>", ContentMode.HTML);	
			blinkLayout = new HorizontalLayout(blinkLabel);	
			blinkLayout.setStyleName("blink");
			blinkLayout.setSizeFull();
		}
		
		

		VerticalLayout layout = new VerticalLayout();
		initTable();
		table.setWidth("100%");
		table.setHeight("84px");
		table.setPageLength(table.getItemIds().size());
		layout.addComponent(captionLayout);
		layout.addComponent(vplocylayout);
		layout.addComponent(table);
		if(bonusAlertDTO != null
				&& bonusAlertDTO.getUtilizedAmount() > 0){
			layout.addComponent(blinkLayout);
		}				
		layout.setComponentAlignment(captionLayout , Alignment.TOP_LEFT);
		if(bonusAlertDTO != null) {						
			addToList(bonusAlertDTO);			
		}
		setCompositionRoot(layout);
	}
	
	public void initTable() {
		
		data.removeAllItems();
		table = new Table("",data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		Object[] VISIBLE_COLUMNS = new Object[] {"sNo","productName","previousPolicyNumber","policyYear","policyFromdate","policyTodate","sumInsured","bonus","utilizedAmount"};

		table.setVisibleColumns(VISIBLE_COLUMNS);			
		
		table.setColumnHeader("slNo", "S.No");
		table.setColumnHeader("productName", "Product Name");
		table.setColumnHeader("previousPolicyNumber", "Previous Policy Number");
		table.setColumnHeader("policyYear", "Policy Year");
		table.setColumnHeader("policyFromdate", "Policy From date");
		table.setColumnHeader("policyTodate", "Policy To date");
		table.setColumnHeader("sumInsured", "Sum Insured");
		table.setColumnHeader("bonus", "Bonus");
		table.setColumnHeader("utilizedAmount", "Utilized Amount");
		
		table.setColumnAlignment("slNo", Align.CENTER);
		table.setColumnAlignment("productName", Align.CENTER);
		table.setColumnAlignment("previousPolicyNumber", Align.CENTER);
		table.setColumnAlignment("policyYear", Align.CENTER);
		table.setColumnAlignment("policyFromdate", Align.CENTER);
		table.setColumnAlignment("policyTodate", Align.CENTER);
		table.setColumnAlignment("sumInsured", Align.CENTER);
		table.setColumnAlignment("bonus", Align.CENTER);
		table.setColumnAlignment("utilizedAmount", Align.CENTER);
		
		//Adding the height for procedure table.
		table.setPageLength(table.getItemIds().size());
	}
	
	public void addToList(SumInsuredBonusAlertDTO idaTableDTO) {
		data.addItem(idaTableDTO);
	}


}
