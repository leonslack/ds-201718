package com.ucab.base.webservices.dtos.request.role;

import java.util.HashSet;
import java.util.Set;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.ucab.base.data.models.security.Permission;
import com.ucab.base.data.models.security.Role;


@ApiObject(name="UpdateRoleRequest",group="Role",description="Object used to update roles stored in the DB")
public class UpdateRoleRequest {
	
	@ApiObjectField(order=0,required=true, description="Role's Name")
	public Long id;

	@ApiObjectField(order=1,required=true, description="Role's Name")
	public String name;
	
	@ApiObjectField(order=3,required=true, description="Role's Description")
	public String description;
	
	@ApiObjectField(order=4,required=true, description="List of permissions assigned to the role")
	public Set<Long> permissionList  = new HashSet<Long>();
	
	
	
	public Role toEntity(){
		Role entity = new Role();
		
		entity.setId(this.id);
		
		entity.setName(this.name);
		
		entity.setDescription(this.description);
		
		for(Long permission: permissionList){
			Permission newPermission = new Permission();			
			newPermission.setId(permission);
			entity.getPermissions().add(newPermission);
		}

		return entity;
	}
}
