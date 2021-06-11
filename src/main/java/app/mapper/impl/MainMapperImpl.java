package app.mapper.impl;

import app.domain.DTO.CarDTO;
import app.domain.DTO.RoadBlockDTO;
import app.domain.DTO.RoadDTO;
import app.domain.model.Car;
import app.domain.model.Road;
import app.domain.model.RoadBlock;

import java.util.ArrayList;
import java.util.List;


public class MainMapperImpl {


    public Car autoDtoToAuto(CarDTO dto) {
        Car car = new Car();
        car.setLineSide(dto.getLSide());
        car.setHasTurned(dto.getHasTurned());
        car.setSpeed(dto.getSpeed());
        car.setId(dto.getId());

        if (car.getRoadBlock() != null) {
            car.setRoadBlock(blockDtoToBlock(dto.getRoadBlock()));
        }

        return car;
    }


    public CarDTO autoToAutoDTO(Car ent) {
        CarDTO dto = new CarDTO();
        dto.setId(ent.getId());
        dto.setLSide(ent.getLineSide());
        dto.setSpeed(ent.getSpeed());
        dto.setHasTurned(ent.getHasTurned());

        if (ent.getRoadBlock() != null)
            dto.setRoadBlock(blockToBlockDTO(ent.getRoadBlock()));

        return dto;
    }


    public List<Car> autoDtoToAuto(List<CarDTO> dto) {
        List<Car> res = new ArrayList<>();
        for (var item : dto)
            res.add(autoDtoToAuto(item));
        return res;
    }


    public List<CarDTO> autoToAutoDTO(List<Car> ent) {
        List<CarDTO> res = new ArrayList<>();
        for (var item : ent)
            res.add(autoToAutoDTO(item));
        return res;
    }


    public RoadBlock blockDtoToBlock(RoadBlockDTO dto) {
        RoadBlock roadBlock = new RoadBlock();

        roadBlock.setId(dto.getId());
        roadBlock.setIsCircle(dto.getIsCircle());

        if (dto.getCarLinksList()[1] != null) {
            roadBlock.setRightBlock(blockDtoToBlock(dto.getCarLinksList()[1]));
            roadBlock.setLeftBlock(blockDtoToBlock(dto.getCarLinksList()[0]));
        }

        if (dto.getCar() != null) {
            Car auto = new Car();
            auto.setSpeed(dto.getCar().getSpeed());
            auto.setRoadBlock(roadBlock);
            auto.setHasTurned(dto.getCar().getHasTurned());
            auto.setLineSide(dto.getCar().getLSide());
            auto.setId(dto.getCar().getId());
            roadBlock.setCar(auto);
        }

        return roadBlock;
    }


    public RoadBlockDTO blockToBlockDTO(RoadBlock ent) {
        RoadBlockDTO dto = new RoadBlockDTO();
        dto.setId(ent.getId());
        dto.setIsCircle(ent.getIsCircle());

        if (ent.getRightBlock() != null) {
            dto.getCarLinksList()[0] = blockToBlockDTO(ent.getLeftBlock());
            dto.getCarLinksList()[1] = blockToBlockDTO(ent.getRightBlock());
        }

        if (ent.getCar() != null) {
            CarDTO carDTO = new CarDTO();
            carDTO.setId(ent.getCar().getId());
            carDTO.setSpeed(ent.getCar().getSpeed());
            carDTO.setHasTurned(ent.getCar().getHasTurned());
            carDTO.setLSide(ent.getCar().getLineSide());
            carDTO.setRoadBlock(dto);
        }
        return dto;
    }


    public Road lineDtoToLine(RoadDTO dto) {
        Road r = new Road();
        r.setLineLength(dto.getLineLength());
        r.setId(dto.getId());

        return r;
    }


    public RoadDTO lineToLineDTO(Road ent) {
        RoadDTO rDto = new RoadDTO();
        rDto.setLineLength(ent.getLineLength());
        rDto.setId(ent.getId());

        return rDto;
    }

    private RoadBlockDTO readAllDescendants(RoadBlock block) {
        if (block.getLeftBlock() != null)
            readAllDescendants(block.getLeftBlock());

        if (block.getRightBlock() != null)
            readAllDescendants(block.getRightBlock());

        return blockToBlockDTO(block);
    }


    public List<RoadBlock> blockDtoToBlock(List<RoadBlockDTO> dto) {
        List<RoadBlock> res = new ArrayList<>();
        for (var dt : dto)
            res.add(blockDtoToBlock(dt));

        return res;
    }


    public List<RoadBlockDTO> blockToBlockDTO(List<RoadBlock> ent) {
        List<RoadBlockDTO> res = new ArrayList<>();
        for (var elem : ent)
            res.add(blockToBlockDTO(elem));

        return res;
    }


    public List<Road> lineDtoToLine(List<RoadDTO> dto) {
        List<Road> res = new ArrayList<>();

        for (var item : dto)
            res.add(lineDtoToLine(item));

        return res;
    }


    public List<RoadDTO> lineToLineDTO(List<Road> ent) {
        List<RoadDTO> res = new ArrayList<>();

        for (var item : ent)
            res.add(lineToLineDTO((item)));

        return res;
    }

}
