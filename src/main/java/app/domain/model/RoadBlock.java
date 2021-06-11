package app.domain.model;

import com.sun.istack.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity(name="roadblocks")
@NoArgsConstructor
public class RoadBlock {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Nullable
    private RoadBlock leftBlock;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Nullable
    private RoadBlock rightBlock;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Nullable
    private Car car;

    private Boolean isCircle;

    @Override
    public String toString() {
        return "RoadBlock{" +
                "id=" + id +
                ", leftBlockId=" + (leftBlock == null ? "none" : leftBlock.getId()) +
                ", rightBlockId=" + (rightBlock == null ? "none" : rightBlock.getId()) +
                ", automobileId=" + (car == null ? "none" : car.getId()) +
                ", isCrossroad=" + isCircle +
                '}';
    }
}
