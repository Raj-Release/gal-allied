package com.shaic.arch.table;

import java.io.Serializable;

public class AbstractRowEnablerDTO  extends AbstractTableDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6291636397066749622L;
	
	private boolean statusFlag = false;
	private boolean gmcFlag = false;
	
	public boolean isStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(boolean statusFlag) {
		this.statusFlag = statusFlag;
	}

	public boolean isGmcFlag() {
		return gmcFlag;
	}

	public void setGmcFlag(boolean gmcFlag) {
		this.gmcFlag = gmcFlag;
	}

}
