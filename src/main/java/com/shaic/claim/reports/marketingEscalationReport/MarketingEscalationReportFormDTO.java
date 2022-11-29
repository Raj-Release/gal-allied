package com.shaic.claim.reports.marketingEscalationReport;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class MarketingEscalationReportFormDTO extends AbstractSearchDTO implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 2654844019100152362L;
	private Date fromDate;
	private Date toDate;
	
	private SpecialSelectValue  productNameCode;
	
	private String forProduct;
	
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public SpecialSelectValue getProductNameCode() {
		return productNameCode;
	}
	public void setProductNameCode(SpecialSelectValue productNameCode) {
		this.productNameCode = productNameCode;
	}
	public String getForProduct() {
		return forProduct;
	}
	public void setForProduct(String forProduct) {
		this.forProduct = forProduct;
	}
	
	
	

	
	
	

}
