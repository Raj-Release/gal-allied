package com.shaic.domain.menu;

import java.io.Serializable;
import java.util.Date;

import com.shaic.domain.Intimation;
import com.shaic.domain.OMPIntimation;
import com.vaadin.ui.Button;

public class RegistrationBean implements Serializable{

	public Button intimationNoButton;	
	public String hospitalType;
	public Date intimationDate;
	public String intimationNo;
	public String status;
	public Intimation intimation;
	
	public OMPIntimation ompIntimation;
	
	public RegistrationBean() {
	
	}
	
//	@SuppressWarnings("deprecation")
//	public RegistrationBean(Map<String, String> valuesMap){
//		
//	  this.intimationDate = new Date(valuesMap.get("intimationDate"));
//	  this.hospitalType = valuesMap.get("hospitalType");
//	  this.intimationNo = valuesMap.get("intimationNo");
//	  this.status = valuesMap.get("status");
//	  
//	  Button intimationButton = new Button(intimationNo);
//	  intimationButton.setData(this.intimationNo);
//	  intimationButton.addClickListener(new Button.ClickListener() {
//	        public void buttonClick(ClickEvent event) {
//	            // Get the item identifier from the user-defined data.
//	            String id = (String)event.getButton().getData();
////	            Page.getCurrent().setUriFragment("!" + MenuItemBean.REGISTER_CLAIM);
//	           // ViewPreviousIntimation view = new ViewPreviousIntimation();
//	           // UI.getCurrent().addWindow(view);
//	        } 
//	    });
//	  intimationButton.addStyleName("link");
//	  this.intimationNoButton = intimationButton; 	  	  	
//	}
//	
//	public BeanContainer<String, RegistrationBean> basic() {
//	    // Create a container for such beans with
//	    // strings as item IDs.
//	    BeanContainer<String, RegistrationBean> beans =
//	        new BeanContainer<String, RegistrationBean>(RegistrationBean.class);
//	    
//	    // Use the name property as the item ID of the bean
//	    beans.setBeanIdProperty("intimationNo");
//
//	    HashMap<String, String> valuesMap = new HashMap<String, String>();
//	    valuesMap.put("intimationNo", "0001");
//	    valuesMap.put("intimationDate", "01/07/2014");
//	    valuesMap.put("hospitalType", "Non-Network");
//	    valuesMap.put("status", "Open");
//
//	    // Add some beans to it
//	    beans.addBean(new RegistrationBean(valuesMap));
//	    valuesMap.put("intimationNo", "0002");
//	    beans.addBean(new RegistrationBean(valuesMap));
//	    valuesMap.put("intimationNo", "0003");
//	    beans.addBean(new RegistrationBean(valuesMap));
//	    valuesMap.put("intimationNo", "0004");
//	    beans.addBean(new RegistrationBean(valuesMap));
//	    valuesMap.put("intimationNo", "0005");
//	    beans.addBean(new RegistrationBean(valuesMap));
//	    valuesMap.put("intimationNo", "0006");
//	    beans.addBean(new RegistrationBean(valuesMap));
//	    valuesMap.put("intimationNo", "0007");
//	    beans.addBean(new RegistrationBean(valuesMap));
//	    valuesMap.put("intimationNo", "0008");
//	    beans.addBean(new RegistrationBean(valuesMap));
//	    valuesMap.put("intimationNo", "0009");
//	    beans.addBean(new RegistrationBean(valuesMap));
//	    valuesMap.put("intimationNo", "0011");
//	    beans.addBean(new RegistrationBean(valuesMap));
//	    valuesMap.put("intimationNo", "0012");
//	    beans.addBean(new RegistrationBean(valuesMap));
//	    
//	    return beans;
//	}


	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public Date getIntimationDate() {
		return intimationDate;
	}

	public void setIntimationDate(Date intimationDate) {
		this.intimationDate = intimationDate;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Button getIntimationNoButton() {
		return intimationNoButton;
	}

	public void setIntimationNoButton(Button intimationNoButton) {
		this.intimationNoButton = intimationNoButton;
	}

	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public OMPIntimation getOmpIntimation() {
		return ompIntimation;
	}

	public void setOmpIntimation(OMPIntimation ompIntimation) {
		this.ompIntimation = ompIntimation;
	}

	
	
}
