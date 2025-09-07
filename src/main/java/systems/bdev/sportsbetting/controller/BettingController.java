package systems.bdev.sportsbetting.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import systems.bdev.sportsbetting.service.BettingService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/betting")
@AllArgsConstructor
public class BettingController {
    private final BettingService bettingService;

    @PostMapping
    public void makeBet(
            @RequestParam Long userId,
            @RequestParam BigDecimal amount,
            @RequestParam String eventId,
            @RequestParam String selectionId) {
        if (amount.compareTo(BigDecimal.ZERO) > 0 && !eventId.isBlank() && !selectionId.isBlank()) {
            bettingService.makeBet(userId, amount, eventId, selectionId);
        } else {
            throw new RuntimeException("Validation error!");
        }
    }
}

