package com.ucab.base.services.role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucab.base.commons.exceptions.CustomAlreadyExistsException;
import com.ucab.base.commons.exceptions.CustomBaseException;
import com.ucab.base.commons.exceptions.CustomDatabaseOperationException;
import com.ucab.base.commons.exceptions.CustomInvalidActionException;
import com.ucab.base.commons.exceptions.CustomMissingAttributeException;
import com.ucab.base.commons.exceptions.CustomRegistryNotFoundException;
import com.ucab.base.data.models.security.Permission;
import com.ucab.base.data.models.security.Role;
import com.ucab.base.data.models.security.RolePermission;
import com.ucab.base.data.repositories.PermissionRepository;
import com.ucab.base.data.repositories.RolePermissionRepository;
import com.ucab.base.data.repositories.RoleRepository;
import com.ucab.base.data.specifications.RoleSpecifications;
import com.ucab.base.webservices.dtos.request.role.CreateRoleRequest;
import com.ucab.base.webservices.dtos.request.role.UpdateRoleRequest;

@Service("roleService")
@Transactional
public class RoleService implements IRoleService {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PermissionRepository permissionRepository;

	@Autowired
	RolePermissionRepository rpRepository;

	final static Logger log = Logger.getLogger(IRoleService.class);

	@Override
	public Role findById(Long id) throws CustomBaseException {
		Role role;

		try {
			role = roleRepository.findById(id);
		} catch (Exception e) {
			log.error("Error while trying to find role", e);
			throw new CustomDatabaseOperationException(e.getMessage());
		}
		if (role == null) {
			log.info("Could not find role with ID: " + id.toString());
			throw new CustomRegistryNotFoundException("User With Recid: " + id.toString() + " Not Found");
		}

		return role;
	}

	@Override
	@Transactional
	public Role createNewRole(CreateRoleRequest newRole) throws CustomBaseException {

		Role role = new Role();
		roleValidator(newRole);

		role.setName(newRole.name);
		
		role.setDescription(newRole.description);
		
		Set<RolePermission> permissions = GetRolePermissions(newRole.permissionList, role);

		role.setPermissions(permissions);
		
		role = saveRole(role);

		return role;
	}

	private Role saveRole(Role role) throws CustomBaseException{
		try {
			role = roleRepository.save(role);
			role = roleRepository.findById(role.getId());
		} catch (Exception e) {
			log.error("Error while trying to save role with Name:" + role.getName() + "\n" + e.getMessage(), e);
			throw new CustomDatabaseOperationException(
					"Could not save role with Name:" + role.getName() + "\n ERROR:" + e.getMessage());
		}
		return role;
	}
	
	@Override
	public boolean isValidRoleName(String roleName) throws CustomBaseException {

		Role role = findByName(roleName);
		if (role != null) {
			throw new CustomAlreadyExistsException("Role with name: " + roleName + " Already Exist");
		}
		return true;
	}

	private void roleValidator(CreateRoleRequest newRole) throws CustomBaseException {
		if ((newRole.name == null) || (newRole.name.isEmpty())) {
			throw new CustomMissingAttributeException("Role must have a name");
		} else if (newRole.permissionList.isEmpty()) {
			throw new CustomMissingAttributeException("You must set permissions to this role");
		}
		isValidRoleName(newRole.name);
	}


	private Set<RolePermission> GetRolePermissions(Set<Long> permissions, Role role)
			throws CustomBaseException {

		Set<RolePermission> returnSet = new HashSet<RolePermission>();
		
		Permission permissioAux = new Permission();

		for (Long permission : permissions ) {
			permissioAux = findPermissionsByRecID(permission);
			RolePermission aux = new RolePermission();
			aux.setPermission(permissioAux);
			aux.setRole(role);
			returnSet.add(aux);
		}

		return returnSet;
	}

	private Permission findPermissionsByRecID(Long recId) throws CustomBaseException {
		Permission permission;
		try {
			permission = permissionRepository.findById(recId);
		} catch (Exception e) {
			log.error("Error while trying to get permission with Id:" + recId + "\n" + e.getMessage(),
					e);
			throw new CustomDatabaseOperationException(
					"Could not get permission:" + recId + "\n ERROR:" + e.getMessage());
		}
		if(permission == null){
			throw new CustomRegistryNotFoundException("Couldn't find permission with Id"+ recId);
		}
		return permission;
	}

