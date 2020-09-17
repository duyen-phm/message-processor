package com.diendev.genericmessageprocessor.domain;

public class Line {

    private Long id;

    private String uuid;

    private String lineId;

    public Line(Long id, String uuid, String lineId) {
        this.id = id;
        this.uuid = uuid;
        this.lineId = lineId;
    }
}
