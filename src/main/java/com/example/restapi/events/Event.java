package com.example.restapi.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter  @EqualsAndHashCode(of = "id")
@AllArgsConstructor @Builder @NoArgsConstructor
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;

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

    private boolean offline;
    private boolean free;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    public void update() {
        //Update Free
        if(this.basePrice==0 && this.maxPrice==0)
        {
            this.free=true;
        }
        else
        {
            this.free=false;
        }

        //Update offline
        if(this.location==null || this.location.trim().isEmpty() )
        {
            this.offline=false;
        }
        else
        {
            this.offline = true;
        }

    }
}
