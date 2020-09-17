package com.diendev.genericmessageprocessor.service;

import com.diendev.genericmessageprocessor.domain.Line;

// To lazy to create interfaces haha :)
public class LineServiceImpl extends MessageProcessor<Line> {

    @Override
    protected void delete(Long id) {
        System.out.println("Delete Line id: " + id);
    }

    @Override
    protected Line save(Line entity) {
        // some logic here
        return entity;
    }

    @Override
    protected Line findByUuid(String uuid) {
        // just a mock
        return new Line(1l, "fake-uuid", "fake-line-id");
    }
}
