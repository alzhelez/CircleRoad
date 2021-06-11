package app.mapper;

import app.domain.DTO.CarDTO;
import app.domain.DTO.RoadDTO;
import app.domain.DTO.RoadBlockDTO;
import app.domain.model.Car;
import app.domain.model.Road;
import app.domain.model.RoadBlock;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

@Mapper(componentModel = "spring" )
public abstract class MainMapper {
    public Car autoDtoToAuto(CarDTO dto) {
        Car car = new Car();
        car.setLineSide(dto.getLSide());
        car.setHasTurned(dto.getHasTurned());
        car.setSpeed(dto.getSpeed());
        car.setId(dto.getId());

        if (dto.getRoadBlock() != null) {
            car.setRoadBlock(blockDtoToBlockNoReccurency(dto.getRoadBlock()));
        }

        return car;
    }

    public CarDTO autoToAutoDTO(Car ent) {
        CarDTO dto = new CarDTO();
        dto.setId(ent.getId());
        dto.setLSide(ent.getLineSide());
        dto.setSpeed(ent.getSpeed());
        dto.setHasTurned(ent.getHasTurned());

        if (ent.getRoadBlock() != null) {
            dto.setRoadBlock(blockToBlockDTOnoReccurencyLazy(ent.getRoadBlock()));
            dto.getRoadBlock().setCar(dto);
        }

        return dto;
    }

    private CarDTO autoToAutoDTOLazy(Car ent) {
        CarDTO dto = new CarDTO();
        dto.setId(ent.getId());
        dto.setLSide(ent.getLineSide());
        dto.setSpeed(ent.getSpeed());
        dto.setHasTurned(ent.getHasTurned());

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
        if (dto == null) {
            return null;
        }

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

    public RoadBlock blockDtoToBlockNoReccurency(RoadBlockDTO dto) {
        if (dto == null) {
            return null;
        }

        RoadBlock roadBlock = new RoadBlock();

        roadBlock.setId(dto.getId());
        roadBlock.setIsCircle(dto.getIsCircle());

        if (dto.getCarLinksList()[1] != null) {
            roadBlock.setRightBlock(getRoadBlockFromDTOFields(dto.getCarLinksList()[1]));
            roadBlock.setLeftBlock(getRoadBlockFromDTOFields(dto.getCarLinksList()[0]));
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

    public List<RoadBlock> blockDtoToBlockNoReccurency(List<RoadBlockDTO> dto) {
        var result = new ArrayList<RoadBlock>();
        for (var blockDTO : dto) {
            result.add(blockDtoToBlock(blockDTO));
        }
        return result;
    }

    public List<RoadBlockDTO> blockToBlockDTOnoReccurency(List<RoadBlock> ent) {
        var res = new ArrayList<RoadBlockDTO>();

        for (var block : ent) {
            res.add(blockToBlockDTOnoReccurency(block));
        }

        return res;
    }

    private RoadBlock getRoadBlockFromDTOFields(RoadBlockDTO dto) {
        if (dto == null)
            return null;

        var res = new RoadBlock();
        res.setId(dto.getId());
        res.setIsCircle(dto.getIsCircle());
        return res;
    }


    public RoadBlockDTO blockToBlockDTO(RoadBlock ent) {
        if (ent == null) {
            return null;
        }

        RoadBlockDTO dto = new RoadBlockDTO();
        dto.setId(ent.getId());
        dto.setIsCircle(ent.getIsCircle());

        if (ent.getRightBlock() != null) {
            dto.getCarLinksList()[0] = blockToBlockDTO(ent.getLeftBlock());
            dto.getCarLinksList()[1] = blockToBlockDTO(ent.getRightBlock());
        }

        if (ent.getCar() != null) {
            CarDTO carDTO = autoToAutoDTOLazy(ent.getCar());
            carDTO.setRoadBlock(dto);
        }

        return dto;
    }

    public RoadBlockDTO blockToBlockDTOnoReccurencyLazy(RoadBlock ent) {
        if (ent == null) {
            return null;
        }

        RoadBlockDTO dto = new RoadBlockDTO();
        dto.setId(ent.getId());
        dto.setIsCircle(ent.getIsCircle());

        if (ent.getRightBlock() != null) {
            dto.getCarLinksList()[0] = getRoadBlockDTOFromFields(ent.getLeftBlock());
            dto.getCarLinksList()[1] = getRoadBlockDTOFromFields(ent.getRightBlock());
        }

        return dto;
    }

    public RoadBlockDTO blockToBlockDTOnoReccurency(RoadBlock ent) {
        if (ent == null) {
            return null;
        }

        RoadBlockDTO dto = new RoadBlockDTO();
        dto.setId(ent.getId());
        dto.setIsCircle(ent.getIsCircle());

        if (ent.getRightBlock() != null) {
            dto.getCarLinksList()[0] = getRoadBlockDTOFromFields(ent.getLeftBlock());
            dto.getCarLinksList()[1] = getRoadBlockDTOFromFields(ent.getRightBlock());
        }

        if (ent.getCar() != null) {
            dto.setCar(autoToAutoDTOLazy(ent.getCar()));
            dto.getCar().setRoadBlock(dto);
        }
        return dto;
    }


    private RoadBlockDTO getRoadBlockDTOFromFields(RoadBlock block) {
        if (block == null)
            return null;

        var res = new RoadBlockDTO();
        res.setId(block.getId());
        res.setIsCircle(block.getIsCircle());
        return res;
    }

    public Road lineDtoToLine(RoadDTO dto) { // only for generation
        Road line = new Road();
        line.setLineLength(dto.getLineLength());
        line.setId(dto.getId());

        List<RoadBlock> lines = new ArrayList<>();
        var block = dto.getStartBlock();

        Stack<RoadBlockDTO> stack = new Stack<>();

        while (block != null) {
            stack.add(block);
            block = block.getCarLinksList()[1];
        }

        var lastBlock = blockDtoToBlockNoReccurency(stack.pop());
        lines.add(lastBlock);

        while (stack.size() > 0) {
            var newBlock = blockDtoToBlockNoReccurency(stack.pop());
            newBlock.setRightBlock(lastBlock);
            lines.add(newBlock);
            lastBlock = newBlock;
        }

        line.setBlockList(lines);
        return line;
    }

    public RoadDTO lineToLineDTO(Road ent) {
        RoadDTO lineDto = new RoadDTO();
        lineDto.setLineLength(ent.getLineLength());
        lineDto.setId(ent.getId());

        ent.getBlockList().sort(Comparator.comparingLong(RoadBlock::getId));

        var lastBlock = blockToBlockDTOnoReccurency(ent.getBlockList().get(0));
        for (int i = 1; i < ent.getBlockList().size(); i++) {
            var newBlock = blockToBlockDTOnoReccurency(ent.getBlockList().get(i));
            newBlock.getCarLinksList()[1] = lastBlock;
            lastBlock = newBlock;
        }

        lineDto.setStartBlock(lastBlock);
        return lineDto;
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