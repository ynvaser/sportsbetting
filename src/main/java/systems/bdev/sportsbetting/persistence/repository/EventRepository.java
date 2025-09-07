package systems.bdev.sportsbetting.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import systems.bdev.sportsbetting.persistence.entity.EventEntity;
import systems.bdev.sportsbetting.persistence.entity.SourceExternalIdKey;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, SourceExternalIdKey> {
    @Query("SELECT e FROM EventEntity e WHERE (:sessionType IS NULL OR e.type = :sessionType) AND (:yearValue IS NULL OR e.eventYear = :yearValue) AND (:country IS NULL OR e.country = :country)")
    List<EventEntity> findByOptionalFilters(@Param("sessionType") String sessionType,
                                            @Param("yearValue") Integer year,
                                            @Param("country") String country);
}
