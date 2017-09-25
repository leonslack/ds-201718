package com.ucab.base.data.repositories;

import java.util.Set;

import com.ucab.base.data.models.security.RolePermission;

public interface RolePermissionRepository extends CustomBaseRepository<RolePermission, Long> {
	
	<S extends RolePermission> Set<RolePermission> save(Iterable<RolePermission> entities);
}
