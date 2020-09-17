package com.diendev.genericmessageprocessor.message;

import org.springframework.messaging.Message;

public interface Parser {
    String ENTITY_TYPE_JSON_FIELD = "entityType";
    String STATE_JSON_FIELD = "state";
    String BODY_JSON_FIELD = "body";

    void parse(Message message);
}
