package com.example.restapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        //무제한 경매인 경우
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0) {
            //field error
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong");
            errors.rejectValue("maxPrice", "wrongValue", "maxPrice is wrong");
            //global error
            errors.reject("wrongPrices", "Value for Prices are wrong");
        }
        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) || endEventDateTime.isBefore(eventDto.getBeginEnrollDateTime()) || endEventDateTime.isBefore(eventDto.getCloseEnrollDateTime())) {
            errors.rejectValue("endEventDateTime", "WrongValue", "endEventDateTime is wrong");
        }

        //TODO beginEventDateTime
        //ToDo CloseEnrollmentDate
    }
}
