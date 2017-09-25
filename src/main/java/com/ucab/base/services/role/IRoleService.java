package com.ucab.base.services.role;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ucab.base.commons.exceptions.CustomBaseException;
import com.ucab.base.commons.exceptions.CustomDatabaseOperationException;
import com.ucab.base.data.models.security.Permission;
import com.ucab.base.data.models.security.Role;
import com.ucab.base.webservices.dtos.request.role.CreateRoleRequest;
import com.ucab.base.webservices.dtos.request.role.UpdateRoleRequest;
public interface IRoleService {
	
	/**
	 * Returns Role entity with the recId given.
	 * @param recId
	 * @return Role
	 */
    Role findById(Long id) throws CustomBaseException;
    
	/**
	 * Stores the given role object into the database and returns the created entity.
	 * <p>
	 * Validates that all the required fields are not null, in that case it returns a CustomMissingAttributeException.class
	 * that specifies in it's message wich is the missing attribute and the HttpCode 400 (BadRequest). 
	 * @param Role entity to be stored, recID must be null or empty so it stores the role as a new Record 
	 * @return Role With the recID given by the generation strategy definied for the entity
	 */
    Role createNewRole(CreateRoleRequest newRole) throws CustomBaseException;
    
    /**
     * Returns a page of Role objects that are marked as roles
     * @param pageable
     * @param keyword 
     * @return
     * @throws CustomDatabaseOperationException
     */
    Page<Role> listAllRoles(Pageable pageable, String keyword) throws CustomDatabaseOperationException;
    
    
    boolean isValidRoleName(String roleName) throws CustomBaseException;

	Role updateRole(UpdateRoleRequest updateRoleRequest) throws CustomBaseException;
	
	boolean activate(Long id) throws CustomBaseException;
	
	boolean deactivate(Long id) throws CustomBaseException;

	List<Role> listActiveRoles() throws CustomBaseException;
	
	List<Permission> listPermissions() throws CustomBaseException;
}
