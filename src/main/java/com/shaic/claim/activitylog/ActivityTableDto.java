package com.shaic.claim.activitylog;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ActivityTableDto extends AbstractTableDTO  implements Serializable {
	
	@NotNull(message = "Please select atleast one activity")
	private SelectValue activityName;
	private String activityDesc;
	
	private BeanItemContainer<SelectValue> activityList;
	
	public ActivityTableDto() {
		super();
	}

	public ActivityTableDto(BeanItemContainer<SelectValue> activityList) {
		super();
		this.activityList = activityList;
	}
	
	public SelectValue getActivityName() {
		return activityName;
	}

	public void setActivityName(SelectValue activityName) {
		this.activityName = activityName;
	}

	public String getActivityDesc() {
		return activityDesc;
	}

	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}

	public BeanItemContainer<SelectValue> getActivityList() {
		return activityList;
	}

	public void setActivityList(BeanItemContainer<SelectValue> activityList) {
		this.activityList = activityList;
	}
}
