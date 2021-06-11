package app.service;

import app.domain.DTO.CarDTO;
import app.domain.DTO.RoadBlockDTO;
import app.mapper.MainMapper;
import app.repository.CarRepository;
import app.repository.RoadBlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarMovingService {

    private final RoadBlockRepository roadBlockRepository;
    private final CarRepository carRepository;

    private final MainMapper mapper;

    @Autowired
    public CarMovingService(MainMapper mapper, RoadBlockRepository roadBlockRepository, CarRepository carRepository) {
        this.mapper = mapper;
        this.roadBlockRepository = roadBlockRepository;
        this.carRepository = carRepository;
    }

    public void moveAllCars() {
        List<Long> carsToRemove = new ArrayList<>();

        getAllAutomobiles().forEach(auto -> {
            RoadBlockDTO currRoadBlockDTO = auto.getRoadBlock();

            if (currRoadBlockDTO != null)
                if (currRoadBlockDTO.getCarLinksList()[0] == null &&
                        currRoadBlockDTO.getCarLinksList()[1] == null) {

                    moveCar(currRoadBlockDTO, null, auto);
                    carsToRemove.add(auto.getId());

                } else if (currRoadBlockDTO.getCarLinksList()[auto.getLSide().ordinal()] != null &&
                        currRoadBlockDTO.getCarLinksList()[auto.getLSide().ordinal()].getCar() == null && !auto.getHasTurned()) {

                    RoadBlockDTO nextRoadBlockDTO = currRoadBlockDTO.getCarLinksList()[auto.getLSide().ordinal()];

                    if (currRoadBlockDTO.getIsCircle() && !auto.getHasTurned())
                        auto.setHasTurned(true);

                    moveCar(currRoadBlockDTO, nextRoadBlockDTO, auto);

                } else if (currRoadBlockDTO.getCarLinksList()[1] != null &&
                        (currRoadBlockDTO.getCarLinksList()[auto.getLSide().ordinal()] == null || auto.getHasTurned())) {

                    moveCar(currRoadBlockDTO, currRoadBlockDTO.getCarLinksList()[1], auto);
                }
        });

        carsToRemove.forEach(carRepository::delete);
    }

    public List<CarDTO> getAllAutomobiles() {
        return mapper.autoToAutoDTO(carRepository.getAll());
    }

    private void moveCar(RoadBlockDTO currBlock, RoadBlockDTO nextBlock, CarDTO auto) {

        if (nextBlock == null) {
            currBlock.setCar(null);
            auto.setRoadBlock(null);
            return;
        }

        if (nextBlock.getCar() == null) {

            nextBlock.setCar(auto);
            currBlock.setCar(null);
            auto.setRoadBlock(nextBlock);
        } else
            return;

        var currBlockEnt = mapper.blockDtoToBlockNoReccurency(currBlock);
        var nextBlockEnt = mapper.blockDtoToBlockNoReccurency(nextBlock);
        var autoEnt = mapper.autoDtoToAuto(auto);
        roadBlockRepository.updateSavingNextBlocks(currBlockEnt);
        roadBlockRepository.updateSavingNextBlocks(nextBlockEnt);
        carRepository.update(autoEnt);
    }
}
