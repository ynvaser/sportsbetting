package systems.bdev.sportsbetting.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import systems.bdev.sportsbetting.persistence.entity.SelectionEntity;
import systems.bdev.sportsbetting.persistence.entity.SourceExternalIdKey;

@Repository
public interface SelectionRepository extends JpaRepository<SelectionEntity, SourceExternalIdKey> {
}
