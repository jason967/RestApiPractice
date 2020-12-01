package com.example.restapi.events;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Event {
    private long id;

    private String name;
    private String description;
    private LocalDateTime beginEnrollDateTime;
    private LocalDateTime closeEnrollDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventlDateTime;
    private String location;
    private int basePrice;
    private int limitOfEnrollment;

    private boolean offLine;
    private boolean free;

    private EventStatus eventStatus;

}
