package systems.bdev.sportsbetting.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import systems.bdev.sportsbetting.persistence.entity.*;
import systems.bdev.sportsbetting.persistence.repository.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class OutcomeService {
    private final UserRepository userRepository;
    private final BetRepository betRepository;
    private final EventRepository eventRepository;
    private final SelectionRepository selectionRepository;
    private final EventSelectionLinkRepository eventSelectionLinkRepository;

    @Transactional
    public boolean finalizeOutcome(String eventId, String selectionId) {
        SourceExternalIdKey eventKey = SourceExternalIdKey.fromString(eventId);
        SourceExternalIdKey selectionKey = SourceExternalIdKey.fromString(selectionId);
        Optional<EventEntity> maybeEvent = eventRepository.findById(eventKey);
        if (maybeEvent.isPresent()) {
            EventEntity event = maybeEvent.get();
            Optional<SelectionEntity> maybeSelection = selectionRepository.findById(selectionKey);
            if (maybeSelection.isPresent()) {
                SelectionEntity selection = maybeSelection.get();
                if (event.getSelectionEntities().contains(selection)) {
                    eventRepository.updateWinner(eventKey.getSource(), eventKey.getExternalId(), selectionKey.getExternalId());
                    Set<BetEntity> bets = betRepository.findByEventSourceAndEventExternalIdAndSelectionSourceAndSelectionExternalId(
                            eventKey.getSource(),
                            eventKey.getExternalId(),
                            selectionKey.getSource(),
                            selectionKey.getExternalId());
                    EventSelectionLinkId eventSelectionLinkId = new EventSelectionLinkId();
                    eventSelectionLinkId.setSelectionExternalId(selectionKey.getExternalId());
                    eventSelectionLinkId.setSelectionSource(selectionKey.getSource());
                    eventSelectionLinkId.setEventExternalId(eventKey.getExternalId());
                    eventSelectionLinkId.setEventSource(eventKey.getSource());
                    Optional<EventSelectionLinkEntity> maybeEventSelectionLink = eventSelectionLinkRepository.findById(eventSelectionLinkId);
                    EventSelectionLinkEntity eventSelectionLink = maybeEventSelectionLink.orElseThrow(() -> new IllegalStateException("No link between event and selection!"));
                    for (BetEntity winningBet : bets) {
                        UserEntity winner = winningBet.getUser();
                        winner.setEurBalance(winner.getEurBalance().add(winningBet.getBetAmount().multiply(BigDecimal.valueOf(eventSelectionLink.getOdds()))));
                        userRepository.save(winner);
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
