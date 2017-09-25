package com.ucab.base.webservices.dtos.request.role;

import java.util.Set;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@ApiObject(name="CreateRoleRequest",group="Role",description="Object used to create a new role and store it in the DB")
public class CreateRoleRequest {

	@ApiObjectField(order=0,required=true, description="Role's Name")
	public String name;
	
	@ApiObjectField(order=1,required=true, description="List of permissions assigned to the role")
	public Set<Long> permissionList;	
	
	@ApiObjectField(order=2,required=true, description="Role's description")
	public String description;	
	
}
