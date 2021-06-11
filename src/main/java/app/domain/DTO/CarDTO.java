package app.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {

    private Long id;
    private Integer speed;
    private LineSide lSide;
    private RoadBlockDTO roadBlock;
    private Boolean hasTurned = false;

    public CarDTO(Integer speed, LineSide lSide, RoadBlockDTO roadBlock) {
        this.speed = speed;
        this.lSide = lSide;
        this.roadBlock = roadBlock;
    }

}

