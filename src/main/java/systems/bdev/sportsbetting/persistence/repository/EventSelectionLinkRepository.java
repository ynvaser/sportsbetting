package systems.bdev.sportsbetting.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import systems.bdev.sportsbetting.persistence.entity.EventSelectionLinkEntity;
import systems.bdev.sportsbetting.persistence.entity.EventSelectionLinkId;

@Repository
public interface EventSelectionLinkRepository extends JpaRepository<EventSelectionLinkEntity, EventSelectionLinkId> {
}