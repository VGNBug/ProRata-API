package com.pawsey.api.controller.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pawsey.api.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

/**
 * <p>
 *     A base REST controller.
 * </p>
 * <p>
 *     It is advised that you define a @RequestMapping and @RestController annotation for any class that extends this.
 * </p>
 */
public abstract class BaseRestController<T, TService extends BaseService> {
    protected ObjectMapper mapper;

    @Autowired
    protected ApplicationContext appContext;

    @Autowired
    protected TService service;
    protected T exampleEntity;

    public BaseRestController() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
    }

    /**
     * Persists a POSTed JSON map as an entity, returning it as such if successful.
     *
     * @param map The JSON representation of the entity to be persisted.
     * @return The persisted entity, if successful.
     */
    protected ResponseEntity create(final @RequestBody Map<String, Object> map) {
        try {
            final T responseBody = (T) service.create(mapper.convertValue(map, exampleEntity.getClass()));

            if (responseBody != null) {
                return new ResponseEntity(responseBody, null, HttpStatus.CREATED);
            } else
                return new ResponseEntity("Could not create " + exampleEntity.getClass().getSimpleName() + " due to an unknown error.", null, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return getJsonFormatExceptionMessage(e, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * GETs an existing entity, using it's ID value.
     *
     * @param id The database ID value.
     * @return An entity of the supplied type with the supplied ID value.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    protected ResponseEntity read(@PathVariable Integer id) {
        try {
            T responseBody = (T) service.findById(id);

            if (responseBody != null) {
                return new ResponseEntity(responseBody, null, HttpStatus.OK);
            } else
                return new ResponseEntity(responseBody.getClass().getSimpleName() + " with database ID value " + id + " not found", null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return getJsonFormatExceptionMessage(e, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates an existing entity with data a PUTted JSON map, returning the updated entity if successful,
     *
     * @param map The JSON map of the entity as it will be once updated.
     *            This must have an ID matching an existing entity of the supplied type.
     * @return The updated entity.
     * @throws EntityNotFoundException
     */
    protected ResponseEntity update(final @RequestBody Map<String, Object> map) {
        try {
            T responseBody = (T) service.update(mapper.convertValue(map, exampleEntity.getClass()));

            if (responseBody != null) {
                return new ResponseEntity(responseBody, null, HttpStatus.OK);
            } else return new ResponseEntity("Update failed", null, HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return getJsonFormatExceptionMessage(e, HttpStatus.NOT_MODIFIED);
        }
    }

    /**
     * Deletes an entity, using it's ID to find it.
     *
     * @param id The database ID of the entity of the type to be deleted.
     * @throws EntityNotFoundException
     */
    protected ResponseEntity delete(Integer id) throws EntityNotFoundException, Exception {

        try {
            service.delete(id);
            return new ResponseEntity("Entity deleted", null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return getJsonFormatExceptionMessage(e, HttpStatus.BAD_REQUEST);
        }
    }

    protected HttpHeaders getHttpHeaders(String responseHeader, String value) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(responseHeader, value);
        return responseHeaders;
    }

    protected ResponseEntity<String> getJsonFormatExceptionMessage(Exception e, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(getJsonFormatString(e.getMessage()));
    }

    protected String getJsonFormatString(String message) {
        return "{\"message\": \"" + message + "\"}";
    }

}
