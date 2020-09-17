package com.diendev.genericmessageprocessor.message;

import com.diendev.genericmessageprocessor.message.enumeration.EntityType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Duyen.PhamThiCam@vn.bosch.com on 08/19/2020
 */
public final class MessageParserUtil {

    public static final String DTO_PACKAGE_PREFIX = "com.diendev.genericmessageprocessor.domain.dto.";

    public static GenericMessageWrapper convertToDTO(JsonNode body, EntityType entityType,
                                                     ObjectMapper objectMapper) throws ClassNotFoundException, JsonProcessingException {
        GenericMessageWrapper wrapper = new GenericMessageWrapper();

        Object object = objectMapper.readValue(body.toString(), Class.forName(DTO_PACKAGE_PREFIX + entityType.dtoClassName));
        wrapper.setEntity(object);
        return wrapper;
    }

    private MessageParserUtil() {
    }
}