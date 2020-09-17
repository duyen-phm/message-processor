package com.diendev.genericmessageprocessor.message;

import com.diendev.genericmessageprocessor.domain.Line;
import com.diendev.genericmessageprocessor.domain.dto.LineDTO;
import com.diendev.genericmessageprocessor.message.enumeration.EntityState;
import com.diendev.genericmessageprocessor.message.enumeration.EntityType;
import com.diendev.genericmessageprocessor.service.LineServiceImpl;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * Created by Duyen.PhamThiCam@vn.bosch.com on 08/13/2020
 */
// TODO (EntityType.TypeConstants.PROCON_CORE_ENTITY)
@Component
public class EntityMessageParser implements Parser{
    private static final Logger logger = LoggerFactory.getLogger(EntityMessageParser.class);

    private final ObjectMapper objectMapper;
    private final LineServiceImpl lineService;

    public EntityMessageParser(ObjectMapper objectMapper,
                                         LineServiceImpl lineService) {
        this.objectMapper = objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.lineService = lineService;
    }

    @Override
    public void parse(Message message) {
        logger.info("Incoming ProconCoreEntity message ");
        String payload = message.getPayload().toString();

        try {
            JsonNode jsonPayload = objectMapper.readTree(payload);
            final String infoMsg = jsonPayload.asText();
            logger.info("Payload: {}", infoMsg);

            JsonNode body = jsonPayload.get(BODY_JSON_FIELD);
            EntityType entityType = Enum.valueOf(EntityType.class, jsonPayload.get(ENTITY_TYPE_JSON_FIELD).asText());
            EntityState entityState = Enum.valueOf(EntityState.class, jsonPayload.get(STATE_JSON_FIELD).asText());

            if(!body.isArray()) {
                GenericMessageWrapper objectWrapper = MessageParserUtil.convertToDTO(body, entityType,  objectMapper);
                logger.info("Procon core entity extracted from JsonNode Array: {}", objectWrapper);

                this.process(objectWrapper, entityType, entityState);
            } else {
                for (JsonNode element : body) {
                    GenericMessageWrapper objectWrapper = MessageParserUtil.convertToDTO(element, entityType,  objectMapper);
                    logger.info("Procon core entity extracted from JsonNode Array: {}", objectWrapper);

                    this.process(objectWrapper, entityType, entityState);
                }
            }
        } catch (Exception e) {
            logger.error("Something went wrong while mapping message payload, {}, {}", payload, e);
        }
    }

    private void process(GenericMessageWrapper objectWrapper, EntityType entityType, EntityState entityState) throws NoSuchFieldException, IllegalAccessException {
        switch (entityType) {
            case LINE:
                LineDTO lineDTO = (LineDTO) objectWrapper.getEntity();

                // DTO mapper should be here to convert to Line
                Line line = new Line(lineDTO.getId(), lineDTO.getUuid(), lineDTO.getLineId());
                this.lineService.processFromMessage(line, entityState);
                break;
        }
    }
}
