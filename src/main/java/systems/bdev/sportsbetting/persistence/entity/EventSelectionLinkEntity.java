package systems.bdev.sportsbetting.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "event_selection_link")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventSelectionLinkEntity {
    @EmbeddedId
    private EventSelectionLinkId id;

    @Column(name = "odds", nullable = false)
    private Integer odds;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof EventSelectionLinkEntity that)) return false;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}