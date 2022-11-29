package com.shaic.domain.preauth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;


@Entity
@Table(name = "TMP_AD_USERS_LIST")
@NamedQueries({
		@NamedQuery(name = "TmpAdUserList.findAll", query = "SELECT i FROM TmpAdUserList i")
})
public class TmpAdUserList extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TMP_AD_USERS_LIST_KEY_GENERATOR", sequenceName = "SEQ_TMP_AD_USERS", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TMP_AD_USERS_LIST_KEY_GENERATOR" ) 
	@Column(name="USER_KEY", updatable=false)
	private Long key;
	
	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name = "USER_NAME")
	private String userName;
	
	@Column(name = "ROLE_ID")
	private String roleId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}


}
