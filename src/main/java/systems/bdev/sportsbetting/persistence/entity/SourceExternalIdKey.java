package systems.bdev.sportsbetting.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

import static systems.bdev.sportsbetting.util.ApplicationConstants.DELIMITER;

@Embeddable
@Getter
public class SourceExternalIdKey implements Serializable {
    @Column(name = "source")
    private String source;
    @Column(name = "external_id")
    private String externalId;

    public SourceExternalIdKey() {
    }

    public SourceExternalIdKey(String source, String externalId) {
        this.source = source;
        this.externalId = externalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SourceExternalIdKey)) return false;
        SourceExternalIdKey that = (SourceExternalIdKey) o;
        return Objects.equals(source, that.source) && Objects.equals(externalId, that.externalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, externalId);
    }

    @Override
    public String toString() {
        return source + DELIMITER + externalId;
    }

    public static SourceExternalIdKey of(String source, String externalId) {
        return new SourceExternalIdKey(source, externalId);
    }

    public static SourceExternalIdKey fromString(String source) {
        String[] split = source.split(DELIMITER);
        return SourceExternalIdKey.of(split[0], split[1]);
    }
}
