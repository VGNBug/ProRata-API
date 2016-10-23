package com.pawsey.api.repository;

import com.pawsey.api.controller.rest.BaseRestController;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Base repository class, intended to decouple from the database-accessing class, and to follow the inheritence-pattern
 * of this project set in {@link BaseRestController} and
 * {@link com.pawsey.api.service.BaseService}.
 *
 * @param <T> The enntity-class to be accessed by this repository
 * @param <ID> The ID type of this object, e.g. {@link java.lang.Integer}.
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {}
