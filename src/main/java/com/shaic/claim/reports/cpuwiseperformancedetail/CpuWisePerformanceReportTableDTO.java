package com.shaic.claim.reports.cpuwiseperformancedetail;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class CpuWisePerformanceReportTableDTO extends AbstractTableDTO  implements Serializable{
	
	private String type;
	private Long hospitalTypeId;
	private Double gmcNetWork;
	private Double gmcProvisionAmount;
	private Double gmcNonNetWork;
	private Double gmcProvisionAmount1;
	private Double nonGmcNetWork;
	private Double nonGmcProvisionAmount;
	private Double nonGmcNonNetWork;
	private Double nonGmcProvisionAmount1;
	private Double total;
	private Double provisionAmountTotal;

	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getHospitalTypeId() {
		return hospitalTypeId;
	}
	public void setHospitalTypeId(Long hospitalTypeId) {
		this.hospitalTypeId = hospitalTypeId;
	}
	public Double getGmcNetWork() {
		return gmcNetWork;
	}
	public void setGmcNetWork(Double gmcNetWork) {
		this.gmcNetWork = gmcNetWork;
	}
	public Double getGmcProvisionAmount() {
		return gmcProvisionAmount;
	}
	public void setGmcProvisionAmount(Double gmcProvisionAmount) {
		this.gmcProvisionAmount = gmcProvisionAmount;
	}
	public Double getGmcNonNetWork() {
		return gmcNonNetWork;
	}
	public void setGmcNonNetWork(Double gmcNonNetWork) {
		this.gmcNonNetWork = gmcNonNetWork;
	}
	public Double getGmcProvisionAmount1() {
		return gmcProvisionAmount1;
	}
	public void setGmcProvisionAmount1(Double gmcProvisionAmount1) {
		this.gmcProvisionAmount1 = gmcProvisionAmount1;
	}
	public Double getNonGmcNetWork() {
		return nonGmcNetWork;
	}
	public void setNonGmcNetWork(Double nonGmcNetWork) {
		this.nonGmcNetWork = nonGmcNetWork;
	}
	public Double getNonGmcProvisionAmount() {
		return nonGmcProvisionAmount;
	}
	public void setNonGmcProvisionAmount(Double nonGmcProvisionAmount) {
		this.nonGmcProvisionAmount = nonGmcProvisionAmount;
	}
	public Double getNonGmcNonNetWork() {
		return nonGmcNonNetWork;
	}
	public void setNonGmcNonNetWork(Double nonGmcNonNetWork) {
		this.nonGmcNonNetWork = nonGmcNonNetWork;
	}
	public Double getNonGmcProvisionAmount1() {
		return nonGmcProvisionAmount1;
	}
	public void setNonGmcProvisionAmount1(Double nonGmcProvisionAmount1) {
		this.nonGmcProvisionAmount1 = nonGmcProvisionAmount1;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Double getProvisionAmountTotal() {
		return provisionAmountTotal;
	}
	public void setProvisionAmountTotal(Double provisionAmountTotal) {
		this.provisionAmountTotal = provisionAmountTotal;
	}
	
	
	

}
