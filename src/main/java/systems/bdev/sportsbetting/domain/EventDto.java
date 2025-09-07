package systems.bdev.sportsbetting.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EventDto {
    private String id;
    private String type;
    private Integer year;
    private String country;
    private String name;
    private SelectionDto winner;
    private List<SelectionDto> selections;
}
