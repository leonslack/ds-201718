package com.ucab.base.data.repositories;

import java.io.Serializable;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
interface CustomBaseRepository<T, ID extends Serializable> extends Repository<T, ID> {

	T findOne(ID id);

	T save(T entity);

	void delete(T entity);

}