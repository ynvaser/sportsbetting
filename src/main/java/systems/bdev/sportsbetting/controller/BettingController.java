package systems.bdev.sportsbetting.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import systems.bdev.sportsbetting.service.BettingService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/bet")
public class BettingController {
    private final BettingService bettingService;

    public BettingController(BettingService bettingService) {
        this.bettingService = bettingService;
    }

    @PostMapping("/create")
    public String makeBet(
            @RequestParam Long userId,
            @RequestParam BigDecimal amount,
            @RequestParam String eventId,
            @RequestParam String selectionId) {
        if (amount.compareTo(BigDecimal.ZERO) > 0 && !eventId.isBlank() && !selectionId.isBlank()) {
            boolean success = bettingService.makeBet(userId, amount, eventId, selectionId);
            if (success) {
                return "SUCCESS";
            }
        } else {
            throw new RuntimeException("Validation error!");
        }
        return "FAILURE";
    }
}

