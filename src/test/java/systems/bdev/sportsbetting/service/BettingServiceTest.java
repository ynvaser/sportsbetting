package systems.bdev.sportsbetting.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import systems.bdev.sportsbetting.persistence.entity.EventEntity;
import systems.bdev.sportsbetting.persistence.entity.SelectionEntity;
import systems.bdev.sportsbetting.persistence.entity.SourceExternalIdKey;
import systems.bdev.sportsbetting.persistence.entity.UserEntity;
import systems.bdev.sportsbetting.persistence.repository.BetRepository;
import systems.bdev.sportsbetting.persistence.repository.EventRepository;
import systems.bdev.sportsbetting.persistence.repository.SelectionRepository;
import systems.bdev.sportsbetting.persistence.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BettingServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BetRepository betRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private SelectionRepository selectionRepository;

    @InjectMocks
    private BettingService bettingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void makeBet_UserExists_EventAndSelectionValid_BetMadeAndBalanceUpdated() {
        // Given
        Long userId = 1L;
        BigDecimal betAmount = BigDecimal.valueOf(50);
        String eventId = "OpenF1,ev123";
        String selectionId = "OpenF1,sel456";

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEurBalance(BigDecimal.valueOf(100));

        SourceExternalIdKey eventKey = SourceExternalIdKey.fromString(eventId);
        SourceExternalIdKey selectionKey = SourceExternalIdKey.fromString(selectionId);

        EventEntity event = new EventEntity();
        event.setWinner(null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(eventRepository.findById(eventKey)).thenReturn(Optional.of(event));
        when(selectionRepository.existsById(selectionKey)).thenReturn(true);
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(betRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        bettingService.makeBet(userId, betAmount, eventId, selectionId);

        // Then
        assertEquals(BigDecimal.valueOf(50), user.getEurBalance());
        verify(userRepository).save(user);
        verify(betRepository).save(argThat(bet ->
                bet.getUser().equals(user) &&
                        bet.getBetAmount().compareTo(betAmount) == 0 &&
                        bet.getEventSource().equals(eventKey.getSource()) &&
                        bet.getEventExternalId().equals(eventKey.getExternalId()) &&
                        bet.getSelectionSource().equals(selectionKey.getSource()) &&
                        bet.getSelectionExternalId().equals(selectionKey.getExternalId())
        ));
    }

    @Test
    void makeBet_UserDoesNotExist_CreatesUserAndMakesBet() {
        // Given
        Long userId = 2L;
        BigDecimal betAmount = BigDecimal.valueOf(30);
        String eventId = "OpenF1,ev789";
        String selectionId = "OpenF1,sel123";

        SourceExternalIdKey eventKey = SourceExternalIdKey.fromString(eventId);
        SourceExternalIdKey selectionKey = SourceExternalIdKey.fromString(selectionId);

        EventEntity event = new EventEntity();
        event.setWinner(null);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(eventRepository.findById(eventKey)).thenReturn(Optional.of(event));
        when(selectionRepository.existsById(selectionKey)).thenReturn(true);
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(betRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        bettingService.makeBet(userId, betAmount, eventId, selectionId);

        // Then
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());

        UserEntity createdUser = userCaptor.getValue();
        assertEquals(userId, createdUser.getId());
        assertEquals(new BigDecimal("100.00").subtract(betAmount), createdUser.getEurBalance());
    }

    @Test
    void makeBet_UserHasInsufficientBalance_DoesNotPlaceBet() {
        // Given
        Long userId = 3L;
        BigDecimal betAmount = BigDecimal.valueOf(150);
        String eventId = "OpenF1,ev000";
        String selectionId = "OpenF1,sel000";

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEurBalance(BigDecimal.valueOf(100));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        bettingService.makeBet(userId, betAmount, eventId, selectionId);

        // Then
        verify(userRepository, never()).save(any());
        verify(betRepository, never()).save(any());
    }

    @Test
    void makeBet_EventNotFound_DoesNotPlaceBet() {
        // Given
        Long userId = 4L;
        BigDecimal betAmount = BigDecimal.valueOf(30);
        String eventId = "OpenF1,ev404";
        String selectionId = "OpenF1,sel404";

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEurBalance(BigDecimal.valueOf(100));

        SourceExternalIdKey eventKey = SourceExternalIdKey.fromString(eventId);
        SourceExternalIdKey selectionKey = SourceExternalIdKey.fromString(selectionId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(eventRepository.findById(eventKey)).thenReturn(Optional.empty());
        when(selectionRepository.existsById(selectionKey)).thenReturn(true);

        // When
        bettingService.makeBet(userId, betAmount, eventId, selectionId);

        // Then
        verify(userRepository, never()).save(any());
        verify(betRepository, never()).save(any());
    }

    @Test
    void makeBet_SelectionDoesNotExist_DoesNotPlaceBet() {
        // Given
        Long userId = 5L;
        BigDecimal betAmount = BigDecimal.valueOf(40);
        String eventId = "OpenF1,ev999";
        String selectionId = "OpenF1,sel999";

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEurBalance(BigDecimal.valueOf(100));

        SourceExternalIdKey eventKey = SourceExternalIdKey.fromString(eventId);
        SourceExternalIdKey selectionKey = SourceExternalIdKey.fromString(selectionId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(eventRepository.findById(eventKey)).thenReturn(Optional.of(new EventEntity()));
        when(selectionRepository.existsById(selectionKey)).thenReturn(false);

        // When
        bettingService.makeBet(userId, betAmount, eventId, selectionId);

        // Then
        verify(userRepository, never()).save(any());
        verify(betRepository, never()).save(any());
    }

    @Test
    void makeBet_EventAlreadyHasWinner_DoesNotPlaceBet() {
        // Given
        Long userId = 6L;
        BigDecimal betAmount = BigDecimal.valueOf(20);
        String eventId = "OpenF1,ev777";
        String selectionId = "OpenF1,sel777";

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEurBalance(BigDecimal.valueOf(100));

        SourceExternalIdKey eventKey = SourceExternalIdKey.fromString(eventId);

        EventEntity event = new EventEntity();
        event.setWinner(new SelectionEntity());

        SourceExternalIdKey selectionKey = SourceExternalIdKey.fromString(selectionId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(eventRepository.findById(eventKey)).thenReturn(Optional.of(event));
        when(selectionRepository.existsById(selectionKey)).thenReturn(true);

        // When
        bettingService.makeBet(userId, betAmount, eventId, selectionId);

        // Then
        verify(userRepository, never()).save(any());
        verify(betRepository, never()).save(any());
    }
}

