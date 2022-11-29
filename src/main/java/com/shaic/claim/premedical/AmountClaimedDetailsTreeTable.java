package com.shaic.claim.premedical;

import java.io.Serializable;
import java.util.List;

import com.shaic.main.navigator.domain.MenuItem;

public class AmountClaimedDetailsTreeTable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 645690718232635323L;
	
	private Long id;
	
	private String Details;
	
	private String noOfDays;
	
	private String perDayAmt;
	
	private String requestedAmount;
	
	private String deductiblesAmount;
	
	private String netAmount;
	
	private String amountConsidered;
	
	private String calculationLogic;
	
	private boolean accessFlag = true;
	
	private List<MenuItem> childMenus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDetails() {
		return Details;
	}

	public void setDetails(String details) {
		Details = details;
	}

	public String getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
	}

	public String getPerDayAmt() {
		return perDayAmt;
	}

	public void setPerDayAmt(String perDayAmt) {
		this.perDayAmt = perDayAmt;
	}

	public String getRequestedAmount() {
		return requestedAmount;
	}

	public void setRequestedAmount(String requestedAmount) {
		this.requestedAmount = requestedAmount;
	}

	public String getDeductiblesAmount() {
		return deductiblesAmount;
	}

	public void setDeductiblesAmount(String deductiblesAmount) {
		this.deductiblesAmount = deductiblesAmount;
	}

	public String getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}

	public String getAmountConsidered() {
		return amountConsidered;
	}

	public void setAmountConsidered(String amountConsidered) {
		this.amountConsidered = amountConsidered;
	}

	public String getCalculationLogic() {
		return calculationLogic;
	}

	public void setCalculationLogic(String calculationLogic) {
		this.calculationLogic = calculationLogic;
	}

	public boolean isAccessFlag() {
		return accessFlag;
	}

	public void setAccessFlag(boolean accessFlag) {
		this.accessFlag = accessFlag;
	}

	public List<MenuItem> getChildMenus() {
		return childMenus;
	}

	public void setChildMenus(List<MenuItem> childMenus) {
		this.childMenus = childMenus;
	}
	
	public boolean hasChild()
	{
		return this.childMenus.size() > 0;
	}
	
	public boolean isChild()
	{
		return this.childMenus.size() == 0;
	}
	
	public void addChild(MenuItem item)
	{
		this.childMenus.add(item);
	}
	
	public void removeChild(MenuItem item)
	{
		this.childMenus.remove(item);
	}
	
}
