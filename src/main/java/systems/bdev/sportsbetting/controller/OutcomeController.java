package systems.bdev.sportsbetting.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import systems.bdev.sportsbetting.service.OutcomeService;

@RestController
@RequestMapping("/api/outcome")
@AllArgsConstructor
public class OutcomeController {
    private final OutcomeService outcomeService;

    @PostMapping("/finalize")
    public String finalizeEvent(
            @RequestParam String eventId,
            @RequestParam String selectionId) {
        if (!eventId.isBlank() && !selectionId.isBlank()) {
            boolean success = outcomeService.finalizeOutcome(eventId, selectionId);
            if (success) {
                return "SUCCESS";
            }
        }
        return "FAILURE";
    }
}

