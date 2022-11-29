package com.shaic.claim.reports.paymentpendingdashboard;

import java.io.Serializable;
import java.util.Date;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class PaymentPendingDashboardFormDTO extends AbstractSearchDTO implements Serializable {
	
	private Date date;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	
}
