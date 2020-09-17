package com.diendev.genericmessageprocessor.message;

/**
 * Created by Duyen.PhamThiCam@vn.bosch.com on 18.09.2020
 * */
public class GenericMessageWrapper<T> {
    T entity;

    public GenericMessageWrapper() {

    }

    public GenericMessageWrapper(T entity) {
        this.entity = entity;
    }

    public T getEntity() { return entity; }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
