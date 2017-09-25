package com.ucab.base.data.repositories;

import com.ucab.base.data.models.security.User;

public interface UserRepository extends CustomBaseJpaRepository<User, Long> {
		
	User findByEmail(String email);
	
	User findById(Long id);
	
	User findByResetToken(String resetToken);
}
