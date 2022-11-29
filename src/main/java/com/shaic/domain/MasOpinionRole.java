package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * @author Gokulprasath.A
 *
 */
@Entity
@Table(name="MAS_OPINION_ROLE")
@NamedQueries({
@NamedQuery(name="MasOpinionRole.findAll", query="SELECT m FROM MasOpinionRole m "),
@NamedQuery(name ="MasOpinionRole.findByKey",query="SELECT o FROM MasOpinionRole o WHERE o.roleCode = :roleCodeKey"),
})
public class MasOpinionRole extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="ROLE_KEY")
	private Long key;

	@Column(name="ROLE_CODE")
	private String roleCode;
	
	@Column(name="ROLE_DESCRIPTION")
	private String roleDescription;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	

}
