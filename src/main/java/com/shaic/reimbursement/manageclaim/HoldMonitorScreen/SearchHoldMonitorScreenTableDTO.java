package com.shaic.reimbursement.manageclaim.HoldMonitorScreen;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class SearchHoldMonitorScreenTableDTO extends AbstractTableDTO implements Serializable{
	
	private String intimationNumber;
	private String reqDate;
	private String type;
	private String leg;
	private String holdBy;
	private String holdDate;
	private String holdRemarks;
	private Long wkKey;
	private Object dbOutArray;
	private Long preauthKey;
	private String screenName;
	private Long productKey;
	
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public String getReqDate() {
		return reqDate;
	}
	public void setReqDate(String reqDate) {
		this.reqDate = reqDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLeg() {
		return leg;
	}
	public void setLeg(String leg) {
		this.leg = leg;
	}
	public String getHoldBy() {
		return holdBy;
	}
	public void setHoldBy(String holdBy) {
		this.holdBy = holdBy;
	}
	public String getHoldDate() {
		return holdDate;
	}
	public void setHoldDate(String holdDate) {
		this.holdDate = holdDate;
	}
	public String getHoldRemarks() {
		return holdRemarks;
	}
	public void setHoldRemarks(String holdRemarks) {
		this.holdRemarks = holdRemarks;
	}
	public Long getWkKey() {
		return wkKey;
	}
	public void setWkKey(Long wkKey) {
		this.wkKey = wkKey;
	}
	public Object getDbOutArray() {
		return dbOutArray;
	}
	public void setDbOutArray(Object dbOutArray) {
		this.dbOutArray = dbOutArray;
	}
	public Long getPreauthKey() {
		return preauthKey;
	}
	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public Long getProductKey() {
		return productKey;
	}
	public void setProductKey(Long productKey) {
		this.productKey = productKey;
	}
	
}
