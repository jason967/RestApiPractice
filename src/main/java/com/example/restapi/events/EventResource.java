package com.example.restapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.RepresentationModel;

//Bean serialize를 함
public class EventResource extends RepresentationModel {

    @JsonUnwrapped
    private Event event;

    public EventResource(Event event)
    {
        this.event=event;
    }

    public Event getEvent()
    {
        return event;
    }
}