	private Role findByName(String roleName) throws CustomDatabaseOperationException {
		Role role = new Role();
		try {
			role = roleRepository.findByNameIgnoreCase(roleName);
		} catch (Exception e) {
			log.error("Error while trying to get role with Id:" + role.getId() + "\n" + e.getMessage(), e);
			throw new CustomDatabaseOperationException(
					"Could not get role:" + role.getId() + "\n ERROR:" + e.getMessage());
		}
		return role;
	}

	@Override
	public Page<Role> listAllRoles(Pageable pageable, String keyword) throws CustomDatabaseOperationException {
		log.debug("Starting listAllRoles with Pageable: " + pageable.toString());

		Page<Role> result;

		try {
			log.debug("Trying to Call findAll method of the repository");
			if (keyword != null && !keyword.isEmpty()) {
				result = roleRepository.findAll(RoleSpecifications.recordContains(keyword), pageable);
			} else {
				result = roleRepository.findAll(pageable);
			}
		} catch (Exception e) {
			log.error("Error while trying to find role pageable:" + pageable.toString() + "\n" + e.getMessage(), e);
			throw new CustomDatabaseOperationException("Error while trying to find Roles" + e.getMessage());
		}
		log.debug("Finishing ListAllRoles with " + result.getSize() + "Results");
		return result;
	}
	
	@Override
	public Role updateRole(UpdateRoleRequest updateRoleRequest) throws CustomBaseException{
		Role role = findById(updateRoleRequest.id);
		role.setName(updateRoleRequest.name);
		role.setDescription(updateRoleRequest.description);
		Set<RolePermission> permissions = GetRolePermissions(updateRoleRequest.permissionList, role);
		role.setPermissions(permissions);
		validateUpdateFields(role);
		role = saveRole(role);
		return role;
	} 
	
	@Override
	public boolean activate(Long id) throws CustomBaseException {
		Role role;
		role = findById(id);
		if (role.isActive()) {
			throw new CustomInvalidActionException("Role is already activated");
		}
		role.setActive(true);
		try {
			role = roleRepository.save(role);
		} catch (Exception e) {
			log.error("Error while trying to save Role \n Error: " + e.getMessage());

			throw new CustomDatabaseOperationException("Error while trying to save Role \n Error: " + e.getMessage());
		}

		return role.isActive();
	}
	
	@Override
	public boolean deactivate(Long id) throws CustomBaseException {
		Role role;
		role = findById(id);
		if (!role.isActive()) {
			throw new CustomInvalidActionException("Role is already deactivated");
		}
		role.setActive(false);
		try {
			role = roleRepository.save(role);
		} catch (Exception e) {
			log.error("Error while trying to save Role \n Error: " + e.getMessage());

			throw new CustomDatabaseOperationException("Error while trying to save Role \n Error: " + e.getMessage());
		}

		return role.isActive();
	}
	
	private void validateUpdateFields(Role role) throws CustomBaseException {
		if (!role.isActive()) {
			throw new CustomInvalidActionException("Cannot update a role that is deactivated");
		}
		if(role.getName() == null || role.getName().isEmpty()){
			throw new CustomMissingAttributeException("Name field is required");			
		}

	}

	@Override
	public List<Role> listActiveRoles() throws CustomBaseException {
		List<Role> result;
		try {
			log.debug("Trying to Call findAll method of the repository");
				result = roleRepository.findByIsActiveTrue();
		} catch (Exception e) {
			log.error("Error while trying to find role pageable:\n" + e.getMessage(), e);
			throw new CustomDatabaseOperationException("Error while trying to find Roles" + e.getMessage());
		}		return result;
	}

	@Override
	public List<Permission> listPermissions() throws CustomBaseException {
		// permissionRepository
		List<Permission> result;
		try {
			log.debug("Trying to Call findAll method of the repository");
				result = permissionRepository.findAll();
		} catch (Exception e) {
			log.error("Error while trying to find permission pageable:\n" + e.getMessage(), e);
			throw new CustomDatabaseOperationException("Error while trying to find permissions" + e.getMessage());
		}		
		return result;
	}

}
