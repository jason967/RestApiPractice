package com.example.restapi.events;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

//Bean serialize를 함
public class  EventResource extends EntityModel<Event> {

    public EventResource(Event event, Link... links) {
        super(event, links);
        //이렇게하면 /api/event 이부분을 type safe하게 만들기 어려우므로 밑에걸로 하자
        //add(new Link("http://localhost:8080/api/events/"+event.getId()));
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }

}
