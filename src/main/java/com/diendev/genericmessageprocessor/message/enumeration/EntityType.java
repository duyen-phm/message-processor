package com.diendev.genericmessageprocessor.message.enumeration;

public enum EntityType {
    LINE("LineDTO");

    public final String dtoClassName;

    EntityType(String dtoClassName) {
        this.dtoClassName = dtoClassName;
    }
}
