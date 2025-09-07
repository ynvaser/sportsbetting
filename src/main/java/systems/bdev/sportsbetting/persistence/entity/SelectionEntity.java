package systems.bdev.sportsbetting.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "selection")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectionEntity {

    @EmbeddedId
    private SourceExternalIdKey id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "racing_number")
    private Integer racingNumber;

    @ManyToMany(mappedBy = "selectionEntities")
    private Set<EventEntity> eventEntities;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof SelectionEntity selectionEntity)) return false;

        return Objects.equals(id, selectionEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
