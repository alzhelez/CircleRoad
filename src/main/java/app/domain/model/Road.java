package app.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "road")
@NoArgsConstructor
public class Road {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "lineLength")
    private Integer lineLength;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<RoadBlock> blockList;

    public Road(List<RoadBlock> blockList, int lineLength){
        this.blockList = blockList;
        this.lineLength = lineLength;
    }
}
