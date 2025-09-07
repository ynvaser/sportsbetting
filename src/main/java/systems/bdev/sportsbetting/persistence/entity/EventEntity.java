package systems.bdev.sportsbetting.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {

    @EmbeddedId
    private SourceExternalIdKey id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer eventYear;

    @Column(nullable = false)
    private String country;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "source", referencedColumnName = "source", insertable = false, updatable = false),
            @JoinColumn(name = "winner_external_id", referencedColumnName = "external_id", insertable = false, updatable = false)
    })
    private SelectionEntity winner;

    @ManyToMany
    @JoinTable(
            name = "event_selection_link",
            joinColumns = {
                    @JoinColumn(name = "event_source", referencedColumnName = "source", insertable = false, updatable = false),
                    @JoinColumn(name = "event_external_id", referencedColumnName = "external_id", insertable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "selection_source", referencedColumnName = "source"),
                    @JoinColumn(name = "selection_external_id", referencedColumnName = "external_id")
            }
    )
    private Set<SelectionEntity> selectionEntities;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof EventEntity eventEntity)) return false;

        return Objects.equals(id, eventEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
