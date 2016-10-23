package com.pawsey.api.service.impl;

import com.pawsey.api.repository.BaseRepository;
import com.pawsey.api.service.BaseService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import javax.security.auth.login.CredentialException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link com.pawsey.api.service.BaseService}
 */
@Service
public abstract class BaseServiceImpl<T, TRepository extends BaseRepository> implements BaseService<T> {
    protected static final Log LOGGER = LogFactory.getLog(BaseServiceImpl.class);

    @Autowired
    protected
    TRepository repository;

    @Override
    @Transactional
    public T create(T entity) throws Exception {
        return createOrUpdateEntity(entity);
    }

    @Override
    @Transactional
    public T findById(Integer id) {
        if (id != null) {
            T response = (T) repository.findOne(id);

            if (response != null) {
                LOGGER.info(this.getClass().getSimpleName()
                        + " retrieved an instance of it's principle entity with ID " + id);
                return response;
            } else {
                String stateMessage = this.getClass().getSimpleName() + " could not retrieve an entity from "
                        + repository.getClass().getSimpleName() + " with ID " + id;
                DataRetrievalFailureException e = new DataRetrievalFailureException(stateMessage);
                LOGGER.error(e);
                throw e;
            }
        } else {
            String stateMessage = "entity ID cannot be null.";
            IllegalArgumentException e = new IllegalArgumentException(stateMessage);
            LOGGER.error(e);
            throw e;
        }
    }

    @Override
    @Transactional
    public List<T> findAll() {
        List<T> response = new ArrayList<>();

        for (Object entity : repository.findAll()) {
            response.add((T) entity);
        }

        return response;
    }

    @Override
    @Transactional
    public T update(T entity) {
        return createOrUpdateEntity(entity);
    }

    @Override
    @Transactional
    public void delete(T entity) {
        try {
            repository.delete(entity);

            LOGGER.info(entity.getClass().getSimpleName()
                    + " with hash of " + entity.getClass().toString() + " was deleted successfully.");
        } catch (Exception e) {
            LOGGER.error(e);
            throw e;
        }
    }

    private T createOrUpdateEntity(T entity) {
        try {
            T response = (T) repository.save(entity);
            LOGGER.info(entity.getClass().getSimpleName()
                    + " with hash of " + entity.toString() + " was persisted.");
            return response;
        } catch(Exception e) {
            String stateMessage = entity.getClass().getSimpleName()
                    + " with hash of " + entity.toString() + " was not persisted successfully.";
            PersistenceException persistenceException = new PersistenceException(stateMessage);
            LOGGER.error(e);
            throw persistenceException;
        }
    }

}
