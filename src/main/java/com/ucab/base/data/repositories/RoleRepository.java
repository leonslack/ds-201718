package com.ucab.base.data.repositories;

import java.util.List;

import com.ucab.base.data.models.security.Role;

public interface RoleRepository extends CustomBaseJpaRepository<Role, Long> {
	
	Role findByName(String name);
	
	Role findByNameIgnoreCase(String name);
	
	Role findById(Long id);
	
	List<Role> findByIsActiveTrue();
	
}
