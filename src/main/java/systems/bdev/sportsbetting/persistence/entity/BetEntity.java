package systems.bdev.sportsbetting.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "bets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "event_source", nullable = false)
    private String eventSource;

    @Column(name = "event_external_id", nullable = false)
    private String eventExternalId;

    @Column(name = "selection_source", nullable = false)
    private String selectionSource;

    @Column(name = "selection_external_id", nullable = false)
    private String selectionExternalId;

    @Column(name = "bet_amount", nullable = false)
    private BigDecimal betAmount;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof BetEntity betEntity)) return false;

        return Objects.equals(id, betEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
