package systems.bdev.sportsbetting.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import systems.bdev.sportsbetting.service.OutcomeService;

@RestController
@RequestMapping("/api/betting")
@AllArgsConstructor
public class OutcomeController {
    private final OutcomeService outcomeService;

    @PostMapping
    public void finalizeEvent(
            @RequestParam String eventId,
            @RequestParam String selectionId) {
        if (!eventId.isBlank() && !selectionId.isBlank()) {
            outcomeService.finalizeOutcome(eventId, selectionId);
        }
    }
}

