package com.ucab.base.data.models.security;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.ucab.base.data.models.CustomBaseEntity;

@Entity
@Table(name = "RolePermission")
@Where(clause = "IsDeleted=0")
public class RolePermission extends CustomBaseEntity  implements java.io.Serializable {

	private static final long serialVersionUID = 7042271393179132068L;

	@ManyToOne
	@JoinColumn(name = "RoleID", nullable = false)
	private Role role;
	
	@ManyToOne
	@JoinColumn(name = "PermissionID", nullable = false)
	private Permission  permission;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}
	
}
