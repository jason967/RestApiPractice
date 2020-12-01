package com.example.restapi.events;

import java.time.LocalDateTime;

public class Event {
    private String name;
    private String description;
    private LocalDateTime beginEnrollDateTime;
    private LocalDateTime closeEnrollDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventlDateTime;
    private String location;
    private int basePrice;
    private int limitOfEnrollment;
}
