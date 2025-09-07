package systems.bdev.sportsbetting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import systems.bdev.sportsbetting.domain.EventDto;
import systems.bdev.sportsbetting.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/list")
    public List<EventDto> getEvents(
            @RequestParam(required = false) String sessionType,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String country
    ) {
        return eventService.updateAndGetEventsFiltered(sessionType, year, country);
    }
}

