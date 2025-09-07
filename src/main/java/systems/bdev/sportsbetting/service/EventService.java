package systems.bdev.sportsbetting.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import systems.bdev.sportsbetting.client.openf1.OpenF1Client;
import systems.bdev.sportsbetting.domain.EventDto;
import systems.bdev.sportsbetting.domain.openf1.Driver;
import systems.bdev.sportsbetting.domain.openf1.Session;
import systems.bdev.sportsbetting.persistence.entity.EventEntity;
import systems.bdev.sportsbetting.persistence.entity.EventSelectionLinkEntity;
import systems.bdev.sportsbetting.persistence.entity.SelectionEntity;
import systems.bdev.sportsbetting.persistence.entity.SourceExternalIdKey;
import systems.bdev.sportsbetting.persistence.repository.EventRepository;
import systems.bdev.sportsbetting.persistence.repository.EventSelectionLinkRepository;
import systems.bdev.sportsbetting.persistence.repository.SelectionRepository;
import systems.bdev.sportsbetting.util.Mappings;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static systems.bdev.sportsbetting.util.ApplicationConstants.OPEN_F1;

@Component
@AllArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final SelectionRepository selectionRepository;
    private final EventSelectionLinkRepository eventSelectionLinkRepository;
    private final OpenF1Client openF1Client;

    @Transactional
    public List<EventDto> updateAndGetEventsFiltered(String sessionType, Integer year, String country) {
        Map<SourceExternalIdKey, EventEntity> eventEntities = eventRepository.findByOptionalFilters(sessionType, year, country)
                .stream()
                .collect(Collectors.toMap(EventEntity::getId, Function.identity()));
        fetchAndUpdateEventsIfNeeded(sessionType, year, country, eventEntities);
        return eventEntities.values().stream().map(Mappings::entityToDto).toList();
    }

    private void fetchAndUpdateEventsIfNeeded(String sessionType, Integer year, String country, Map<SourceExternalIdKey, EventEntity> eventEntities) {
        List<EventEntity> eventsToUpdate = new ArrayList<>();
        Set<SelectionEntity> selectionsToUpdate = new HashSet<>();
        Set<EventSelectionLinkEntity> eventSelectionLinksToUpdate = new HashSet<>();
        try {
            log.debug("Fetching sessions");
            List<Session> sessions = openF1Client.getSessions(sessionType, year, country);
            for (Session session : sessions) {
                log.debug("Fetching session: {}", session.sessionKey());
                SourceExternalIdKey key = SourceExternalIdKey.of(OPEN_F1, "" + session.sessionKey());
                boolean exists = eventEntities.containsKey(key);
                if (!exists) {
                    List<Driver> drivers = openF1Client.getDrivers("" + session.sessionKey());
                    EventEntity eventEntity = Mappings.fromF1Dto(key, session, drivers);
                    eventEntities.put(key, eventEntity);
                    eventsToUpdate.add(eventEntity);
                    selectionsToUpdate.addAll(eventEntity.getSelectionEntities());
                    eventEntity.getSelectionEntities().stream()
                            .map(selection -> Mappings.getLink(eventEntity, selection))
                            .collect(Collectors.toCollection(() -> eventSelectionLinksToUpdate));
                }
            }
            selectionRepository.saveAll(selectionsToUpdate);
            eventRepository.saveAll(eventsToUpdate);
            eventSelectionLinkRepository.saveAll(eventSelectionLinksToUpdate);
        } catch (Exception e) {
            log.error("Couldn't fetch F1 sessions or drivers: {}", e);
            throw e;
        }
    }
}
