package hello.querestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors){
        if (eventDto.getBasePrice() > eventDto.getMaxPrice()
                && eventDto.getMaxPrice() > 0){
            errors.reject("wrongPrices", "Values of price is Wrong.");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime())
            || endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())
            || endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())){
            errors.rejectValue("endEventDateTime","wrongValue", "EndEventDateTime is wrong.");
        }

        // TODO beginEventDateTime
        // TODO closeEnrollmentDateTime
    }
}
