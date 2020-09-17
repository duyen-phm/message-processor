package com.diendev.genericmessageprocessor.service;

import com.diendev.genericmessageprocessor.message.enumeration.EntityState;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Created by Duyen.PhamThiCam@vn.bosch.com on 08/19/2020
 */
public abstract class MessageProcessor<T> {
    private final Logger log = LoggerFactory.getLogger(MessageProcessor.class);

    private final static String DEFAULT_IDENTIFIER_START_WITH = "_edited_";

    private final static String ID_FIELD = "id";

    private final static String UUID_FIELD = "uuid";

    /**
     * Process Message which consumed from ProconCoreService Solace to persist or delete entity
     * @param entity
     * @param entityState
     */
    public void processFromMessage(T entity, EntityState entityState) throws NoSuchFieldException, IllegalAccessException {
        log.debug("{} converted from received message {}", entityState, entity);
        if(entityState == EntityState.CREATED || entityState == EntityState.UPDATED) {
            T existingEntity = findByUuid(this.getUuid(entity));
            if(existingEntity != null) {
                entity = this.setId(entity, getId(existingEntity));
            }

            log.debug("Request to save {}", entity);
            entity = this.save(entity);
            log.debug("Entity after saving {}", entity);
        } else if(entityState == EntityState.DELETED) {
            T existingEntity = findByUuid(getUuid(entity));
            if(existingEntity == null) {
                log.error("Entity {} does not exist, can not be deleted", entity);
                return;
            }
            this.delete(this.getId(existingEntity));

        }
    }

    private Long getId(T entity) throws NoSuchFieldException, IllegalAccessException{
        Field idField =  entity.getClass().getDeclaredField(ID_FIELD);
        idField.setAccessible(true);
        return (Long) idField.get(entity);
    }

    private String getUuid(T entity) throws NoSuchFieldException, IllegalAccessException{
        Field uuidField =  entity.getClass().getDeclaredField(UUID_FIELD);
        uuidField.setAccessible(true);
        return (String) uuidField.get(entity);
    }

    private T setId(T entity, Long id) throws NoSuchFieldException, IllegalAccessException{
        Field idField =  entity.getClass().getDeclaredField(ID_FIELD);
        idField.setAccessible(true);
        idField.set(entity, Long.valueOf(id));
        return entity;
    }

    protected boolean isExistedUuid(T existingEntity) throws NoSuchFieldException, IllegalAccessException {
        return StringUtils.isNotBlank(getUuid(existingEntity)) && !getUuid(existingEntity).startsWith(DEFAULT_IDENTIFIER_START_WITH);
    }

    protected abstract void delete(Long id);

    protected abstract T save(T entity);

    protected abstract T findByUuid(String uuid);
}
