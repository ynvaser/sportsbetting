package systems.bdev.sportsbetting.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import systems.bdev.sportsbetting.persistence.entity.BetEntity;
import systems.bdev.sportsbetting.persistence.entity.EventEntity;
import systems.bdev.sportsbetting.persistence.entity.SourceExternalIdKey;
import systems.bdev.sportsbetting.persistence.entity.UserEntity;
import systems.bdev.sportsbetting.persistence.repository.BetRepository;
import systems.bdev.sportsbetting.persistence.repository.EventRepository;
import systems.bdev.sportsbetting.persistence.repository.SelectionRepository;
import systems.bdev.sportsbetting.persistence.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@AllArgsConstructor
public class BettingService {
    private final UserRepository userRepository;
    private final BetRepository betRepository;
    private final EventRepository eventRepository;
    private final SelectionRepository selectionRepository;

    @Transactional
    public boolean makeBet(Long userId, BigDecimal amount, String eventId, String selectionId) {
        UserEntity user = userRepository.findById(userId).orElse(createUser(userId));
        if (user.getEurBalance().compareTo(amount) >= 0) {
            SourceExternalIdKey eventKey = SourceExternalIdKey.fromString(eventId);
            SourceExternalIdKey selectionKey = SourceExternalIdKey.fromString(selectionId);
            Optional<EventEntity> event = eventRepository.findById(eventKey);
            boolean selectionExists = selectionRepository.existsById(selectionKey);
            if (selectionExists && event.isPresent() && event.get().getWinner() == null) {
                BetEntity betEntity = new BetEntity();
                betEntity.setUser(user);
                betEntity.setBetAmount(amount);
                betEntity.setEventSource(eventKey.getSource());
                betEntity.setEventExternalId(eventKey.getExternalId());
                betEntity.setSelectionSource(selectionKey.getSource());
                betEntity.setSelectionExternalId(selectionKey.getExternalId());
                user.setEurBalance(user.getEurBalance().subtract(amount));
                userRepository.save(user);
                betRepository.save(betEntity);
                return true;
            }
        }
        return false;
    }

    private UserEntity createUser(Long userId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEurBalance(new BigDecimal("100.00"));
        return userEntity;
    }
}
