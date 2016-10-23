package com.pawsey.api.service;

import com.pawsey.api.repository.BaseRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class BaseServiceTest<T, TService extends BaseService, TRepository extends BaseRepository> {

    protected TService service;

    protected T entity;

    @Mock
    protected TRepository repository;

    public void setup() {
        List<T> entityList = new ArrayList<T>();
        entityList.add(entity);

        when(repository.findOne(anyInt())).thenReturn(entity);
        when(repository.findAll()).thenReturn(entityList);
    }

    public T runTestCreate(T entity) throws Exception {
        T response = (T) service.create(entity);

        return saveAssertions(entity, response);
    }

    public T runTestFindById(int id) {
        T response = (T) service.findById(id);

        verify(repository).findOne(1);
        assertNotNull(response);

        return response;
    }

    public List<T> runTestFindAll() {
        List<T> response = service.findAll();

        verify(repository).findAll();
        assertNotNull(response);
        assertTrue(response.size() > 0);

        return response;
    }

    public T runTestUpdate(T entity) {
        T response = (T) service.update(entity);

        return saveAssertions(entity, response);
    }

    public void runTestDelete() {
        service.delete(entity);
        verify(repository).delete(entity);
    }

    private T saveAssertions(T entity, T response) {
        verify(repository).save(entity);
        assertNotNull(response);

        return response;
    }

    public abstract void setupRepositorySaveMock();

    protected abstract void setEntities();

    protected abstract void setRepositories();

    protected abstract void setService();

    public abstract void testCreate() throws Exception;

    public abstract void testFindById();

    public abstract void testFindAll();

    public abstract void testUpdate();

    public abstract void testDelete();
}
