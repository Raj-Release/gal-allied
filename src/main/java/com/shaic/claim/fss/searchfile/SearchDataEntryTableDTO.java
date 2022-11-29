/**
 * 
 */
package com.shaic.claim.fss.searchfile;

import java.util.List;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.fss.filedetail.ChequeDetailsTableDTO;

/**
 * 
 *
 */
public class SearchDataEntryTableDTO extends AbstractTableDTO{
	
	private Integer sNo;
	
	private Long key;
	
	private String claimNo;
	
	private String patientName;
	
	private String location;
	
	private String rackNo;
	
	private String shelfNo;
	
	private Boolean isCheckInOutStatus;
	
	private String checkInOutStatus;
	
	private SelectValue selectLocation;
	
	private SelectValue selectRack;
	
	private SelectValue selectShelf;
	
	private String client;
	
	private String year;
	
	private String almirahNo;
	
	//private Long addlShelfNo;
	
	private Boolean isRejectStatus;
	
	private String rejectStatus;
	
	private List<ChequeDetailsTableDTO> chequeList;
	
	private List<ChequeDetailsTableDTO> deletedChequeList;
	
	private Boolean addFlag = false;
	
	private Map<String, Object> dataSourcesMap;
	
	private Boolean isPremiaIntimation = false;
	
	private String claimSeqNo;
	
	private String bundleNo;

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	
	public String getRackNo() {
		return rackNo;
	}

	public void setRackNo(String rackNo) {
		this.rackNo = rackNo;
	}

	public String getShelfNo() {
		return shelfNo;
	}

	public void setShelfNo(String shelfNo) {
		this.shelfNo = shelfNo;
	}

	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}

	public Boolean getIsCheckInOutStatus() {
		return isCheckInOutStatus;
	}

	public void setIsCheckInOutStatus(Boolean isCheckInOutStatus) {
		this.isCheckInOutStatus = isCheckInOutStatus;
	}

	public String getCheckInOutStatus() {
		return checkInOutStatus;
	}

	public void setCheckInOutStatus(String checkInOutStatus) {
		this.checkInOutStatus = checkInOutStatus;
	}

	public SelectValue getSelectRack() {
		return selectRack;
	}

	public void setSelectRack(SelectValue selectRack) {
		this.selectRack = selectRack;
	}

	public SelectValue getSelectShelf() {
		return selectShelf;
	}

	public void setSelectShelf(SelectValue selectShelf) {
		this.selectShelf = selectShelf;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getAlmirahNo() {
		return almirahNo;
	}

	public void setAlmirahNo(String almirahNo) {
		this.almirahNo = almirahNo;
	}

	public Boolean getIsRejectStatus() {
		return isRejectStatus;
	}

	public void setIsRejectStatus(Boolean isRejectStatus) {
		this.isRejectStatus = isRejectStatus;
	}

	public String getRejectStatus() {
		return rejectStatus;
	}

	public void setRejectStatus(String rejectStatus) {
		this.rejectStatus = rejectStatus;
	}

	public Map<String, Object> getDataSourcesMap() {
		return dataSourcesMap;
	}

	public void setDataSourcesMap(Map<String, Object> dataSourcesMap) {
		this.dataSourcesMap = dataSourcesMap;
	}

	public SelectValue getSelectLocation() {
		return selectLocation;
	}

	public void setSelectLocation(SelectValue selectLocation) {
		this.selectLocation = selectLocation;
	}

	public List<ChequeDetailsTableDTO> getDeletedChequeList() {
		return deletedChequeList;
	}

	public void setDeletedChequeList(List<ChequeDetailsTableDTO> deletedChequeList) {
		this.deletedChequeList = deletedChequeList;
	}

	public List<ChequeDetailsTableDTO> getChequeList() {
		return chequeList;
	}

	public void setChequeList(List<ChequeDetailsTableDTO> chequeList) {
		this.chequeList = chequeList;
	}

	public Boolean getAddFlag() {
		return addFlag;
	}

	public void setAddFlag(Boolean addFlag) {
		this.addFlag = addFlag;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	/*public Long getAddlShelfNo() {
		return addlShelfNo;
	}

	public void setAddlShelfNo(Long addlShelfNo) {
		this.addlShelfNo = addlShelfNo;
	}*/

	public Boolean getIsPremiaIntimation() {
		return isPremiaIntimation;
	}

	public void setIsPremiaIntimation(Boolean isPremiaIntimation) {
		this.isPremiaIntimation = isPremiaIntimation;
	}

	public String getClaimSeqNo() {
		return claimSeqNo;
	}

	public void setClaimSeqNo(String claimSeqNo) {
		this.claimSeqNo = claimSeqNo;
	}

	public String getBundleNo() {
		return bundleNo;
	}

	public void setBundleNo(String bundleNo) {
		this.bundleNo = bundleNo;
	}

	
}
