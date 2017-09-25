package com.ucab.base.data.repositories;


import com.ucab.base.data.models.security.Permission;


public   interface PermissionRepository extends CustomBaseJpaRepository<Permission, Long> {

	Permission findById(Long id);
	

}
