package com.ucab.base.data.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.ucab.base.data.models.security.Role;

public class RoleSpecifications {

	public static Specification<Role> recordContains(String keyword) {

		return new Specification<Role>() {

			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				return cb.or(cb.like(root.get("name"), "%" + keyword + "%"));

			}
		};
	}
}
