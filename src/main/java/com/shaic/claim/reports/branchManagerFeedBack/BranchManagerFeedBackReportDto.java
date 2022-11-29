package com.shaic.claim.reports.branchManagerFeedBack;

import java.util.Date;
import java.util.List;
import java.util.WeakHashMap;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.newcode.wizard.dto.SearchTableDTO;
import com.vaadin.server.ThemeResource;
/**
 * AS Part of CR  R1238
 * @author Lakshminarayana
 *
 */
public class BranchManagerFeedBackReportDto extends SearchTableDTO {
	
	private SelectValue feedbackSelect;
	private SelectValue zonalOfficeSelect;
	private SelectValue periodSelect;
	private SelectValue branchOfficeSelect;
	private String searchType;
	
	private String sno;
	private String zoneCode;
	private String zoneName;
	private String branchCode;
	private String branchName;	
	private Double rating;
	private String reported;
	private String responded;
	private String pending;
	private String claimRetailReported;
	private String claimRetailPending;
	private String claimRetailResponded;
	private String merReported;
	private String merPending;
	private String merResponded;
	private String claimGmcReported;
	private String claimGmcPending;
	private String claimGmcResponded;
	private String ratingImg;
	private ThemeResource ratingTheme;
	
	
	private List<BranchManagerFeedBackReportDto> searchResultList;


	public SelectValue getFeedbackSelect() {
		return feedbackSelect;
	}


	public void setFeedbackSelect(SelectValue feedbackSelect) {
		this.feedbackSelect = feedbackSelect;
	}


	public SelectValue getZonalOfficeSelect() {
		return zonalOfficeSelect;
	}


	public void setZonalOfficeSelect(SelectValue zonalOfficeSelect) {
		this.zonalOfficeSelect = zonalOfficeSelect;
	}


	public SelectValue getPeriodSelect() {
		return periodSelect;
	}


	public void setPeriodSelect(SelectValue periodSelect) {
		this.periodSelect = periodSelect;
	}


	public SelectValue getBranchOfficeSelect() {
		return branchOfficeSelect;
	}


	public void setBranchOfficeSelect(SelectValue branchOfficeSelect) {
		this.branchOfficeSelect = branchOfficeSelect;
	}


	public String getSno() {
		return sno;
	}


	public void setSno(String sno) {
		this.sno = sno;
	}


	public String getZoneCode() {
		return zoneCode;
	}


	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}


	public String getZoneName() {
		return zoneName;
	}


	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}


	public String getBranchCode() {
		return branchCode;
	}


	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}


	public String getBranchName() {
		return branchName;
	}


	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}


	public Double getRating() {
		return rating;
	}


	public void setRating(Double rating) {
		this.rating = rating;
		getRatingImageForRating();		
	}


	public String getReported() {
		return reported;
	}


	public void setReported(String reported) {
		this.reported = reported;
	}


	public String getResponded() {
		return responded;
	}


	public void setResponded(String responded) {
		this.responded = responded;
	}


	public String getPending() {
		return pending;
	}

	public void setPending(String pending) {
		this.pending = pending;
	}
	

	public List<BranchManagerFeedBackReportDto> getSearchResultList() {
		return searchResultList;
	}

	public String getMerReported() {
		return merReported;
	}


	public void setMerReported(String merReported) {
		this.merReported = merReported;
	}


	public String getMerPending() {
		return merPending;
	}


	public void setMerPending(String merPending) {
		this.merPending = merPending;
	}


	public String getMerResponded() {
		return merResponded;
	}


	public void setMerResponded(String merResponded) {
		this.merResponded = merResponded;
	}


	public String getClaimRetailReported() {
		return claimRetailReported;
	}


	public void setClaimRetailReported(String claimRetailReported) {
		this.claimRetailReported = claimRetailReported;
	}


	public String getClaimRetailPending() {
		return claimRetailPending;
	}


	public void setClaimRetailPending(String claimRetailPending) {
		this.claimRetailPending = claimRetailPending;
	}


	public String getClaimRetailResponded() {
		return claimRetailResponded;
	}


	public void setClaimRetailResponded(String claimRetailResponded) {
		this.claimRetailResponded = claimRetailResponded;
	}


	public String getClaimGmcReported() {
		return claimGmcReported;
	}


	public void setClaimGmcReported(String claimGmcReported) {
		this.claimGmcReported = claimGmcReported;
	}


	public String getClaimGmcPending() {
		return claimGmcPending;
	}


	public void setClaimGmcPending(String claimGmcPending) {
		this.claimGmcPending = claimGmcPending;
	}


	public String getClaimGmcResponded() {
		return claimGmcResponded;
	}


	public void setClaimGmcResponded(String claimGmcResponded) {
		this.claimGmcResponded = claimGmcResponded;
	}


	public void setRatingTheme(ThemeResource ratingTheme) {
		this.ratingTheme = ratingTheme;
	}


	public void setSearchResultList(
			List<BranchManagerFeedBackReportDto> searchResultList) {
		this.searchResultList = searchResultList;
	}


	public String getSearchType() {
		return searchType;
	}


	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public void getRatingImageForRating(){

		WeakHashMap<Double, String> feedbackRatingMap = SHAUtils.getBMFBRatingImgMap();
		
		ratingImg = feedbackRatingMap.get(0.0d);
		
		if(getRating().doubleValue() >= 0.5d &&  getRating().doubleValue() < 1.0d){
			ratingImg = feedbackRatingMap.get(0.5d);
		}
		if(getRating().doubleValue() >= 1.0d && getRating().doubleValue() < 1.5d){
			ratingImg = feedbackRatingMap.get(1.0d);
		}
		if(getRating().doubleValue() >= 1.5d && getRating().doubleValue() < 2.0d){
			ratingImg = feedbackRatingMap.get(1.5d);
		}
		if(getRating().doubleValue() >= 2.0d && getRating().doubleValue() < 2.5d){
			ratingImg = feedbackRatingMap.get(2.0d);
		}
		if(getRating().doubleValue() >= 2.5d && getRating().doubleValue() < 3.0d){
			ratingImg = feedbackRatingMap.get(2.5d);
		}
		if(getRating().doubleValue() >= 3.0d && getRating().doubleValue() < 3.5d){
			ratingImg = feedbackRatingMap.get(3.0d);
		}
		if(getRating().doubleValue() >= 3.5d && getRating().doubleValue() < 4.0d){
			ratingImg = feedbackRatingMap.get(3.5d);
		}
		if(getRating().doubleValue() >= 4.0d && getRating().doubleValue() < 4.5d){
			ratingImg = feedbackRatingMap.get(4.0d);
		}
		if(getRating().doubleValue() >= 4.5d && getRating().doubleValue() < 5.0d){
			ratingImg = feedbackRatingMap.get(4.5d);
		}
		if(getRating().doubleValue() >= 5.0d){
			ratingImg = feedbackRatingMap.get(5.0d);
		}
		
			ratingTheme = new ThemeResource("images/"+ratingImg);		
	}

	public ThemeResource getRatingTheme() {

		getRatingImageForRating();
		
		return ratingTheme;
	}

	public String getRatingImg() {
		return ratingImg;
	}

	public void setRatingImg(String ratingImg) {
		this.ratingImg = ratingImg;
	}
	
}
