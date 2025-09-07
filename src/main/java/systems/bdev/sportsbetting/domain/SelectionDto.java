package systems.bdev.sportsbetting.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SelectionDto {
    private String id;
    private String fullName;
    private Integer racingNumber;
}
