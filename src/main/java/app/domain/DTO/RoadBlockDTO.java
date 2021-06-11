package app.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadBlockDTO {

    private Long id;
    private RoadBlockDTO[] carLinksList = new RoadBlockDTO[2];
    private CarDTO car;
    private Boolean isCircle = false;

    @Override
    public String toString() {
        return "RoadBlockDTO{" +
                "id=" + id +
                ", automobileLinksList=[" +
                "leftId=" + (carLinksList[0] == null ? "null" : carLinksList[0].getId()) +
                "rightId=" + (carLinksList[1] == null ? "null" : carLinksList[1].getId()) +
                "], automobileId=" + (car == null ? "null" : car.getId()) +
                ", isCircle=" + isCircle +
                '}';
    }
}
