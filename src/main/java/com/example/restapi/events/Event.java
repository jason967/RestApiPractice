package com.example.restapi.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
@Builder
public class Event {

    @Id @GeneratedValue
    private long id;

    private String name;
    private String description;
    private LocalDateTime beginEnrollDateTime;
    private LocalDateTime closeEnrollDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location;
    private int basePrice;
    private int maxPrice;
    private int limitOfEnrollment;

    private boolean offLine;
    private boolean free;

@Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

}
