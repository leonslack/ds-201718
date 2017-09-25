package com.ucab.base.data.models.security;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.hibernate.envers.NotAudited;
import org.springframework.security.core.GrantedAuthority;

import com.ucab.base.data.models.CustomBaseEntity;

@Entity
@Table(name = "[Permission]")
@Where(clause = "IsDeleted=0")
public class Permission extends CustomBaseEntity implements GrantedAuthority {

	private static final long serialVersionUID = 113584353909486534L;

	@Column(name = "Name", nullable = false)
    private String name; 
	
	@Column(name = "Description", nullable = false)
    private String description; 
	
	@Column(name = "Entity", nullable = false)
    private String entity; 
	
	@Column(name = "Action", nullable = false)
    private String action; 
	
    @OneToMany(mappedBy="permission")
	@NotAudited
    private Set<RolePermission> rolePermissions;
    
	public Permission(){
		super();
	}

	public Permission(String name) {
		super();		
		this.name = name;		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String code) {
		this.description = code;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String getAuthority() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof Permission) {
			return name.equals(((Permission) obj).name);
		}

		return false;
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.permisssion")
	public Set<RolePermission> getRolePermission() {
		return this.rolePermissions;
	}

	public void setRolePermission(Set<RolePermission> rolePermission) {
		this.rolePermissions = rolePermission;
	}
}
