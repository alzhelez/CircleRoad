package app.service;

import app.component.CircleComp;
import app.domain.DTO.*;
import app.mapper.MainMapper;
import app.repository.RoadRepository;
import app.repository.RoadBlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

@Service
public class RoadGenerationService {
    private final CircleComp roadComponent;

    private final RoadRepository roadRepository;
    private final RoadBlockRepository roadBlockRepository;

    private final MainMapper mapper;

    @Autowired
    public RoadGenerationService(RoadRepository roadRepository,
                                 CircleComp roadComponent,
                                 RoadBlockRepository roadBlockRepository,
                                 MainMapper mapper) {
        this.roadRepository = roadRepository;
        this.roadComponent = roadComponent;
        this.roadBlockRepository = roadBlockRepository;
        this.mapper = mapper;
    }

    public void generateRoad(int count) {

        List<RoadDTO> lines = new ArrayList<>();

        for (int index = 0; index < count; index++) {
            for (int j = 0; j < roadComponent.getLinesPerSide(); j++) {
                initLine(roadComponent.getLineLength());
                initCircle(roadComponent.getCircleLength());
                if (j > 0) {
                    var firstLine = roadRepository.get((long) (index * roadComponent.getLinesPerSide() + j)).get();
                    if (!lines.stream().anyMatch(dto -> dto.getId() == firstLine.getId()))
                        lines.add(mapper.lineToLineDTO(firstLine));

                    var aaa = mapper.lineToLineDTO(firstLine);

                    var secLine = roadRepository.get((long) (index * roadComponent.getLinesPerSide() + j + 1)).get(); // FIXED COUNTER
                    if (!lines.stream().anyMatch(dto -> dto.getId() == secLine.getId()))
                        lines.add(mapper.lineToLineDTO(secLine));
                    linkCoDirectionalLines(lines.get(index * roadComponent.getLinesPerSide() + j - 1),
                            lines.get(count + 1),
                            roadComponent.getLineLength(), roadComponent.getCircleLength());
                }
            }
        }

        final int LINES_PER_SIDE = roadComponent.getLinesPerSide();
        final int LINE_LENGTH = roadComponent.getLineLength();
        final int LINE_COUNT = roadComponent.getLinesPerSide() * count;

        //Связывает дороги с кольцом
        for (int index = 0; index < count + 1; index++) {
            System.out.println(index);
            RoadBlockDTO rightTurnC = getRoadBlockShiftByIndex(
                    lines.get(LINES_PER_SIDE * index+1).getStartBlock(), //not fixed
                    LINE_LENGTH);

            RoadBlockDTO rightTurnR = getRoadBlockShiftByIndex(
                    lines.get(LINES_PER_SIDE * (index + 1)).getStartBlock(), //not fixed
                    LINE_LENGTH / 2 - LINES_PER_SIDE);

            rightTurnC.getCarLinksList()[0] = getRoadBlockShiftByIndex(lines
                            .get(LINE_COUNT / 2 + (index < 2 ? index * LINES_PER_SIDE : -(index / 2 + index % 2) * LINES_PER_SIDE)).getStartBlock(), //not fixed
                    (LINE_LENGTH / 2 - 1));

            rightTurnR.getCarLinksList()[1] = getRoadBlockShiftByIndex(lines
                            .get(LINE_COUNT - (index < 2 ? index : index - 2 * (index % 2) + 1) * LINES_PER_SIDE - 1).getStartBlock(), //not fixed
                    LINE_LENGTH / 2 + (LINES_PER_SIDE - 1));

            rightTurnC.setIsCircle(true);
            rightTurnR.setIsCircle(true);

            roadBlockRepository.update(mapper.blockDtoToBlockNoReccurency(rightTurnC));
            roadBlockRepository.update(mapper.blockDtoToBlockNoReccurency(rightTurnR));
        }
    }

    public void initLine(int lineLength) {

        RoadBlockDTO startBlock = new RoadBlockDTO();
        RoadBlockDTO curr = startBlock;

        for (int i = 0; i < lineLength - 1; i++) {
            RoadBlockDTO next = new RoadBlockDTO();
            curr.getCarLinksList()[0] = next;
            curr = next;
        }

        roadRepository.save(mapper.lineDtoToLine(new RoadDTO(startBlock, lineLength)));
    }

    public void initCircle(int cLength) {

        RoadBlockDTO startBlock = new RoadBlockDTO();
        RoadBlockDTO curr = startBlock;

        for (int i = 0; i < cLength - 1; i++) {
            RoadBlockDTO next = new RoadBlockDTO();
            curr.getCarLinksList()[0] = next;
            curr = next;
        }

        roadRepository.save(mapper.lineDtoToLine(new RoadDTO(startBlock, cLength)));
    }

    private void linkRoadBlocksByIndex(RoadBlockDTO from, RoadBlockDTO to, int index){
        if (index < 0 || index > 2)
            return;

        from.getCarLinksList()[index] = to;
        roadBlockRepository.update(mapper.blockDtoToBlock(from));
    }

    private RoadBlockDTO getRoadBlockShiftByIndex(RoadBlockDTO startBlock, int index){
        RoadBlockDTO block = startBlock;
        for(int i = 0; i < index; i++)
            block = block.getCarLinksList()[1];

        return block;
    }


    private void linkCoDirectionalLines(RoadDTO road, RoadDTO circle, int targetLineCount, int circleLength) {
        RoadBlockDTO rightBlockC = road.getStartBlock();
        RoadBlockDTO rightBlockR = circle.getStartBlock();

        for (int i = 0; i < road.getLineLength() - 1; i++) {
            RoadBlockDTO rightNextBlockC = rightBlockC.getCarLinksList()[0];
            RoadBlockDTO rightNextBlockR = rightBlockR.getCarLinksList()[1];

            if (!(i <= road.getLineLength() / 2 - (targetLineCount - 1) && i >= (circleLength / 2))) {
                linkRoadBlocksByIndex(rightBlockC, rightNextBlockC, 0);
                linkRoadBlocksByIndex(rightBlockR, rightNextBlockR, 1);
            }

            rightBlockC = rightNextBlockC;
            rightBlockR = rightNextBlockR;
        }
    }

}
