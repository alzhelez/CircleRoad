package app.domain.model;

import app.domain.DTO.LineSide;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Data
@Entity(name ="automobiles")
@NoArgsConstructor
public class Car {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "speed")
    private Integer speed;

    @Column(name = "lineSide", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private LineSide lineSide;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Nullable
    private RoadBlock roadBlock;

    private Boolean hasTurned;
}
