package com.ucab.base.data.models.security;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.ucab.base.data.models.CustomBaseEntity;



@Entity
@Table(name = "[Role]")
@Where(clause = "IsDeleted=0")
public class Role extends CustomBaseEntity {

	private static final long serialVersionUID = -5527247938260735720L;

	@Column(name = "Name", nullable = false)
	private String name;
	
	@Column(name = "Description")
	private String description;	

	@OneToMany(fetch=FetchType.EAGER, mappedBy="role",cascade = CascadeType.ALL, orphanRemoval=true)
	private Set<RolePermission> rolePermissions = new HashSet<RolePermission>();

	
	@OneToMany(mappedBy="role")
	private Set<User> users = new HashSet<User>();
	
	public Set<User> getUsers() {
		return users;
	}

	public Role() {
		super();
		rolePermissions = new HashSet<RolePermission>();
	}

	public Role(String name) {

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

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<RolePermission> getRolePermission() {
		return this.rolePermissions;
	}

	public void setPermissions(Set<RolePermission> roleP) {
		this.rolePermissions.clear();
		for (RolePermission permission : roleP) {
			permission.setRole(this);
		}
		this.rolePermissions.addAll(roleP);
	}
	
	public Set<Permission> getPermissions(){
		
		if(this.rolePermissions.isEmpty())
		{
			return Collections.emptySet();
		}
		Set<Permission> returnSet = new HashSet<Permission>();
		this.rolePermissions.forEach(rolePermission -> returnSet.add(rolePermission.getPermission()));		
		return returnSet;
	}
}
