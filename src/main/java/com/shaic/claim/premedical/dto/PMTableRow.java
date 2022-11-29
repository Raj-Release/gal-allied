package com.shaic.claim.premedical.dto;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import com.shaic.claim.leagalbilling.LegalBaseCell;

public class PMTableRow implements Serializable, Comparable<PMTableRow> {

	private static final long serialVersionUID = 4643435823574141019L;

	private String detailLabel;

	private Integer serialNum;

	private Set<PMTableRow> childRow;
	
	private BaseCell inputCell;
	
	private LegalBaseCell legalInputCell;
	
	private Integer masterId;

	public Set<PMTableRow> getChildRow() {
		return childRow;
	}

	public void setChildRow(Set<PMTableRow> childRow) {
		this.childRow = childRow;
	}

	public PMTableRow() {
		this.childRow = new TreeSet<PMTableRow>();
		this.inputCell = new BaseCell();
		this.legalInputCell = new LegalBaseCell();
	}

	public PMTableRow(int serialNo, String detailLabel, BaseCell cell, Integer masterId)
	{
		this();
		this.serialNum = serialNo;
		this.detailLabel = detailLabel;
		this.inputCell = cell;
		this.masterId = masterId;
	}
	
	public PMTableRow(int serialNo, String detailLabel, LegalBaseCell cell, Integer masterId)
	{
		this();
		this.serialNum = serialNo;
		this.detailLabel = detailLabel;
		this.legalInputCell = cell;
		this.masterId = masterId;
	}
	
	public Integer getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
	}

	public String getDetailLabel() {
		return detailLabel;
	}

	public void setDetailLabel(String detailLabel) {
		this.detailLabel = detailLabel;
	}

	public BaseCell getInputCell() {
		return inputCell;
	}

	public void setInputCell(BaseCell inputCell) {
		this.inputCell = inputCell;
	}

	public void addChild(PMTableRow row)
	{
		this.childRow.add(row);
	}
	
	public void removeChild(PMTableRow row)
	{
		this.childRow.remove(row);
	}

	public boolean hasChild() {
		return this.childRow.size() > 0;
	}

	@Override
	public int compareTo(PMTableRow o) {
		return this.serialNum.compareTo(o.serialNum);
	}

	public Integer getMasterId() {
		return masterId;
	}

	public void setMasterId(Integer masterId) {
		this.masterId = masterId;
	}

	public LegalBaseCell getLegalInputCell() {
		return legalInputCell;
	}

	public void setLegalInputCell(LegalBaseCell legalInputCell) {
		this.legalInputCell = legalInputCell;
	}
	
}
