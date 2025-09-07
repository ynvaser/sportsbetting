package systems.bdev.sportsbetting.util;

import systems.bdev.sportsbetting.domain.EventDto;
import systems.bdev.sportsbetting.domain.SelectionDto;
import systems.bdev.sportsbetting.domain.openf1.Driver;
import systems.bdev.sportsbetting.domain.openf1.Session;
import systems.bdev.sportsbetting.persistence.entity.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static systems.bdev.sportsbetting.util.ApplicationConstants.OPEN_F1;

public class Mappings {
    private static final Random RANDOM = new Random();

    public static EventDto entityToDto(EventEntity entity) {
        return EventDto.builder()
                .id(entity.getId().toString())
                .type(entity.getType())
                .year(entity.getEventYear())
                .country(entity.getCountry())
                .name(entity.getName())
                .winner(entity.getWinner() != null ? Mappings.entityToDto(entity.getWinner()) : null)
                .selections(entity.getSelectionEntities() != null ? entity.getSelectionEntities().stream().map(Mappings::entityToDto).collect(Collectors.toList()) : null)
                .build();
    }

    public static SelectionDto entityToDto(SelectionEntity entity) {
        return SelectionDto.builder()
                .id(entity.getId().toString())
                .fullName(entity.getFullName())
                .racingNumber(entity.getRacingNumber())
                .build();
    }

    public static EventEntity fromF1Dto(SourceExternalIdKey sessionSourceExternalIdKey, Session session, List<Driver> drivers) {
        return EventEntity.builder()
                .id(sessionSourceExternalIdKey)
                .country(session.countryName())
                .eventYear(session.year())
                .type(session.sessionType())
                .name(session.sessionName())
                .selectionEntities(drivers.stream().map(driver -> SelectionEntity.builder()
                        .id(SourceExternalIdKey.of(OPEN_F1, driver.lastName() + driver.driverNumber())) //number alone isn't unique
                        .fullName(driver.fullName())
                        .racingNumber(driver.driverNumber())
                        .build()).collect(Collectors.toSet()))
                .build();
    }

    public static EventSelectionLinkEntity getLink(EventEntity eventEntity, SelectionEntity selectionEntity) {
        EventSelectionLinkId id = new EventSelectionLinkId();
        id.setEventSource(eventEntity.getId().getSource());
        id.setSelectionSource(eventEntity.getId().getSource());
        id.setEventExternalId(eventEntity.getId().getExternalId());
        id.setSelectionExternalId(selectionEntity.getId().getExternalId());
        EventSelectionLinkEntity entity = new EventSelectionLinkEntity();
        entity.setId(id);
        entity.setOdds(RANDOM.nextInt(3) + 2);
        return entity;
    }
}
