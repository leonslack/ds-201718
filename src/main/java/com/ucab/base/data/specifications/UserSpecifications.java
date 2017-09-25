package com.ucab.base.data.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.ucab.base.data.models.security.User;

public class UserSpecifications {

	public static Specification<User> recordContains(String keyword) {

		return new Specification<User>() {

			//TODO OMAR FIX String field names
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				return cb.or(cb.like(root.get("name"), "%" + keyword + "%"),
					         cb.like(root.get("email"), "%" + keyword + "%"),
					         cb.like(root.get("role").get("name"), "%" + keyword + "%"));
			}
		};
	}
}
