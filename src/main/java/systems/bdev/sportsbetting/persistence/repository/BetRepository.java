package systems.bdev.sportsbetting.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import systems.bdev.sportsbetting.persistence.entity.BetEntity;

import java.util.Set;

@Repository
public interface BetRepository extends JpaRepository<BetEntity, Long> {
    Set<BetEntity> findByEventSourceAndEventExternalIdAndSelectionSourceAndSelectionExternalId(
            String eventSource,
            String eventExternalId,
            String selectionSource,
            String selectionExternalId
    );
}
