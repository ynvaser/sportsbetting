package systems.bdev.sportsbetting.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventSelectionLinkId implements Serializable {
    @Column(name = "event_source")
    private String eventSource;

    @Column(name = "event_external_id")
    private String eventExternalId;

    @Column(name = "selection_source")
    private String selectionSource;

    @Column(name = "selection_external_id")
    private String selectionExternalId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventSelectionLinkId)) return false;
        EventSelectionLinkId that = (EventSelectionLinkId) o;
        return Objects.equals(eventSource, that.eventSource) &&
                Objects.equals(eventExternalId, that.eventExternalId) &&
                Objects.equals(selectionSource, that.selectionSource) &&
                Objects.equals(selectionExternalId, that.selectionExternalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventSource, eventExternalId, selectionSource, selectionExternalId);
    }
}