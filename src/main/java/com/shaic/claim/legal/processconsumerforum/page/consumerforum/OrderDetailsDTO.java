package com.shaic.claim.legal.processconsumerforum.page.consumerforum;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;

public class OrderDetailsDTO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4421741131400846189L;
	
	private Boolean orderDetails = false;

	@NotNull(message="Please Select ORDER")
	private SelectValue order;
	
	@NotNull(message="Please Select Date of Receipt")
	private Date dateOfReciept;
	
	@NotNull(message="Please Select Date of Order/Award")
	private Date dateOforder;
	
	@NotNull(message="Please Select Limitation Date")
	private Date limitationOfComplainance;
	
	@NotNull(message="Please Select ORDER Outcome")
	private SelectValue orderOutcome;

	public SelectValue getOrder() {
		return order;
	}

	public void setOrder(SelectValue order) {
		this.order = order;
	}

	public Date getDateOfReciept() {
		return dateOfReciept;
	}

	public void setDateOfReciept(Date dateOfReciept) {
		this.dateOfReciept = dateOfReciept;
	}

	public Date getDateOforder() {
		return dateOforder;
	}

	public void setDateOforder(Date dateOforder) {
		this.dateOforder = dateOforder;
	}

	public Date getLimitationOfComplainance() {
		return limitationOfComplainance;
	}

	public void setLimitationOfComplainance(Date limitationOfComplainance) {
		this.limitationOfComplainance = limitationOfComplainance;
	}

	public SelectValue getOrderOutcome() {
		return orderOutcome;
	}

	public void setOrderOutcome(SelectValue orderOutcome) {
		this.orderOutcome = orderOutcome;
	}

	public Boolean getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Boolean orderDetails) {
		this.orderDetails = orderDetails;
	}
}
