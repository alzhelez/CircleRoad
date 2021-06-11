package app.service;

import app.component.CircleComp;
import app.domain.DTO.CarDTO;
import app.domain.DTO.LineSide;
import app.domain.DTO.RoadBlockDTO;
import app.domain.DTO.RoadDTO;
import app.mapper.MainMapper;
import app.repository.CarRepository;
import app.repository.RoadRepository;
import app.repository.RoadBlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CarGenerationService {
    private final RoadRepository roadRepository;
    private final CarRepository carRepository;
    private final CircleComp circleComponent;
    private final RoadBlockRepository roadBlockRepository;

    private final MainMapper mapper;

    @Autowired
    public CarGenerationService(MainMapper mapper, RoadRepository roadRepository,
                                RoadBlockRepository roadBlockRepository,
                                CarRepository carRepository,
                                CircleComp circleComponent) {
        this.mapper = mapper;
        this.roadRepository = roadRepository;
        this.roadBlockRepository = roadBlockRepository;
        this.carRepository = carRepository;
        this.circleComponent = circleComponent;
    }

    public int generateCars(){
        Random random = new Random();
        int count = random.nextInt(circleComponent.getMaxExit() - circleComponent.getMinExit());

        for (int i = 0; i < count && i < circleComponent.getLinesPerSide() * 4; i++){
            RoadDTO ln;

            do{
                int lineNum = count;
                ln = mapper.lineToLineDTO(roadRepository.get(lineNum + 1L).get());
            }
            while (ln == null || (ln != null && ln.getStartBlock().getCar() != null));

            RoadBlockDTO startBlock = ln.getStartBlock();
            int autoSpeed  = random.nextInt(circleComponent.getMaxAutoSpeed() - circleComponent.getMinAutoSpeed());

            CarDTO automobileDTO = new CarDTO(autoSpeed + circleComponent.getMinAutoSpeed(),
                    LineSide.values()[random.nextInt(LineSide.values().length)],
                    startBlock);
            startBlock.setCar(automobileDTO);
            automobileDTO.setRoadBlock(startBlock);

            var auto = mapper.autoDtoToAuto(automobileDTO);
            carRepository.save(auto);
            automobileDTO.setId(auto.getId());
            startBlock.setCar(automobileDTO);

            auto = mapper.autoDtoToAuto(automobileDTO);
            carRepository.update(auto);
            roadBlockRepository.update(auto.getRoadBlock());
        }
        return count;
    }
}
